package com.meditate.app_meditation.meditation.service;

import com.meditate.app_meditation.ai.service.OpenAiService;
import com.meditate.app_meditation.meditation.dto.MeditationRoutineResponse;
import com.meditate.app_meditation.meditation.dto.RoutineGenerationRequest;
import com.meditate.app_meditation.meditation.dto.RoutineResponseDTO;
import com.meditate.app_meditation.meditation.dto.SessionDTO;
import com.meditate.app_meditation.meditation.entity.MeditationRoutine;
import com.meditate.app_meditation.meditation.repository.RoutineRepository;
import com.meditate.app_meditation.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineGeneratorService {

    private final OpenAiService openAiService;
    private final RoutineRepository routineRepository;

    @Transactional
    @com.meditate.app_meditation.common.annotation.RequiresPremium
    public RoutineResponseDTO generateRoutine(User user, RoutineGenerationRequest request) {
        String prompt = String.format(
                "Generate a meditation routine for a user with stress level: %s. " +
                "Duration: %d days. Preference: %s. " +
                "Provide a title, description, and a list of techniques.",
                request.stressLevel(), request.totalDurationDays(), request.preference()
        );

        MeditationRoutineResponse aiResponse = openAiService.generateStructuredContent(
                prompt, MeditationRoutineResponse.class
        );

        MeditationRoutine routine = MeditationRoutine.builder()
                .user(user)
                .title(aiResponse.title())
                .description(aiResponse.description())
                .focusArea(MeditationRoutine.FocusArea.valueOf(request.preference().name()))
                .aiGenerated(true)
                .techniques(aiResponse.sessions().stream()
                        .map(s -> s.title() + ": " + s.script())
                        .collect(Collectors.toList()))
                .build();

        MeditationRoutine saved = routineRepository.save(routine);

        return mapToDTO(saved);
    }

    private RoutineResponseDTO mapToDTO(MeditationRoutine routine) {
        return new RoutineResponseDTO(
                routine.getId(),
                routine.getTitle(),
                routine.getDescription(),
                com.meditate.app_meditation.meditation.entity.RoutineType.valueOf(routine.getFocusArea().name()),
                routine.getTechniques().stream()
                        .map(t -> new SessionDTO(null, t, "", 0))
                        .toList()
        );
    }
}
