package com.meditate.app_meditation.breathing.dto;

import com.meditate.app_meditation.breathing.entity.BreathingExercise.Category;
import java.time.LocalTime;
import java.util.List;

public record BreathingScheduleRequest(
    List<LocalTime> preferredTimes,
    List<Category> preferredCategories,
    int maxDailySessions,
    boolean useAiGenerated
) {}
