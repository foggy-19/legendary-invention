package com.panonit.user_service.service.impl;

import com.panonit.user_service.dto.*;
import com.panonit.user_service.entity.User;
import com.panonit.user_service.mapper.UserMapper;
import com.panonit.user_service.repository.UserRepository;
import com.panonit.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper mapper;
    private final UserRepository repository;

    @Override
    @Transactional
    public CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        if (repository.existsByEmail(createUserRequestDto.getEmail())) {
            throw new IllegalArgumentException("User already exists");
        }

        User created = repository.save(mapper.toEntity(createUserRequestDto));

        return mapper.toCreateUserResponseDto(created);
    }

    @Override
    public Optional<GetUserResponseDto> getUser(Long id) {
        return repository.findById(id).map(mapper::toGetUserResponseDto);
    }


    @Override
    @Transactional
    public UpdateUserResponseDto updateUser(Long id, UpdateUserRequestDto updateUserRequestDto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User with ID %s not found", id)));

        user.setFirstName(updateUserRequestDto.getFirstName());
        user.setLastName(updateUserRequestDto.getLastName());
        user.setEmail(updateUserRequestDto.getEmail());
        user.setAddress(updateUserRequestDto.getAddress());
        user.setAlerting(updateUserRequestDto.getNotifications());
        user.setEnergyAlertingThreshold(updateUserRequestDto.getEnergyAlertingThreshold());

        User updated = repository.save(user);

        return mapper.toUpdateUserResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User with ID %s not found", id)));

        repository.delete(user);
    }
}
