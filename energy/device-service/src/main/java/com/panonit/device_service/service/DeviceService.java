package com.panonit.device_service.service;

import com.panonit.device_service.dto.*;

import java.util.Optional;

public interface DeviceService {

    CreateDeviceResponseDto createDevice(CreateDeviceRequestDto createDeviceRequestDto);

    Optional<GetDeviceResponseDto> getDevice(Long id);

    UpdateDeviceResponseDto updateDevice(Long id, UpdateDeviceRequestDto updateDeviceRequestDto);

    void deleteDevice(Long id);
}
