package com.api_colllab.api_collab.controller.dto.errors;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthErrorController {
    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<AuthErrorDto> runtimeExceptionHanlder(BadCredentialsException ex){
        AuthErrorDto error = AuthErrorDto.builder()
                .code("P-500")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
