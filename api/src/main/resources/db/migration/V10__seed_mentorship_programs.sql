-- ============================================================================
-- Migration V10: Seed mentorship programs catalog
-- ----------------------------------------------------------------------------
-- Loads 26 synthetic but coherent mentorship programs for the Florianópolis RM
-- into the mentorship_schema.mentorship_programs table created by V9.
--
-- Coverage strategy:
--   * 15 clusters WITH programs (better connectivity / consolidated urban zones)
--   * 8 clusters intentionally WITHOUT programs -> gap analysis demo:
--       AEROPORTO_HLZ, BIGUACU_BR101_NORTE, CAMPECHE, JURERE,
--       NORTE_ILHA, PALHOCA_CENTRO, PALHOCA_PEDRA_BRANCA, SAO_JOSE_BARREIROS
--     (several of those are LOW connectivity + income D > 21% -> CRITICAL gaps)
--
-- Clusters match EXACTLY the clusters from the CDRView dataset
-- (telemetry_schema.antennas), so every cluster_name here is a real cluster.
--
-- Organizations are real entities from Florianópolis/SC (UFSC, SENAC, SENAI,
-- SESC, SEST/SENAT, ACATE, SEBRAE, Prefeitura, etc.). Contact data is plausible
-- but not verified. DO NOT execute against production without review.
--
-- Verification queries are kept as comments at the bottom of this file.
-- ============================================================================

INSERT INTO mentorship_schema.mentorship_programs (
    program_id, name, description, organization, focus_area, modality,
    target_audience, target_income_level, cluster_name, total_capacity,
    active_mentees, start_date, end_date, is_active, website_url, contact_email
) VALUES
    -- ===== UFSC cluster (3) — university hub, HIGH connectivity anchor =====
    ('MPR-001',
     'Tech Mentorship UFSC',
     'Technology mentorship program for UFSC undergraduate students in computing, engineering and data science tracks, pairing them with industry professionals.',
     'UFSC', 'TECH', 'IN_PERSON', 'YOUNG_ADULTS', 'ALL', 'UFSC',
     50, 35, '2025-03-01', NULL, true,
     'https://mentoria.paginas.ufsc.br', 'mentoria@ufsc.br'),

    ('MPR-002',
     'UFSC Extension: Digital Inclusion for Women',
     'Extension program training low-income women in basic digital literacy, office automation and online government services access.',
     'UFSC', 'TECH', 'HYBRID', 'WOMEN', 'C', 'UFSC',
     40, 28, '2024-08-15', NULL, true,
     'https://extensao.ufsc.br/inclusao-digital', 'inclusao.digital@ufsc.br'),

    ('MPR-003',
     'UFSC Careers: First Job Mentoring',
     'Career mentoring for final-year students covering CV building, interview practice and labour market orientation in the Florianópolis RM.',
     'UFSC', 'EMPLOYMENT', 'IN_PERSON', 'YOUNG_ADULTS', 'ALL', 'UFSC',
     80, 64, '2024-02-01', '2025-12-15', true,
     'https://career.ufsc.br/first-job', 'career@ufsc.br'),

    -- ===== TRINDADE cluster (2) — adjacent to UFSC, dense urban =====
    ('MPR-004',
     'ACATE Tech Mentoring',
     'Mentorship driven by the Santa Catarina Technology Association connecting startups and established tech companies with junior developers from the region.',
     'ACATE', 'TECH', 'HYBRID', 'YOUNG_ADULTS', 'ALL', 'TRINDADE',
     60, 41, '2025-01-20', NULL, true,
     'https://www.acate.com.br/mentoria', 'mentoria@acate.com.br'),

    ('MPR-005',
     'SEBRAE SC: Entrepreneurial Mindset',
     'Mentorship for aspiring entrepreneurs in the early stages of business validation, focused on the urban corridor of Trindade.',
     'SEBRAE SC', 'EMPLOYMENT', 'IN_PERSON', 'GENERAL', 'B', 'TRINDADE',
     45, 33, '2024-09-10', NULL, true,
     'https://www.sebrae.com.br/sc', 'sc@sebrae.com.br'),

    -- ===== CBD_BEIRAMAR cluster (2) — central business district, MEDIUM =====
    ('MPR-006',
     'SENAC SC: Professional Re-skilling',
     'Short-cycle professional re-skilling courses in retail, hospitality and administrative support for adults seeking re-entry into the labour market.',
     'SENAC SC', 'EMPLOYMENT', 'IN_PERSON', 'GENERAL', 'C', 'CBD_BEIRAMAR',
     120, 88, '2024-03-05', NULL, true,
     'https://www.sc.senac.br/qualificacao', 'qualificacao@sc.senac.br'),

    ('MPR-007',
     'SENAI SC: Industry 4.0 Fundamentals',
     'Technical mentorship on Industry 4.0 fundamentals (IoT, automation, basic data analysis) for technicians and operators.',
     'SENAI SC', 'TECH', 'IN_PERSON', 'GENERAL', 'C', 'CBD_BEIRAMAR',
     70, 52, '2024-06-01', NULL, true,
     'https://www.sc.senai.br/industria40', 'industria40@sc.senai.br'),

    -- ===== CENTRO_HISTORICO cluster (2) — cultural core, LOW connectivity =====
    ('MPR-008',
     'CASQ Cultural Mentorship',
     'Mentorship in traditional crafts, music and performing arts led by the Santa Catarina Culture Association for young residents of the historic centre.',
     'CASQ', 'CULTURE', 'IN_PERSON', 'YOUNG_ADULTS', 'C', 'CENTRO_HISTORICO',
     35, 19, '2024-05-12', NULL, true,
     'https://www.casq.org.br/cultura', 'cultura@casq.org.br'),

    ('MPR-009',
     'SESC SC: Active Ageing Programme',
     'Mentorship and social activities for elderly residents covering digital literacy, health awareness and community engagement.',
     'SESC SC', 'HEALTH', 'IN_PERSON', 'ELDERLY', 'ALL', 'CENTRO_HISTORICO',
     50, 38, '2024-04-01', NULL, true,
     'https://www.sesc-sc.com.br/idosos', 'idosos@sesc-sc.com.br'),

    -- ===== ESTREITO_CAPOEIRAS cluster (2) — mainland MEDIUM =====
    ('MPR-010',
     'Projovem Urbano Florianópolis',
     'Federal programme for young adults aged 18-29 offering vocational initiation, citizenship and basic qualification in the Estreito zone.',
     'Prefeitura Florianópolis', 'EMPLOYMENT', 'IN_PERSON', 'YOUNG_ADULTS', 'D', 'ESTREITO_CAPOEIRAS',
     100, 72, '2024-02-20', NULL, true,
     'https://www.florianopolis.sc.gov.br/projovem', 'projovem@pmf.sc.gov.br'),

    ('MPR-011',
     'Programa Nossa Bolsa: Education Support',
     'Educational mentorship and scholarship support for low-income students from the mainland side of the metropolitan area.',
     'Instituto Geração', 'EDUCATION', 'HYBRID', 'YOUNG_ADULTS', 'D', 'ESTREITO_CAPOEIRAS',
     55, 47, '2025-01-15', NULL, true,
     'https://www.institutogeracao.org.br/nossa-bolsa', 'contato@institutogeracao.org.br'),

    -- ===== COQUEIROS cluster (1) — residential MEDIUM =====
    ('MPR-012',
     'PMF Education: Homework Support',
     'After-school homework support and reading mentorship run by the Florianópolis Municipal Secretariat of Education in the Coqueiros district.',
     'PMF Secretaria de Educacao', 'EDUCATION', 'IN_PERSON', 'YOUNG_ADULTS', 'C', 'COQUEIROS',
     60, 44, '2024-03-01', NULL, true,
     'https://educacao.florianopolis.sc.gov.br/apoio', 'educacao@pmf.sc.gov.br'),

    -- ===== SC401_CORREDOR cluster (2) — high-mobility corridor MEDIUM =====
    ('MPR-013',
     'Bolsa Futuro Digital SC',
     'MCTI-backed digital formation programme offering scholarships for tech tracks (programming, data, cloud) to young adults along the SC-401 corridor.',
     'MCTI', 'TECH', 'HYBRID', 'YOUNG_ADULTS', 'C', 'SC401_CORREDOR',
     90, 68, '2025-02-01', NULL, true,
     'https://www.gov.br/mcti/bolsa-futuro-digital', 'bolsa.futuro@mcti.gov.br'),

    ('MPR-014',
     'SEBRAE SC: Startup Pre-Acceleration',
     'Pre-acceleration mentorship for early-stage startups along the SC-401 innovation corridor, with focus on product validation and go-to-market.',
     'SEBRAE SC', 'EMPLOYMENT', 'HYBRID', 'GENERAL', 'B', 'SC401_CORREDOR',
     30, 18, '2024-07-01', NULL, true,
     'https://www.sebrae.com.br/sc/startups', 'startups.sc@sebrae.com.br'),

    -- ===== SAO_JOSE_KOBRASOL cluster (2) — HIGH connectivity hub =====
    ('MPR-015',
     'SENAI Florianópolis: Full-Stack Bootcamp',
     'Intensive full-stack development bootcamp with mentorship and project-based learning in the Kobrasol commercial zone.',
     'SENAI Florianópolis', 'TECH', 'IN_PERSON', 'YOUNG_ADULTS', 'C', 'SAO_JOSE_KOBRASOL',
     75, 60, '2025-03-10', NULL, true,
     'https://www.sc.senai.br/florianopolis/bootcamp', 'bootcamp@sc.senai.br'),

    ('MPR-016',
     'SEST/SENAT: Logistics Career Path',
     'Career path mentorship for freight transport workers and drivers transitioning into logistics coordination roles.',
     'SEST/SENAT', 'EMPLOYMENT', 'HYBRID', 'GENERAL', 'C', 'SAO_JOSE_KOBRASOL',
     45, 29, '2024-10-01', NULL, true,
     'https://www.sestsenat.org.br/sc', 'sc@sestsenat.org.br'),

    -- ===== SAO_JOSE_CENTRO cluster (2) — mainland LOW =====
    ('MPR-017',
     'PRONATEc São José: Technical Qualification',
     'National programme for technical education access offering free qualification courses at the São José technical centre.',
     'PRONATEc', 'TECH', 'IN_PERSON', 'YOUNG_ADULTS', 'D', 'SAO_JOSE_CENTRO',
     110, 81, '2024-04-15', NULL, true,
     'https://www.gov.br/pt-br/servicos/pronatec', 'pronatec.sc@mec.gov.br'),

    ('MPR-018',
     'SESC SC: Community Health Workshops',
     'Series of community health promotion workshops and mentorship for residents of the São José central district.',
     'SESC SC', 'HEALTH', 'IN_PERSON', 'GENERAL', 'C', 'SAO_JOSE_CENTRO',
     40, 26, '2024-08-01', '2025-07-31', true,
     'https://www.sesc-sc.com.br/saude-comunitaria', 'saude@sesc-sc.com.br'),

    -- ===== CANASVIEIRAS cluster (1) — HIGH coastal =====
    ('MPR-019',
     'SENAC SC: Tourism & Hospitality Mentoring',
     'Mentorship in tourism, hospitality and service excellence targeting seasonal workers of the Canasvieiras coastal zone.',
     'SENAC SC', 'EMPLOYMENT', 'IN_PERSON', 'GENERAL', 'C', 'CANASVIEIRAS',
     80, 55, '2024-09-01', NULL, true,
     'https://www.sc.senac.br/turismo', 'turismo@sc.senac.br'),

    -- ===== LAGOA_CONCEICAO cluster (2) — HIGH, mixed income =====
    ('MPR-020',
     'ACATE Lagoa: Tech for Women',
     'Mentorship dedicated to women pursuing tech careers in the Lagoa da Conceição area, with remote-first delivery.',
     'ACATE', 'TECH', 'REMOTE', 'WOMEN', 'B', 'LAGOA_CONCEICAO',
     35, 22, '2025-04-01', NULL, true,
     'https://www.acate.com.br/mulheres-tech', 'mulheres.tech@acate.com.br'),

    ('MPR-021',
     'SEBRAE SC: Sustainable Tourism',
     'Mentorship for small tourism businesses in Lagoa da Conceição focused on sustainability, digital presence and local value chains.',
     'SEBRAE SC', 'EMPLOYMENT', 'HYBRID', 'GENERAL', 'B', 'LAGOA_CONCEICAO',
     50, 37, '2024-06-15', NULL, true,
     'https://www.sebrae.com.br/sc/turismo-sustentavel', 'turismo.sc@sebrae.com.br'),

    -- ===== INGLESES cluster (1) — HIGH coastal =====
    ('MPR-022',
     'PMF Education: Coastal Digital Literacy',
     'Digital literacy mentorship for residents of the Ingleses coastal community, focusing on access to online public services.',
     'PMF Secretaria de Educacao', 'EDUCATION', 'IN_PERSON', 'GENERAL', 'C', 'INGLESES',
     45, 31, '2024-11-01', NULL, true,
     'https://educacao.florianopolis.sc.gov.br/ingleses', 'ingleses@pmf.sc.gov.br'),

    -- ===== RESIDENCIAL_NORTE cluster (2) — HIGH residential =====
    ('MPR-023',
     'Instituto Geração: Youth Mentorship',
     'Long-term youth mentorship programme combining life skills, citizenship and career orientation for residents of the northern residential zone.',
     'Instituto Geração', 'GENERAL', 'IN_PERSON', 'YOUNG_ADULTS', 'C', 'RESIDENCIAL_NORTE',
     60, 48, '2024-05-01', NULL, true,
     'https://www.institutogeracao.org.br/jovens', 'jovens@institutogeracao.org.br'),

    ('MPR-024',
     'SESC SC: Elderly Digital Inclusion',
     'Digital inclusion mentorship for elderly residents of the northern residential zone, with volunteer mentors and assisted device use.',
     'SESC SC', 'TECH', 'IN_PERSON', 'ELDERLY', 'C', 'RESIDENCIAL_NORTE',
     30, 21, '2025-02-10', NULL, true,
     'https://www.sesc-sc.com.br/inclusao-idosos', 'inclusao.idosos@sesc-sc.com.br'),

    -- ===== SAO_JOSE_ROÇADO cluster (1) — HIGH suburban =====
    ('MPR-025',
     'SENAC SC: Administrative Qualification',
     'Administrative qualification and mentorship programme for residents of the Roçado district seeking office roles.',
     'SENAC SC', 'EMPLOYMENT', 'IN_PERSON', 'GENERAL', 'D', 'SAO_JOSE_ROÇADO',
     65, 50, '2024-07-20', NULL, true,
     'https://www.sc.senac.br/administrativo', 'administrativo@sc.senac.br'),

    -- ===== VIA_EXPRESSA_CORREDOR cluster (1) — MEDIUM mobility corridor =====
    ('MPR-026',
     'Bolsa Futuro Digital: Data Track',
     'Remote-first data analytics formation track along the Via Expressa corridor, with scholarships and mentor-mentee pairing.',
     'MCTI', 'TECH', 'REMOTE', 'YOUNG_ADULTS', 'C', 'VIA_EXPRESSA_CORREDOR',
     70, 49, '2025-01-05', NULL, true,
     'https://www.gov.br/mcti/bolsa-futuro-digital/dados', 'dados.futuro@mcti.gov.br');

-- ============================================================================
-- Verification queries (NOT executed by Flyway — kept here for documentation only)
-- ============================================================================
--
-- -- Verification 1: total count
-- SELECT COUNT(*) FROM mentorship_schema.mentorship_programs;
--
-- -- Verification 2: active programs per cluster (coverage map)
-- SELECT cluster_name, COUNT(*) AS program_count,
--        STRING_AGG(DISTINCT focus_area, ', ') AS focus_areas
-- FROM mentorship_schema.mentorship_programs
-- WHERE is_active = true
-- GROUP BY cluster_name
-- ORDER BY cluster_name;
--
-- -- Verification 3: clusters WITHOUT active programs (the gap demo)
-- SELECT a.cluster
-- FROM (SELECT DISTINCT cluster FROM telemetry_schema.antennas) a
-- LEFT JOIN mentorship_schema.mentorship_programs mp
--        ON mp.cluster_name = a.cluster AND mp.is_active = true
-- WHERE mp.id IS NULL
-- ORDER BY a.cluster;
