package com.panonit.usage_service.service.impl;

import com.panonit.usage_service.dto.DeviceDto;
import com.panonit.usage_service.service.DeviceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    public DeviceServiceImpl(@Value("${device.service.url}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public DeviceDto getDeviceById(Long deviceId) {
        String url = String.format("%s/%s", baseUrl, deviceId);
        ResponseEntity<DeviceDto> response = restTemplate.getForEntity(url, DeviceDto.class);

        return response.getBody();
    }
}
