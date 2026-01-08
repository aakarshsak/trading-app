package com.sinha.trading_app.auth_service.service;

import com.sinha.trading_app.auth_service.dto.request.LoginRequest;
import com.sinha.trading_app.auth_service.dto.request.RegisterRequest;
import com.sinha.trading_app.auth_service.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse registerAuth(RegisterRequest request);

    AuthResponse loginAuth(LoginRequest request);
}
