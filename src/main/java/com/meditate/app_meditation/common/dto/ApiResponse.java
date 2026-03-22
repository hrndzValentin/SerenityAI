package com.meditate.app_meditation.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    boolean success,
    String message,
    T data,
    LocalDateTime timestamp,
    PaginationMetadata pagination
) {
    public ApiResponse {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data, LocalDateTime.now(), null);
    }

    public static <T> ApiResponse<T> success(T data, String message, PaginationMetadata pagination) {
        return new ApiResponse<>(true, message, data, LocalDateTime.now(), pagination);
    }

    public record PaginationMetadata(
        int page,
        int size,
        long totalElements,
        int totalPages
    ) {}
}
