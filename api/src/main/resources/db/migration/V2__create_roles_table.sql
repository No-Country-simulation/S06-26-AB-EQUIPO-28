-- Migration: Create roles table
-- BC: userandrolesmanagement
-- Description: Creates the roles table for user permissions
-- Note: This must run BEFORE users table due to FK dependency

CREATE TABLE IF NOT EXISTS roles (
    role_id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    name VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Constraints matching VO validation
    CONSTRAINT chk_role_name_values CHECK (name IN ('ADMIN', 'GENERAL_USER')),
    CONSTRAINT uq_roles_name UNIQUE (name)
);

-- Comments for documentation
COMMENT ON TABLE roles IS 'Role definitions for user permissions (RBAC)';
COMMENT ON COLUMN roles.role_id IS 'Role ID (Entity ID) - UUID';
COMMENT ON COLUMN roles.name IS 'Role name enum: ADMIN, GENERAL_USER';

-- Seed initial roles (in English as per PRD)
INSERT INTO roles (role_id, name) VALUES
    (uuid_generate_v7(), 'ADMIN'),
    (uuid_generate_v7(), 'GENERAL_USER')
ON CONFLICT (name) DO NOTHING;
