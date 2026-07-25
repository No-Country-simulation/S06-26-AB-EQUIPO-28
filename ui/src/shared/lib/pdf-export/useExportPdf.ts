import { useCallback, useState, useRef } from "react";
import { generatePdfReport } from "./generateReport.ts";
import type { IndicatorValue } from "@/entities/indicator";
import type { AiResponse } from "@/entities/ai-agent";
import type { Region } from "@/entities/region";
import type { MentalHealthReport, RegionVulnerabilitySummary } from "@/entities/mental-health";
import type { EmployabilityGap } from "@/entities/employability";

interface UseExportPdfOptions {
  region: Region | null;
  indicators: IndicatorValue[];
  aiResponse: AiResponse | null;
  period: string;
  locale: string;
  mentalHealthReport: MentalHealthReport | null;
  vulnerableRegions: readonly RegionVulnerabilitySummary[];
  employabilityGaps: readonly EmployabilityGap[];
  allRegions: Region[];
}

export function useExportPdf(options: UseExportPdfOptions) {
  const [exporting, setExporting] = useState(false);
  const optionsRef = useRef(options);
  optionsRef.current = options;

  const exportPdf = useCallback(async () => {
    setExporting(true);
    try {
      await generatePdfReport({
        region: optionsRef.current.region,
        indicators: optionsRef.current.indicators,
        aiResponse: optionsRef.current.aiResponse,
        period: optionsRef.current.period,
        locale: optionsRef.current.locale,
        mentalHealthReport: optionsRef.current.mentalHealthReport,
        vulnerableRegions: [...optionsRef.current.vulnerableRegions],
        employabilityGaps: [...optionsRef.current.employabilityGaps],
        allRegions: optionsRef.current.allRegions,
      });
    } catch (err) {
      console.error("PDF export failed:", err);
    } finally {
      setExporting(false);
    }
  }, []);

  return { exportPdf, exporting };
}