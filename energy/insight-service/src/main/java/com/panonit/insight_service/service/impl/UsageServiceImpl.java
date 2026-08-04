package com.panonit.insight_service.service.impl;

import com.panonit.insight_service.dto.UsageDto;
import com.panonit.insight_service.service.UsageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class UsageServiceImpl implements UsageService {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    public UsageServiceImpl(@Value("${usage.service.url}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public UsageDto getUsage(long userId, int days) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/{userId}")
                .queryParam("days", days)
                .buildAndExpand(userId)
                .toString();

        ResponseEntity<UsageDto> response = restTemplate.getForEntity(url, UsageDto.class);

        return response.getBody();
    }
}
