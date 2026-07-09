// ---------------------------------------------------------------------------
// usePanelData — Data-fetching orchestration for PanelDemoPage.
//
// Loads the mental-health report, the vulnerable-region list, and the
// per-region indicators. Kept separate from PanelDemoPage so the page
// component stays focused on presentation and view-state.
// ---------------------------------------------------------------------------

import { useState, useEffect } from "react";
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

export function usePanelData(
  mentalHealthRepository: MentalHealthRepository,
  indicatorRepository: IndicatorRepository,
  selectedRegionId: string | null,
  reportErrorFallback: string,
): PanelData {
  const [report, setReport] = useState<MentalHealthReport | null>(null);
  const [reportLoading, setReportLoading] = useState(true);
  const [reportError, setReportError] = useState<string | null>(null);

  const [vulnerableRegions, setVulnerableRegions] = useState<MentalHealthRegionDetail[]>([]);
  const [vulnLoading, setVulnLoading] = useState(true);

  const [indicators, setIndicators] = useState<IndicatorValue[]>([]);

  useEffect(() => {
    let cancelled = false;
    async function load() {
      setReportLoading(true);
      setReportError(null);
      try {
        const data = await mentalHealthRepository.getReport();
        if (!cancelled) setReport(data);
      } catch (err) {
        if (!cancelled) {
          setReport(null);
          setReportError(err instanceof Error ? err.message : reportErrorFallback);
        }
      } finally {
        if (!cancelled) setReportLoading(false);
      }
    }
    load();
    return () => { cancelled = true; };
  }, [mentalHealthRepository]);

  useEffect(() => {
    let cancelled = false;
    async function load() {
      setVulnLoading(true);
      try {
        const data = await mentalHealthRepository.getVulnerableRegions();
        if (!cancelled) setVulnerableRegions(data);
      } catch {
        if (!cancelled) setVulnerableRegions([]);
      } finally {
        if (!cancelled) setVulnLoading(false);
      }
    }
    load();
    return () => { cancelled = true; };
  }, [mentalHealthRepository]);

  useEffect(() => {
    let cancelled = false;
    async function load() {
      try {
        const data = await indicatorRepository.getIndicators(selectedRegionId ?? "");
        if (!cancelled) setIndicators(data);
      } catch {
        if (!cancelled) setIndicators([]);
      }
    }
    load();
    return () => { cancelled = true; };
  }, [selectedRegionId, indicatorRepository]);

  return {
    report,
    reportLoading,
    reportError,
    vulnerableRegions,
    vulnLoading,
    indicators,
  };
}
