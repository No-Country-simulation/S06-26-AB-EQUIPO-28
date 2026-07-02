import type { IndicatorId, IndicatorMeta } from "../model/types.ts";

// ---------------------------------------------------------------------------
// Metadata catalogue — single source of truth for all 5 core indicators.
// Labels and descriptions are in Spanish (default UI language). Colours come
// from the DESIGN.md semantic palette (green / yellow / red gradient range).
// ---------------------------------------------------------------------------

const META: Record<IndicatorId, IndicatorMeta> = {
  EMPLOYABILITY_GAP: {
    id: "EMPLOYABILITY_GAP",
    label: "Brecha de Empleabilidad",
    description:
      "Diferencia entre la oferta de mano de obra disponible y las oportunidades formales de empleo en la región.",
    icon: "briefcase",
    color: "#B91C1C",
    unit: "/1",
  },
  TRAINING_COVERAGE: {
    id: "TRAINING_COVERAGE",
    label: "Cobertura de Formación",
    description:
      "Proporción de la población con acceso a programas de cualificación profesional y capacitación técnica.",
    icon: "book-open",
    color: "#92400E",
    unit: "%",
  },
  MENTAL_HEALTH_ACCESS: {
    id: "MENTAL_HEALTH_ACCESS",
    label: "Acceso a Salud Mental",
    description:
      "Disponibilidad de servicios públicos de salud mental y conectividad necesaria para atención remota.",
    icon: "heart",
    color: "#1D4ED8",
    unit: "/1",
  },
  MENTORSHIP_PROGRAMS: {
    id: "MENTORSHIP_PROGRAMS",
    label: "Programas de Mentoría",
    description:
      "Presencia y alcance de programas de mentoría pública orientados al desarrollo profesional.",
    icon: "users",
    color: "#166534",
    unit: "%",
  },
  STRUCTURED_EXPERIENCES: {
    id: "STRUCTURED_EXPERIENCES",
    label: "Experiencias Estructurantes",
    description:
      "Iniciativas sociales y culturales de impacto territorial mapeadas por región.",
    icon: "star",
    color: "#6B7280",
    unit: "/1",
  },
};

/**
 * Returns the full metadata for a given indicator.
 * Guaranteed to exist for all values of `IndicatorId`.
 */
export function getIndicatorMeta(id: IndicatorId): IndicatorMeta {
  return META[id];
}

/**
 * All 5 core indicators with their complete metadata.
 * Useful for rendering indicator selection lists, legends, etc.
 */
export const ALL_INDICATORS: readonly IndicatorMeta[] = Object.freeze(
  Object.values(META),
);

/**
 * Maps a normalised score (0-1) to a colour string.
 *
 * - 0.00 – 0.33 → red    (`#B91C1C`)
 * - 0.33 – 0.66 → yellow (`#92400E`)
 * - 0.66 – 1.00 → green  (`#166534`)
 *
 * Values outside [0, 1] are clamped before lookup.
 */
export function getColorForScore(score: number): string {
  const clamped = Math.max(0, Math.min(1, score));

  if (clamped < 0.33) return "#B91C1C";
  if (clamped < 0.66) return "#92400E";
  return "#166534";
}
