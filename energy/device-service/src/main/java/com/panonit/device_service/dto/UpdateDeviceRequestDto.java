package com.panonit.device_service.dto;

import com.panonit.device_service.model.DeviceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDeviceRequestDto {
    String name;
    DeviceType type;
    String location;
}
