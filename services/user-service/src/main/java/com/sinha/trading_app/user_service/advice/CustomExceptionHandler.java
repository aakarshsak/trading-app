package com.sinha.trading_app.user_service.advice;

import com.sinha.trading_app.common.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(ErrorResponse.builder()
                        .errorCode(HttpStatus.NOT_FOUND.value())
                        .status(HttpStatus.NOT_FOUND.toString())
                        .message(ex.getMessage())
                        .timestamp(java.time.LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(ErrorResponse.builder()
                        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .message(ex.getMessage())
                        .timestamp(java.time.LocalDateTime.now())
                        .build());
    }
}
