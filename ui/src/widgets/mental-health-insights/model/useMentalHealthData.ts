// ---------------------------------------------------------------------------
// useMentalHealthData — Fetches the mental health report from the backend.
//
// Takes either a fetch function or an IndicatorRepository port and loads
// indicator values on mount with proper cleanup.
//
// The fetch function is stored in a ref so changing its identity does not
// cause unnecessary re-fetches.  To trigger a manual refresh, call
// `refetch()`.
//
// Uses plain useState + useEffect (no TanStack Query, no react-router).
// ---------------------------------------------------------------------------

import { useState, useEffect, useRef, useCallback } from "react";
import type { IndicatorValue } from "@/entities/indicator";

interface HealthFetchFn {
  (): Promise<IndicatorValue[]>;
}

export interface MentalHealthDataState {
  readonly report: IndicatorValue[];
  readonly isLoading: boolean;
  readonly error: string | null;
  readonly refetch: () => void;
}

export function useMentalHealthData(
  fetchFn: HealthFetchFn,
): MentalHealthDataState {
  const [report, setReport] = useState<IndicatorValue[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [retryCount, setRetryCount] = useState(0);

  // Store the fetch function in a ref so it doesn't trigger re-fetches
  // when the parent re-renders with a new closure.
  const fetchRef = useRef<HealthFetchFn>(fetchFn);
  fetchRef.current = fetchFn;

  useEffect(() => {
    let cancelled = false;

    async function load() {
      setIsLoading(true);
      setError(null);

      try {
        const data = await fetchRef.current();
        if (!cancelled) {
          setReport(data);
        }
      } catch (err) {
        if (cancelled) return;

        const message =
          err instanceof Error
            ? err.message
            : "Error al cargar datos de salud mental.";
        setError(message);
      } finally {
        if (!cancelled) {
          setIsLoading(false);
        }
      }
    }

    load();

    return () => {
      cancelled = true;
    };
  }, [retryCount]);

  const refetch = useCallback(() => {
    setRetryCount((c) => c + 1);
  }, []);

  return {
    report,
    isLoading,
    error,
    refetch,
  } as const;
}
