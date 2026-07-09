-- ============================================================================
-- V11: Empleabilidad vertical tables
-- Bounded Context: employability (mobility OD matrix + inter-cluster travel times)
-- Source data: CDRView tensor_od.csv and tensor_tempo_deslocamento.csv
-- ============================================================================

CREATE SCHEMA IF NOT EXISTS employability_schema;

-- ---------------------------------------------------------------------------
-- mobility_od_pairs: Origin-Destination mobility matrix from CDRView tensor_od.csv
-- ---------------------------------------------------------------------------
CREATE TABLE employability_schema.mobility_od_pairs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    origin_cluster VARCHAR(100) NOT NULL,
    origin_municipio VARCHAR(100),
    origin_latitude DOUBLE PRECISION,
    origin_longitude DOUBLE PRECISION,
    destination_cluster VARCHAR(100) NOT NULL,
    destination_municipio VARCHAR(100),
    destination_latitude DOUBLE PRECISION,
    destination_longitude DOUBLE PRECISION,
    same_cluster BOOLEAN NOT NULL DEFAULT false,
    unique_users INTEGER NOT NULL,
    total_trips INTEGER NOT NULL,
    avg_distance_km DOUBLE PRECISION NOT NULL,
    predominant_period VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_od_distance CHECK (avg_distance_km >= 0),
    CONSTRAINT chk_od_period CHECK (predominant_period IN ('DAWN', 'MORNING', 'AFTERNOON', 'NIGHT')),
    CONSTRAINT chk_od_users CHECK (unique_users >= 0),
    CONSTRAINT chk_od_trips CHECK (total_trips >= 0),
    UNIQUE (origin_cluster, destination_cluster)
);

-- ---------------------------------------------------------------------------
-- travel_times: Inter-cluster travel times from CDRView tensor_tempo_deslocamento.csv
-- ---------------------------------------------------------------------------
CREATE TABLE employability_schema.travel_times (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    origin_cluster VARCHAR(100) NOT NULL,
    destination_cluster VARCHAR(100) NOT NULL,
    same_cluster BOOLEAN NOT NULL DEFAULT false,
    observations INTEGER NOT NULL,
    avg_distance_km DOUBLE PRECISION NOT NULL,
    p25_distance_km DOUBLE PRECISION,
    p75_distance_km DOUBLE PRECISION,
    predominant_period VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_tt_distance CHECK (avg_distance_km >= 0),
    CONSTRAINT chk_tt_period CHECK (predominant_period IN ('DAWN', 'MORNING', 'AFTERNOON', 'NIGHT')),
    CONSTRAINT chk_tt_obs CHECK (observations >= 0),
    UNIQUE (origin_cluster, destination_cluster)
);

-- ---------------------------------------------------------------------------
-- Indexes
-- ---------------------------------------------------------------------------
CREATE INDEX idx_od_origin ON employability_schema.mobility_od_pairs(origin_cluster);
CREATE INDEX idx_od_destination ON employability_schema.mobility_od_pairs(destination_cluster);
CREATE INDEX idx_od_period ON employability_schema.mobility_od_pairs(predominant_period);
CREATE INDEX idx_tt_origin ON employability_schema.travel_times(origin_cluster);
CREATE INDEX idx_tt_destination ON employability_schema.travel_times(destination_cluster);

-- ---------------------------------------------------------------------------
-- Comments (catalog metadata)
-- ---------------------------------------------------------------------------
COMMENT ON TABLE employability_schema.mobility_od_pairs IS 'Origin-Destination mobility matrix from CDRView tensor_od.csv';
COMMENT ON COLUMN employability_schema.mobility_od_pairs.origin_cluster IS 'Geographic cluster of origin (CDRView)';
COMMENT ON COLUMN employability_schema.mobility_od_pairs.destination_cluster IS 'Geographic cluster of destination (CDRView)';
COMMENT ON COLUMN employability_schema.mobility_od_pairs.unique_users IS 'Distinct users who traveled this OD pair';
COMMENT ON COLUMN employability_schema.mobility_od_pairs.total_trips IS 'Total trips observed for this OD pair';
COMMENT ON COLUMN employability_schema.mobility_od_pairs.avg_distance_km IS 'Average distance in km (Haversine)';
COMMENT ON COLUMN employability_schema.mobility_od_pairs.predominant_period IS 'Predominant time period: DAWN, MORNING, AFTERNOON, NIGHT';
COMMENT ON TABLE employability_schema.travel_times IS 'Inter-cluster travel times from CDRView tensor_tempo_deslocamento.csv';
COMMENT ON COLUMN employability_schema.travel_times.observations IS 'Number of observations for this OD pair';
COMMENT ON COLUMN employability_schema.travel_times.avg_distance_km IS 'Average distance in km';
COMMENT ON COLUMN employability_schema.travel_times.p25_distance_km IS '25th percentile distance in km';
COMMENT ON COLUMN employability_schema.travel_times.p75_distance_km IS '75th percentile distance in km';