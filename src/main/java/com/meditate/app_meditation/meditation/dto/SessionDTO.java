package com.meditate.app_meditation.meditation.dto;

public record SessionDTO(
    Long id,
    String title,
    String script,
    int durationMinutes
) {}
