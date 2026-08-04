package com.panonit.usage_service.service;

import com.panonit.kafka.event.EnergyUsageEvent;
import com.panonit.usage_service.dto.GetUserDeviceUsageDto;

public interface UsageService {

    void onEnergyUsageEvent(EnergyUsageEvent event);

    GetUserDeviceUsageDto getUserDeviceUsageForDays(Long userId, int days);
}
