// ---------------------------------------------------------------------------
// Indicator entity — immutable domain types for the 5 core impact areas.
// ---------------------------------------------------------------------------

export type IndicatorId =
  | "EMPLOYABILITY_GAP"
  | "TRAINING_COVERAGE"
  | "MENTAL_HEALTH_ACCESS"
  | "MENTORSHIP_PROGRAMS"
  | "STRUCTURED_EXPERIENCES";

export type Trend = "IMPROVING" | "STABLE" | "DECLINING";

export interface IndicatorMeta {
  readonly id: IndicatorId;
  readonly label: string;
  readonly description: string;
  readonly icon: string;
  readonly color: string;
  readonly unit: string;
}

export interface IndicatorValue {
  readonly indicatorId: IndicatorId;
  readonly value: number;
  readonly trend: Trend | null; // null = no real trend data available
}
