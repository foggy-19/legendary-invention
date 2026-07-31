package com.panonit.ingestion_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record CreateEnergyUsageRequestDto(
        Long deviceId,
        Double consumption,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Instant timestamp
) {
}

