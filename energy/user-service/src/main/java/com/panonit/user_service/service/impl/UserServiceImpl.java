package com.panonit.user_service.service.impl;

import com.panonit.user_service.dto.*;
import com.panonit.user_service.entity.User;
import com.panonit.user_service.mapper.UserMapper;
import com.panonit.user_service.repository.UserRepository;
import com.panonit.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper mapper;
    private final UserRepository repository;

    @Override
    @Transactional
    public CreateUserResponseDto create(CreateUserRequestDto createUserRequestDto) {
        log.info("create {}", createUserRequestDto.getEmail());

        if (repository.existsByEmail(createUserRequestDto.getEmail())) {
            throw new IllegalArgumentException("User already exists");
        }

        User created = repository.save(mapper.toEntity(createUserRequestDto));

        return mapper.toCreateUserResponseDto(created);
    }

    @Override
    @Transactional
    public UpdateUserResponseDto update(Long id, UpdateUserRequestDto updateUserRequestDto) {
        log.info("update {}", updateUserRequestDto.getEmail());

        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User with ID %s not found", id)));

        user.setFirstName(updateUserRequestDto.getFirstName());
        user.setLastName(updateUserRequestDto.getLastName());
        user.setEmail(updateUserRequestDto.getEmail());
        user.setAddress(updateUserRequestDto.getAddress());
        user.setAlerting(updateUserRequestDto.getNotifications());
        user.setEnergyAlertingThreshold(updateUserRequestDto.getEnergyAlertingThreshold());

        return mapper.toUpdateUserResponseDto(repository.save(user));
    }

    @Override
    @Transactional
    public Optional<GetUserResponseDto> get(Long id) {
        log.info("get {}", id);

        return repository.findById(id).map(mapper::toGetUserResponseDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("delete {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User with ID %s not found", id)));

        repository.delete(user);
    }
}
