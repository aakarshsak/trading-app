package com.sinha.trading_app.auth_service.service;

import com.sinha.trading_app.auth_service.model.RefreshToken;
import com.sinha.trading_app.auth_service.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private static final String BLACKLIST_PREFIX = "token:blacklist:";

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public TokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }


    @Override
    public void blacklistToken(String token, Date expiryDate) {
        // Implementation to blacklist the token

//        String key
    }

    @Override
    public int revokeAllUserTokens(UUID userId) {
        return refreshTokenRepository.revokeAllUserTokens(userId, LocalDateTime.now());
    }

    @Override
    public RefreshToken getTokenByHash(String tokenHash) {
        return refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
    }

    @Override
    public void addRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }
}
