package com.meditate.app_meditation.subscription.service;

import com.meditate.app_meditation.user.entity.User;
import com.meditate.app_meditation.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserRepository userRepository;

    public boolean isUserPremium(UUID userId) {
        return userRepository.findById(userId)
                .map(User::getSubscriptionStatus)
                .map(status -> status == User.SubscriptionStatus.PREMIUM || status == User.SubscriptionStatus.ENTERPRISE)
                .orElse(false);
    }
}
