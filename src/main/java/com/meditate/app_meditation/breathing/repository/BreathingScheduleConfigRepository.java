package com.meditate.app_meditation.breathing.repository;

import com.meditate.app_meditation.breathing.entity.BreathingScheduleConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface BreathingScheduleConfigRepository extends JpaRepository<BreathingScheduleConfig, UUID> {
    Optional<BreathingScheduleConfig> findByUserId(UUID userId);
}
