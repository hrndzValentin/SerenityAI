package com.meditate.app_meditation.meditation.repository;

import com.meditate.app_meditation.meditation.entity.MeditationRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoutineRepository extends JpaRepository<MeditationRoutine, Long> {
    List<MeditationRoutine> findByUserId(UUID userId);
    Optional<MeditationRoutine> findByIdAndUserId(Long id, UUID userId);
    void deleteByIdAndUserId(Long id, UUID userId);
}
