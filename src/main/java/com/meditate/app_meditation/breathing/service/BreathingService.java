package com.meditate.app_meditation.breathing.service;

import com.meditate.app_meditation.breathing.entity.BreathingExercise;
import com.meditate.app_meditation.common.annotation.RequiresPremium;
import org.springframework.stereotype.Service;

@Service
public class BreathingService {

    /**
     * Advanced breathing patterns are for premium users only.
     */
    @RequiresPremium
    public void configureAdvancedPattern(BreathingExercise exercise, BreathingExercise.BreathingPattern pattern) {
        exercise.setPattern(pattern);
        // Save logic...
    }
}
