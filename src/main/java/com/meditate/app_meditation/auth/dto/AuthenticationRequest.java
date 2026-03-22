package com.meditate.app_meditation.auth.dto;

public record AuthenticationRequest(
    String email,
    String password
) {}
