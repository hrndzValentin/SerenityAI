package com.meditate.app_meditation.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final ChatClient chatClient;

    public String generateContent(String promptText) {
        return chatClient.prompt()
                .user(promptText)
                .call()
                .content();
    }

    public <T> T generateStructuredContent(String promptText, Class<T> responseType) {
        return chatClient.prompt()
                .user(promptText)
                .call()
                .entity(responseType);
    }
}
