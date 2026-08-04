package com.panonit.alert_service.service.impl;

import com.panonit.alert_service.entity.Alert;
import com.panonit.alert_service.repository.AlertRepository;
import com.panonit.alert_service.service.AlertService;
import com.panonit.alert_service.service.EmailService;
import com.panonit.kafka.event.AlertingEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final EmailService service;
    private final AlertRepository repository;

    @Override
    @KafkaListener(topics = "energy-alerts", groupId = "alert-service")
    public void onAlertingEvent(AlertingEvent event) {
        var subject = String.format("Energy Alert - %s", event.userId());
        var content = String.format("Alert: %s\nThreshold: %s\nEnergy consumed: %s", event.message(), event.threshold(), event.energyConsumed());

        var sent = service.send(event.email(), subject, content);

        Alert alert = Alert.builder()
                .sent(sent)
                .userId(event.userId())
                .createdAt(LocalDateTime.now())
                .build();

        repository.saveAndFlush(alert);
    }
}
