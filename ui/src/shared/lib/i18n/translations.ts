// ---------------------------------------------------------------------------
// Translations — Spanish (default) + Portuguese (alternative)
//
// Keys use dot notation for namespacing. Spanish is the default/fallback
// language. Portuguese is the secondary alternative.
// ---------------------------------------------------------------------------

export const LOCALES = {
  es: "Español",
  pt: "Português",
} as const;

export type Locale = keyof typeof LOCALES;

// ---------------------------------------------------------------------------
// Translation dictionary
// ---------------------------------------------------------------------------

export type TranslationKey = keyof typeof translations.es;

export const translations: Record<Locale, Record<string, string>> = {
  es: {
    // ── Navbar ──────────────────────────────────────────────────────
    "nav.map": "Mapa Interactivo",
    "nav.methodology": "Metodología",
    "nav.brand": "App BiT",
    "nav.menuLabel": "Abrir menú de navegación",

    // ── Dashboard ───────────────────────────────────────────────────
    "dashboard.title": "Consultas de IA",
    "dashboard.subtitle": "Haz preguntas en lenguaje natural sobre los datos",
    "dashboard.summary": "Resumen",
    "dashboard.stats": "Estadísticas Rápidas",
    "dashboard.regions": "Regiones",
    "dashboard.statsPrioritarias": "Prioritarias",
    "dashboard.statsPopulation": "Población",
    "dashboard.statsScore": "Puntuación",
    "dashboard.emptyBanner": "Seleccioná una región para ver los indicadores de inclusión.",
    "dashboard.vulnerabilityScore": "Puntuación de Vulnerabilidad",
    "dashboard.allRegions": "Todas las regiones",
    "dashboard.viewing": "Visualizando",
    "dashboard.newQuery": "Nueva consulta",
    "dashboard.searchPlaceholder": "Ej: ¿Cuáles son las regiones más vulnerables?",
    "dashboard.loading": "Procesando consulta…",
    "dashboard.error": "Error al procesar la consulta",
    "dashboard.retry": "Intentar de nuevo",
    "dashboard.keyFindings": "Principales Hallazgos",
    "dashboard.recommendedActions": "Acciones Recomendadas",
    "dashboard.sources": "Fuentes",
    "dashboard.warnings": "Advertencias",
    "dashboard.indicators.all": "Todos",

    // ── Map ─────────────────────────────────────────────────────────
    "map.title": "Mapa Interactivo — Visualización Geoespacial",
    "map.placeholder": "Mapa se cargará al instalar maplibre-gl",
    "map.loading": "Cargando mapa…",
    "map.error": "Error al cargar el mapa",
    "map.period.dawn": "Madrugada",
    "map.period.morning": "Mañana",
    "map.period.afternoon": "Tarde",
    "map.period.night": "Noche",
    "map.vulnerableOnly": "Solo regiones vulnerables (≥66)",
    "map.vulnerability": "Vulnerabilidad",
    "map.population": "Población",
    "map.connectivity": "Conectividad",
    "map.priority": "Prioritaria",
    "map.selectRegion": "Selecciona una región para ver detalles",
    "map.nearbyRegions": "Regiones cercanas",
    "map.legend.title": "Leyenda",
    "map.legend.antenna": "Antena",
    "map.legend.highConcentration": "Alta concentración",
    "map.legend.vulnerableRegion": "Región vulnerable",
    "map.legend.low": "Baja",
    "map.legend.high": "Alta",
    "map.region": "Región",

    // ── Methodology ─────────────────────────────────────────────────
    "methodology.title": "Metodología y Fuentes",
    "methodology.subtitle": "Transparencia sobre datos, métodos y privacidad",
    "methodology.dataSources": "Fuentes de Datos",
    "methodology.aiMethodology": "Metodología de IA",
    "methodology.privacy": "Privacidad y Cumplimiento",
    "methodology.syntheticData": "Datos Sintéticos",
    "methodology.syntheticDataDesc":
      "Todos los datos de movilidad urbana mostrados en la plataforma son sintéticos, generados artificialmente para fines de prototipado y demostración. No se utilizan datos reales de operadoras de telefonía.",
    "methodology.anonymization": "Anonimización",
    "methodology.anonymizationDesc":
      "Los datos demográficos se agregan por clúster regional y rango de ingresos, sin identificación individual. Los ciudadanos se representan mediante hashes anonimizados.",
    "methodology.lgpd": "LGPD",
    "methodology.lgpdDesc":
      "La plataforma sigue los principios de la Ley General de Protección de Datos (LGPD — Ley nº 13.709/2018), incluyendo finalidad, adecuación, necesidad y transparencia en el tratamiento de datos.",
    "methodology.security": "Seguridad",
    "methodology.securityDesc":
      "El acceso a la API está protegido por autenticación JWT con tokens de acceso y refresh. Los endpoints de IA tienen rate limiting de 10 solicitudes por minuto por IP para evitar abusos.",

    // ── Common ──────────────────────────────────────────────────────
    "common.loading": "Cargando…",
    "common.error": "Error",
    "common.retry": "Intentar de nuevo",
    "common.close": "Cerrar",
    "common.submit": "Enviar",
    "common.cancel": "Cancelar",
    "common.noData": "Sin datos disponibles",
    "common.search": "Buscar",

    // ── Indicators ──────────────────────────────────────────────────
    "indicator.employabilityGap": "Brecha de Empleabilidad",
    "indicator.trainingCoverage": "Cobertura de Formación",
    "indicator.mentalHealthAccess": "Acceso a Salud Mental",
    "indicator.mentorshipPrograms": "Programas de Mentoría",
    "indicator.structuredExperiences": "Experiencias Estructurantes",

    // ── Severity ────────────────────────────────────────────────────
    "severity.high": "Alto",
    "severity.medium": "Medio",
    "severity.low": "Bajo",
    "severity.critical": "Crítico",

    // ── Trends ──────────────────────────────────────────────────────
    "trend.improving": "Mejorando",
    "trend.stable": "Estable",
    "trend.declining": "Deteriorando",

    // ── Badge ───────────────────────────────────────────────────────
    "badge.success": "Éxito",
    "badge.warning": "Advertencia",
    "badge.error": "Error",
    "badge.info": "Información",
    "badge.neutral": "Neutral",

    // ── Mental Health ──────────────────────────────────────────────
    "mentalHealth.title": "Salud Mental Comunitaria",
    "mentalHealth.indicators": "Indicadores",
    "mentalHealth.score": "Puntuación",
    "mentalHealth.scoreTooltip": "Puntuación de salud mental basada en indicadores de infraestructura y acceso",
    "mentalHealth.scoreLow": "Bajo — Cobertura insuficiente",
    "mentalHealth.scoreMedium": "Medio — Cobertura parcial",
    "mentalHealth.scoreHigh": "Alto — Buena cobertura",
    "mentalHealth.vulnerablePopulation": "Población vulnerable",
    "mentalHealth.averageScore": "Score promedio",
    "mentalHealth.priorityRegions": "Regiones prioritarias",
    "map.showAntennas": "Mostrar antenas",
    "map.highConcentrationOnly": "Solo alta concentración",
  },

  pt: {
    // ── Navbar ──────────────────────────────────────────────────────
    "nav.map": "Mapa Interativo",
    "nav.methodology": "Metodologia",
    "nav.brand": "App BiT",
    "nav.menuLabel": "Abrir menu de navegação",

    // ── Dashboard ───────────────────────────────────────────────────
    "dashboard.title": "Consultas de IA",
    "dashboard.subtitle": "Faça perguntas em linguagem natural sobre os dados",
    "dashboard.summary": "Resumo",
    "dashboard.stats": "Estatísticas Rápidas",
    "dashboard.regions": "Regiões",
    "dashboard.statsPrioritarias": "Prioritárias",
    "dashboard.statsPopulation": "População",
    "dashboard.statsScore": "Pontuação",
    "dashboard.emptyBanner": "Selecione uma região para ver os indicadores de inclusão.",
    "dashboard.vulnerabilityScore": "Pontuação de Vulnerabilidade",
    "dashboard.allRegions": "Todas as regiões",
    "dashboard.viewing": "Visualizando",
    "dashboard.newQuery": "Nova consulta",
    "dashboard.searchPlaceholder": "Ex: Quais são as regiões mais vulneráveis?",
    "dashboard.loading": "Processando consulta…",
    "dashboard.error": "Erro ao processar a consulta",
    "dashboard.retry": "Tentar novamente",
    "dashboard.keyFindings": "Principais Descobertas",
    "dashboard.recommendedActions": "Ações Recomendadas",
    "dashboard.sources": "Fontes",
    "dashboard.warnings": "Avisos",
    "dashboard.indicators.all": "Todos",

    // ── Map ─────────────────────────────────────────────────────────
    "map.title": "Mapa Interativo — Visualização Geoespacial",
    "map.placeholder": "Mapa será carregado ao instalar maplibre-gl",
    "map.loading": "Carregando mapa…",
    "map.error": "Erro ao carregar o mapa",
    "map.period.dawn": "Madrugada",
    "map.period.morning": "Manhã",
    "map.period.afternoon": "Tarde",
    "map.period.night": "Noite",
    "map.vulnerableOnly": "Só regiões vulneráveis (≥66)",
    "map.vulnerability": "Vulnerabilidade",
    "map.population": "População",
    "map.connectivity": "Conectividade",
    "map.priority": "Prioritária",
    "map.selectRegion": "Selecione uma região para ver detalhes",
    "map.nearbyRegions": "Regiões próximas",
    "map.legend.title": "Legenda",
    "map.legend.antenna": "Antena",
    "map.legend.highConcentration": "Alta concentração",
    "map.legend.vulnerableRegion": "Região vulnerável",
    "map.legend.low": "Baixa",
    "map.legend.high": "Alta",
    "map.region": "Região",

    // ── Methodology ─────────────────────────────────────────────────
    "methodology.title": "Metodologia e Fontes",
    "methodology.subtitle": "Transparência sobre dados, métodos e privacidade",
    "methodology.dataSources": "Fontes de Dados",
    "methodology.aiMethodology": "Metodologia de IA",
    "methodology.privacy": "Privacidade e Conformidade",
    "methodology.syntheticData": "Dados Sintéticos",
    "methodology.syntheticDataDesc":
      "Todos os dados de mobilidade urbana exibidos na plataforma são sintéticos, gerados artificialmente para fins de prototipação e demonstração. Nenhum dado real de operadoras de telefonia é utilizado.",
    "methodology.anonymization": "Anonimização",
    "methodology.anonymizationDesc":
      "Os dados demográficos são agregados por cluster regional e faixa de renda, sem identificação individual. Cidadãos são representados por hashes anonimizados.",
    "methodology.lgpd": "LGPD",
    "methodology.lgpdDesc":
      "A plataforma segue os princípios da Lei Geral de Proteção de Dados (LGPD — Lei nº 13.709/2018), incluindo finalidade, adequação, necessidade e transparência no tratamento de dados.",
    "methodology.security": "Segurança",
    "methodology.securityDesc":
      "O acesso à API é protegido por autenticação JWT com tokens de acesso e refresh. Endpoints de IA possuem rate limiting de 10 requisições por minuto por IP para evitar abuso.",

    // ── Common ──────────────────────────────────────────────────────
    "common.loading": "Carregando…",
    "common.error": "Erro",
    "common.retry": "Tentar novamente",
    "common.close": "Fechar",
    "common.submit": "Enviar",
    "common.cancel": "Cancelar",
    "common.noData": "Sem dados disponíveis",
    "common.search": "Buscar",

    // ── Indicators ──────────────────────────────────────────────────
    "indicator.employabilityGap": "Brecha de Empregabilidade",
    "indicator.trainingCoverage": "Cobertura de Treinamento",
    "indicator.mentalHealthAccess": "Acesso à Saúde Mental",
    "indicator.mentorshipPrograms": "Programas de Mentoría",
    "indicator.structuredExperiences": "Experiências Estruturantes",

    // ── Severity ────────────────────────────────────────────────────
    "severity.high": "Alto",
    "severity.medium": "Médio",
    "severity.low": "Baixo",
    "severity.critical": "Crítico",

    // ── Trends ──────────────────────────────────────────────────────
    "trend.improving": "Melhorando",
    "trend.stable": "Estável",
    "trend.declining": "Piorando",

    // ── Badge ───────────────────────────────────────────────────────
    "badge.success": "Sucesso",
    "badge.warning": "Aviso",
    "badge.error": "Erro",
    "badge.info": "Informação",
    "badge.neutral": "Neutro",

    // ── Mental Health ──────────────────────────────────────────────
    "mentalHealth.title": "Saúde Mental Comunitária",
    "mentalHealth.indicators": "Indicadores",
    "mentalHealth.score": "Pontuação",
    "mentalHealth.scoreTooltip": "Pontuação de saúde mental baseada em indicadores de infraestrutura e acesso",
    "mentalHealth.scoreLow": "Baixo — Cobertura insuficiente",
    "mentalHealth.scoreMedium": "Médio — Cobertura parcial",
    "mentalHealth.scoreHigh": "Alto — Boa cobertura",
    "mentalHealth.vulnerablePopulation": "População vulnerável",
    "mentalHealth.averageScore": "Pontuação média",
    "mentalHealth.priorityRegions": "Regiões prioritárias",
    "map.showAntennas": "Mostrar antenas",
    "map.highConcentrationOnly": "Só alta concentração",
  },
};
