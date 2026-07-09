// ---------------------------------------------------------------------------
// Mentorship entity — immutable domain types for mentorship programs.
// Maps the backend GET /api/v1/mentorship/* responses.
// ---------------------------------------------------------------------------

export type MentorshipGapSeverity = "CRITICAL" | "HIGH" | "MODERATE" | "LOW";

export interface MentorshipProgram {
  readonly programId: string;
  readonly name: string;
  readonly description: string;
  readonly organization: string;
  readonly focusArea: string;
  readonly modality: string;
  readonly targetAudience: string | null;
  readonly targetIncomeLevel: string | null;
  readonly clusterName: string;
  readonly totalCapacity: number;
  readonly activeMentees: number;
  readonly startDate: string | null;
  readonly endDate: string | null;
  readonly isActive: boolean;
  readonly websiteUrl: string | null;
  readonly contactEmail: string | null;
}

export interface MentorshipProgramSummary {
  readonly programId: string;
  readonly name: string;
  readonly focusArea: string;
  readonly modality: string;
  readonly isActive: boolean;
}

export interface MentorshipGap {
  readonly clusterName: string;
  readonly vulnerabilityScore: number;
  readonly vulnerabilityLevel: string;
  readonly vulnerablePopulationCount: number;
  readonly totalPopulation: number;
  readonly vulnerablePercentage: number;
  readonly connectivityLevel: string;
  readonly concentrationIndex: number;
  readonly isPriorityForIntervention: boolean;
  readonly hasMentorshipPrograms: boolean;
  readonly matchingPrograms: readonly MentorshipProgramSummary[];
  readonly gapSeverity: MentorshipGapSeverity;
}

export interface MentorshipClusterSummary {
  readonly clusterName: string;
  readonly totalPrograms: number;
  readonly activePrograms: number;
  readonly focusAreas: readonly string[];
  readonly modalities: readonly string[];
  readonly targetAudiences: readonly string[];
  readonly totalCapacity: number;
  readonly totalActiveMentees: number;
  readonly programs: readonly MentorshipProgramSummary[];
}