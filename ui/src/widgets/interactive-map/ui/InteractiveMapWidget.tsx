// ---------------------------------------------------------------------------
// InteractiveMapWidget — MapLibre GL JS map widget.
//
// Displays antenna markers (coloured by load level), concentration pins
// (sized by intensity), and a legend overlay.
//
// Uses a two-phase render: on mount it tries to dynamically import
// MapLibreMap.  If the import succeeds (maplibre-gl is installed) the
// live map is shown; otherwise a styled placeholder is displayed.
//
// Map tiles: OpenFreeMap (https://tiles.openfreemap.org/styles/liberty).
// ---------------------------------------------------------------------------

import React, { Suspense } from "react";
import { useLanguage } from "@/shared/lib/i18n";
import type { MobilityDataRepository } from "@/entities/mobility-data";
import { Spinner } from "@/shared/ui";
import { useMapData } from "../model/useMapData.ts";
import { useMapViewport } from "../model/useMapViewport.ts";
import { MapContainer } from "./MapContainer.tsx";
import { MapLegend } from "./MapLegend.tsx";
import type { PopupStrings } from "./MapLibreMap.tsx";
import styles from "./InteractiveMapWidget.module.css";

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
}

export function InteractiveMapWidget({
  repository,
  regionId,
  period,
  vulnerableOnly = false,
  showAntennas = true,
  popupStrings,
}: InteractiveMapWidgetProps) {
  const { t } = useLanguage();
  const { antennas, pins, legend, isLoading, error } = useMapData(
    repository,
    regionId,
    period,
    vulnerableOnly,
  );
  const { viewport } = useMapViewport();

  console.log("[InteractiveMapWidget] render — antennas:", antennas.length, "pins:", pins.length);

  return (
    <section className={styles.widget} aria-label={t("map.title")}>
      <MapContainer>
        {/* ── Map or placeholder ──────────────────────────────────── */}
        <Suspense fallback={<MapPlaceholder />}>
          <LazyMapLibreMap
            antennas={antennas}
            pins={pins}
            viewport={viewport}
            showAntennas={showAntennas}
            popupStrings={popupStrings}
          />
        </Suspense>

        {/* ── Loading overlay ──────────────────────────────────────── */}
        {isLoading && (
          <div className={styles.loadingOverlay}>
            <Spinner size="md" />
            <span className={styles.loadingText}>{t("map.loading")}</span>
          </div>
        )}

        {/* ── Error banner ─────────────────────────────────────────── */}
        {error && (
          <div className={styles.errorBanner} role="alert">
            <svg
              width="16"
              height="16"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              aria-hidden="true"
            >
              <circle cx="12" cy="12" r="10" />
              <line x1="12" y1="8" x2="12" y2="12" />
              <line x1="12" y1="16" x2="12.01" y2="16" />
            </svg>
            <span>{error}</span>
          </div>
        )}

        {/* ── Empty state (no data and no error) ─────────────────── */}
        {!isLoading && !error && antennas.length === 0 && pins.length === 0 && (
          <div className={styles.emptyState}>
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="1.5"
              strokeLinecap="round"
              strokeLinejoin="round"
              aria-hidden="true"
            >
              <circle cx="12" cy="12" r="10" />
              <path d="M12 16v-4" />
              <path d="M12 8h.01" />
            </svg>
            <span>{t("common.noData")}</span>
          </div>
        )}

        {/* ── Legend ───────────────────────────────────────────────── */}
        {!isLoading && legend.length > 0 && <MapLegend legend={legend} />}
      </MapContainer>
    </section>
  );
}

// ---------------------------------------------------------------------------
// MapPlaceholder — Rendered when maplibre-gl is not installed.
// ---------------------------------------------------------------------------

function MapPlaceholder() {
  const { t } = useLanguage();
  return (
    <div className={styles.placeholder}>
      <svg
        className={styles.placeholderIcon}
        width="48"
        height="48"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        strokeWidth="1.5"
        strokeLinecap="round"
        strokeLinejoin="round"
        aria-hidden="true"
      >
        <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" />
        <circle cx="12" cy="10" r="3" />
      </svg>
      <p className={styles.placeholderTitle}>{t("map.title")}</p>
      <p className={styles.placeholderSubtitle}>{t("map.placeholder")}</p>
    </div>
  );
}
