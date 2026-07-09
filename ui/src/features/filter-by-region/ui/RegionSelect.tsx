import { type ChangeEvent, useId } from "react";
import { useLanguage } from "@/shared/lib/i18n";
import { formatRegionName } from "@/shared/lib/formatters";
import type { Region } from "@/entities/region";

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
    <div className="relative">
      <div className="relative">
        <select
          id={selectId}
          className="h-9 appearance-none rounded-lg border border-border bg-card py-1.5 pr-8 pl-3 text-sm text-foreground outline-none transition-colors cursor-pointer focus-visible:border-ring focus-visible:ring-3 focus-visible:ring-ring/50"
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
        <span className="pointer-events-none absolute right-2.5 top-1/2 -translate-y-1/2 text-muted-foreground" aria-hidden="true">
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
            <path d="M4 6L8 10L12 6" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
          </svg>
        </span>
      </div>
    </div>
  );
}
