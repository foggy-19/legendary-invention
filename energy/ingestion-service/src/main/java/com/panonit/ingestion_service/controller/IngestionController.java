package com.panonit.ingestion_service.controller;

import com.panonit.ingestion_service.dto.CreateEnergyUsageRequestDto;
import com.panonit.ingestion_service.service.IngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/ingestion")
@RequiredArgsConstructor
public class IngestionController {

    private final IngestionService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void ingest(@RequestBody CreateEnergyUsageRequestDto createEnergyUsageRequestDto) {
        service.create(createEnergyUsageRequestDto);
    }
}
