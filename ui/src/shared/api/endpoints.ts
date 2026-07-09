// ---------------------------------------------------------------------------
// Endpoint definitions — typed request/response pairs for every backend route.
// Matches the actual Spring Boot response shapes.
// ---------------------------------------------------------------------------

// ── Shared primitives ──────────────────────────────────────────────────────

export type ApiError = {
  readonly status: number;
  readonly title: string;
  readonly detail: string;
};

// ── POST /data/query ──────────────────────────────────────────────────────

export interface DataQueryRequest {
  readonly query: string;
  readonly filters?: {
    readonly region?: string;
    readonly indicator?: string;
  };
  readonly language?: string;
}

export interface DataQueryResponseItem {
  readonly region: string;
  readonly value: number | string;
  readonly source: string;
}

export interface DataQueryResponse {
  readonly aiResponse: string;
  readonly data: readonly DataQueryResponseItem[];
  readonly sources: readonly string[];
}

// ── GET /map/regions ─────────────────────────────────────────────────────

export interface MapRegionIndicators {
  readonly antennas: number;
  readonly averageUsers: number;
  readonly averageDropPct: number;
  readonly averageCongestion: number;
}

export interface MapRegionItem {
  readonly name: string;
  readonly lat: number;
  readonly lng: number;
  readonly concentration: number;
  readonly connectivity: number;
  readonly indicators: MapRegionIndicators;
}

// ── GET /inclusion/vulnerable-regions (legacy — used by mental-health) ───

export interface VulnerableRegionItem {
  readonly regionName: string;
  readonly vulnerabilityScore?: number | null;
  readonly vulnerabilityLevel: string;
  readonly vulnerablePopulationCount?: number | null;
  readonly totalPopulation?: number | null;
  readonly vulnerablePercentage?: number | null;
  readonly connectivityLevel: string;
  readonly concentrationIndex?: number | null;
  readonly isPriorityForIntervention: boolean;
}

// ── GET /inclusion/health-report ───────────────────────────────────────────
//
// Returns an object with regionSummaries array.

export interface RegionVulnerabilitySummary {
  readonly regionName: string;
  readonly vulnerabilityScore: number;
  readonly vulnerablePercentage: number;
  readonly connectivityLevel: string;
  readonly isPriorityForIntervention: boolean;
}

export interface HealthReportResource {
  readonly reportId: string;
  readonly generatedAt: string;
  readonly reportPeriod: string;
  readonly regionSummaries: readonly RegionVulnerabilitySummary[];
  readonly metadata: {
    readonly totalVulnerablePopulation: number;
    readonly totalPopulation: number;
    readonly averageVulnerabilityScore: number;
    readonly priorityRegionCount: number;
  };
}

/** Kept for indicator mapper compat — not directly returned by any current endpoint. */
export interface HealthIndicator {
  readonly name: string;
  readonly value: number;
  readonly unit: string;
  readonly trend: "IMPROVING" | "STABLE" | "DECLINING";
}

// ── GET /telemetry/antennas ────────────────────────────────────────────────
//
// Returns paginated: { content: AntennaItem[], totalElements, currentPage, pageSize, totalPages }

export interface AntennaItem {
  readonly ecgi: string;
  readonly cluster: string;
  readonly municipality: string;
  readonly latitude?: number | null;
  readonly longitude?: number | null;
}

export interface AntennaPaginatedResource {
  readonly content: readonly AntennaItem[];
  readonly totalElements: number;
  readonly currentPage: number;
  readonly pageSize: number;
  readonly totalPages: number;
}

// ── GET /telemetry/concentration ───────────────────────────────────────────
//
// Returns paginated: { content: ConcentrationItem[], totalElements, currentPage, pageSize, totalPages }

export interface ConcentrationItem {
  readonly ecgi: string;
  readonly dayDate: string;
  readonly period: string;
  readonly userCount?: number | null;
  readonly sessionCount?: number | null;
  readonly downloadBytes?: number | null;
  readonly uploadBytes?: number | null;
  readonly averageDurationS?: number | null;
  readonly dropPct?: number | null;
  readonly congestionLevel?: number | null;
  readonly totalCalls?: number | null;
  readonly totalMessages?: number | null;
  readonly latitude?: number | null;
  readonly longitude?: number | null;
}

export interface ConcentrationPaginatedResource {
  readonly content: readonly ConcentrationItem[];
  readonly totalElements: number;
  readonly currentPage: number;
  readonly pageSize: number;
  readonly totalPages: number;
}
