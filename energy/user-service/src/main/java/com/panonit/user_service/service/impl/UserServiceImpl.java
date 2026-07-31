package com.panonit.user_service.service.impl;

import com.panonit.user_service.dto.*;
import com.panonit.user_service.entity.User;
import com.panonit.user_service.exception.UserAlreadyExistsException;
import com.panonit.user_service.exception.UserNotFoundException;
import com.panonit.user_service.mapper.UserMapper;
import com.panonit.user_service.repository.UserRepository;
import com.panonit.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper mapper;
    private final UserRepository repository;

    @Override
    @Transactional
    public CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        if (repository.existsByEmail(createUserRequestDto.email())) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User created = repository.save(mapper.toEntity(createUserRequestDto));

        return mapper.toCreateUserResponseDto(created);
    }

    @Override
    public GetUserResponseDto getUser(Long id) {
        return repository.findById(id).map(mapper::toGetUserResponseDto)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with ID %s not found", id)));
    }


    @Override
    @Transactional
    public UpdateUserResponseDto updateUser(Long id, UpdateUserRequestDto updateUserRequestDto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with ID %s not found", id)));

        user.setFirstName(updateUserRequestDto.firstName());
        user.setLastName(updateUserRequestDto.lastName());
        user.setEmail(updateUserRequestDto.email());
        user.setAddress(updateUserRequestDto.address());
        user.setAlerting(updateUserRequestDto.notifications());
        user.setEnergyAlertingThreshold(updateUserRequestDto.energyAlertingThreshold());

        User updated = repository.save(user);

        return mapper.toUpdateUserResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with ID %s not found", id)));

        repository.delete(user);
    }
}
