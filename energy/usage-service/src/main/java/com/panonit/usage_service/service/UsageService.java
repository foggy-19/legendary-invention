package com.panonit.usage_service.service;

import com.panonit.kafka.event.EnergyUsageEvent;

public interface UsageService {

    void onEnergyUsageEvent(EnergyUsageEvent event);
}
