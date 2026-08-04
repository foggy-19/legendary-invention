package com.panonit.insight_service.controller;

import com.panonit.insight_service.dto.InsightDto;
import com.panonit.insight_service.service.InsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/insight")
@RequiredArgsConstructor
public class InsightController {

    private final InsightService service;

    @GetMapping(path = "/overview/{userId}")
    public ResponseEntity<InsightDto> getOverview(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getOverview(userId));
    }

    @GetMapping(path = "/saving-tips/{userId}")
    public ResponseEntity<InsightDto> savingTips(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getSavingTips(userId));
    }
}
