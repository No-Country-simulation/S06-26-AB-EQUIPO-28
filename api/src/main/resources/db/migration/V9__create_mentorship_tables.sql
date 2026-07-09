-- Migration: Create mentorship tables
-- Description: Public mentorship programs catalog for the B2G AppBit challenge.
--
-- The mentorship vertical exposes public programs (TECH, EMPLOYMENT, HEALTH, CULTURE,
-- EDUCATION, GENERAL) that government managers can cross with inclusion-core gap
-- analysis to prioritize intervention zones. The cluster_name column links each
-- program to the Visent CDRView geographic clusters (telemetry/demographics BCs).
--
-- UUID v7 is used as the primary key (time-based, optimal B-Tree append behaviour).
-- See V1__create_uuid_v7_function.sql for the function definition.

CREATE SCHEMA IF NOT EXISTS mentorship_schema;

CREATE TABLE mentorship_schema.mentorship_programs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    program_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    organization VARCHAR(255),
    focus_area VARCHAR(50) NOT NULL,
    modality VARCHAR(50) NOT NULL,
    target_audience VARCHAR(50),
    target_income_level VARCHAR(10),
    cluster_name VARCHAR(100) NOT NULL,
    total_capacity INTEGER,
    active_mentees INTEGER DEFAULT 0,
    start_date DATE,
    end_date DATE,
    is_active BOOLEAN DEFAULT true,
    website_url VARCHAR(500),
    contact_email VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_mentees CHECK (active_mentees >= 0 AND active_mentees <= COALESCE(total_capacity, 999999)),
    CONSTRAINT chk_dates CHECK (end_date IS NULL OR start_date IS NULL OR end_date >= start_date),
    CONSTRAINT chk_income CHECK (target_income_level IS NULL OR target_income_level IN ('A', 'B', 'C', 'D', 'ALL'))
);

CREATE INDEX idx_mp_cluster_name ON mentorship_schema.mentorship_programs(cluster_name);
CREATE INDEX idx_mp_focus_area ON mentorship_schema.mentorship_programs(focus_area);
CREATE INDEX idx_mp_is_active ON mentorship_schema.mentorship_programs(is_active);

COMMENT ON TABLE mentorship_schema.mentorship_programs IS 'Public mentorship programs catalog for the B2G AppBit challenge';
COMMENT ON COLUMN mentorship_schema.mentorship_programs.program_id IS 'Unique business identifier for the mentorship program (e.g., MPR-001)';
COMMENT ON COLUMN mentorship_schema.mentorship_programs.focus_area IS 'Mentorship focus area: TECH, EMPLOYMENT, HEALTH, CULTURE, EDUCATION, GENERAL';
COMMENT ON COLUMN mentorship_schema.mentorship_programs.modality IS 'Delivery modality: REMOTE, IN_PERSON, HYBRID';
COMMENT ON COLUMN mentorship_schema.mentorship_programs.target_audience IS 'Target demographic: YOUNG_ADULTS, WOMEN, ELDERLY, GENERAL';
COMMENT ON COLUMN mentorship_schema.mentorship_programs.target_income_level IS 'Target income cluster: A, B, C, D, ALL';
COMMENT ON COLUMN mentorship_schema.mentorship_programs.cluster_name IS 'Geographic cluster from Visent CDRView dataset (connects to telemetry/demographics)';
