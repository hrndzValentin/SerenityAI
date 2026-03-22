package com.meditate.app_meditation.auth.dto;

import com.meditate.app_meditation.user.entity.User;

public record RegisterRequest(
    String firstName,
    String lastName,
    String email,
    String password
) {}
