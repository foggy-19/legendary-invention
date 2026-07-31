package com.panonit.device_service.controller;

import com.panonit.device_service.dto.*;
import com.panonit.device_service.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService service;

    @PostMapping()
    public ResponseEntity<CreateDeviceResponseDto> createDevice(@RequestBody CreateDeviceRequestDto createDeviceRequestDto) {
        return new ResponseEntity<>(service.createDevice(createDeviceRequestDto), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GetDeviceResponseDto> getDevice(@PathVariable Long id) {
        return ResponseEntity.ok(service.getDevice(id));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UpdateDeviceResponseDto> updateDevice(@PathVariable Long id, @RequestBody UpdateDeviceRequestDto updateDeviceRequestDto) {
        return ResponseEntity.ok(service.updateDevice(id, updateDeviceRequestDto));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        service.deleteDevice(id);

        return ResponseEntity.noContent().build();
    }
}
