package com.meditate.app_meditation.ai.dto;

import com.meditate.app_meditation.breathing.entity.BreathingExercise.Category;

public record BreathingRequest(
    Category purpose,
    int intensityLevel,
    int availableMinutes
) {}
