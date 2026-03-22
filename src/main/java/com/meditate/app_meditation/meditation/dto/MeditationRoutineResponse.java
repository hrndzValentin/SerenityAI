package com.meditate.app_meditation.meditation.dto;

import java.util.List;

public record MeditationRoutineResponse(
    String title,
    String description,
    List<AiSessionResponse> sessions
) {
    public record AiSessionResponse(
        String title,
        String script,
        int durationMinutes
    ) {}
}
