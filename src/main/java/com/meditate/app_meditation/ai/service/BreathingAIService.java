package com.meditate.app_meditation.ai.service;

import com.meditate.app_meditation.ai.dto.BreathingExerciseDTO;
import com.meditate.app_meditation.ai.dto.BreathingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BreathingAIService {

    private final ChatClient chatClient;

    @Value("classpath:/prompts/breathing-exercise.st")
    private Resource breathingTemplate;

    @Retryable(
        retryFor = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public BreathingExerciseDTO generateBreathingExercise(BreathingRequest request) {
        return chatClient.prompt()
                .user(u -> u.text(breathingTemplate)
                        .params(Map.of(
                                "purpose", request.purpose(),
                                "intensityLevel", request.intensityLevel(),
                                "availableMinutes", request.availableMinutes()
                        )))
                .call()
                .entity(BreathingExerciseDTO.class);
    }
}
