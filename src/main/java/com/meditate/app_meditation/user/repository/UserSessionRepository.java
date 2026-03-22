package com.meditate.app_meditation.user.repository;

import com.meditate.app_meditation.user.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    
    @Query("SELECT s.entityId FROM UserSession s WHERE s.user.id = :userId AND s.entityType = 'BREATHING' AND s.startedAt > :since")
    List<Long> findRecentlyCompletedBreathingIds(UUID userId, LocalDateTime since);

    @Query("SELECT s FROM UserSession s WHERE s.user.id = :userId AND s.startedAt > :since ORDER BY s.startedAt DESC")
    List<UserSession> findRecentSessions(UUID userId, LocalDateTime since);
}
