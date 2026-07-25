import { useState, useEffect, useRef } from "react";
import type {
  MentalHealthRepository,
  MentalHealthReport,
  MentalHealthRegionDetail,
} from "@/entities/mental-health";
import type { IndicatorRepository, IndicatorValue } from "@/entities/indicator";

export interface PanelData {
  readonly report: MentalHealthReport | null;
  readonly reportLoading: boolean;
  readonly reportError: string | null;
  readonly vulnerableRegions: MentalHealthRegionDetail[];
  readonly vulnLoading: boolean;
  readonly indicators: IndicatorValue[];
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

export function usePanelData(
  mentalHealthRepository: MentalHealthRepository,
  indicatorRepository: IndicatorRepository,
  selectedRegionId: string | null,
  reportErrorFallback: string,
  retryKey: number = 0,
): PanelData {
  const [report, setReport] = useState<MentalHealthReport | null>(null);
  const [reportLoading, setReportLoading] = useState(true);
  const [reportError, setReportError] = useState<string | null>(null);

  const [vulnerableRegions, setVulnerableRegions] = useState<MentalHealthRegionDetail[]>([]);
  const [vulnLoading, setVulnLoading] = useState(true);

  const [indicators, setIndicators] = useState<IndicatorValue[]>([]);

  const fetchingRef = useRef(false);

  useEffect(() => {
    if (fetchingRef.current) return;
    fetchingRef.current = true;

    let cancelled = false;

    async function load() {
      setReportLoading(true);
      setVulnLoading(true);
      setReportError(null);

      // Fetch report, vulnerable regions, and indicators in parallel
      const reportPromise = withRetry(() => mentalHealthRepository.getReport())
        .then((data) => { if (!cancelled) setReport(data); })
        .catch((err) => {
          if (!cancelled) {
            setReport(null);
            setReportError(err instanceof Error ? err.message : reportErrorFallback);
          }
        });

      const vulnPromise = withRetry(() => mentalHealthRepository.getVulnerableRegions())
        .then((data) => { if (!cancelled) setVulnerableRegions(data); })
        .catch(() => { if (!cancelled) setVulnerableRegions([]); });

      const indicatorPromise = withRetry(() => indicatorRepository.getIndicators(selectedRegionId ?? ""))
        .then((data) => { if (!cancelled) setIndicators(data); })
        .catch(() => { if (!cancelled) setIndicators([]); });

      await Promise.allSettled([reportPromise, vulnPromise, indicatorPromise]);

      if (!cancelled) {
        setReportLoading(false);
        setVulnLoading(false);
      }
      fetchingRef.current = false;
    }

    load();
    return () => { cancelled = true; fetchingRef.current = false; };
  }, [mentalHealthRepository, indicatorRepository, selectedRegionId, retryKey, reportErrorFallback]);

  return {
    report,
    reportLoading,
    reportError,
    vulnerableRegions,
    vulnLoading,
    indicators,
  };
}
