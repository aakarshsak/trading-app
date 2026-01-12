package com.sinha.trading_app.auth_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "auth_credentials")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude={"userRoles"})
@EqualsAndHashCode(of = {"id", "userId", "email"})
public class AuthCredential {

    @Id
    @SequenceGenerator(name="auth_credential_seq_gen", sequenceName="auth_credential_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="auth_credential_seq_gen")

    private Long id;

    @Column(name="user_id", nullable=false, unique=true)
    private UUID userId;

    @Column(name="email", nullable=false, unique=true, length=255)
    private String email;

    @Column(name="password_hash", nullable=false, length=255)
    private String passwordHash;

    @Column(name="salt", length=255)
    private String salt;

    @Column(name="is_email_verified")
    @Builder.Default
    private Boolean isEmailVerified = false;

    @Column(name="is_phone_verified")
    @Builder.Default
    private Boolean isPhoneVerified = false;

    @Column(name="is_2fa_enabled")
    @Builder.Default
    private Boolean is2FAEnabled = false;

    @Column(name = "totp_secret", length = 255)
    private String totpSecret;

    @Column(name="failed_attempts")
    private Integer failedAttempts;

    @Column(name="lockout_until")
    private LocalDateTime lockoutUntil;

    @Column(name="last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name="last_password_change_at")
    private LocalDateTime lastPasswordChangeAt;

    @Column(name="created_at", nullable=false, updatable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable=false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy="authCredential", cascade=CascadeType.ALL, orphanRemoval=true)
    @Builder.Default
    private Set<UserRole> userRoles = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if(this.lastPasswordChangeAt == null) {
            this.lastPasswordChangeAt = this.createdAt;
        }
        if(this.failedAttempts == null) {
            this.failedAttempts = 0;
        }
        if(this.isEmailVerified == null) {
            this.isEmailVerified = false;
        }
        if(this.isPhoneVerified == null) {
            this.isPhoneVerified = false;
        }
        if(this.is2FAEnabled == null) {
            this.is2FAEnabled = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addRole(Role role, UUID grantedBy) {
        UserRole userRole = UserRole.builder()
                .id(new UserRoleId(this.id, role.getId()))
                .authCredential(this)
                .role(role)
                .grantedAt(LocalDateTime.now())
                .grantedBy(grantedBy)
                .isActive(true)
                .build();
        this.userRoles.add(userRole);
    }


    public void removeRole(Role role) {
        this.userRoles.removeIf(ur -> ur.getRole().getId().equals(role.getId()));
    }

    public List<String> getActiveRoleNames() {
        return this.userRoles.stream()
                .filter(UserRole::isValid)
                .map(ur -> ur.getRole().getName())
                .toList();
    }

    public boolean hasRole(String roleName) {
        return this.userRoles.stream()
                .filter(UserRole::isValid)
                .anyMatch(ur -> ur.getRole().getName().equals(roleName));
    }

    public boolean hasPermission(String permissionName) {
        return this.userRoles.stream()
                .filter(UserRole::isValid)
                .map(UserRole::getRole)
                .anyMatch(role -> role.getPermissions() != null &&
                        role.getPermissions().hasPermission(permissionName));
    }

    public Set<String> getAllPermissions() {
        Set<String> permissions = new HashSet<>();
        userRoles.stream()
                .filter(UserRole::isValid)
                .forEach(ur -> {
                    RolePermissions rolePermissions = ur.getRole().getPermissions();
                    if(rolePermissions != null) {
                        if(rolePermissions.getCanTrade()) {
                            permissions.add("TRADE");
                        }
                        if(rolePermissions.getCanViewReports()) {
                            permissions.add("VIEW_REPORTS");
                        }
                        if(rolePermissions.getCanManageUsers()) {
                            permissions.add("MANAGE_USERS");
                        }
                        if(rolePermissions.getCanViewReports()) {
                            permissions.add("VIEW_REPORTS");
                        }
                        if(rolePermissions.getCanModifyOrders()) {
                            permissions.add("MODIFY_ORDERS");
                        }
                        if(rolePermissions.getCanAccessAPI()) {
                            permissions.add("ACCESS_API");
                        }
                    }
                });
        return permissions;
    }


    public boolean isAccountLocked() {
        return this.lockoutUntil != null && LocalDateTime.now().isBefore(this.lockoutUntil);
    }

    public void incrementFailedAttempts() {
        this.failedAttempts = (this.failedAttempts == null ? 0 : this.failedAttempts) + 1;
    }

    public void resetFailedAttempts() {
        this.failedAttempts = 0;
        this.lockoutUntil = null;
    }

    public void lockAccount(int lockoutMinutes) {
        this.lockoutUntil = LocalDateTime.now().plusMinutes(lockoutMinutes);
    }


}
