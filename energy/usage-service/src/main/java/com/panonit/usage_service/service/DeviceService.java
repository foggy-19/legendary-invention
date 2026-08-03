package com.panonit.usage_service.service;

import com.panonit.usage_service.dto.DeviceDto;

public interface DeviceService {

    DeviceDto getDeviceById(Long deviceId);
}
