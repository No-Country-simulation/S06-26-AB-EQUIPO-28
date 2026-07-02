// ---------------------------------------------------------------------------
// MapLegend — Floating legend box pinned to the bottom-left of the map.
//
// Displays colour swatches with labels from the concentration data so users
// understand what each gradient level represents.
// ---------------------------------------------------------------------------

import type { ConcentrationLegend } from "@/entities/mobility-data";
import { useLanguage } from "@/shared/lib/i18n";
import styles from "./MapLegend.module.css";

interface MapLegendProps {
  legend: ConcentrationLegend[];
}

export function MapLegend({ legend }: MapLegendProps) {
  const { t } = useLanguage();

  if (legend.length === 0) return null;

  return (
    <div className={styles.legend} aria-label={t("map.legend.title")}>
      <p className={styles.title}>{t("map.legend.title")}</p>
      <ul className={styles.list}>
        {legend.map((item, index) => (
          <li key={`legend-${index}`} className={styles.item}>
            <span
              className={styles.swatch}
              style={{ backgroundColor: item.color }}
              aria-hidden="true"
            />
            <span className={styles.label}>{item.label}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}
