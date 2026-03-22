package com.meditate.app_meditation.notification.service;

import com.meditate.app_meditation.common.exception.SubscriptionRequiredException;
import com.meditate.app_meditation.notification.entity.NotificationSchedule;
import com.meditate.app_meditation.notification.repository.NotificationRepository;
import com.meditate.app_meditation.subscription.service.SubscriptionService;
import com.meditate.app_meditation.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SubscriptionService subscriptionService;

    public void scheduleNotification(User user, NotificationSchedule schedule) {
        boolean isPremium = subscriptionService.isUserPremium(user.getId());
        long activeCount = notificationRepository.countByUserIdAndIsActiveTrue(user.getId());

        if (!isPremium && activeCount >= 3) {
            throw new SubscriptionRequiredException("Free users are limited to 3 active notification schedules. Upgrade to Premium for unlimited scheduling.");
        }

        schedule.setUser(user);
        notificationRepository.save(schedule);
    }
}
