package com.panonit.user_service.dto;


public record CreateUserRequestDto(
        String firstName,
        String lastName,
        String email,
        String address,
        Boolean notifications,
        Double energyAlertingThreshold
) {
}
