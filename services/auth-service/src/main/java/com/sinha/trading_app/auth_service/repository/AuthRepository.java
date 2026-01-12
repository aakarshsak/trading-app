package com.sinha.trading_app.auth_service.repository;

import com.sinha.trading_app.auth_service.model.AuthCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthRepository extends JpaRepository<AuthCredential, Long> {
    Optional<AuthCredential> findByEmail(String email);

    Optional<AuthCredential> existsByEmail(String email);

    // Find auth credential with roles loaded
    @Query("SELECT ac FROM AuthCredential ac LEFT JOIN FETCH ac.userRoles ur LEFT JOIN FETCH ur.role WHERE ac.email = :email")
    Optional<AuthCredential> findByEmailWithRoles(String email);

    @Query("SELECT ac FROM AuthCredential ac LEFT JOIN FETCH ac.userRoles ur LEFT JOIN FETCH ur.role WHERE ac.userId = :userId")
    Optional<AuthCredential> findByUserIdWithRoles(UUID userId);
}
