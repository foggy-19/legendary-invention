package com.panonit.device_service.service.impl;

import com.panonit.device_service.dto.*;
import com.panonit.device_service.entity.Device;
import com.panonit.device_service.mapper.DeviceMapper;
import com.panonit.device_service.repository.DeviceRepository;
import com.panonit.device_service.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceMapper mapper;
    private final DeviceRepository repository;

    @Override
    @Transactional
    public CreateDeviceResponseDto createDevice(CreateDeviceRequestDto createDeviceRequestDto) {
        final Device created = repository.save(mapper.toEntity(createDeviceRequestDto));

        return mapper.toCreateDeviceResponseDto(created);
    }

    @Override
    public Optional<GetDeviceResponseDto> getDevice(Long id) {
        return repository.findById(id).map(mapper::toGetDeviceResponseDto);
    }

    @Override
    @Transactional
    public UpdateDeviceResponseDto updateDevice(Long id, UpdateDeviceRequestDto updateDeviceRequestDto) {
        Device device = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Device with ID %s not found", id)));

        device.setName(updateDeviceRequestDto.getName());
        device.setType(updateDeviceRequestDto.getType());
        device.setLocation(updateDeviceRequestDto.getLocation());

        final Device updated = repository.save(device);

        return mapper.toUpdateDeviceResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteDevice(Long id) {
        Device device = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Device with ID %s not found", id)));

        repository.delete(device);
    }
}
