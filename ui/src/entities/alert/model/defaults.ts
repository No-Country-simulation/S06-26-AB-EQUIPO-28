import type { AlertThreshold } from "./types.ts";

export const DEFAULT_THRESHOLDS: AlertThreshold[] = [
  {
    indicatorId: "MENTAL_HEALTH_ACCESS",
    regionId: null,
    operator: "lt",
    value: 30,
    enabled: true,
  },
  {
    indicatorId: "TRAINING_COVERAGE",
    regionId: null,
    operator: "lt",
    value: 25,
    enabled: true,
  },
  {
    indicatorId: "EMPLOYABILITY_GAP",
    regionId: null,
    operator: "lt",
    value: 20,
    enabled: true,
  },
  {
    indicatorId: "MENTORSHIP_PROGRAMS",
    regionId: null,
    operator: "lt",
    value: 15,
    enabled: true,
  },
  {
    indicatorId: "STRUCTURED_EXPERIENCES",
    regionId: null,
    operator: "lt",
    value: 10,
    enabled: true,
  },
];
