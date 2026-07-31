package com.panonit.user_service.service;

import com.panonit.user_service.dto.*;

public interface UserService {

    CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto);

    GetUserResponseDto getUser(Long id);

    UpdateUserResponseDto updateUser(Long id, UpdateUserRequestDto updateUserRequestDto);

    void deleteUser(Long id);
}
