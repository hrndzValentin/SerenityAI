package com.meditate.app_meditation.notification.repository;

import com.meditate.app_meditation.notification.entity.NotificationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationSchedule, Long> {
    long countByUserIdAndIsActiveTrue(UUID userId);
}
