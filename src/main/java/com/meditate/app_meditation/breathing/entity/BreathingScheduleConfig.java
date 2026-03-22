package com.meditate.app_meditation.breathing.entity;

import com.meditate.app_meditation.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "breathing_schedule_configs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreathingScheduleConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "preferred_times", columnDefinition = "time[]")
    private List<LocalTime> preferredTimes;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "preferred_categories", columnDefinition = "varchar[]")
    private List<BreathingExercise.Category> preferredCategories;

    @Column(name="max_daily_sessions")
    private int maxDailySessions;

    @Column(name="use_ai_generated")
    private boolean useAiGenerated;
}
