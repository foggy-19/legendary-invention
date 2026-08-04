package com.panonit.insight_service.service;

import com.panonit.insight_service.dto.UsageDto;

public interface UsageService {

    UsageDto getUsage(long userId, int days);
}
