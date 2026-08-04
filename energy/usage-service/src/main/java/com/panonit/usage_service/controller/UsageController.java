package com.panonit.usage_service.controller;

import com.panonit.usage_service.dto.GetUserDeviceUsageDto;
import com.panonit.usage_service.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/usage")
@RequiredArgsConstructor
public class UsageController {

    private final UsageService service;

    @GetMapping(path = "/{userId}")
    public ResponseEntity<GetUserDeviceUsageDto> getUserDeviceUsageForDays(@PathVariable Long userId, @RequestParam(defaultValue = "3") int days) {
        return ResponseEntity.ok(service.getUserDeviceUsageForDays(userId, days));
    }
}
