package com.panonit.usage_service.service.impl;

import com.panonit.kafka.event.EnergyUsageEvent;
import com.panonit.usage_service.service.UsageService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {

    @KafkaListener(topics = "energy-usage", groupId = "usage-service")
    public void onEnergyUsageEvent(EnergyUsageEvent energyUsageEvent) {

    }
}
