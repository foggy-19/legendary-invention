package com.panonit.insight_service.service.impl;

import com.panonit.insight_service.dto.DeviceDto;
import com.panonit.insight_service.dto.InsightDto;
import com.panonit.insight_service.dto.UsageDto;
import com.panonit.insight_service.service.InsightService;
import com.panonit.insight_service.service.UsageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsightServiceImpl implements InsightService {

    private final UsageService service;
    private final OllamaChatModel ollama;

    @Override
    public InsightDto getOverview(Long userId) {
        final UsageDto usage = service.getUsage(userId, 3);
        double total = usage.devices().stream().mapToDouble(DeviceDto::energyConsumed).sum();

        ChatResponse response = ollama.call(getOverviewPrompt(usage.devices()));
        String tips = response.getResult() == null ? "No overview" : response.getResult().getOutput().getText();

        return new InsightDto(userId, tips, total);
    }

    @Override
    public InsightDto getSavingTips(Long userId) {
        final UsageDto usage = service.getUsage(userId, 3);
        double total = usage.devices().stream().mapToDouble(DeviceDto::energyConsumed).sum();

        ChatResponse response = ollama.call(getSavingPrompt(total));
        String tips = response.getResult() == null ? "No good tips" : response.getResult().getOutput().getText();

        return new InsightDto(userId, tips, total);
    }

    private Prompt getOverviewPrompt(List<DeviceDto> devices) {
        String content = "Analyze the following energy usage data and provide a concise overview with actionable insights." +
                "This data is the aggregate data for the past 3 days." +
                "Usage data: \n" +
                devices;

        return Prompt.builder().content(content).build();
    }

    private Prompt getSavingPrompt(double total) {
        String content = "This is my total consumption over the past 3 days." +
                "How can I reduce my energy consumption?" +
                "How does it compare to average households?" +
                "Total energy used: \n" +
                total;

        return Prompt.builder().content(content).build();
    }
}
