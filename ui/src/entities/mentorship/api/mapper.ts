import type {
  MentorshipProgramDto,
  MentorshipProgramSummaryDto,
  MentorshipGapDto,
  MentorshipClusterSummaryDto,
} from "@/shared/api/index.ts";
import type {
  MentorshipProgram,
  MentorshipProgramSummary,
  MentorshipGap,
  MentorshipClusterSummary,
  MentorshipGapSeverity,
} from "../model/types.ts";

/** Maps a backend MentorshipProgramDto to our domain MentorshipProgram. */
export function toProgram(dto: MentorshipProgramDto): MentorshipProgram {
  return {
    programId: dto.programId,
    name: dto.name,
    description: dto.description ?? "",
    organization: dto.organization ?? "",
    focusArea: dto.focusArea,
    modality: dto.modality,
    targetAudience: dto.targetAudience ?? null,
    targetIncomeLevel: dto.targetIncomeLevel ?? null,
    clusterName: dto.clusterName,
    totalCapacity: dto.totalCapacity ?? 0,
    activeMentees: dto.activeMentees ?? 0,
    startDate: dto.startDate ?? null,
    endDate: dto.endDate ?? null,
    isActive: dto.isActive,
    websiteUrl: dto.websiteUrl ?? null,
    contactEmail: dto.contactEmail ?? null,
  };
}

/** Maps a backend MentorshipProgramSummaryDto to our domain summary. */
export function toProgramSummary(
  dto: MentorshipProgramSummaryDto,
): MentorshipProgramSummary {
  return {
    programId: dto.programId,
    name: dto.name,
    focusArea: dto.focusArea,
    modality: dto.modality,
    isActive: dto.isActive,
  };
}

/** Maps a backend MentorshipGapDto to our domain MentorshipGap. */
export function toGap(dto: MentorshipGapDto): MentorshipGap {
  return {
    clusterName: dto.clusterName,
    vulnerabilityScore: dto.vulnerabilityScore ?? 0,
    vulnerabilityLevel: dto.vulnerabilityLevel ?? "",
    vulnerablePopulationCount: dto.vulnerablePopulationCount ?? 0,
    totalPopulation: dto.totalPopulation ?? 0,
    vulnerablePercentage: dto.vulnerablePercentage ?? 0,
    connectivityLevel: dto.connectivityLevel ?? "",
    concentrationIndex: dto.concentrationIndex ?? 0,
    isPriorityForIntervention: dto.isPriorityForIntervention,
    hasMentorshipPrograms: dto.hasMentorshipPrograms,
    matchingPrograms: (dto.matchingPrograms ?? []).map(toProgramSummary),
    gapSeverity: normalizeGapSeverity(dto.gapSeverity),
  };
}

/** Maps a backend MentorshipClusterSummaryDto to our domain cluster summary. */
export function toCluster(dto: MentorshipClusterSummaryDto): MentorshipClusterSummary {
  return {
    clusterName: dto.clusterName,
    totalPrograms: dto.totalPrograms ?? 0,
    activePrograms: dto.activePrograms ?? 0,
    focusAreas: dto.focusAreas ?? [],
    modalities: dto.modalities ?? [],
    targetAudiences: dto.targetAudiences ?? [],
    totalCapacity: dto.totalCapacity ?? 0,
    totalActiveMentees: dto.totalActiveMentees ?? 0,
    programs: (dto.programs ?? []).map(toProgramSummary),
  };
}

function normalizeGapSeverity(value: string | undefined): MentorshipGapSeverity {
  switch (value) {
    case "CRITICAL":
      return "CRITICAL";
    case "HIGH":
      return "HIGH";
    case "MODERATE":
      return "MODERATE";
    case "LOW":
      return "LOW";
    default:
      return "MODERATE";
  }
}