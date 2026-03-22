package com.meditate.app_meditation.auth.dto;

public record AuthenticationResponse(
    String accessToken,
    String refreshToken
) {}
