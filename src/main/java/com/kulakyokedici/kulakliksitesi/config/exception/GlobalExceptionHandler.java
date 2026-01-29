package com.kulakyokedici.kulakliksitesi.config.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kulakyokedici.kulakliksitesi.objects.exception.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllGeneric(Exception ex) {
        return new ResponseEntity<>(
            new ErrorResponse(500, ex.getMessage(), LocalDateTime.now()), // şimdilik error yollansın. bu bir güvenlik açığıdır
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
