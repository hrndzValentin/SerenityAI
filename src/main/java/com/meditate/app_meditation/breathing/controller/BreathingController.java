package com.meditate.app_meditation.breathing.controller;

import com.meditate.app_meditation.breathing.entity.BreathingExercise;
import com.meditate.app_meditation.breathing.service.BreathingExerciseService;
import com.meditate.app_meditation.common.dto.ApiResponse;
import com.meditate.app_meditation.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/breathing")
@RequiredArgsConstructor
@Tag(name = "Breathing", description = "Endpoints for breathing exercises and patterns")
public class BreathingController {

    private final BreathingExerciseService breathingService;

    @Operation(summary = "Get today's scheduled breathing exercises")
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<BreathingExercise>>> getToday(
            @AuthenticationPrincipal User user) {
        List<BreathingExercise> exercises = breathingService.getExercisesForTimeOfDay(user.getId(), LocalTime.now());
        return ResponseEntity.ok(ApiResponse.success(exercises, "Today's exercises retrieved"));
    }

    @Operation(summary = "Log completion of a breathing session")
    @PostMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<Void>> complete(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @RequestParam int durationSeconds) {
        breathingService.completeSession(user, id, durationSeconds);
        return ResponseEntity.ok(ApiResponse.success(null, "Breathing session logged"));
    }

    @Operation(summary = "Get user breathing stats and streaks")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<?>> getStats(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(breathingService.getStreakData(user.getId()), "Stats retrieved"));
    }
}
