// ---------------------------------------------------------------------------
// RegionSelect — Dropdown to pick a region for dashboard filtering.
//
// Styled consistently with shared/ui/Select and shows region name + severity
// in each option.  The first option ("Todas as regiões") corresponds to the
// null value (no filter).  Responsive: full width on mobile, 320 px max
// on desktop.
// ---------------------------------------------------------------------------

import { type ChangeEvent, useId } from "react";
import { useLanguage } from "@/shared/lib/i18n";
import { formatRegionName } from "@/shared/lib/formatters";
import type { Region } from "@/entities/region";
import styles from "./RegionSelect.module.css";

interface RegionSelectProps {
  regions: Region[];
  value: string | null;
  onChange: (regionId: string | null) => void;
}

export function RegionSelect({
  regions,
  value,
  onChange,
}: RegionSelectProps) {
  const { t } = useLanguage();
  const selectId = useId();

  const handleChange = (e: ChangeEvent<HTMLSelectElement>) => {
    const val = e.target.value;
    onChange(val === "" ? null : val);
  };

  return (
    <div className={styles.wrapper}>
      <div className={styles.selectContainer}>
        <select
          id={selectId}
          className={styles.select}
          value={value ?? ""}
          onChange={handleChange}
          aria-label={t("dashboard.allRegions")}
        >
          <option value="">{t("dashboard.allRegions")}</option>
          {regions.map((region) => (
            <option key={region.id} value={region.id}>
              {formatRegionName(region.name)}
            </option>
          ))}
        </select>
        <span className={styles.chevron} aria-hidden="true">
          <svg
            width="16"
            height="16"
            viewBox="0 0 16 16"
            fill="none"
          >
            <path
              d="M4 6L8 10L12 6"
              stroke="currentColor"
              strokeWidth="1.5"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        </span>
      </div>
    </div>
  );
}
