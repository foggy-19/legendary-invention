package com.panonit.ingestion_service.service.impl;

import com.panonit.ingestion_service.dto.CreateEnergyUsageRequestDto;
import com.panonit.ingestion_service.mapper.EnergyUsageMapper;
import com.panonit.ingestion_service.service.IngestionService;
import com.panonit.kafka.event.EnergyUsageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngestionServiceImpl implements IngestionService {

    private static final String ENERGY_USAGE_TOPIC = "energy-usage";

    private final EnergyUsageMapper mapper;
    private final KafkaTemplate<String, EnergyUsageEvent> kafka;

    @Override
    public void create(CreateEnergyUsageRequestDto createEnergyUsageRequestDto) {
        kafka.send(ENERGY_USAGE_TOPIC, mapper.toEvent(createEnergyUsageRequestDto));
    }
}
