// ---------------------------------------------------------------------------
// useMentorshipData — Data-fetching orchestration for the Mentorship section.
//
// Loads programs, per-cluster coverage gaps, and cluster summaries from the
// mentorship repository. Mirrors the usePanelData pattern: useState + useEffect
// with defensive error handling (no TanStack Query).
// ---------------------------------------------------------------------------

import { useState, useEffect } from "react";
import type {
  MentorshipRepository,
  MentorshipProgram,
  MentorshipGap,
  MentorshipClusterSummary,
} from "@/entities/mentorship";

export interface MentorshipData {
  readonly programs: readonly MentorshipProgram[];
  readonly gaps: readonly MentorshipGap[];
  readonly clusters: readonly MentorshipClusterSummary[];
  readonly loading: boolean;
  readonly error: string | null;
}

export function useMentorshipData(
  mentorshipRepository: MentorshipRepository,
  errorFallback: string,
): MentorshipData {
  const [programs, setPrograms] = useState<readonly MentorshipProgram[]>([]);
  const [gaps, setGaps] = useState<readonly MentorshipGap[]>([]);
  const [clusters, setClusters] = useState<readonly MentorshipClusterSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;
    async function load() {
      setLoading(true);
      setError(null);
      let failed = false;
      try {
        const data = await mentorshipRepository.getPrograms();
        if (!cancelled) setPrograms(data);
      } catch {
        if (!cancelled) {
          setPrograms([]);
          failed = true;
        }
      }
      try {
        const data = await mentorshipRepository.getGaps();
        if (!cancelled) setGaps(data);
      } catch {
        if (!cancelled) {
          setGaps([]);
          failed = true;
        }
      }
      try {
        const data = await mentorshipRepository.getClusters();
        if (!cancelled) setClusters(data);
      } catch {
        if (!cancelled) {
          setClusters([]);
          failed = true;
        }
      }
      if (!cancelled) {
        if (failed) setError(errorFallback);
        setLoading(false);
      }
    }
    load();
    return () => {
      cancelled = true;
    };
  }, [mentorshipRepository, errorFallback]);

  return { programs, gaps, clusters, loading, error };
}
