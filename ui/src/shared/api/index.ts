// ---------------------------------------------------------------------------
// Public API — src/shared/api
//
// Re-exports the shared Axios client, QueryClient, endpoint types, and
// the mock bootstrap function.  Consumers should import from this barrel
// (e.g. `import { apiClient } from "@/shared/api"`).
// ---------------------------------------------------------------------------

export { apiClient } from "./client.ts";
export { queryClient } from "./query-client.ts";

export type {
  // Shared primitives
  ApiError,
  // POST /data/query
  DataQueryRequest,
  DataQueryResponse,
  DataQueryResponseItem,
  // GET /map/regions
  MapRegionItem,
  MapRegionIndicators,
  // GET /inclusion/vulnerable-regions (legacy — used by mental-health)
  VulnerableRegionItem,
  // GET /inclusion/health-report
  HealthReportResource,
  RegionVulnerabilitySummary,
  HealthIndicator,
  // GET /telemetry/antennas
  AntennaItem,
  AntennaPaginatedResource,
  // GET /telemetry/concentration
  ConcentrationItem,
  ConcentrationPaginatedResource,
  // Mentorship
  MentorshipProgramDto,
  MentorshipProgramPaginatedDto,
  MentorshipProgramSummaryDto,
  MentorshipGapDto,
  MentorshipClusterSummaryDto,
  // Employability
  MobilityODPairDto,
  MobilityODPairPaginatedDto,
  TravelTimeDto,
  TravelTimePaginatedDto,
  EmployabilityGapDto,
} from "./endpoints.ts";
