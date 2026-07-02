// ---------------------------------------------------------------------------
// HealthIndicatorBar — Horizontal bar showing a single health indicator.
//
// Displays the label on the left and a value bar on the right, with a
// trend arrow indicating direction.  The bar fill colour is determined by
// the value (red → yellow → green gradient matching the gauge).
// ---------------------------------------------------------------------------

import { useLanguage } from "@/shared/lib/i18n";
import type { Trend } from "@/entities/indicator";
import styles from "./HealthIndicatorBar.module.css";

interface HealthIndicatorBarProps {
  name: string;
  value: number;
  trend: Trend | null;
  unit: string;
}

function getBarColor(score: number): string {
  if (score < 33) return "#B91C1C";
  if (score < 66) return "#92400E";
  return "#166534";
}

function TrendArrow({ trend }: { trend: Trend | null }) {
  const { t } = useLanguage();
  if (trend === null) return null;
  switch (trend) {
    case "IMPROVING":
      return (
        <span className={styles.trendUp} aria-label={t("trend.improving")}>
          <svg
            width="14"
            height="14"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2.5"
            strokeLinecap="round"
            strokeLinejoin="round"
            aria-hidden="true"
          >
            <polyline points="18 15 12 9 6 15" />
          </svg>
        </span>
      );
    case "DECLINING":
      return (
        <span className={styles.trendDown} aria-label={t("trend.declining")}>
          <svg
            width="14"
            height="14"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2.5"
            strokeLinecap="round"
            strokeLinejoin="round"
            aria-hidden="true"
          >
            <polyline points="6 9 12 15 18 9" />
          </svg>
        </span>
      );
    default:
      return (
        <span className={styles.trendStable} aria-label={t("trend.stable")}>
          <svg
            width="14"
            height="14"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2.5"
            strokeLinecap="round"
            strokeLinejoin="round"
            aria-hidden="true"
          >
            <line x1="5" y1="12" x2="19" y2="12" />
          </svg>
        </span>
      );
  }
}

export function HealthIndicatorBar({
  name,
  value,
  trend,
  unit,
}: HealthIndicatorBarProps) {
  const clampedValue = Math.max(0, Math.min(100, value));
  const barColor = getBarColor(clampedValue);

  return (
    <div className={styles.bar}>
      <div className={styles.labelRow}>
        <span className={styles.name}>{name}</span>
        <span className={styles.value}>
          {clampedValue} {unit}
        </span>
        <TrendArrow trend={trend} />
      </div>

      <div className={styles.track}>
        <div
          className={styles.fill}
          style={{ width: `${clampedValue}%`, backgroundColor: barColor }}
          role="progressbar"
          aria-valuenow={clampedValue}
          aria-valuemin={0}
          aria-valuemax={100}
          aria-label={`${name}: ${clampedValue}${unit}`}
        />
      </div>
    </div>
  );
}
