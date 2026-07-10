// ---------------------------------------------------------------------------
// Employability entity — public API barrel.
// ---------------------------------------------------------------------------

export type {
  EmployabilityGapSeverity,
  MobilityODPair,
  TravelTime,
  EmployabilityGap,
} from "./model/types.ts";

export type { EmployabilityRepository } from "./api/port.ts";
export {
  toOdPair,
  toTravelTime,
  toEmployabilityGap,
} from "./api/mapper.ts";
