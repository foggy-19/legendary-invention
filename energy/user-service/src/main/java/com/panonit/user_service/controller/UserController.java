package com.panonit.user_service.controller;

import com.panonit.user_service.dto.*;
import com.panonit.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<CreateUserResponseDto> createUser(@RequestBody CreateUserRequestDto request) {
        log.info("createUser");

        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UpdateUserResponseDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        log.info("updateUser");

        return ResponseEntity.ok(service.update(id, updateUserRequestDto));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GetUserResponseDto> getUser(@PathVariable Long id) {
        log.info("getUser");

        return service.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("deleteUser");

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
