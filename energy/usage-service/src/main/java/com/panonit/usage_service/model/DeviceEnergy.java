package com.panonit.usage_service.model;

public record DeviceEnergy(
        Long userId,
        Long deviceId,
        Double energyConsumed
) {
}
