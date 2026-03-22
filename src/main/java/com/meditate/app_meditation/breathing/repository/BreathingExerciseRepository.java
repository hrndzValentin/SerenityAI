package com.meditate.app_meditation.breathing.repository;

import com.meditate.app_meditation.breathing.entity.BreathingExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BreathingExerciseRepository extends JpaRepository<BreathingExercise, Long> {
    List<BreathingExercise> findByUserId(UUID userId);
    List<BreathingExercise> findByCategory(BreathingExercise.Category category);
    Optional<BreathingExercise> findByIdAndUserId(Long id, UUID userId);
}
