package com.meditate.app_meditation.breathing.service;

import com.meditate.app_meditation.ai.service.BreathingAIService;
import com.meditate.app_meditation.ai.dto.BreathingRequest;
import com.meditate.app_meditation.ai.dto.BreathingExerciseDTO;
import com.meditate.app_meditation.breathing.dto.BreathingScheduleRequest;
import com.meditate.app_meditation.breathing.dto.StreakDataDTO;
import com.meditate.app_meditation.breathing.entity.BreathingExercise;
import com.meditate.app_meditation.breathing.entity.BreathingScheduleConfig;
import com.meditate.app_meditation.breathing.event.BreathingScheduleUpdatedEvent;
import com.meditate.app_meditation.breathing.repository.BreathingExerciseRepository;
import com.meditate.app_meditation.breathing.repository.BreathingScheduleConfigRepository;
import com.meditate.app_meditation.user.entity.User;
import com.meditate.app_meditation.user.entity.UserSession;
import com.meditate.app_meditation.user.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BreathingExerciseService {

    private final BreathingExerciseRepository exerciseRepository;
    private final BreathingScheduleConfigRepository configRepository;
    private final UserSessionRepository sessionRepository;
    private final BreathingAIService aiService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void createPersonalizedSchedule(User user, BreathingScheduleRequest request) {
        BreathingScheduleConfig config = configRepository.findByUserId(user.getId())
                .orElse(BreathingScheduleConfig.builder().user(user).build());

        config.setPreferredTimes(request.preferredTimes());
        config.setPreferredCategories(request.preferredCategories());
        config.setMaxDailySessions(request.maxDailySessions());
        config.setUseAiGenerated(request.useAiGenerated());

        configRepository.save(config);

        if (request.useAiGenerated()) {
            // Logic to pre-generate or flag for JIT AI generation
            generateAiExercises(user, request);
        }

        eventPublisher.publishEvent(new BreathingScheduleUpdatedEvent(user.getId(), request.preferredTimes()));
    }

    private void generateAiExercises(User user, BreathingScheduleRequest request) {
        // Implementation calling aiService for each preferred category
        request.preferredCategories().forEach(cat -> {
            BreathingExerciseDTO dto = aiService.generateBreathingExercise(
                new BreathingRequest(cat, 3, 5)
            );
            
            BreathingExercise exercise = BreathingExercise.builder()
                .user(user)
                .name(dto.name())
                .description(dto.description())
                .category(cat)
                .pattern(new BreathingExercise.BreathingPattern(
                    dto.pattern().inhaleSeconds(), 
                    dto.pattern().holdSeconds(), 
                    dto.pattern().exhaleSeconds(), 
                    dto.pattern().holdAfterExhale()))
                .isActive(true)
                .build();
            
            exerciseRepository.save(exercise);
        });
    }

    public List<BreathingExercise> getExercisesForTimeOfDay(UUID userId, LocalTime time) {
        BreathingScheduleConfig config = configRepository.findByUserId(userId).orElseThrow();
        
        boolean isDue = config.getPreferredTimes().stream()
                .anyMatch(t -> t.isAfter(time.minusMinutes(1)) && t.isBefore(time.plusMinutes(31)));

        if (!isDue) return Collections.emptyList();

        // Anti-repetition selection
        return getVarietyPool(userId, BreathingExercise.Category.CALMING); // Simplified for example
    }

    public List<BreathingExercise> getVarietyPool(UUID userId, BreathingExercise.Category category) {
        List<BreathingExercise> allOfCategory = exerciseRepository.findByCategory(category);
        List<Long> recentIds = sessionRepository.findRecentlyCompletedBreathingIds(userId, LocalDateTime.now().minusDays(7));

        return allOfCategory.stream()
                .sorted(Comparator.comparingInt(e -> {
                    if (recentIds.contains(e.getId())) {
                        return 1; // Lower priority if recently done
                    }
                    return 0;
                }))
                .limit(3)
                .collect(Collectors.toList());
    }

    @Transactional
    public void completeSession(User user, Long exerciseId, int durationSeconds) {
        exerciseRepository.findByIdAndUserId(exerciseId, user.getId())
                .orElseThrow(() -> new org.springframework.security.access.AccessDeniedException("Unauthorized exercise access"));

        UserSession session = UserSession.builder()
                .user(user)
                .entityType("BREATHING")
                .entityId(exerciseId)
                .startedAt(LocalDateTime.now().minusSeconds(durationSeconds))
                .completedAt(LocalDateTime.now())
                .durationSeconds(durationSeconds)
                .completionRate(1.0)
                .build();
        
        sessionRepository.save(session);
    }

    public StreakDataDTO getStreakData(UUID userId) {
        List<UserSession> sessions = sessionRepository.findRecentSessions(userId, LocalDateTime.now().minusMonths(1));
        
        Set<LocalDate> activityDates = sessions.stream()
                .map(s -> s.getStartedAt().toLocalDate())
                .collect(Collectors.toSet());

        int currentStreak = 0;
        LocalDate date = LocalDate.now();
        while (activityDates.contains(date)) {
            currentStreak++;
            date = date.minusDays(1);
        }

        return new StreakDataDTO(currentStreak, currentStreak, activityDates.size());
    }
}
