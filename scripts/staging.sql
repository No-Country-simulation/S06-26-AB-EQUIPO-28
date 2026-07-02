-- 1. Crear esquema temporal
CREATE SCHEMA IF NOT EXISTS staging;

-- 2. Tabla idéntica a assinantes.csv
CREATE TABLE staging.assinantes (
    assinante_hash BIGINT,
    home_cluster VARCHAR(100),
    home_municipio VARCHAR(100),
    income_cluster CHAR(1),
    age_group VARCHAR(20),
    mobility_pattern VARCHAR(50),
    flag_flagship SMALLINT
);

-- 3. Tabla idéntica a antenas_flp.csv
CREATE TABLE staging.antennas (
    ecgi VARCHAR(50),
    cluster VARCHAR(100),
    municipio VARCHAR(100),
    lat DOUBLE PRECISION,
    lon DOUBLE PRECISION
);

-- 3. Tabla idéntica a tensor_concentracao.csv
CREATE TABLE staging.tensor_concentracao (
    ecgi VARCHAR(50),
    cluster VARCHAR(100),
    municipio VARCHAR(100),
    day_date DATE,
    periodo VARCHAR(20),
    n_usuarios INTEGER,
    n_sessoes INTEGER,
    download_bytes BIGINT,
    upload_bytes BIGINT,
    dur_media_s INTEGER,
    drop_pct_medio DOUBLE PRECISION,
    congestionamento_medio DOUBLE PRECISION,
    chamadas_total INTEGER,
    mensagens_total INTEGER,
    lat DOUBLE PRECISION,
    lon DOUBLE PRECISION
);