package com.panonit.user_service.service;

import com.panonit.user_service.dto.*;

import java.util.Optional;

public interface UserService {

    CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto);

    Optional<GetUserResponseDto> getUser(Long id);

    UpdateUserResponseDto updateUser(Long id, UpdateUserRequestDto updateUserRequestDto);

    void deleteUser(Long id);
}
