package com.meditate.app_meditation.ai.dto;

import com.meditate.app_meditation.meditation.entity.MeditationRoutine.FocusArea;

public record MeditationRequest(
    FocusArea focusArea,
    int durationMinutes,
    String userPreferences,
    int anxietyLevel
) {}
