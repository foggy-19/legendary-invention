package com.panonit.ingestion_service.simulation.impl;

import com.panonit.ingestion_service.dto.CreateEnergyUsageRequestDto;
import com.panonit.ingestion_service.simulation.DataSimulator;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;

@Slf4j
public class SequentialDataSimulatorImpl extends DataSimulator {

    @Override
    public void run(String @NonNull ... args) {
        log.info("Sequential simulation started ...");
    }

    @Override
    @Scheduled(fixedRateString = "${simulation.interval-ms}")
    public void execute() {
        sendData();
    }

    @PreDestroy
    private void shutdown() {
        log.info("Sequential simulation stopped ...");
    }

    private void sendData() {
        log.info("[burst] -> [started]");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        for (int i = 0; i < requestsPerInterval; i++) {
            Long deviceId = random.nextLong(1, 100);
            Double consumption = Math.round(random.nextDouble(0.0, 2.0) * 100.0) / 100.0;
            Instant timestamp = Instant.now();

            CreateEnergyUsageRequestDto dto = new CreateEnergyUsageRequestDto(deviceId, consumption, timestamp);

            try {
                restTemplate.postForEntity(ingestionEndpoint, new HttpEntity<>(dto, headers), Void.class);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        log.info("[burst] -> [done]");
    }
}
