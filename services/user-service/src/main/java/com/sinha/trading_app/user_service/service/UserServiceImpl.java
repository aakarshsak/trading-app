package com.sinha.trading_app.user_service.service;

import com.sinha.trading_app.dto.UserInfoRequest;
import com.sinha.trading_app.dto.UserInfoResponse;
import com.sinha.trading_app.enums.RiskProfile;
import com.sinha.trading_app.enums.UserStatus;
import com.sinha.trading_app.user_service.model.User;
import com.sinha.trading_app.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserInfoResponse addUser(UserInfoRequest user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }

        // Create new User entity
        User newUser = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .dob(user.getDob())
                .gender(user.getGender())
                .nationality(user.getNationality())
                .riskProfile(RiskProfile.CONSERVATIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        newUser = userRepository.save(newUser);

        return UserInfoResponse.builder()
                .userId(newUser.getId())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .email(newUser.getEmail())
                .mobileNumber(newUser.getMobileNumber())
                .dob(newUser.getDob())
                .gender(newUser.getGender())
                .nationality(newUser.getNationality())
                .userStatus(newUser.getUserStatus())
                .accountType(newUser.getAccountType())
                .kycStatus(newUser.getKycStatus())
                .kycVerifiedAt(newUser.getKycVerifiedAt())
                .kycVerifiedBy(newUser.getKycVerifiedBy())
                .tradingStatus(newUser.getTradingStatus())
                .riskProfile(newUser.getRiskProfile())
                .createdAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt())
                .lastActiveAt(newUser.getLastActiveAt())
                .deletedAt(newUser.getDeletedAt())
                .build();
    }

    @Override
    public UserInfoResponse getUser(UUID id) throws Exception {
        return null;
    }

    @Override
    public UserInfoResponse updateUser(UUID id, UserInfoRequest user) throws Exception {
        return null;
    }

    @Override
    public void updateUserStatus(UUID id, UserStatus userStatus) {

    }
}
