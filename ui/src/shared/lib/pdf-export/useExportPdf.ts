import { useCallback, useState } from "react";
import { generatePdfReport } from "./generateReport.ts";
import type { IndicatorValue } from "@/entities/indicator";
import type { AiResponse } from "@/entities/ai-agent";
import type { Region } from "@/entities/region";

interface UseExportPdfOptions {
  region: Region | null;
  indicators: IndicatorValue[];
  aiResponse: AiResponse | null;
  period: string;
  locale: string;
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
      });
    } finally {
      setExporting(false);
    }
  }, [options]);

  return { exportPdf, exporting };
}
