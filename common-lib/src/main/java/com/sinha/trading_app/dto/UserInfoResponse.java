package com.sinha.trading_app.dto;

import com.sinha.trading_app.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserInfoResponse {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private LocalDate dob;
    private Gender gender;
    private String nationality;
    private UserStatus userStatus;
    private AccountType accountType;
    private KycStatus kycStatus;
    private LocalDateTime kycVerifiedAt;
    private UUID kycVerifiedBy;
    private TradingStatus tradingStatus;
    private RiskProfile riskProfile;
    private LocalDateTime createdAt;
    private LocalDateTime lastActiveAt;
    private LocalDateTime deletedAt;
}
