import { useCallback, useState } from "react";
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

  const exportPdf = useCallback(async () => {
    setExporting(true);
    try {
      await generatePdfReport({
        region: options.region,
        indicators: options.indicators,
        aiResponse: options.aiResponse,
        period: options.period,
        locale: options.locale,
        mentalHealthReport: options.mentalHealthReport,
        vulnerableRegions: [...options.vulnerableRegions],
        employabilityGaps: [...options.employabilityGaps],
        allRegions: options.allRegions,
      });
    } finally {
      setExporting(false);
    }
  }, [options]);

  return { exportPdf, exporting };
}