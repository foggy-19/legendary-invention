package com.panonit.user_service.dto;

public record UpdateUserRequestDto(
        String firstName,
        String lastName,
        String email,
        String address,
        Boolean notifications,
        Double energyAlertingThreshold
) {
}
