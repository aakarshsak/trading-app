CREATE SEQUENCE IF NOT EXISTS auth_credentials_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE auth_credentials (
    id                      BIGINT PRIMARY KEY DEFAULT NEXTVAL('auth_credentials_seq'),
    user_id                 UUID NOT NULL UNIQUE,
    email                   VARCHAR(255) NOT NULL UNIQUE,
    password_hash           VARCHAR(255) NOT NULL,
    salt                    VARCHAR(255),


    is_email_verified       BOOLEAN DEFAULT FALSE,
    is_phone_verified       BOOLEAN DEFAULT FALSE,
    is_2fa_enabled          BOOLEAN DEFAULT FALSE,
    totp_secret             VARCHAR(255),

    failed_attempts         INT DEFAULT 0,
    lockout_until           TIMESTAMP,
    last_login_at           TIMESTAMP,
    last_password_change_at TIMESTAMP DEFAULT NOW(),

    created_at              TIMESTAMP DEFAULT NOW(),
    updated_at              TIMESTAMP DEFAULT NOW()
);


CREATE INDEX idx_auth_email ON auth_credentials(email);
CREATE INDEX idx_auth_user ON auth_credentials(user_id);


--ROLES

CREATE SEQUENCE IF NOT EXISTS role_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE roles (
    id                      BIGINT PRIMARY KEY DEFAULT NEXTVAL('role_seq'),
    name                    VARCHAR(50) NOT NULL UNIQUE,
    description             VARCHAR(255),
    permissions             JSONB,
    is_active               BOOLEAN DEFAULT TRUE,
    created_at              TIMESTAMP DEFAULT NOW(),
    updated_at              TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_roles_name ON roles(name);



--USER ROLES

CREATE TABLE user_roles (
    auth_id                 BIGINT NOT NULL,
    role_id                 BIGINT NOT NULL,
    granted_at              TIMESTAMP DEFAULT NOW(),
    granted_by              UUID,
    expires_at              TIMESTAMP,
    is_active               BOOLEAN DEFAULT TRUE,

    PRIMARY KEY (auth_id, role_id),
    CONSTRAINT fk_user_roles_auth FOREIGN KEY (auth_id) REFERENCES auth_credentials(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);


CREATE INDEX idx_user_roles_auth ON user_roles(auth_id);




