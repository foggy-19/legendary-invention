package com.panonit.ingestion_service.service.impl;

import com.panonit.ingestion_service.dto.CreateEnergyUsageRequestDto;
import com.panonit.ingestion_service.service.IngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngestionServiceImpl implements IngestionService {

    @Override
    public void create(CreateEnergyUsageRequestDto createEnergyUsageRequestDto) {
        // todo
    }
}
