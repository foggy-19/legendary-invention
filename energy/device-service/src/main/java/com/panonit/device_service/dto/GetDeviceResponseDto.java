package com.panonit.device_service.dto;

import com.panonit.device_service.model.DeviceType;

public record GetDeviceResponseDto(
        String id,
        String name,
        DeviceType type,
        String location
) {
}