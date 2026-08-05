package com.panonit.usage_service.service;

import com.panonit.usage_service.dto.DeviceDto;

import java.util.List;

public interface DeviceService {

    DeviceDto getDeviceById(Long deviceId);

    List<DeviceDto> getAllDevicesForUser(Long userId);
}
