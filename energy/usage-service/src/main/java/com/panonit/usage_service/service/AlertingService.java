package com.panonit.usage_service.service;

import com.panonit.kafka.event.AlertingEvent;

import java.util.List;

public interface AlertingService {

    void publish(List<AlertingEvent> events);
}
