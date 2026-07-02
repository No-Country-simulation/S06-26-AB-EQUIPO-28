import type { HealthIndicator } from "../../../shared/api/index.ts";
import type { IndicatorId, IndicatorValue } from "../model/types.ts";

/**
 * Maps backend health‑indicator names to domain `IndicatorId` values.
 *
 * Some backend names (e.g. `"mobility_access"`, `"employment_proximity"`)
 * do not correspond to any of the 5 core domain indicators. Those entries
 * are silently skipped — the calling adapter should not propagate rows
 * that cannot be mapped.
 */
const BACKEND_TO_INDICATOR_ID: Record<string, IndicatorId> = {
  training_coverage: "TRAINING_COVERAGE",
  employability_gap: "EMPLOYABILITY_GAP",
  mental_health_services: "MENTAL_HEALTH_ACCESS",
  mentorship_programs: "MENTORSHIP_PROGRAMS",
  structured_experiences: "STRUCTURED_EXPERIENCES",
};

/**
 * Converts a backend `HealthIndicator` DTO into a domain `IndicatorValue`.
 *
 * Returns `null` when the backend indicator name does not match any of the
 * 5 core domain indicators.
 */
export function toIndicatorValue(dto: HealthIndicator): IndicatorValue | null {
  const indicatorId = BACKEND_TO_INDICATOR_ID[dto.name];
  if (indicatorId === undefined) return null;

  return {
    indicatorId,
    value: dto.value,
    trend: dto.trend,
  };
}
