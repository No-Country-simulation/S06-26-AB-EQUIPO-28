import React, { Suspense } from "react";
import { useLanguage } from "@/shared/lib/i18n";
import type { MobilityDataRepository } from "@/entities/mobility-data";
import { Spinner } from "@/shared/ui";
import { useMapData } from "../model/useMapData.ts";
import { useMapViewport } from "../model/useMapViewport.ts";
import { MapContainer } from "./MapContainer.tsx";
import { MapLegend } from "./MapLegend.tsx";
import type { PopupStrings, RegionPoint } from "./MapLibreMap.tsx";

const LazyMapLibreMap = React.lazy(
  () => import("./MapLibreMap").then((m) => ({ default: m.MapLibreMap })),
);

interface InteractiveMapWidgetProps {
  repository: MobilityDataRepository;
  regionId?: string | null;
  period?: string;
  vulnerableOnly?: boolean;
  showAntennas?: boolean;
  popupStrings?: PopupStrings;
  regions?: RegionPoint[];
  selectedRegionId?: string | null;
  onRegionSelect?: (id: string) => void;
}

export function InteractiveMapWidget({
  repository,
  regionId,
  period,
  vulnerableOnly = false,
  showAntennas = true,
  popupStrings,
  regions,
  selectedRegionId,
  onRegionSelect,
}: InteractiveMapWidgetProps) {
  const { t } = useLanguage();
  const { antennas, pins, legend, isLoading, error } = useMapData(
    repository,
    regionId,
    period,
    vulnerableOnly,
  );
  const { viewport } = useMapViewport();

  return (
    <section className="relative h-full w-full" aria-label={t("map.title")}>
      <MapContainer>
        <Suspense fallback={<MapPlaceholder />}>
          <LazyMapLibreMap
            antennas={antennas}
            pins={pins}
            regions={regions}
            selectedRegionId={selectedRegionId}
            onRegionSelect={onRegionSelect}
            viewport={viewport}
            showAntennas={showAntennas}
            popupStrings={popupStrings}
          />
        </Suspense>

        {isLoading && (
          <div className="absolute inset-0 z-10 flex items-center justify-center gap-3 bg-white/60">
            <Spinner size="md" />
            <span className="text-sm text-muted-foreground">{t("map.loading")}</span>
          </div>
        )}

        {error && (
          <div className="absolute bottom-4 left-4 right-4 z-10 flex items-center gap-2 rounded-lg bg-red-50 border border-red-200 p-3 text-xs text-red-700" role="alert">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" aria-hidden="true">
              <circle cx="12" cy="12" r="10" />
              <line x1="12" y1="8" x2="12" y2="12" />
              <line x1="12" y1="16" x2="12.01" y2="16" />
            </svg>
            <span>{error}</span>
          </div>
        )}

        {!isLoading && !error && antennas.length === 0 && pins.length === 0 && (regions?.length ?? 0) === 0 && (
          <div className="absolute inset-0 z-10 flex flex-col items-center justify-center gap-2 text-muted-foreground">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
              <circle cx="12" cy="12" r="10" />
              <path d="M12 16v-4" />
              <path d="M12 8h.01" />
            </svg>
            <span className="text-sm">{t("common.noData")}</span>
          </div>
        )}

        {!isLoading && legend.length > 0 && <MapLegend legend={legend} />}
      </MapContainer>
    </section>
  );
}

function MapPlaceholder() {
  const { t } = useLanguage();
  return (
    <div className="flex h-full min-h-[300px] flex-col items-center justify-center gap-3 rounded-xl bg-muted/50 p-6">
      <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" className="text-muted-foreground" aria-hidden="true">
        <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" />
        <circle cx="12" cy="10" r="3" />
      </svg>
      <p className="m-0 text-sm font-semibold text-foreground">{t("map.title")}</p>
      <p className="m-0 text-xs text-muted-foreground">{t("map.placeholder")}</p>
    </div>
  );
}
