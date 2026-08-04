package com.panonit.usage_service.service;

import com.panonit.usage_service.dto.UserDto;

public interface UserService {

    UserDto getUserById(Long userId);
}
