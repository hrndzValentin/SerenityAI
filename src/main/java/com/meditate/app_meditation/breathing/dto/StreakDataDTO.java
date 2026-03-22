package com.meditate.app_meditation.breathing.dto;

public record StreakDataDTO(
    int currentStreak,
    int longestStreak,
    int totalCompletions
) {}
