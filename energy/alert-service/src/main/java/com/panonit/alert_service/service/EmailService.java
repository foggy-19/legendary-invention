package com.panonit.alert_service.service;

public interface EmailService {

    boolean send(String email, String subject, String content);
}
