package com.panonit.usage_service.service.impl;

import com.panonit.usage_service.dto.UserDto;
import com.panonit.usage_service.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImpl implements UserService {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    public UserServiceImpl(@Value("${user.service.url}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public UserDto getUserById(Long userId) {
        String url = String.format("%s/%s", this.baseUrl, userId);

        ResponseEntity<UserDto> response = restTemplate.getForEntity(url, UserDto.class);
        return response.getBody();
    }
}
