package com.sinha.trading_app.auth_service.advice;


import com.sinha.trading_app.common.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception e) {
        System.out.println("In Custom HAndler" + e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.builder()
                .errorCode(500)
                .message("Internal Server Error")
                .timestamp(LocalDateTime.now())
                .status("error")
                .build());
    }
}
