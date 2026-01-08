package com.sinha.trading_app.auth_service.service;

import com.sinha.trading_app.auth_service.dto.request.LoginRequest;
import com.sinha.trading_app.auth_service.dto.request.RegisterRequest;
import com.sinha.trading_app.auth_service.dto.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {


    @Override
    public AuthResponse registerAuth(RegisterRequest request) {
        return null;
    }

    @Override
    public AuthResponse loginAuth(LoginRequest request) {
        return null;
    }
}
