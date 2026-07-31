package com.panonit.user_service.dto;

public record ErrorResponseDto(
        int status,
        String message
) {
}
