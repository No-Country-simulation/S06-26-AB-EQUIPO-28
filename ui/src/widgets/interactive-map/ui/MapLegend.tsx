import type { ConcentrationLegend } from "@/entities/mobility-data";
import { useLanguage } from "@/shared/lib/i18n";

interface MapLegendProps {
  legend: ConcentrationLegend[];
}

export function MapLegend({ legend }: MapLegendProps) {
  const { t } = useLanguage();

  if (legend.length === 0) return null;

  return (
    <div className="absolute bottom-4 left-4 z-10 rounded-xl border border-border bg-card/90 p-3 shadow-sm backdrop-blur-sm" aria-label={t("map.legend.title")}>
      <p className="m-0 mb-2 text-xs font-semibold text-foreground">{t("map.legend.title")}</p>
      <ul className="m-0 list-none p-0 flex flex-col gap-1.5">
        {legend.map((item, index) => (
          <li key={`legend-${index}`} className="flex items-center gap-2">
            <span
              className="inline-block size-3 rounded-sm shrink-0"
              style={{ backgroundColor: item.color }}
              aria-hidden="true"
            />
            <span className="text-[11px] text-muted-foreground">{item.label}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}
