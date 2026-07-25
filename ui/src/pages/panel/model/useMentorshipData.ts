import { useState, useEffect, useRef } from "react";
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

const MAX_RETRIES = 2;
const RETRY_DELAY_MS = 2000;

async function withRetry<T>(fn: () => Promise<T>, retries: number = MAX_RETRIES): Promise<T> {
  let lastError: unknown;
  for (let attempt = 0; attempt <= retries; attempt++) {
    try {
      return await fn();
    } catch (err) {
      lastError = err;
      if (attempt < retries) {
        await new Promise((r) => setTimeout(r, RETRY_DELAY_MS * (attempt + 1)));
      }
    }
  }
  throw lastError;
}

export function useMentorshipData(
  mentorshipRepository: MentorshipRepository,
  errorFallback: string,
  retryKey: number = 0,
): MentorshipData {
  const [programs, setPrograms] = useState<readonly MentorshipProgram[]>([]);
  const [gaps, setGaps] = useState<readonly MentorshipGap[]>([]);
  const [clusters, setClusters] = useState<readonly MentorshipClusterSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const fetchingRef = useRef(false);

  useEffect(() => {
    if (fetchingRef.current) return;
    fetchingRef.current = true;

    let cancelled = false;

    async function load() {
      setLoading(true);
      setError(null);

      try {
        const [programsData, gapsData, clustersData] = await Promise.all([
          withRetry(() => mentorshipRepository.getPrograms()),
          withRetry(() => mentorshipRepository.getGaps()),
          withRetry(() => mentorshipRepository.getClusters()),
        ]);
        if (!cancelled) {
          setPrograms(programsData);
          setGaps(gapsData);
          setClusters(clustersData);
        }
      } catch {
        if (!cancelled) {
          setPrograms([]);
          setGaps([]);
          setClusters([]);
          setError(errorFallback);
        }
      } finally {
        if (!cancelled) setLoading(false);
        fetchingRef.current = false;
      }
    }

    load();
    return () => { cancelled = true; fetchingRef.current = false; };
  }, [mentorshipRepository, errorFallback, retryKey]);

  return { programs, gaps, clusters, loading, error };
}
