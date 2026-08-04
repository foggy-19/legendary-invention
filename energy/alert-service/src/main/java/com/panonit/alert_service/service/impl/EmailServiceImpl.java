package com.panonit.alert_service.service.impl;

import com.panonit.alert_service.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender sender;
    private final String noReply;

    public EmailServiceImpl(JavaMailSender sender, @Value("${mail.sender}") String noReply) {
        this.sender = sender;
        this.noReply = noReply;
    }

    @Override
    public boolean send(String email, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(noReply);
        message.setSubject(subject);
        message.setText(content);

        try {
            sender.send(message);
            return true;
        } catch (MailException ex) {
            return false;
        }
    }
}
