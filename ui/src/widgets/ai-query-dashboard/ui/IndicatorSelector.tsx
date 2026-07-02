// ---------------------------------------------------------------------------
// IndicatorSelector — Horizontal pill row to pick an indicator.
//
// Shows "Todos" (default / all) plus the five core indicators.  The active
// pill has a dark background; inactive pills are light.  On mobile the row
// scrolls horizontally.
// ---------------------------------------------------------------------------

import { useLanguage } from "@/shared/lib/i18n";
import { ALL_INDICATORS } from "@/entities/indicator";
import styles from "./IndicatorSelector.module.css";

interface IndicatorSelectorProps {
  selected: string | null;
  onChange: (id: string | null) => void;
}

export function IndicatorSelector({
  selected,
  onChange,
}: IndicatorSelectorProps) {
  const { t } = useLanguage();
  return (
    <div
      className={styles.wrapper}
      role="group"
      aria-label={t("dashboard.indicators.all")}
    >
      {/* ── "Todos" pill ──────────────────────────────────────────── */}
      <button
        type="button"
        className={`${styles.pill} ${selected === null ? styles.active : styles.inactive}`}
        onClick={() => onChange(null)}
        aria-pressed={selected === null}
      >
        {t("dashboard.indicators.all")}
      </button>

      {/* ── Indicator pills ───────────────────────────────────────── */}
      {ALL_INDICATORS.map((indicator) => (
        <button
          key={indicator.id}
          type="button"
          className={`${styles.pill} ${selected === indicator.id ? styles.active : styles.inactive}`}
          onClick={() => onChange(indicator.id)}
          aria-pressed={selected === indicator.id}
        >
          {indicator.label}
        </button>
      ))}
    </div>
  );
}
