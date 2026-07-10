// ---------------------------------------------------------------------------
// Employability entity — immutable domain types for mobility and employability gaps.
// Maps the backend GET /api/v1/employability/* responses.
// ---------------------------------------------------------------------------

export type EmployabilityGapSeverity =
  | "CRITICAL"
  | "HIGH"
  | "MODERATE"
  | "LOW"
  | "NONE";

export interface MobilityODPair {
  readonly originCluster: string;
  readonly originMunicipio: string;
  readonly originLatitude: number;
  readonly originLongitude: number;
  readonly destinationCluster: string;
  readonly destinationMunicipio: string;
  readonly destinationLatitude: number;
  readonly destinationLongitude: number;
  readonly sameCluster: boolean;
  readonly uniqueUsers: number;
  readonly totalTrips: number;
  readonly averageDistanceKm: number;
  readonly predominantPeriod: string;
}

export interface TravelTime {
  readonly originCluster: string;
  readonly destinationCluster: string;
  readonly sameCluster: boolean;
  readonly observations: number;
  readonly averageDistanceKm: number;
  readonly p25DistanceKm: number | null;
  readonly p75DistanceKm: number | null;
  readonly predominantPeriod: string;
}

export interface EmployabilityGap {
  readonly cluster: string;
  readonly municipalities: readonly string[];
  readonly citizenCount: number;
  readonly incomeDCount: number;
  readonly incomeCCount: number;
  readonly youthCount18_24: number;
  readonly hasTelemetryCoverage: boolean;
  readonly daytimeAvgUsers: number;
  readonly outboundTripsToHubs: number;
  readonly distanceToNearestHubKm: number;
  readonly mobilityIntensity: string;
  readonly gapSeverity: EmployabilityGapSeverity;
  readonly gapScore: number;
  readonly primaryFactors: readonly string[];
}
