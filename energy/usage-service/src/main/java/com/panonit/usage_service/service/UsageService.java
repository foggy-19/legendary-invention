package com.panonit.usage_service.service;

import com.panonit.kafka.event.EnergyUsageEvent;
import com.panonit.usage_service.dto.UsageDto;

public interface UsageService {

    void onEnergyUsageEvent(EnergyUsageEvent event);

    UsageDto getXDaysUsageForUser(Long userId, int days);
}
