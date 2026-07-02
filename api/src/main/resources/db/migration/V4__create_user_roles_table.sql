-- Migration: Create user_roles join table
-- BC: userandrolesmanagement
-- Description: Creates the Many-to-Many join table for users and roles
-- Note: This runs AFTER users and roles tables

CREATE TABLE IF NOT EXISTS user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,

    -- Primary key (composite)
    PRIMARY KEY (user_id, role_id),

    -- Foreign keys
    CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role_id FOREIGN KEY (role_id)
        REFERENCES roles(role_id)
        ON DELETE CASCADE
);

-- Comments for documentation
COMMENT ON TABLE user_roles IS 'Many-to-Many relationship between users and roles';
COMMENT ON COLUMN user_roles.user_id IS 'Reference to users.userId (UUID)';
COMMENT ON COLUMN user_roles.role_id IS 'Reference to roles.userId (UUID)';

-- Indexes for performance (FK indexes)
CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX idx_user_roles_role_id ON user_roles(role_id);
