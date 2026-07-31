package com.panonit.ingestion_service.simulation.impl;

import com.panonit.ingestion_service.dto.CreateEnergyUsageRequestDto;
import com.panonit.ingestion_service.simulation.DataSimulator;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class ParallelDataSimulatorImpl extends DataSimulator {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Value("${simulation.parallel-threads}")
    private int parallelThreads;

    @Override
    public void run(String @NonNull ... args) {
        log.info("Parallel simulation started ...");

        ((ThreadPoolExecutor) executor).setCorePoolSize(parallelThreads);
    }

    @Override
    @Scheduled(fixedRateString = "${simulation.interval-ms}")
    public void execute() {
        int batch = requestsPerInterval / parallelThreads;
        int reminder = requestsPerInterval % parallelThreads;

        for (int i = 0; i < parallelThreads; i++) {
            int thread = i;
            int requestsPerThread = batch + (i < reminder ? 1 : 0);
            executor.submit(() -> sendData(thread, requestsPerThread));
        }
    }

    @PreDestroy
    private void shutdown() {
        log.info("Parallel simulation stopped ...");
        executor.shutdown();
    }

    private void sendData(final int thread, final int requestCount) {
        log.info("[thread-{}] -> [burst] -> [{} requests] -> [started]", thread, requestCount);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        for (int i = 0; i < requestCount; i++) {
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

        log.info("[thread-{}] -> [burst] -> [done]", thread);
    }
}
