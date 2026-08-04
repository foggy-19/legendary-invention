package com.panonit.insight_service.dto;

public record ErrorResponseDto(
        int status,
        String message
) {
}
