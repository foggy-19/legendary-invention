package com.panonit.device_service.service;

import com.panonit.device_service.dto.*;

import java.util.List;

public interface DeviceService {

    CreateDeviceResponseDto createDevice(CreateDeviceRequestDto createDeviceRequestDto);

    GetDeviceResponseDto getDevice(Long id);

    UpdateDeviceResponseDto updateDevice(Long id, UpdateDeviceRequestDto updateDeviceRequestDto);

    void deleteDevice(Long id);

    List<DeviceDto> getUserDevices(Long userId);
}
