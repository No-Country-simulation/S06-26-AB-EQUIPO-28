// ---------------------------------------------------------------------------
// Mentorship entity — public API barrel.
// ---------------------------------------------------------------------------

export type {
  MentorshipGapSeverity,
  MentorshipProgram,
  MentorshipProgramSummary,
  MentorshipGap,
  MentorshipClusterSummary,
} from "./model/types.ts";

export type { MentorshipRepository } from "./api/port.ts";
export {
  toProgram,
  toProgramSummary,
  toGap,
  toCluster,
} from "./api/mapper.ts";