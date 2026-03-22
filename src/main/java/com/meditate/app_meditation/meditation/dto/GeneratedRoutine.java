package com.meditate.app_meditation.meditation.dto;

import lombok.Data;

import java.util.List;

@Data
public class GeneratedRoutine {
    private String title;
    private String description;
    private String type;
    private List<GeneratedSession> sessions;
}