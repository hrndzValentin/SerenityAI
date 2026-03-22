--liquibase formatted sql

--changeset serenity:v1-initial-schema
--validCheckSum: ANY

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    timezone VARCHAR(100),
    subscription_status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_email ON users(email);

-- 2. Meditation Routines Table
CREATE TABLE IF NOT EXISTS meditation_routines (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    title VARCHAR(255),
    description TEXT,
    duration_minutes INTEGER,
    ai_generated BOOLEAN DEFAULT FALSE,
    is_public BOOLEAN DEFAULT FALSE,
    focus_area VARCHAR(50),
    techniques JSONB,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_routine_user_id ON meditation_routines(user_id);
CREATE INDEX IF NOT EXISTS idx_routine_focus_area ON meditation_routines(focus_area);

CREATE TABLE IF NOT EXISTS breathing_schedule_configs(
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    preferred_times TIME[] DEFAULT '{}',
    preferred_categories VARCHAR[] DEFAULT '{}',
    max_daily_sessions INTEGER,
    use_ai_generated BOOLEAN DEFAULT FALSE

);
-- 3. Breathing Exercises Table
CREATE TABLE IF NOT EXISTS breathing_exercises (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    name VARCHAR(255),
    description TEXT,
    category VARCHAR(50),
    pattern JSONB,
    scheduled_times TIME[] DEFAULT '{}',
    total_daily_reps INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE
);
CREATE INDEX IF NOT EXISTS idx_breathing_user_id ON breathing_exercises(user_id);

-- 4. Notification Schedules Table
CREATE TABLE IF NOT EXISTS notification_schedules (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    entity_type VARCHAR(50),
    entity_id BIGINT,
    scheduled_time TIME,
    days_of_week INTEGER,
    is_active BOOLEAN DEFAULT TRUE,
    timezone VARCHAR(100)
);
CREATE INDEX IF NOT EXISTS idx_notif_user_time ON notification_schedules(user_id, scheduled_time);

-- 5. User Sessions Table
CREATE TABLE IF NOT EXISTS user_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    entity_type VARCHAR(50),
    entity_id BIGINT,
    started_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    completed_at TIMESTAMP WITHOUT TIME ZONE,
    duration_seconds INTEGER,
    completion_rate DOUBLE PRECISION
);
CREATE INDEX IF NOT EXISTS idx_session_user_started ON user_sessions(user_id, started_at);

-- 6. Subscriptions Table
CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES users(id),
    plan_type VARCHAR(50),
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    stripe_customer_id VARCHAR(255),
    stripe_subscription_id VARCHAR(255)
);
CREATE INDEX IF NOT EXISTS idx_sub_stripe_cust ON subscriptions(stripe_customer_id);

-- 7. User Device Tokens
CREATE TABLE IF NOT EXISTS user_device_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    fcm_token VARCHAR(255) NOT NULL,
    device_type VARCHAR(50),
    last_used_at TIMESTAMP WITHOUT TIME ZONE
);
CREATE INDEX IF NOT EXISTS idx_device_user_id ON user_device_tokens(user_id);

-- 8. Notification Preferences
CREATE TABLE IF NOT EXISTS notification_preferences (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES users(id),
    quiet_hours_start TIME,
    quiet_hours_end TIME,
    max_daily INTEGER DEFAULT 10,
    breathing_enabled BOOLEAN DEFAULT TRUE,
    meditation_enabled BOOLEAN DEFAULT TRUE
);
