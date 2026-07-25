import { useState, useEffect, useRef } from "react";
import type {
  EmployabilityRepository,
  MobilityODPair,
  TravelTime,
  EmployabilityGap,
} from "@/entities/employability";

export interface EmployabilityData {
  readonly odMatrix: readonly MobilityODPair[];
  readonly travelTimes: readonly TravelTime[];
  readonly gaps: readonly EmployabilityGap[];
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

export function useEmployabilityData(
  employabilityRepository: EmployabilityRepository,
  errorFallback: string,
  retryKey: number = 0,
): EmployabilityData {
  const [odMatrix, setOdMatrix] = useState<readonly MobilityODPair[]>([]);
  const [travelTimes, setTravelTimes] = useState<readonly TravelTime[]>([]);
  const [gaps, setGaps] = useState<readonly EmployabilityGap[]>([]);
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
        const [odData, travelData, gapsData] = await Promise.all([
          withRetry(() => employabilityRepository.getOdMatrix()),
          withRetry(() => employabilityRepository.getTravelTimes()),
          withRetry(() => employabilityRepository.getGaps()),
        ]);
        if (!cancelled) {
          setOdMatrix(odData);
          setTravelTimes(travelData);
          setGaps(gapsData);
        }
      } catch {
        if (!cancelled) {
          setOdMatrix([]);
          setTravelTimes([]);
          setGaps([]);
          setError(errorFallback);
        }
      } finally {
        if (!cancelled) setLoading(false);
        fetchingRef.current = false;
      }
    }

    load();
    return () => { cancelled = true; fetchingRef.current = false; };
  }, [employabilityRepository, errorFallback, retryKey]);

  return { odMatrix, travelTimes, gaps, loading, error };
}
