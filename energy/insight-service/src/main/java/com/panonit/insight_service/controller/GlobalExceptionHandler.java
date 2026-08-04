package com.panonit.insight_service.controller;


import com.panonit.insight_service.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        return new ResponseEntity<>(
                new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected failure"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
