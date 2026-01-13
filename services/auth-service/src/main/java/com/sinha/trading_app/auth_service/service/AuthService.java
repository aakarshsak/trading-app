package com.sinha.trading_app.auth_service.service;

import com.sinha.trading_app.auth_service.dto.request.LoginRequest;
import com.sinha.trading_app.auth_service.dto.request.RefreshTokenRequest;
import com.sinha.trading_app.auth_service.dto.request.RegisterRequest;
import com.sinha.trading_app.auth_service.dto.response.AuthResponse;
import com.sinha.trading_app.auth_service.dto.response.TokenResponse;

import java.util.UUID;

public interface AuthService {
    AuthResponse registerAuth(RegisterRequest request);

    AuthResponse loginAuth(LoginRequest request);

    TokenResponse refreshAccessToken(RefreshTokenRequest request);

    void logout(UUID userId, String refreshToken);

    void logoutAllDevices(UUID userId);
}
