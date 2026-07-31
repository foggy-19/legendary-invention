package com.panonit.user_service.controller;

import com.panonit.user_service.dto.*;
import com.panonit.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<CreateUserResponseDto> createUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        return new ResponseEntity<>(service.createUser(createUserRequestDto), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GetUserResponseDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUser(id));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UpdateUserResponseDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        return ResponseEntity.ok(service.updateUser(id, updateUserRequestDto));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
