import type { Locale } from "./translations.ts";

export interface DataSourceItem {
  readonly name: string;
  readonly description: string;
}

export interface AiMethodologyItem {
  readonly title: string;
  readonly description: string;
}

export const DATA_SOURCES: Record<Locale, readonly DataSourceItem[]> = {
  es: [
    {
      name: "Datos de Movilidad Urbana",
      description:
        "Información sobre patrones de desplazamiento de la población en la región metropolitana, permitiendo identificar zonas con mayor concentración de personas y sus flujos de movilidad diarios.",
    },
    {
      name: "Cobertura de Programas de Formación",
      description:
        "Registros de programas de capacitación profesional y educativa disponibles por región, incluyendo vacantes, áreas de formación y tasas de ocupación.",
    },
    {
      name: "Indicadores de Empleo e Ingresos",
      description:
        "Datos sobre empleo formal, ingresos promedio por sector y proximidad a polos laborales, utilizados para calcular brechas de empleabilidad.",
    },
    {
      name: "Reportes de Salud Pública",
      description:
        "Indicadores de acceso a servicios de salud mental, cobertura de programas públicos y tasas de vulnerabilidad social por región.",
    },
    {
      name: "Infraestructura de Conectividad",
      description:
        "Cobertura de red móvil, calidad de señal y densidad de antenas por región, utilizados como indicador de inclusión digital.",
    },
  ],
  pt: [
    {
      name: "Dados de Mobilidade Urbana",
      description:
        "Informações sobre padrões de deslocamento da população na região metropolitana, permitindo identificar zonas com maior concentração de pessoas e seus fluxos de mobilidade diários.",
    },
    {
      name: "Cobertura de Programas de Formação",
      description:
        "Registros de programas de capacitação profissional e educacional disponíveis por região, incluindo vagas, áreas de formação e taxas de ocupação.",
    },
    {
      name: "Indicadores de Emprego e Renda",
      description:
        "Dados sobre emprego formal, renda média por setor e proximidade a polos de emprego, utilizados para calcular gaps de empregabilidade.",
    },
    {
      name: "Relatórios de Saúde Pública",
      description:
        "Indicadores de acesso a serviços de saúde mental, cobertura de programas públicos e taxas de vulnerabilidade social por região.",
    },
    {
      name: "Infraestrutura de Conectividade",
      description:
        "Cobertura de rede móvel, qualidade de sinal e densidade de antenas por região, utilizados como indicador de inclusão digital.",
    },
  ],
  en: [
    {
      name: "Urban Mobility Data",
      description:
        "Information on population movement patterns in the metropolitan region, enabling identification of areas with higher concentration of people and their daily mobility flows.",
    },
    {
      name: "Training Program Coverage",
      description:
        "Records of professional and educational training programs available by region, including vacancies, training areas, and occupancy rates.",
    },
    {
      name: "Employment and Income Indicators",
      description:
        "Data on formal employment, average income by sector, and proximity to job hubs, used to calculate employability gaps.",
    },
    {
      name: "Public Health Reports",
      description:
        "Indicators of access to mental health services, public program coverage, and social vulnerability rates by region.",
    },
    {
      name: "Connectivity Infrastructure",
      description:
        "Mobile network coverage, signal quality, and antenna density by region, used as a digital inclusion indicator.",
    },
  ],
};

export const AI_METHODOLOGY: Record<Locale, readonly AiMethodologyItem[]> = {
  es: [
    {
      title: "Análisis Inteligente",
      description:
        "El asistente interpreta preguntas en lenguaje natural y busca la información más relevante en los datos disponibles para generar respuestas claras y accionables.",
    },
    {
      title: "Fuentes Integradas",
      description:
        "Combina múltiples fuentes de datos para ofrecer una visión completa de cada región, cruzando información de movilidad, salud, educación y empleo.",
    },
    {
      title: "Respuestas Claras",
      description:
        "Las respuestas incluyen resúmenes, datos concretos y referencias a las fuentes utilizadas, para que pueda verificar y profundizar cuando sea necesario.",
    },
  ],
  pt: [
    {
      title: "Análise Inteligente",
      description:
        "O assistente interpreta perguntas em linguagem natural e busca as informações mais relevantes nos dados disponíveis para gerar respostas claras e acionáveis.",
    },
    {
      title: "Fontes Integradas",
      description:
        "Combina múltiplas fontes de dados para oferecer uma visão completa de cada região, cruzando informações de mobilidade, saúde, educação e emprego.",
    },
    {
      title: "Respostas Claras",
      description:
        "As respostas incluem resumos, dados concretos e referências às fontes utilizadas, para que você possa verificar e se aprofundar quando necessário.",
    },
  ],
  en: [
    {
      title: "Intelligent Analysis",
      description:
        "The assistant interprets natural language questions and searches for the most relevant information in available data to generate clear and actionable responses.",
    },
    {
      title: "Integrated Sources",
      description:
        "Combines multiple data sources to offer a comprehensive view of each region, cross-referencing mobility, health, education, and employment information.",
    },
    {
      title: "Clear Answers",
      description:
        "Responses include summaries, concrete data, and references to the sources used, so you can verify and dig deeper when needed.",
    },
  ],
};

export function getDataSources(locale: Locale): readonly DataSourceItem[] {
  return DATA_SOURCES[locale] ?? DATA_SOURCES.es;
}

export function getAiMethodology(locale: Locale): readonly AiMethodologyItem[] {
  return AI_METHODOLOGY[locale] ?? AI_METHODOLOGY.es;
}
