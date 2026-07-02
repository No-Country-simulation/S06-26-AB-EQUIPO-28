// ---------------------------------------------------------------------------
// FilterBar — Region select, period chips, and vulnerable-only toggle.
//
// Sits between the page title and the map area on the Map Explorer page.
// ---------------------------------------------------------------------------

import { useLanguage } from "@/shared/lib/i18n";
import { RegionSelect } from "@/features/filter-by-region";
import type { Region } from "@/entities/region";
import styles from "./FilterBar.module.css";

export type Period = "dawn" | "morning" | "afternoon" | "night";

export const PERIOD_TRANSLATION_KEY: Record<Period, string> = {
  dawn: "map.period.dawn",
  morning: "map.period.morning",
  afternoon: "map.period.afternoon",
  night: "map.period.night",
};

interface FilterBarProps {
  regions: Region[];
  selectedRegionId: string | null;
  onRegionChange: (id: string | null) => void;
  selectedPeriod: Period;
  onPeriodChange: (period: Period) => void;
  showAntennas: boolean;
  onAntennasToggle: (value: boolean) => void;
  highConcentrationOnly: boolean;
  onHighConcentrationToggle: (value: boolean) => void;
}

export function FilterBar({
  regions,
  selectedRegionId,
  onRegionChange,
  selectedPeriod,
  onPeriodChange,
  showAntennas,
  onAntennasToggle,
  highConcentrationOnly,
  onHighConcentrationToggle,
}: FilterBarProps) {
  const { t } = useLanguage();

  const periods: Period[] = ["dawn", "morning", "afternoon", "night"];

  return (
    <div className={styles.filterBar}>
      <div className={styles.filterRow}>
        <RegionSelect
          regions={regions}
          value={selectedRegionId}
          onChange={onRegionChange}
        />

        <div className={styles.periodChips}>
          {periods.map((period) => (
            <button
              key={period}
              className={`${styles.chip} ${
                selectedPeriod === period ? styles.chipActive : ""
              }`}
              onClick={() => onPeriodChange(period)}
              aria-pressed={selectedPeriod === period}
              type="button"
            >
              {t(PERIOD_TRANSLATION_KEY[period])}
            </button>
          ))}
        </div>
      </div>

      <div className={styles.toggleRow}>
        <label className={styles.toggle}>
          <input
            type="checkbox"
            className={styles.toggleInput}
            checked={showAntennas}
            onChange={(e) => onAntennasToggle(e.target.checked)}
          />
          <span className={styles.toggleTrack}>
            <span className={styles.toggleThumb} />
          </span>
          <span className={styles.toggleLabel}>
            {t("map.showAntennas")}
          </span>
        </label>

        <label className={styles.toggle}>
          <input
            type="checkbox"
            className={styles.toggleInput}
            checked={highConcentrationOnly}
            onChange={(e) => onHighConcentrationToggle(e.target.checked)}
          />
          <span className={styles.toggleTrack}>
            <span className={styles.toggleThumb} />
          </span>
          <span className={styles.toggleLabel}>
            {t("map.highConcentrationOnly")}
          </span>
        </label>
      </div>
    </div>
  );
}
