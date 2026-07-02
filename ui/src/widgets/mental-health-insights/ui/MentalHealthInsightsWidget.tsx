// ---------------------------------------------------------------------------
// MentalHealthInsightsWidget — Mental health widget that receives data via
// props (no self-fetching).
//
// The parent page is responsible for loading data from the
// MentalHealthRepository and passing it down along with optional metadata.
// ---------------------------------------------------------------------------

import { useLanguage } from "@/shared/lib/i18n";
import type { IndicatorValue } from "@/entities/indicator";
import { MentalHealthWidget } from "./MentalHealthWidget.tsx";
import styles from "./MentalHealthInsightsWidget.module.css";

export interface MentalHealthInsightsWidgetProps {
  /** The indicator values to render. */
  readonly report: IndicatorValue[];
  /** Optional region name shown in the header. */
  readonly regionName?: string;
  /** Optional metadata card data sourced from the health-report. */
  readonly metadata?: {
    readonly totalVulnerablePopulation: number;
    readonly totalPopulation: number;
    readonly priorityRegionCount: number;
    readonly averageVulnerabilityScore: number;
    readonly reportPeriod: string;
    readonly reportId: string;
    readonly generatedAt: string;
  };
}

export function MentalHealthInsightsWidget({
  report,
  regionName,
  metadata,
}: MentalHealthInsightsWidgetProps) {
  const { t } = useLanguage();

  return (
    <section className={styles.widget} aria-label={t("mentalHealth.title")}>
      <MentalHealthWidget
        report={report}
        regionName={regionName}
        metadata={metadata}
      />
    </section>
  );
}
