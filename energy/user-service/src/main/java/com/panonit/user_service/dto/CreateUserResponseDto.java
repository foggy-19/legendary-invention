package com.panonit.user_service.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserResponseDto {
    private Long id;
    String firstName;
    String lastName;
    String email;
    String address;
    private Boolean notifications;
    private Double energyAlertingThreshold;
}
