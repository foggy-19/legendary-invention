package com.panonit.ingestion_service.service;

import com.panonit.ingestion_service.dto.CreateEnergyUsageRequestDto;

public interface IngestionService {

    void create(CreateEnergyUsageRequestDto createEnergyUsageRequestDto);
}
