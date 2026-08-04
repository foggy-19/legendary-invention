package com.panonit.usage_service.service.impl;

import com.panonit.kafka.event.AlertingEvent;
import com.panonit.usage_service.service.AlertingService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertingServiceImpl implements AlertingService {

    private static final String ENERGY_ALERTS_TOPIC = "energy-alerts";

    private final KafkaTemplate<String, AlertingEvent> kafka;

    public AlertingServiceImpl(KafkaTemplate<String, AlertingEvent> kafka) {
        this.kafka = kafka;
    }

    @Override
    public void publish(List<AlertingEvent> alerts) {
        alerts.forEach(alert -> kafka.send(ENERGY_ALERTS_TOPIC, alert));
    }
}
