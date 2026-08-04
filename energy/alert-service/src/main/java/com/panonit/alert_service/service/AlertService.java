package com.panonit.alert_service.service;

import com.panonit.kafka.event.AlertingEvent;

public interface AlertService {

    void onAlertingEvent(AlertingEvent event);
}
