package com.panonit.user_service.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequestDto {
    String firstName;
    String lastName;
    String email;
    String address;
    private Boolean notifications;
    private Double energyAlertingThreshold;
}
