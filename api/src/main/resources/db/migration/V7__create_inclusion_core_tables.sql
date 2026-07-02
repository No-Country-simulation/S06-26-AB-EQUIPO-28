-- V7__create_inclusion_core_tables.sql
-- Migration for the Inclusion Core bounded context
-- Creates the health vulnerability index table

-- Schema for inclusion core isolation
CREATE SCHEMA IF NOT EXISTS inclusion_schema;

-- Health vulnerability index table
CREATE TABLE IF NOT EXISTS inclusion_schema.health_vulnerability_index (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    region_name VARCHAR(100) NOT NULL,
    vulnerable_population_count INTEGER NOT NULL,
    total_population INTEGER NOT NULL,
    vulnerability_score INTEGER NOT NULL,
    connectivity_level VARCHAR(10) NOT NULL,
    concentration_index DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Indexes for analytical queries
CREATE INDEX IF NOT EXISTS idx_hvi_vulnerability_score
    ON inclusion_schema.health_vulnerability_index (vulnerability_score);

CREATE INDEX IF NOT EXISTS idx_hvi_connectivity_level
    ON inclusion_schema.health_vulnerability_index (connectivity_level);

CREATE INDEX IF NOT EXISTS idx_hvi_region_name
    ON inclusion_schema.health_vulnerability_index (region_name);

-- Comments for documentation
COMMENT ON TABLE inclusion_schema.health_vulnerability_index IS 'Health vulnerability index by geographic region for mental health program planning';
COMMENT ON COLUMN inclusion_schema.health_vulnerability_index.region_name IS 'Geographic region name';
COMMENT ON COLUMN inclusion_schema.health_vulnerability_index.vulnerable_population_count IS 'Count of vulnerable citizens (income level D)';
COMMENT ON COLUMN inclusion_schema.health_vulnerability_index.total_population IS 'Total population in the region';
COMMENT ON COLUMN inclusion_schema.health_vulnerability_index.vulnerability_score IS 'Calculated vulnerability score (0-100)';
COMMENT ON COLUMN inclusion_schema.health_vulnerability_index.connectivity_level IS 'Network connectivity level: HIGH, MEDIUM or LOW';
COMMENT ON COLUMN inclusion_schema.health_vulnerability_index.concentration_index IS 'Population concentration index (0-100)';
