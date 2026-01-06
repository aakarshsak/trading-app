package com.sinha.trading_app.user_service.model;


import com.sinha.trading_app.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="users", indexes={
        @Index(name = "idx_users_email", columnList = "email"),
        @Index(name = "idx_users_status", columnList = "user_status"),
        @Index(name = "idx_users_kyc", columnList = "kyc_status"),
        @Index(name = "idx_users_trading", columnList = "trading_status"),
        @Index(name = "idx_users_created", columnList = "created_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "mobile_number", length = 200)
    private String mobileNumber;

    @Column(name = "dob")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private Gender gender;

    @Column(name = "nationality", length = 50)
    private String nationality;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", length = 20)
    @Builder.Default
    private UserStatus userStatus = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", length = 50)
    @Builder.Default
    private AccountType accountType = AccountType.RETAILS;

    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_status", length = 20)
    @Builder.Default
    private KycStatus kycStatus = KycStatus.PENDING;

    @Column(name = "kyc_verified_at")
    private LocalDateTime kycVerifiedAt;

    @Column(name = "kyc_verified_by")
    private UUID kycVerifiedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "trading_status", length = 20)
    @Builder.Default
    private TradingStatus tradingStatus = TradingStatus.RESTRICTED;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_profile", length = 20)
    private RiskProfile riskProfile;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if(this.userStatus == null) {
            this.userStatus = UserStatus.ACTIVE;
        }
        if(this.kycStatus == null) {
            this.kycStatus = KycStatus.PENDING;
        }
        if(this.tradingStatus == null) {
            this.tradingStatus = TradingStatus.RESTRICTED;
        }
        if(this.accountType == null) {
            this.accountType = AccountType.RETAILS;
        }
    }


    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Check if user account is soft deleted
     */
    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    /**
     * Check if user is active
     */
    public boolean isActive() {
        return this.userStatus == UserStatus.ACTIVE && !isDeleted();
    }

    /**
     * Check if user KYC is verified
     */
    public boolean isKycVerified() {
        return this.kycStatus == KycStatus.VERIFIED;
    }

    /**
     * Check is user can trade
     */
    public boolean canTrade() {
        return this.tradingStatus == TradingStatus.ENABLED
                && isActive()
                && isKycVerified();
    }

    /**
     * Soft delete the user
     */
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        this.userStatus = UserStatus.DELETED;
    }

    /**
     * Update last active timestamp
     */
    public void updateLastActive() {
        this.lastActiveAt = LocalDateTime.now();
    }

    /**
     * Verify KYC
     */
    public void verifyKyc(UUID verifiedBy) {
        this.kycStatus = KycStatus.VERIFIED;
        this.kycVerifiedAt = LocalDateTime.now();
        this.kycVerifiedBy = verifiedBy;
    }

    /**
     * Enable trading for user (required Kyc to be verified)
     */
    public void enableTrading() {
        if (!isKycVerified()) {
            throw new IllegalStateException("Cannot enable trading for user with unverified KYC");
        }
        this.tradingStatus = TradingStatus.ENABLED;
    }
}
