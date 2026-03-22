package com.meditate.app_meditation.meditation.dto;

import com.meditate.app_meditation.meditation.entity.RoutineType;
import java.util.List;

public record RoutineResponseDTO(
    Long id,
    String title,
    String description,
    RoutineType type,
    List<SessionDTO> sessions
) {}
