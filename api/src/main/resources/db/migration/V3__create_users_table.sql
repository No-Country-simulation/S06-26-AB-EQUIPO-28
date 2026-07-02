-- Migration: Create users table
-- BC: userandrolesmanagement Creates the users table for authentication
-- Description: Note: This runs AFTER roles table (V2)

CREATE TABLE IF NOT EXISTS users (
    user_id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    username VARCHAR(100) NOT NULL,
    password VARCHAR(200) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Constraints matching VO validation
    CONSTRAINT chk_username_not_empty CHECK (LENGTH(TRIM(username)) > 0),
    CONSTRAINT chk_password_not_empty CHECK (LENGTH(TRIM(password)) > 0),
    CONSTRAINT uq_users_username UNIQUE (username)
);

-- Comments for documentation
COMMENT ON TABLE users IS 'User authentication and status (IAM BC)';
COMMENT ON COLUMN users.user_id IS 'User ID (Aggregate Root ID) - UUID';
COMMENT ON COLUMN users.username IS 'Username - max 100 chars, unique';
COMMENT ON COLUMN users.password IS 'Hashed password - max 200 chars';
COMMENT ON COLUMN users.is_active IS 'User active status: true = active, false = inactive';

-- Indexes
CREATE INDEX IF NOT EXISTS idx_users_is_active ON users(is_active);
COMMENT ON INDEX idx_users_is_active IS 'Index for user active status queries';

CREATE INDEX idx_users_username ON users(username);