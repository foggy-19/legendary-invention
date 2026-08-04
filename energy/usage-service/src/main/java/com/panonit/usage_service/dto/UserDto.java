package com.panonit.usage_service.dto;

public record UserDto(
        Long id,
        String email,
        Boolean notifications,
        Double energyAlertingThreshold
) {
}
