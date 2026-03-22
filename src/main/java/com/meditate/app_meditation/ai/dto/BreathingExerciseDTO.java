package com.meditate.app_meditation.ai.dto;

public record BreathingExerciseDTO(
    String name,
    String description,
    Pattern pattern,
    int totalRounds,
    String benefits,
    String bestTimeOfDay,
    String frequency
) {
    public record Pattern(int inhaleSeconds, int holdSeconds, int exhaleSeconds, int holdAfterExhale) {}
}
