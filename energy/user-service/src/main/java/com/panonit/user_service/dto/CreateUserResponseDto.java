package com.panonit.user_service.dto;

public record CreateUserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String address,
        Boolean notifications,
        Double energyAlertingThreshold
) {
}
