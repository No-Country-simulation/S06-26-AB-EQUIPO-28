-- V6__create_demographics_tables.sql
-- Migration for the Demographics bounded context
-- Creates the citizen profiles tables

-- Schema for demographics isolation
CREATE SCHEMA IF NOT EXISTS demographics_schema;

-- Citizen profiles table
CREATE TABLE IF NOT EXISTS demographics_schema.citizen_profiles (
    citizen_hash VARCHAR(64) PRIMARY KEY,
    income_level VARCHAR(1) NOT NULL CHECK (income_level IN ('A', 'B', 'C', 'D')),
    age_group VARCHAR(10) NOT NULL CHECK (age_group IN ('18-24', '25-34', '35-44', '45-54', '55+')),
    mobility_pattern VARCHAR(10) NOT NULL CHECK (mobility_pattern IN ('LOW', 'MODERATE', 'INTENSE')),
    home_cluster VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Indexes for frequent queries
CREATE INDEX IF NOT EXISTS idx_citizen_profiles_income_level
    ON demographics_schema.citizen_profiles (income_level);

CREATE INDEX IF NOT EXISTS idx_citizen_profiles_age_group
    ON demographics_schema.citizen_profiles (age_group);

CREATE INDEX IF NOT EXISTS idx_citizen_profiles_home_cluster
    ON demographics_schema.citizen_profiles (home_cluster);

-- Index for vulnerable population queries (cluster D)
CREATE INDEX IF NOT EXISTS idx_citizen_profiles_vulnerable
    ON demographics_schema.citizen_profiles (income_level)
    WHERE income_level = 'D';

-- Comments for documentation
COMMENT ON TABLE demographics_schema.citizen_profiles IS 'Anonymized demographic citizen profiles';
COMMENT ON COLUMN demographics_schema.citizen_profiles.citizen_hash IS 'Unique anonymized hash of the citizen';
COMMENT ON COLUMN demographics_schema.citizen_profiles.income_level IS 'Income level: A (high), B (medium-high), C (medium), D (low - vulnerable)';
COMMENT ON COLUMN demographics_schema.citizen_profiles.age_group IS 'Age group: 18-24, 25-34, 35-44, 45-54, 55+';
COMMENT ON COLUMN demographics_schema.citizen_profiles.mobility_pattern IS 'Mobility pattern: LOW, MODERATE, INTENSE';
COMMENT ON COLUMN demographics_schema.citizen_profiles.home_cluster IS 'Geographic cluster of probable residence';