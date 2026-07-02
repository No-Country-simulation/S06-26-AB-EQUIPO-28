// ---------------------------------------------------------------------------
// MethodologyPage — Data sources, methodology method, and privacy.
//
// Static information page documenting what data is available and how it
// can help government officials make informed decisions.
// ---------------------------------------------------------------------------

import { useLanguage } from "@/shared/lib/i18n";
import styles from "./MethodologyPage.module.css";

// ---------------------------------------------------------------------------
// Bilingual data — Spanish (default) / Portuguese (alternative)
// ---------------------------------------------------------------------------

const DATA_SOURCES = {
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
} as const;

const AI_METHODOLOGY = {
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
} as const;

export function MethodologyPage() {
  const { t, locale } = useLanguage();
  const sources = DATA_SOURCES[locale] ?? DATA_SOURCES.es;
  const aiMethods = AI_METHODOLOGY[locale] ?? AI_METHODOLOGY.es;

  return (
    <main className={styles.page}>
      <header className={styles.header}>
        <h1 className={styles.title}>{t("methodology.title")}</h1>
        <p className={styles.subtitle}>{t("methodology.subtitle")}</p>
      </header>

      <div className={styles.content}>
        {/* ── Data Sources ──────────────────────────────────────────── */}
        <section className={styles.section}>
          <h2 className={styles.sectionTitle}>{t("methodology.dataSources")}</h2>
          <div className={styles.sourceGrid}>
            {sources.map((source) => (
              <article key={source.name} className={styles.sourceCard}>
                <h3 className={styles.sourceName}>{source.name}</h3>
                <p className={styles.sourceDescription}>{source.description}</p>
              </article>
            ))}
          </div>
        </section>

        {/* ── AI Methodology ────────────────────────────────────────── */}
        <section className={styles.section}>
          <h2 className={styles.sectionTitle}>{t("methodology.aiMethodology")}</h2>
          <div className={styles.methodologyGrid}>
            {aiMethods.map((method) => (
              <div key={method.title} className={styles.methodCard}>
                <h3 className={styles.methodTitle}>{method.title}</h3>
                <p>{method.description}</p>
              </div>
            ))}
          </div>
        </section>

        {/* ── Privacy ──────────────────────────────────────────────── */}
        <section className={styles.section}>
          <h2 className={styles.sectionTitle}>{t("methodology.privacy")}</h2>
          <div className={styles.privacyGrid}>
            <div className={styles.privacyCard}>
              <h3 className={styles.privacyTitle}>{t("methodology.syntheticData")}</h3>
              <p>{t("methodology.syntheticDataDesc")}</p>
            </div>
            <div className={styles.privacyCard}>
              <h3 className={styles.privacyTitle}>{t("methodology.anonymization")}</h3>
              <p>{t("methodology.anonymizationDesc")}</p>
            </div>
            <div className={styles.privacyCard}>
              <h3 className={styles.privacyTitle}>{t("methodology.lgpd")}</h3>
              <p>{t("methodology.lgpdDesc")}</p>
            </div>
            <div className={styles.privacyCard}>
              <h3 className={styles.privacyTitle}>{t("methodology.security")}</h3>
              <p>{t("methodology.securityDesc")}</p>
            </div>
          </div>
        </section>
      </div>
    </main>
  );
}
