--Update column name for users table

ALTER TABLE users RENAME COLUMN key_verified_at TO kyc_verified_at;
ALTER TABLE users RENAME COLUMN key_verified_by TO kyc_verified_by;


--Create user profile table

CREATE TABLE user_profiles (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id                 UUID NOT NULL UNIQUE,


    occupation              VARCHAR(100),
    annual_income           VARCHAR(50),
    source_of_funds         VARCHAR(100),
    employer_name           VARCHAR(200),


    profile_picture_url     TEXT,
    bio                     TEXT,
    referral_code           VARCHAR(20) UNIQUE,
    referred_by             VARCHAR(50),


    language_preference     VARCHAR(10) DEFAULT 'en',
    timezone                VARCHAR(50) DEFAULT 'UTC',

    created_at              TIMESTAMP DEFAULT NOW(),
    updated_at              TIMESTAMP DEFAULT NOW(),

    CONSTRAINT fk_profile_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE INDEX idx_profile_referral ON user_profiles(referral_code);