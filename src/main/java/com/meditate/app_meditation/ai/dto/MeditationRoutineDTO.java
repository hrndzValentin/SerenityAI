package com.meditate.app_meditation.ai.dto;

import java.util.List;

public record MeditationRoutineDTO(
    String title,
    String description,
    List<Step> steps,
    List<String> techniques,
    String breathingCue,
    String estimatedBenefits
) {
    public record Step(int order, String instruction, int durationSeconds) {}
}
