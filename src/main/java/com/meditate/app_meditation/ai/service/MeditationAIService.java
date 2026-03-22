package com.meditate.app_meditation.ai.service;

import com.meditate.app_meditation.ai.dto.MeditationRequest;
import com.meditate.app_meditation.ai.dto.MeditationRoutineDTO;
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
public class MeditationAIService {

    private final ChatClient chatClient;

    @Value("classpath:/prompts/meditation-routine.st")
    private Resource meditationTemplate;

    @Retryable(
        retryFor = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public MeditationRoutineDTO generateMeditationRoutine(MeditationRequest request) {
        return chatClient.prompt()
                .user(u -> u.text(meditationTemplate)
                        .params(Map.of(
                                "focusArea", request.focusArea(),
                                "durationMinutes", request.durationMinutes(),
                                "anxietyLevel", request.anxietyLevel(),
                                "userPreferences", request.userPreferences()
                        )))
                .call()
                .entity(MeditationRoutineDTO.class);
    }
}
