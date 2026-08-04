package com.panonit.usage_service.dto;

import java.util.List;

public record GetUserDeviceUsageDto(
        Long userId,
        List<DeviceDto> devices
) {
}
