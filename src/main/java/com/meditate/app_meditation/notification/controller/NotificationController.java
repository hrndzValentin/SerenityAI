package com.meditate.app_meditation.notification.controller;

import com.meditate.app_meditation.common.dto.ApiResponse;
import com.meditate.app_meditation.notification.entity.NotificationPreferences;
import com.meditate.app_meditation.notification.entity.UserDeviceToken;
import com.meditate.app_meditation.notification.repository.NotificationPreferencesRepository;
import com.meditate.app_meditation.notification.repository.UserDeviceTokenRepository;
import com.meditate.app_meditation.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Endpoints for FCM tokens and preferences")
public class NotificationController {

    private final UserDeviceTokenRepository tokenRepository;
    private final NotificationPreferencesRepository preferencesRepository;

    @Operation(summary = "Register a new device token for push notifications")
    @PostMapping("/token")
    public ResponseEntity<ApiResponse<Void>> registerToken(
            @AuthenticationPrincipal User user,
            @RequestParam String token,
            @RequestParam(required = false) String deviceType) {
        
        UserDeviceToken deviceToken = UserDeviceToken.builder()
                .user(user)
                .fcmToken(token)
                .deviceType(deviceType)
                .lastUsedAt(LocalDateTime.now())
                .build();
        
        tokenRepository.save(deviceToken);
        return ResponseEntity.ok(ApiResponse.success(null, "Token registered successfully"));
    }

    @Operation(summary = "Update notification preferences")
    @PutMapping("/preferences")
    public ResponseEntity<ApiResponse<NotificationPreferences>> updatePreferences(
            @AuthenticationPrincipal User user,
            @RequestBody NotificationPreferences newPrefs) {
        
        NotificationPreferences prefs = preferencesRepository.findByUserId(user.getId())
                .orElse(NotificationPreferences.builder().user(user).build());
        
        prefs.setBreathingEnabled(newPrefs.isBreathingEnabled());
        prefs.setMeditationEnabled(newPrefs.isMeditationEnabled());
        prefs.setQuietHoursStart(newPrefs.getQuietHoursStart());
        prefs.setQuietHoursEnd(newPrefs.getQuietHoursEnd());
        
        return ResponseEntity.ok(ApiResponse.success(preferencesRepository.save(prefs), "Preferences updated"));
    }
}
