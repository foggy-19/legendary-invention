package com.panonit.ingestion_service.simulation;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

public abstract class DataSimulator implements CommandLineRunner {

    protected final Random random = new Random();
    protected final RestTemplate restTemplate = new RestTemplate();

    @Value("${simulation.endpoint}")
    protected String ingestionEndpoint;

    @Value("${simulation.requests-per-interval}")
    protected int requestsPerInterval;

    @Override
    public void run(String @NonNull ... args) {

    }

    public abstract void execute();
}
