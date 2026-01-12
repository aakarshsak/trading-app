package com.sinha.trading_app.auth_service.service;

import com.sinha.trading_app.auth_service.dto.request.LoginRequest;
import com.sinha.trading_app.auth_service.dto.request.RegisterRequest;
import com.sinha.trading_app.auth_service.dto.response.AuthResponse;
import com.sinha.trading_app.auth_service.dto.response.UserInfo;
import com.sinha.trading_app.auth_service.model.AuthCredential;
import com.sinha.trading_app.auth_service.model.Role;
import com.sinha.trading_app.auth_service.proxy.UserProxy;
import com.sinha.trading_app.auth_service.repository.AuthRepository;
import com.sinha.trading_app.auth_service.repository.RoleRepository;
import com.sinha.trading_app.dto.ApiResponse;
import com.sinha.trading_app.dto.UserInfoRequest;
import com.sinha.trading_app.dto.UserInfoResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserProxy userProxy;

    @Autowired
    public AuthServiceImpl(AuthRepository authRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserProxy userProxy) {
        this.authRepository = authRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userProxy = userProxy;
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

        String accessToken = UUID.randomUUID().toString();
        String refreshToken = UUID.randomUUID().toString();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L)
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

        String accessToken = UUID.randomUUID().toString();
        String refreshToken = UUID.randomUUID().toString();

        boolean requires2FA = false;

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L)
                .user(Objects.requireNonNull(response.getBody()).getData())
                .requires2FA(requires2FA)
                .build();
    }

}
