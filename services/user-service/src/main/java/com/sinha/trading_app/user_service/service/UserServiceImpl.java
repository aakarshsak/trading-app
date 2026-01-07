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
import java.util.Optional;
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

        Optional<User> userOptional = userRepository.getUserById(id);

        User user = userOptional.orElseThrow(() -> new Exception("User not found"));

        return UserInfoResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .dob(user.getDob())
                .gender(user.getGender())
                .nationality(user.getNationality())
                .userStatus(user.getUserStatus())
                .accountType(user.getAccountType())
                .kycStatus(user.getKycStatus())
                .kycVerifiedAt(user.getKycVerifiedAt())
                .kycVerifiedBy(user.getKycVerifiedBy())
                .tradingStatus(user.getTradingStatus())
                .riskProfile(user.getRiskProfile())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastActiveAt(user.getLastActiveAt())
                .deletedAt(user.getDeletedAt())
                .build();
    }

    public User getUserFromDB(UUID id) throws Exception {
        Optional<User> userOptional = userRepository.getUserById(id);

        return userOptional.orElseThrow(() -> new Exception("User not found"));
    }

    @Override
    public UserInfoResponse updateUser(UUID id, UserInfoRequest user) throws Exception {
        User existingUser = getUserFromDB(id);

        //Update onlu non-null fields (partial update)
        if(user.getFirstName() != null) {
            existingUser.setFirstName(user.getFirstName());
        }
        if(user.getLastName() != null) {
            existingUser.setLastName(user.getLastName());
        }
        if(user.getMobileNumber() != null) {
            existingUser.setMobileNumber(user.getMobileNumber());
        }
        if(user.getDob() != null) {
            existingUser.setDob(user.getDob());
        }
        if(user.getGender() != null) {
            existingUser.setGender(user.getGender());
        }
        if(user.getNationality() != null) {
            existingUser.setNationality(user.getNationality());
        }

        existingUser.setUpdatedAt(LocalDateTime.now());


        User updatedUser = userRepository.save(existingUser);

        return UserInfoResponse.builder()
                .userId(updatedUser.getId())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .email(updatedUser.getEmail())
                .mobileNumber(updatedUser.getMobileNumber())
                .dob(updatedUser.getDob())
                .gender(updatedUser.getGender())
                .nationality(updatedUser.getNationality())
                .userStatus(updatedUser.getUserStatus())
                .accountType(updatedUser.getAccountType())
                .kycStatus(updatedUser.getKycStatus())
                .kycVerifiedAt(updatedUser.getKycVerifiedAt())
                .kycVerifiedBy(updatedUser.getKycVerifiedBy())
                .tradingStatus(updatedUser.getTradingStatus())
                .riskProfile(updatedUser.getRiskProfile())
                .createdAt(updatedUser.getCreatedAt())
                .updatedAt(updatedUser.getUpdatedAt())
                .lastActiveAt(updatedUser.getLastActiveAt())
                .deletedAt(updatedUser.getDeletedAt())
                .build();
    }

    @Override
    public void updateUserStatus(UUID id, UserStatus userStatus) {
        userRepository.updateUserStatus(id, userStatus, LocalDateTime.now());
    }
}
