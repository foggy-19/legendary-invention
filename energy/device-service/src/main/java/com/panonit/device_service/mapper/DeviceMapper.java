package com.panonit.device_service.mapper;

import com.panonit.device_service.dto.*;
import com.panonit.device_service.entity.Device;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceMapper {

    Device toEntity(CreateDeviceRequestDto createDeviceRequestDto);

    CreateDeviceResponseDto toCreateDeviceResponseDto(Device device);

    UpdateDeviceResponseDto toUpdateDeviceResponseDto(Device device);

    GetDeviceResponseDto toGetDeviceResponseDto(Device device);

    DeviceDto toDeviceDto(Device device);
}
