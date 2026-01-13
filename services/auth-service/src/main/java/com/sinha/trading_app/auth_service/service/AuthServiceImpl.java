package com.sinha.trading_app.auth_service.service;

import com.sinha.trading_app.auth_service.dto.request.LoginRequest;
import com.sinha.trading_app.auth_service.dto.request.RefreshTokenRequest;
import com.sinha.trading_app.auth_service.dto.request.RegisterRequest;
import com.sinha.trading_app.auth_service.dto.response.AuthResponse;
import com.sinha.trading_app.auth_service.dto.response.TokenResponse;
import com.sinha.trading_app.auth_service.model.AuthCredential;
import com.sinha.trading_app.auth_service.model.RefreshToken;
import com.sinha.trading_app.auth_service.model.Role;
import com.sinha.trading_app.auth_service.proxy.UserProxy;
import com.sinha.trading_app.auth_service.repository.AuthRepository;
import com.sinha.trading_app.auth_service.repository.RoleRepository;
import com.sinha.trading_app.common.config.JwtProperties;
import com.sinha.trading_app.common.dto.ApiResponse;
import com.sinha.trading_app.common.dto.UserInfoRequest;
import com.sinha.trading_app.common.dto.UserInfoResponse;
import com.sinha.trading_app.common.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserProxy userProxy;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final JwtProperties jwtProperties;

    @Autowired
    public AuthServiceImpl(AuthRepository authRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           UserProxy userProxy,
                           JwtUtil jwtUtil,
                           TokenService tokenService,
                           JwtProperties jwtProperties) {
        this.authRepository = authRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userProxy = userProxy;
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
        this.jwtProperties = jwtProperties;
    }



    @Override
    @Transactional
    public AuthResponse registerAuth(RegisterRequest request) {

        if(authRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Create new AuthCredential entity
        AuthCredential authCredential = AuthCredential.builder()
                .email(request.getEmail())
                .passwordHash(encodedPassword)
                .build();

        ResponseEntity<ApiResponse<UserInfoResponse>> response = userProxy.addUser(
                UserInfoRequest.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .mobileNumber(request.getMobileNumber())
                        .dob(request.getDob())
                        .email(request.getEmail())
                        .build()
        );

        UUID userId = Objects.requireNonNull(response.getBody()).getData().getUserId();
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));

        authCredential.setUserId(userId);
        authCredential.addRole(userRole, null);

        authRepository.save(authCredential);

        String accessToken = generateAccessToken(authCredential);
        String refreshToken = generateAndStoreRefreshToken(authCredential, null, null);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtProperties.getAccessTokenExpiry() / 1000)
                .user(response.getBody().getData())
                .requires2FA(false)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse loginAuth(LoginRequest request) {
        AuthCredential authCredential = authRepository.findByEmailWithRoles(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if(authCredential.isAccountLocked()) {
            throw new RuntimeException("Account is locked due to multiple failed login attempts. Please try again later.");
        }

        if(!passwordEncoder.matches(request.getPassword(), authCredential.getPasswordHash())) {
            authCredential.incrementFailedAttempts();

            if(authCredential.getFailedAttempts() >= 5) {
                authCredential.lockAccount(15); // Lock account for 15 minutes
            }
            authRepository.save(authCredential);
            throw new RuntimeException("Invalid email or password");
        }

        authCredential.resetFailedAttempts();
        authCredential.setLastLoginAt(LocalDate.now().atStartOfDay());
        authRepository.save(authCredential);

        ResponseEntity<ApiResponse<UserInfoResponse>> response = userProxy.getUserByAuthId(authCredential.getUserId());

        String accessToken = generateAccessToken(authCredential);
        String refreshToken = generateAndStoreRefreshToken(authCredential, null, null);

        boolean requires2FA = false;

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtProperties.getAccessTokenExpiry() / 1000)
                .user(Objects.requireNonNull(response.getBody()).getData())
                .requires2FA(requires2FA)
                .build();
    }


    @Override
    @Transactional
    public TokenResponse refreshAccessToken(RefreshTokenRequest request) {

        if(!jwtUtil.validateToken(request.getRefreshToken())) {
            throw new RuntimeException("Invalid refresh token");
        }

        String tokenType = jwtUtil.getTokenType(request.getRefreshToken());
        if(!"REFRESH".equals(tokenType)) {
            throw new RuntimeException("Provided token is not a refresh token");
        }

        UUID userId = jwtUtil.getUserIdFromToken(request.getRefreshToken());

        String tokenHash = hashToken(request.getRefreshToken());

        RefreshToken storedToken = tokenService.getTokenByHash(tokenHash);

        if(!storedToken.isValid()) {
            throw new RuntimeException("Refresh token is revoked or expired");
        }

        if(!storedToken.getUserId().equals(userId)) {
            throw new RuntimeException("Refresh token does not belong to the user");
        }

        AuthCredential authCredential = authRepository.findByUserIdWithRoles(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // For simplicity, assume the refresh token is valid and generate new tokens
        String newAccessToken = generateAccessToken(authCredential);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(request.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(3600L)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional
    public void logout(UUID userId, String accessToken) {
        if(accessToken != null) {
            try {
                String tokenId = jwtUtil.getTokenId(accessToken);
                Date expiryDate = jwtUtil.getExpirationDate(accessToken);
            } catch(Exception e) {
                // Log error but proceed with logout
                throw new RuntimeException("Error during logout: " + e.getMessage());
            }
        }

        tokenService.revokeAllUserTokens(userId);

    }

    @Override
    @Transactional
    public void logoutAllDevices(UUID userId) {
        tokenService.revokeAllUserTokens(userId);
    }

    private String hashToken(String token) {
        // Simple hashing for demonstration; use a secure hashing algorithm in production
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing token", e);
        }
    }

    private String generateAccessToken(AuthCredential authCredential) {

        List<String> roles = authCredential.getActiveRoleNames();
        List<String> permissions = authCredential.getAllPermissions().stream().toList();

        return jwtUtil.generateAccessToken(
                authCredential.getUserId(),
                authCredential.getEmail(),
                roles,
                permissions
        );

    }

    private String generateAndStoreRefreshToken(AuthCredential authCredential, String deviceInfo, String ipAddress) {
        String refreshToken = jwtUtil.generateRefreshToken(authCredential.getUserId());

        String tokenHash = hashToken(refreshToken);

        LocalDateTime expiresAt = LocalDateTime.now()
                .plusSeconds(jwtProperties.getRefreshTokenExpiry() / 1000);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .userId(authCredential.getUserId())
                .tokenHash(tokenHash)
                .deviceInfo(deviceInfo)
                .ipAddress(ipAddress)
                .expiresAt(expiresAt)
                .revoked(false)
                .build();

        tokenService.addRefreshToken(refreshTokenEntity);

        return refreshToken;
    }
}
