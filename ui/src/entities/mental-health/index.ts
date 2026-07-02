// ---------------------------------------------------------------------------
// Mental Health entity — public API barrel.
// ---------------------------------------------------------------------------

export type {
  ConnectivityLevel,
  VulnerabilityLevel,
  RegionVulnerabilitySummary,
  ReportMetadata,
  MentalHealthReport,
  MentalHealthRegionDetail,
} from "./model/types.ts";

export type { MentalHealthRepository } from "./api/port.ts";
export { toMentalHealthReport, toMentalHealthRegionDetail } from "./api/mapper.ts";
