package com.panonit.insight_service.service.impl;

import com.panonit.insight_service.dto.InsightDto;
import com.panonit.insight_service.service.InsightService;
import org.springframework.stereotype.Service;

@Service
public class InsightServiceImpl implements InsightService {

    @Override
    public InsightDto getOverview(Long userId) {
        return null;
    }

    @Override
    public InsightDto getSavingTips(Long userId) {
        return null;
    }
}
