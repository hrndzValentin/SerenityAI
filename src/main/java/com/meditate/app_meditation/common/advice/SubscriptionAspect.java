package com.meditate.app_meditation.common.advice;

import com.meditate.app_meditation.common.exception.SubscriptionRequiredException;
import com.meditate.app_meditation.subscription.service.SubscriptionService;
import com.meditate.app_meditation.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SubscriptionAspect {

    private final SubscriptionService subscriptionService;

    @Around("@annotation(com.meditate.app_meditation.common.annotation.RequiresPremium)")
    public Object checkSubscription(ProceedingJoinPoint joinPoint) throws Throwable {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new SubscriptionRequiredException("Authentication required to check subscription status");
        }

        if (!subscriptionService.isUserPremium(user.getId())) {
            throw new SubscriptionRequiredException("This feature is exclusive for Premium members.");
        }

        return joinPoint.proceed();
    }
}
