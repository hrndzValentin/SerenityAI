package com.meditate.app_meditation.common.exception;

import lombok.Getter;

@Getter
public class SubscriptionRequiredException extends RuntimeException {
    private final String errorCode = "SUBSCRIPTION_REQUIRED";
    private final String upgradeUrl = "/api/subscription/plans";

    public SubscriptionRequiredException(String message) {
        super(message);
    }
}
