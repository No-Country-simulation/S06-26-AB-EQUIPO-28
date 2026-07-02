// ---------------------------------------------------------------------------
// MentalHealthWidget — Presents mental health data.
//
// Shows a HealthScoreGauge at the top followed by a list of
// HealthIndicatorBars, one per indicator value.  Each bar includes a short
// description explaining what the metric measures.
//
// This is a PURE presentation component — it receives data via props and
// has no side effects.
// ---------------------------------------------------------------------------

import { useLanguage } from "@/shared/lib/i18n";
import type { IndicatorValue } from "@/entities/indicator";
import { getIndicatorMeta } from "@/entities/indicator";
import { Card } from "@/shared/ui";
import { HealthScoreGauge } from "./HealthScoreGauge.tsx";
import { HealthIndicatorBar } from "./HealthIndicatorBar.tsx";
import styles from "./MentalHealthWidget.module.css";

interface MentalHealthWidgetProps {
  report: IndicatorValue[];
  regionName?: string;
  metadata?: {
    readonly totalVulnerablePopulation: number;
    readonly totalPopulation: number;
    readonly priorityRegionCount: number;
    readonly averageVulnerabilityScore: number;
    readonly reportPeriod: string;
    readonly reportId: string;
    readonly generatedAt: string;
  };
}

/**
 * Computes an overall health score (0-100) from the report values by
 * averaging normalised indicator values.
 */
function computeOverallScore(report: IndicatorValue[]): number {
  if (report.length === 0) return 0;
  const total = report.reduce((sum, item) => sum + item.value, 0);
  return Math.round(total / report.length);
}

/**
 * Returns a human-readable label for an indicator ID, or the raw ID as
 * fallback.
 */
function getLabel(id: IndicatorValue["indicatorId"]): string {
  try {
    return getIndicatorMeta(id).label;
  } catch {
    return id;
  }
}

/**
 * Returns the display unit for an indicator ID.
 */
function getUnit(id: IndicatorValue["indicatorId"]): string {
  try {
    return getIndicatorMeta(id).unit;
  } catch {
    return "";
  }
}

/**
 * Returns the description for an indicator ID, or undefined as fallback.
 */
function getDescription(id: IndicatorValue["indicatorId"]): string | undefined {
  try {
    return getIndicatorMeta(id).description;
  } catch {
    return undefined;
  }
}

function getScoreLabel(score: number): string {
  if (score < 33) return "scoreLow";
  if (score < 66) return "scoreMedium";
  return "scoreHigh";
}

export function MentalHealthWidget({
  report,
  regionName,
  metadata,
}: MentalHealthWidgetProps) {
  const { t } = useLanguage();
  const overallScore = computeOverallScore(report);
  const gaugeTooltip = `${t("mentalHealth.scoreTooltip")}\n${t("mentalHealth." + getScoreLabel(overallScore))}`;

  return (
    <Card padding="md">
      <div className={styles.widget}>
        {/* ── Header ──────────────────────────────────────────────── */}
        <div className={styles.header}>
          <h2 className={styles.title}>{t("mentalHealth.title")}</h2>
          {regionName && (
            <p className={styles.regionName}>{regionName}</p>
          )}
        </div>

        {/* ── Score gauge ─────────────────────────────────────────── */}
        <div className={styles.gaugeSection} title={gaugeTooltip}>
          <HealthScoreGauge score={overallScore} size="md" />
          <p className={styles.gaugeLabel}>
            {t("mentalHealth.score")} {overallScore}/100
          </p>
        </div>

        {/* ── Metadata summary ────────────────────────────────────── */}
        {metadata && (
          <div className={styles.metadataSection}>
            <div className={styles.metadataRow}>
              <div className={styles.metadataItem}>
                <span className={styles.metadataValue}>{metadata.totalVulnerablePopulation.toLocaleString()}</span>
                <span className={styles.metadataDesc}>{t("mentalHealth.vulnerablePopulation")}</span>
              </div>
              <div className={styles.metadataItem}>
                <span className={styles.metadataValue}>{metadata.averageVulnerabilityScore}/100</span>
                <span className={styles.metadataDesc}>{t("mentalHealth.averageScore")}</span>
              </div>
              <div className={styles.metadataItem}>
                <span className={styles.metadataValue}>{metadata.priorityRegionCount}</span>
                <span className={styles.metadataDesc}>{t("mentalHealth.priorityRegions")}</span>
              </div>
            </div>
          </div>
        )}

        {/* ── Indicator bars ──────────────────────────────────────── */}
        {report.length > 0 && (
          <div className={styles.barsSection}>
            <h3 className={styles.barsTitle}>{t("mentalHealth.indicators")}</h3>
            <div className={styles.barsList}>
              {report.map((indicator) => {
                const indicatorTooltip = `${t("mentalHealth.scoreTooltip")}\n${t("mentalHealth." + getScoreLabel(indicator.value))}`;
                return (
                  <div key={indicator.indicatorId} title={indicatorTooltip}>
                    <HealthIndicatorBar
                      name={getLabel(indicator.indicatorId)}
                      value={indicator.value}
                      trend={indicator.trend}
                      unit={getUnit(indicator.indicatorId)}
                    />
                    {getDescription(indicator.indicatorId) && (
                      <p className={styles.indicatorSubtext}>
                        {getDescription(indicator.indicatorId)}
                      </p>
                    )}
                  </div>
                );
              })}
            </div>
          </div>
        )}

        {/* ── Empty state ─────────────────────────────────────────── */}
        {report.length === 0 && (
          <p className={styles.emptyText}>
            {t("common.noData")}
          </p>
        )}
      </div>
    </Card>
  );
}
