package com.meditate.app_meditation.meditation.controller;

import com.meditate.app_meditation.common.annotation.RequiresPremium;
import com.meditate.app_meditation.common.dto.ApiResponse;
import com.meditate.app_meditation.meditation.dto.RoutineGenerationRequest;
import com.meditate.app_meditation.meditation.dto.RoutineResponseDTO;
import com.meditate.app_meditation.meditation.entity.MeditationRoutine;
import com.meditate.app_meditation.meditation.repository.RoutineRepository;
import com.meditate.app_meditation.meditation.service.RoutineGeneratorService;
import com.meditate.app_meditation.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meditations")
@RequiredArgsConstructor
@Tag(name = "Meditation", description = "Endpoints for managing meditation routines")
public class MeditationController {

    private final RoutineGeneratorService routineGeneratorService;
    private final RoutineRepository routineRepository;

    @Operation(summary = "Generate a new AI meditation routine")
    @PostMapping("/generate")
    @RequiresPremium
    public ResponseEntity<ApiResponse<RoutineResponseDTO>> generate(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody RoutineGenerationRequest request) {
        
        RoutineResponseDTO routine = routineGeneratorService.generateRoutine(user, request);
        return ResponseEntity.ok(ApiResponse.success(routine, "Routine generated successfully"));
    }

    @Operation(summary = "Get user's meditation routines")
    @GetMapping
    public ResponseEntity<ApiResponse<List<MeditationRoutine>>> getRoutines(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        
        List<MeditationRoutine> routines = routineRepository.findByUserId(user.getId());
        return ResponseEntity.ok(ApiResponse.success(routines, "Routines retrieved successfully"));
    }

    @Operation(summary = "Get single routine details")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MeditationRoutine>> getById(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        return routineRepository.findByIdAndUserId(id, user.getId())
                .map(r -> ResponseEntity.ok(ApiResponse.success(r, "Routine found")))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a routine")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        routineRepository.deleteByIdAndUserId(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "Routine deleted"));
    }
}
