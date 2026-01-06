CREATE TABLE users (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    first_name              VARCHAR(100) NOT NULL,
    last_name               VARCHAR(100) NOT NULL,
    email                   VARCHAR(255) NOT NULL UNIQUE,
    mobile_number           VARCHAR(20),


    dob                     DATE,
    gender                  VARCHAR(20),
    nationality             VARCHAR(50),


    user_status             VARCHAR(20) DEFAULT 'ACTIVE',
    account_type            VARCHAR(20) DEFAULT 'RETAIL',


    kyc_status              VARCHAR(20) DEFAULT 'PENDING',
    key_verified_at         TIMESTAMP,
    key_verified_by         UUID,

    trading_status          VARCHAR(20) DEFAULT 'RESTRICTED',
    risk_profile            VARCHAR(20),


    created_at              TIMESTAMP DEFAULT NOW(),
    updated_at              TIMESTAMP DEFAULT NOW(),
    last_active_at          TIMESTAMP,
    deleted_at              TIMESTAMP

);


CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_status ON users(user_status);
CREATE INDEX idx_users_kyc ON users(kyc_status);
CREATE INDEX idx_users_trading ON users(trading_status);
CREATE INDEX idx_users_created ON users(created_at DESC);








