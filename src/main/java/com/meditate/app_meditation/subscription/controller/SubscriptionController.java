package com.meditate.app_meditation.subscription.controller;

import com.meditate.app_meditation.common.dto.ApiResponse;
import com.meditate.app_meditation.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
@Tag(name = "Subscription", description = "Endpoints for plans and Stripe integration")
public class SubscriptionController {

    @Operation(summary = "List available subscription plans")
    @GetMapping("/plans")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> getPlans() {
        var plans = List.of(
                Map.of("id", "free", "name", "Free", "price", "0"),
                Map.of("id", "premium", "name", "Premium", "price", "9.99"),
                Map.of("id", "enterprise", "name", "Enterprise", "price", "29.99")
        );
        return ResponseEntity.ok(ApiResponse.success(plans, "Plans retrieved"));
    }

    @Operation(summary = "Get current user subscription status")
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<User.SubscriptionStatus>> getStatus(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(user.getSubscriptionStatus(), "Status retrieved"));
    }

    @Operation(summary = "Create a Stripe checkout session")
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<String>> checkout(@RequestParam String planId) {
        // Stripe integration logic
        return ResponseEntity.ok(ApiResponse.success("https://checkout.stripe.com/session_id", "Checkout URL generated"));
    }
}
