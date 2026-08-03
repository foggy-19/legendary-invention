package com.panonit.kafka.event;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record EnergyUsageEvent(
        Long deviceId,
        Double consumption,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Instant timestamp
) {
}
