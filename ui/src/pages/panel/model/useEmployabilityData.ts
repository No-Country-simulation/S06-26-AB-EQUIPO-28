// ---------------------------------------------------------------------------
// useEmployabilityData — Data-fetching orchestration for the Employability section.
//
// Loads the OD mobility matrix, aggregated travel times, and per-cluster
// employability gaps. Mirrors the usePanelData pattern: useState + useEffect
// with defensive error handling (no TanStack Query).
// ---------------------------------------------------------------------------

import { useState, useEffect } from "react";
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

export function useEmployabilityData(
  employabilityRepository: EmployabilityRepository,
  errorFallback: string,
): EmployabilityData {
  const [odMatrix, setOdMatrix] = useState<readonly MobilityODPair[]>([]);
  const [travelTimes, setTravelTimes] = useState<readonly TravelTime[]>([]);
  const [gaps, setGaps] = useState<readonly EmployabilityGap[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;
    async function load() {
      setLoading(true);
      setError(null);
      let failed = false;
      try {
        const data = await employabilityRepository.getOdMatrix();
        if (!cancelled) setOdMatrix(data);
      } catch {
        if (!cancelled) {
          setOdMatrix([]);
          failed = true;
        }
      }
      try {
        const data = await employabilityRepository.getTravelTimes();
        if (!cancelled) setTravelTimes(data);
      } catch {
        if (!cancelled) {
          setTravelTimes([]);
          failed = true;
        }
      }
      try {
        const data = await employabilityRepository.getGaps();
        if (!cancelled) setGaps(data);
      } catch {
        if (!cancelled) {
          setGaps([]);
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
  }, [employabilityRepository, errorFallback]);

  return { odMatrix, travelTimes, gaps, loading, error };
}
