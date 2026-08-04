package com.panonit.alert_service.service.impl;

import com.panonit.alert_service.service.AlertService;
import com.panonit.kafka.event.AlertingEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AlertServiceImpl implements AlertService {

    @Override
    @KafkaListener(topics = "energy-alerts", groupId = "alert-service")
    public void onAlertingEvent(AlertingEvent event) {

    }
}
