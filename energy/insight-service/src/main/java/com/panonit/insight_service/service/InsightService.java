package com.panonit.insight_service.service;

import com.panonit.insight_service.dto.InsightDto;

public interface InsightService {

    InsightDto getOverview(Long userId);

    InsightDto getSavingTips(Long userId);
}
