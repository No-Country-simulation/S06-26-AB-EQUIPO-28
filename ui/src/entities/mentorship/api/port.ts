import type {
  MentorshipProgram,
  MentorshipGap,
  MentorshipClusterSummary,
} from "../model/types.ts";

export interface MentorshipRepository {
  /** Gets all mentorship programs across clusters. */
  getPrograms(): Promise<readonly MentorshipProgram[]>;

  /** Gets mentorship coverage gap analysis per cluster. */
  getGaps(): Promise<readonly MentorshipGap[]>;

  /** Gets per-cluster mentorship program summaries. */
  getClusters(): Promise<readonly MentorshipClusterSummary[]>;
}