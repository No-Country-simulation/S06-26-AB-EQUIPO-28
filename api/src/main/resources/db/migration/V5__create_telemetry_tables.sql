-- V5__create_telemetry_tables.sql
-- Migration for the Telemetry bounded context
-- Creates the antenna and network concentration tables

-- Schema for telemetry isolation
CREATE SCHEMA IF NOT EXISTS telemetry_schema;

-- Telecommunications antenna table
CREATE TABLE IF NOT EXISTS telemetry_schema.antennas (
    ecgi VARCHAR(50) PRIMARY KEY,
    cluster VARCHAR(50) NOT NULL,
    municipality VARCHAR(100) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Indexes for frequent queries
CREATE INDEX IF NOT EXISTS idx_antennas_cluster
    ON telemetry_schema.antennas (cluster);

CREATE INDEX IF NOT EXISTS idx_antennas_municipality
    ON telemetry_schema.antennas (municipality);

-- Network concentration metrics table
CREATE TABLE IF NOT EXISTS telemetry_schema.network_concentration (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    ecgi VARCHAR(50) NOT NULL,
    day_date DATE NOT NULL,
    session_period VARCHAR(20) NOT NULL CHECK (session_period IN ('DAWN', 'MORNING', 'AFTERNOON', 'NIGHT')),
    user_count INTEGER,
    session_count INTEGER,
    download_bytes BIGINT,
    upload_bytes BIGINT,
    average_duration_s DOUBLE PRECISION,
    drop_pct DOUBLE PRECISION,
    congestion_level DOUBLE PRECISION,
    total_calls BIGINT,
    total_messages BIGINT,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_concentration_antenna
        FOREIGN KEY (ecgi) REFERENCES telemetry_schema.antennas (ecgi)
);

-- Indexes for analytical queries
CREATE INDEX IF NOT EXISTS idx_concentration_ecgi
    ON telemetry_schema.network_concentration (ecgi);

CREATE INDEX IF NOT EXISTS idx_concentration_session_period_day_date
    ON telemetry_schema.network_concentration (session_period, day_date);

CREATE INDEX IF NOT EXISTS idx_concentration_drop_pct
    ON telemetry_schema.network_concentration (drop_pct)
    WHERE drop_pct IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_concentration_day_date
    ON telemetry_schema.network_concentration (day_date);

-- Comments for documentation
COMMENT ON TABLE telemetry_schema.antennas IS 'Telecommunication antennas in the RM de Florianopolis';
COMMENT ON TABLE telemetry_schema.network_concentration IS 'Network concentration metrics by cell and session period';
COMMENT ON COLUMN telemetry_schema.network_concentration.ecgi IS 'Unique cell identifier (ECGI)';
COMMENT ON COLUMN telemetry_schema.network_concentration.session_period IS 'Session period: DAWN (0-6), MORNING (6-12), AFTERNOON (12-18), NIGHT (18-24)';