package com.sinha.trading_app.auth_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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
}
