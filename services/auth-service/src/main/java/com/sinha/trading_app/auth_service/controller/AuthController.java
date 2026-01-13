package com.sinha.trading_app.auth_service.controller;

import com.sinha.trading_app.auth_service.dto.request.LoginRequest;
import com.sinha.trading_app.auth_service.dto.request.RefreshTokenRequest;
import com.sinha.trading_app.auth_service.dto.request.RegisterRequest;
import com.sinha.trading_app.auth_service.dto.response.AuthResponse;
import com.sinha.trading_app.auth_service.dto.response.TokenResponse;
import com.sinha.trading_app.auth_service.service.AuthService;
import com.sinha.trading_app.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.registerAuth(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<AuthResponse>builder()
                .status("success")
                .message("User registered successfully")
                .data(authResponse)
                .timestamp(LocalDateTime.now())
                .build());
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.loginAuth(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<AuthResponse>builder()
                .status("success")
                .message("User logged in successfully")
                .data(authResponse)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        TokenResponse tokenResponse = authService.refreshAccessToken(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<TokenResponse>builder()
                        .status("success")
                        .message("Token refreshed successfully")
                        .data(tokenResponse)
                        .timestamp(LocalDateTime.now())
                        .build());
    }



}
