import type {
  HealthReportResource,
  RegionVulnerabilitySummary as BackendSummary,
  VulnerableRegionItem,
} from "@/shared/api/index.ts";
import type {
  MentalHealthReport,
  RegionVulnerabilitySummary,
  ReportMetadata,
  MentalHealthRegionDetail,
  VulnerabilityLevel,
  ConnectivityLevel,
} from "../model/types.ts";

/** Maps a backend HealthReportResource to our domain MentalHealthReport */
export function toMentalHealthReport(dto: HealthReportResource): MentalHealthReport {
  return {
    reportId: dto.reportId,
    generatedAt: dto.generatedAt,
    reportPeriod: dto.reportPeriod,
    regionSummaries: dto.regionSummaries.map(toRegionSummary),
    metadata: toReportMetadata(dto.metadata),
  };
}

/** Maps a backend RegionVulnerabilitySummary to our domain type */
function toRegionSummary(dto: BackendSummary): RegionVulnerabilitySummary {
  return {
    regionName: dto.regionName,
    vulnerabilityScore: dto.vulnerabilityScore,
    vulnerablePercentage: dto.vulnerablePercentage,
    connectivityLevel: dto.connectivityLevel as ConnectivityLevel,
    isPriorityForIntervention: dto.isPriorityForIntervention,
  };
}

/** Maps backend metadata to our domain type */
function toReportMetadata(dto: HealthReportResource["metadata"]): ReportMetadata {
  return {
    totalVulnerablePopulation: dto.totalVulnerablePopulation,
    totalPopulation: dto.totalPopulation,
    averageVulnerabilityScore: dto.averageVulnerabilityScore,
    priorityRegionCount: dto.priorityRegionCount,
  };
}

/** Maps a backend VulnerableRegionItem to our domain MentalHealthRegionDetail */
export function toMentalHealthRegionDetail(dto: VulnerableRegionItem): MentalHealthRegionDetail {
  const score = dto.vulnerabilityScore ?? 0;
  return {
    regionName: dto.regionName,
    vulnerabilityScore: score,
    vulnerabilityLevel: mapVulnerabilityLevel(score, dto.vulnerabilityLevel),
    vulnerablePopulation: dto.vulnerablePopulationCount ?? 0,
    totalPopulation: dto.totalPopulation ?? 0,
    vulnerablePercentage: Math.round(dto.vulnerablePercentage ?? 0),
    connectivityLevel: dto.connectivityLevel as ConnectivityLevel,
    concentrationIndex: Math.min(100, Math.max(0, Math.round(dto.concentrationIndex ?? 0))),
    isPriorityForIntervention: dto.isPriorityForIntervention,
  };
}

function mapVulnerabilityLevel(score: number, backendLevel: string): VulnerabilityLevel {
  if (score >= 80) return "CRITICAL";
  switch (backendLevel) {
    case "HIGH": return "HIGH";
    case "MEDIUM": return "MEDIUM";
    case "LOW": return "LOW";
    default: return "MEDIUM";
  }
}
