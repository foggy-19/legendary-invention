package com.panonit.device_service.dto;

public record ErrorResponseDto(
        int status,
        String message
) {
}