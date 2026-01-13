package com.sinha.trading_app.auth_service.service;

import com.sinha.trading_app.auth_service.model.RefreshToken;

import java.util.Date;
import java.util.UUID;

public interface TokenService {

    void blacklistToken(String token, Date expiryDate);

    int revokeAllUserTokens(UUID userId);

    RefreshToken getTokenByHash(String tokenHash);

    void addRefreshToken(RefreshToken refreshToken);
}
