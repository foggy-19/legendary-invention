package com.panonit.user_service.service;

import com.panonit.user_service.dto.*;

import java.util.Optional;

public interface UserService {

    CreateUserResponseDto create(CreateUserRequestDto createUserRequestDto);

    UpdateUserResponseDto update(Long id, UpdateUserRequestDto updateUserRequestDto);

    Optional<GetUserResponseDto> get(Long id);

    void delete(Long id);
}
