package com.meditate.app_meditation.meditation.dto;

import com.meditate.app_meditation.meditation.entity.RoutineType;

public record RoutineGenerationRequest(
    String stressLevel,
    int totalDurationDays,
    RoutineType preference
) {}
