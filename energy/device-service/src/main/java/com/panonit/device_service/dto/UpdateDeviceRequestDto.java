package com.panonit.device_service.dto;

import com.panonit.device_service.model.DeviceType;

public record UpdateDeviceRequestDto(
        String name,
        DeviceType type,
        String location
) {
}