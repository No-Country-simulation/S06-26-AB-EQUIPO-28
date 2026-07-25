-- 1. Migrar Ciudadanos (Demographics)
INSERT INTO demographics_schema.citizen_profiles (
    citizen_hash, 
    home_cluster, 
    income_level, 
    age_group, 
    mobility_pattern
)
SELECT 
    assinante_hash::VARCHAR, -- Cast a String CitizenId
    home_cluster, 
    income_cluster, 
    age_group, 
    CASE -- Traducción para MobilityPattern
        WHEN mobility_pattern = 'BAIXA' THEN 'LOW'
        WHEN mobility_pattern = 'MODERADA' THEN 'MODERATE'
        WHEN mobility_pattern = 'INTENSA' THEN 'INTENSE'
        ELSE 'LOW'
    END
FROM staging.assinantes;

-- 2. Migrar Antenas
INSERT INTO telemetry_schema.antennas (ecgi, cluster, municipality, latitude, longitude)
SELECT ecgi, cluster, municipio, lat, lon
FROM staging.antennas;

-- 3. Migrar Telemetría (Telemetry)
INSERT INTO telemetry_schema.network_concentration (
    id, 
    ecgi, 
    day_date, 
    session_period, 
    user_count, 
    session_count, 
    download_bytes, 
    upload_bytes, 
    average_duration_s, 
    drop_pct, 
    congestion_level, 
    total_calls, 
    total_messages, 
    latitude, 
    longitude
)
SELECT 
    gen_random_uuid(),
    ecgi, 
    day_date, 
    CASE -- Traducción para SessionPeriod Enum
        WHEN periodo = 'MADRUGADA' THEN 'DAWN'
        WHEN periodo = 'MANHA' THEN 'MORNING'
        WHEN periodo = 'TARDE' THEN 'AFTERNOON'
        WHEN periodo = 'NOITE' THEN 'NIGHT'
    END,
    n_usuarios, 
    n_sessoes, 
    download_bytes, 
    upload_bytes, 
    dur_media_s::DOUBLE PRECISION, 
    drop_pct_medio, 
    congestionamento_medio, 
    chamadas_total, 
    mensagens_total, 
    lat, 
    lon
FROM staging.tensor_concentracao;

-- 4. Migrar Matriz Origen-Destino (Employability)
INSERT INTO employability_schema.mobility_od_pairs (
    origin_cluster, origin_municipio, origin_latitude, origin_longitude,
    destination_cluster, destination_municipio, destination_latitude, destination_longitude,
    same_cluster, unique_users, total_trips, avg_distance_km, predominant_period
)
SELECT 
    cluster_origem,
    municipio_origem,
    lat_origem,
    lon_origem,
    cluster_destino,
    municipio_destino,
    lat_destino,
    lon_destino,
    CASE WHEN mesmo_cluster = 1 THEN true ELSE false END,
    n_usuarios,
    n_viagens,
    dist_media_km,
    CASE -- Traducción para SessionPeriod
        WHEN periodo_predominante = 'MADRUGADA' THEN 'DAWN'
        WHEN periodo_predominante = 'MANHA' THEN 'MORNING'
        WHEN periodo_predominante = 'MANHÃ' THEN 'MORNING'
        WHEN periodo_predominante = 'TARDE' THEN 'AFTERNOON'
        WHEN periodo_predominante = 'NOITE' THEN 'NIGHT'
        ELSE 'NIGHT'
    END
FROM staging.tensor_od;

-- 5. Migrar Tiempos de Desplazamiento (Employability)
INSERT INTO employability_schema.travel_times (
    origin_cluster, destination_cluster, same_cluster,
    observations, avg_distance_km, p25_distance_km, p75_distance_km, predominant_period
)
SELECT 
    cluster_origem,
    cluster_destino,
    CASE WHEN mesmo_cluster = 1 THEN true ELSE false END,
    n_observacoes,
    dist_media_km,
    dist_p25_km,
    dist_p75_km,
    CASE
        WHEN periodo_predominante = 'MADRUGADA' THEN 'DAWN'
        WHEN periodo_predominante = 'MANHA' THEN 'MORNING'
        WHEN periodo_predominante = 'MANHÃ' THEN 'MORNING'
        WHEN periodo_predominante = 'TARDE' THEN 'AFTERNOON'
        WHEN periodo_predominante = 'NOITE' THEN 'NIGHT'
        ELSE 'NIGHT'
    END
FROM staging.tensor_tempo_deslocamento;