// ---------------------------------------------------------------------------
// Mental Health entity — immutable domain types for mental health reports.
// Maps the backend GET /api/v1/inclusion/health-report response.
// ---------------------------------------------------------------------------

export type ConnectivityLevel = "HIGH" | "MEDIUM" | "LOW";

export type VulnerabilityLevel = "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";

export interface RegionVulnerabilitySummary {
  readonly regionName: string;
  readonly vulnerabilityScore: number;
  readonly vulnerablePercentage: number;
  readonly connectivityLevel: ConnectivityLevel;
  readonly isPriorityForIntervention: boolean;
}

export interface ReportMetadata {
  readonly totalVulnerablePopulation: number;
  readonly totalPopulation: number;
  readonly averageVulnerabilityScore: number;
  readonly priorityRegionCount: number;
}

export interface MentalHealthReport {
  readonly reportId: string;
  readonly generatedAt: string;
  readonly reportPeriod: string;
  readonly regionSummaries: readonly RegionVulnerabilitySummary[];
  readonly metadata: ReportMetadata;
}

export interface MentalHealthRegionDetail {
  readonly regionName: string;
  readonly vulnerabilityScore: number;
  readonly vulnerabilityLevel: VulnerabilityLevel;
  readonly vulnerablePopulation: number;
  readonly totalPopulation: number;
  readonly vulnerablePercentage: number;
  readonly connectivityLevel: ConnectivityLevel;
  readonly concentrationIndex: number;
  readonly isPriorityForIntervention: boolean;
}
