package com.panonit.device_service.dto;

import com.panonit.device_service.model.DeviceType;

public record DeviceDto(
        String id,
        String name,
        DeviceType type,
        String location,
        Long userId
) {
}