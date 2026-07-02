// ---------------------------------------------------------------------------
// HealthScoreGauge — Circular SVG gauge showing mental health score (0-100).
//
// Colours: red (0-33) → yellow (33-66) → green (66-100).
// Supports three sizes: sm (80px), md (120px), lg (160px).
// ---------------------------------------------------------------------------

import { useLanguage } from "@/shared/lib/i18n";
import styles from "./HealthScoreGauge.module.css";

interface HealthScoreGaugeProps {
  score: number;
  size?: "sm" | "md" | "lg";
}

const DIMENSIONS: Record<string, { size: number; strokeWidth: number; fontSize: number }> = {
  sm: { size: 80, strokeWidth: 6, fontSize: 18 },
  md: { size: 120, strokeWidth: 8, fontSize: 28 },
  lg: { size: 160, strokeWidth: 10, fontSize: 36 },
};

const RADIUS_RATIO = 0.42; // radius = size * ratio (leaves room for stroke)

function getColor(score: number): string {
  if (score < 33) return "#B91C1C";
  if (score < 66) return "#92400E";
  return "#166534";
}

export function HealthScoreGauge({
  score,
  size = "md",
}: HealthScoreGaugeProps) {
  const { t } = useLanguage();
  const { size: px, strokeWidth, fontSize } = DIMENSIONS[size];
  const radius = px * RADIUS_RATIO;
  const centre = px / 2;
  const circumference = 2 * Math.PI * radius;
  const clampedScore = Math.max(0, Math.min(100, score));
  const offset = circumference - (clampedScore / 100) * circumference;
  const color = getColor(clampedScore);

  return (
    <div
      className={styles.wrapper}
      style={{ width: px, height: px }}
      role="img"
      aria-label={`${t("mentalHealth.title")}: ${clampedScore}/100`}
    >
      <svg
        width={px}
        height={px}
        viewBox={`0 0 ${px} ${px}`}
        className={styles.svg}
      >
        {/* ── Background circle ────────────────────────────────────── */}
        <circle
          cx={centre}
          cy={centre}
          r={radius}
          fill="none"
          stroke="#E5E7EB"
          strokeWidth={strokeWidth}
        />

        {/* ── Foreground (score) circle ────────────────────────────── */}
        <circle
          cx={centre}
          cy={centre}
          r={radius}
          fill="none"
          stroke={color}
          strokeWidth={strokeWidth}
          strokeLinecap="round"
          strokeDasharray={circumference}
          strokeDashoffset={offset}
          transform={`rotate(-90 ${centre} ${centre})`}
          className={styles.foreground}
        />
      </svg>

      <span
        className={styles.scoreText}
        style={{ fontSize, color }}
      >
        {clampedScore}
      </span>
    </div>
  );
}
