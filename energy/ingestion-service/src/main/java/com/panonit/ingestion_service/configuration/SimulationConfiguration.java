package com.panonit.ingestion_service.configuration;

import com.panonit.ingestion_service.simulation.DataSimulator;
import com.panonit.ingestion_service.simulation.impl.ParallelDataSimulatorImpl;
import com.panonit.ingestion_service.simulation.impl.SequentialDataSimulatorImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimulationConfiguration {

    @Bean
    @ConditionalOnProperty(name = "simulation.mode", havingValue = "parallel")
    public DataSimulator parallelSimulator() {
        return new ParallelDataSimulatorImpl();
    }

    @Bean
    @ConditionalOnProperty(name = "simulation.mode", havingValue = "sequential")
    public DataSimulator sequentialSimulator() {
        return new SequentialDataSimulatorImpl();
    }
}
