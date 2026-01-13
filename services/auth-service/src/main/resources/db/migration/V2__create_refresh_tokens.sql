CREATE SEQUENCE IF NOT EXISTS refresh_tokens_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS refresh_tokens (
    id                      BIGINT PRIMARY KEY DEFAULT NEXTVAL('refresh_tokens_seq'),
    user_id                 UUID NOT NULL,
    token_hash              VARCHAR(255) NOT NULL UNIQUE,
    device_info             TEXT,
    ip_address              VARCHAR(45),
    expires_at              TIMESTAMP NOT NULL,
    revoked                 BOOLEAN NOT NULL DEFAULT FALSE,
    revoked_at              TIMESTAMP,
    created_at              TIMESTAMP DEFAULT NOW()
);


CREATE INDEX IF NOT EXISTS idx_refresh_user ON refresh_tokens(user_id);
CREATE INDEX IF NOT EXISTS idx_refresh_token ON refresh_tokens(token_hash);
CREATE INDEX IF NOT EXISTS idx_refresh_expires ON refresh_tokens(expires_at);
CREATE INDEX IF NOT EXISTS idx_refresh_revoked ON refresh_tokens(revoked, expires_at);