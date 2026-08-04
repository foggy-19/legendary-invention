package com.panonit.insight_service.dto;

public record InsightDto(
        Long userId,
        String tips,
        Double energyUsage
) {
}
