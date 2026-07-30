package com.panonit.device_service.dto;

import com.panonit.device_service.model.DeviceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDeviceResponseDto {
    private String id;
    private String name;
    private DeviceType type;
    private String location;
}
