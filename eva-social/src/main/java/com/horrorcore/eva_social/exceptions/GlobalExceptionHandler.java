package com.horrorcore.eva_social.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> exceptionHandler(Exception e, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                500,
                URI.create(request.getRequestURI()),
                e.getMessage()
        );

        return ResponseEntity.internalServerError().body(apiError);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiError> exceptionHandler(PostNotFoundException e, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                404,
                URI.create(request.getRequestURI()),
                e.getMessage()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}
