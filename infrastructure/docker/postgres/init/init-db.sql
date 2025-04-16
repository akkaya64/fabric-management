-- Create schemas
CREATE SCHEMA IF NOT EXISTS identity;
CREATE SCHEMA IF NOT EXISTS organization;
CREATE SCHEMA IF NOT EXISTS finance;
CREATE SCHEMA IF NOT EXISTS hr;
CREATE SCHEMA IF NOT EXISTS audit;

-- Set search path
SET search_path TO identity, organization, finance, hr, audit, public;

-- Identity schema tables
-- Roles table
CREATE TABLE identity.roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Initial roles
INSERT INTO identity.roles (name, description) VALUES
    ('ROLE_USER', 'Standard user role'),
    ('ROLE_MANAGER', 'Manager role with department access'),
    ('ROLE_ADMIN', 'Administrator role with system-wide access'),
    ('ROLE_SUPER_ADMIN', 'Super administrator with full access');

-- Users table
CREATE TABLE identity.users (
    id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20) UNIQUE,
    password VARCHAR(100),
    is_active BOOLEAN NOT NULL DEFAULT FALSE,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    phone_verified BOOLEAN NOT NULL DEFAULT FALSE,
    position VARCHAR(100),
    department VARCHAR(100),
    company VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- User roles mapping table
CREATE TABLE identity.user_roles (
    user_id VARCHAR(36) NOT NULL REFERENCES identity.users(id),
    role_id INTEGER NOT NULL REFERENCES identity.roles(id),
    PRIMARY KEY (user_id, role_id)
);

-- Create function to update timestamp
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = CURRENT_TIMESTAMP;
   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create triggers for timestamp updates
CREATE TRIGGER update_users_timestamp
BEFORE UPDATE ON identity.users
FOR EACH ROW EXECUTE PROCEDURE update_timestamp();

CREATE TRIGGER update_roles_timestamp
BEFORE UPDATE ON identity.roles
FOR EACH ROW EXECUTE PROCEDURE update_timestamp();

-- Create audit logging table
CREATE TABLE audit.audit_log (
    id SERIAL PRIMARY KEY,
    entity_type VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100) NOT NULL,
    action VARCHAR(50) NOT NULL,
    user_id VARCHAR(36),
    data JSONB,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_users_email ON identity.users(email);
CREATE INDEX idx_users_phone ON identity.users(phone_number);
CREATE INDEX idx_audit_entity ON audit.audit_log(entity_type, entity_id);
CREATE INDEX idx_audit_user ON audit.audit_log(user_id);
CREATE INDEX idx_audit_timestamp ON audit.audit_log(timestamp);

-- Grant privileges
GRANT ALL PRIVILEGES ON SCHEMA identity TO fabric;
GRANT ALL PRIVILEGES ON SCHEMA organization TO fabric;
GRANT ALL PRIVILEGES ON SCHEMA finance TO fabric;
GRANT ALL PRIVILEGES ON SCHEMA hr TO fabric;
GRANT ALL PRIVILEGES ON SCHEMA audit TO fabric;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA identity TO fabric;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA organization TO fabric;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA finance TO fabric;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA hr TO fabric;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA audit TO fabric;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA identity TO fabric;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA organization TO fabric;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA finance TO fabric;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA hr TO fabric;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA audit TO fabric;