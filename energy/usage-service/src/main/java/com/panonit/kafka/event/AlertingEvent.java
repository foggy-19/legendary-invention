package com.panonit.kafka.event;

public record AlertingEvent(
        Long userId,
        String email,
        Double threshold,
        Double energyConsumed,
        String message
) {
}
