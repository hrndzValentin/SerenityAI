package com.meditate.app_meditation.meditation.controller;

import com.meditate.app_meditation.meditation.dto.RoutineGenerationRequest;
import com.meditate.app_meditation.meditation.dto.RoutineResponseDTO;
import com.meditate.app_meditation.meditation.service.RoutineGeneratorService;
import com.meditate.app_meditation.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/meditation")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineGeneratorService routineService;

    @PostMapping("/generate")
    public ResponseEntity<RoutineResponseDTO> generateRoutine(
            @AuthenticationPrincipal User user,
            @RequestBody RoutineGenerationRequest request
    ) {
        return ResponseEntity.ok(routineService.generateRoutine(user, request));
    }
}
