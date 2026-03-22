package com.meditate.app_meditation.notification.service;

import com.meditate.app_meditation.breathing.entity.BreathingExercise;
import com.meditate.app_meditation.breathing.repository.BreathingExerciseRepository;
import com.meditate.app_meditation.meditation.entity.MeditationRoutine;
import com.meditate.app_meditation.meditation.repository.RoutineRepository;
import com.meditate.app_meditation.notification.entity.NotificationPreferences;
import com.meditate.app_meditation.notification.entity.NotificationSchedule;
import com.meditate.app_meditation.notification.entity.UserDeviceToken;
import com.meditate.app_meditation.notification.repository.NotificationPreferencesRepository;
import com.meditate.app_meditation.notification.repository.NotificationRepository;
import com.meditate.app_meditation.notification.repository.UserDeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationSchedulerService {

    private final NotificationRepository scheduleRepository;
    private final NotificationPreferencesRepository preferencesRepository;
    private final UserDeviceTokenRepository deviceTokenRepository;
    private final FCMService fcmService;
    private final BreathingExerciseRepository breathingRepository;
    private final RoutineRepository meditationRepository;

    @Scheduled(cron = "0 * * * * *") // Every minute
    public void processSchedules() {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        log.debug("Processing notification schedules for time: {}", now);

        List<NotificationSchedule> dueSchedules = scheduleRepository.findAll().stream()
                .filter(s -> s.isActive() && s.getScheduledTime().equals(now))
                .toList();

        for (NotificationSchedule schedule : dueSchedules) {
            processSingleSchedule(schedule);
        }
    }

    private void processSingleSchedule(NotificationSchedule schedule) {
        NotificationPreferences prefs = preferencesRepository.findByUserId(schedule.getUser().getId())
                .orElse(NotificationPreferences.builder().breathingEnabled(true).meditationEnabled(true).build());

        // Check quiet hours
        if (isQuietHour(prefs)) {
            log.info("Skipping notification for user {} due to quiet hours", schedule.getUser().getId());
            return;
        }

        String title = "";
        String body = "";
        String deeplink = "";

        if (schedule.getEntityType() == NotificationSchedule.EntityType.BREATHING && prefs.isBreathingEnabled()) {
            BreathingExercise exercise = breathingRepository.findById(schedule.getEntityId()).orElse(null);
            if (exercise != null) {
                title = "🌬️ Time for your breathing exercise!";
                body = String.format("Take a moment for '%s'. Your session is ready.", exercise.getName());
                deeplink = "app://breathing/" + exercise.getId();
            }
        } else if (schedule.getEntityType() == NotificationSchedule.EntityType.MEDITATION && prefs.isMeditationEnabled()) {
            MeditationRoutine routine = meditationRepository.findById(schedule.getEntityId()).orElse(null);
            if (routine != null) {
                title = "🌅 Meditation Time";
                body = String.format("Start your day with '%s'.", routine.getTitle());
                deeplink = "app://meditation/" + routine.getId();
            }
        }

        if (!title.isEmpty()) {
            List<String> tokens = deviceTokenRepository.findByUserId(schedule.getUser().getId()).stream()
                    .map(UserDeviceToken::getFcmToken)
                    .toList();

            if (!tokens.isEmpty()) {
                fcmService.sendMulticast(tokens, title, body, Map.of("deeplink", deeplink));
            }
        }
    }

    private boolean isQuietHour(NotificationPreferences prefs) {
        if (prefs.getQuietHoursStart() == null || prefs.getQuietHoursEnd() == null) return false;
        LocalTime now = LocalTime.now();
        if (prefs.getQuietHoursStart().isBefore(prefs.getQuietHoursEnd())) {
            return now.isAfter(prefs.getQuietHoursStart()) && now.isBefore(prefs.getQuietHoursEnd());
        } else {
            return now.isAfter(prefs.getQuietHoursStart()) || now.isBefore(prefs.getQuietHoursEnd());
        }
    }
}
