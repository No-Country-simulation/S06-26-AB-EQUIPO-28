// ---------------------------------------------------------------------------
// MapViewPage — Map-centric decision tool (main view).
//
// This is the primary and only interactive view of the application.
// Layout:
//   ┌─────────────────────────────────────────────┐
//   │ FilterBar (top)                             │
//   ├─────────────────────────────────────────────┤
//   │                                             │
//   │          InteractiveMapWidget               │
//   │               (center)                      │
//   │                                             │
//   ├───────────────────┬─────────────────────────┤
//   │                   │                         │
//   │   Map area        │   SidePanel (right)     │
//   │                   │                         │
//   ├───────────────────┴─────────────────────────┤
//   │ AI Query Bar (sticky bottom)                │
//   └─────────────────────────────────────────────┘
//         ↓ (when response exists)
//   ┌─────────────────────────────────────────────┐
//   │ AI Response Panel (grows downward)          │
//   └─────────────────────────────────────────────┘
//
// Desktop (≥ 1024px): Grid [Map (1fr) | SidePanel (340px)]
// Mobile (< 768px):   Single column — side panel hidden, scroll to see indicators
// ---------------------------------------------------------------------------

import { useState, useCallback, useEffect } from "react";
import { useLanguage } from "@/shared/lib/i18n";
import { useAppContext } from "@/app/providers";
import { InteractiveMapWidget } from "@/widgets/interactive-map";
import { MentalHealthInsightsWidget } from "@/widgets/mental-health-insights";
import type { IndicatorValue } from "@/entities/indicator";
import { useRegionFilter } from "@/features/filter-by-region";
import { useAskAi } from "@/features/ask-ai-query";
import { AiResponseDisplay } from "@/features/ask-ai-query";
import { Spinner } from "@/shared/ui";
import { FilterBar, type Period } from "./FilterBar.tsx";
import { SidePanel } from "./SidePanel.tsx";
import styles from "./MapViewPage.module.css";

export function MapViewPage() {
  const { t } = useLanguage();
  const { mobilityDataRepository, regionRepository, aiAgentRepository, indicatorRepository } =
    useAppContext();

  // ── Region filter state ───────────────────────────────────────────
  const {
    regions,
    selectedRegionId,
    setSelectedRegion,
    selectedRegion,
  } = useRegionFilter(regionRepository);

  // ── Mental health indicators state ────────────────────────────────
  const [indicators, setIndicators] = useState<IndicatorValue[]>([]);
  const [indicatorsLoading, setIndicatorsLoading] = useState(false);

  useEffect(() => {
    let cancelled = false;
    async function loadIndicators() {
      setIndicatorsLoading(true);
      try {
        const data = await indicatorRepository.getIndicators(selectedRegionId ?? "");
        if (!cancelled) setIndicators(data);
      } catch {
        if (!cancelled) setIndicators([]);
      } finally {
        if (!cancelled) setIndicatorsLoading(false);
      }
    }
    loadIndicators();
    return () => { cancelled = true; };
  }, [selectedRegionId, indicatorRepository]);

  // ── Filter bar state ──────────────────────────────────────────────
  const [selectedPeriod, setSelectedPeriod] = useState<Period>("morning");
  const [showAntennas, setShowAntennas] = useState(true);
  const [highConcentrationOnly, setHighConcentrationOnly] = useState(false);

  // ── AI query state ────────────────────────────────────────────────
  const {
    query,
    setQuery,
    submit,
    response,
    isLoading: aiLoading,
    error: aiError,
    clearResponse,
  } = useAskAi(aiAgentRepository, { region: selectedRegionId });

  // When a region is selected
  const handleRegionChange = useCallback(
    (id: string | null) => {
      setSelectedRegion(id);
    },
    [setSelectedRegion],
  );

  const handleAiSubmit = useCallback(() => {
    if (query.trim()) {
      submit();
    }
  }, [query, submit]);

  const handleAiKeyDown = useCallback(
    (e: React.KeyboardEvent<HTMLInputElement>) => {
      if (e.key === "Enter" && query.trim() && !aiLoading) {
        submit();
      }
    },
    [query, aiLoading, submit],
  );

  const hasAiContent = response || aiError || aiLoading;

  return (
    <div className={styles.wrapper}>
      <main className={styles.page}>
        {/* ── Map panel (filter bar + map) ─────────────────────────── */}
        <div className={styles.mapPanel}>
          <FilterBar
            regions={regions}
            selectedRegionId={selectedRegionId}
            onRegionChange={handleRegionChange}
            selectedPeriod={selectedPeriod}
            onPeriodChange={setSelectedPeriod}
            showAntennas={showAntennas}
            onAntennasToggle={setShowAntennas}
            highConcentrationOnly={highConcentrationOnly}
            onHighConcentrationToggle={setHighConcentrationOnly}
          />
          <div className={styles.mapArea}>
            <InteractiveMapWidget
              repository={mobilityDataRepository}
              regionId={selectedRegionId}
              period={selectedPeriod}
              vulnerableOnly={highConcentrationOnly}
              showAntennas={showAntennas}
              popupStrings={{
                antennaLabel: t("map.legend.antenna"),
                loadLabel: t("map.population"),
                concentrationPoint: t("map.legend.highConcentration"),
                intensityLabel: t("map.vulnerability"),
              }}
            />
          </div>
        </div>

        {/* ── Desktop side panel ─────────────────────────────────── */}
        <div className={styles.sidePanel}>
          <SidePanel
            selectedRegion={selectedRegion}
            regions={regions}
            selectedRegionId={selectedRegionId}
          />

          {/* ── Mental Health Widget (below side panel) ──────────── */}
          <div className={styles.mentalHealthSection}>
            {indicatorsLoading ? (
              <div className={styles.aiLoadingContainer}>
                <Spinner size="sm" />
              </div>
            ) : (
              <MentalHealthInsightsWidget
                report={indicators}
                regionName={selectedRegion?.name}
              />
            )}
          </div>
        </div>

        {/* ── AI Query bar (fixed at bottom) ────────────────────── */}
        <div className={styles.aiQueryBar}>
          <div className={styles.aiQueryContainer}>
            <span className={styles.aiQueryIcon} aria-hidden="true">
              <svg
                width="18"
                height="18"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
              >
                <circle cx="11" cy="11" r="8" />
                <path d="M21 21l-4.35-4.35" />
              </svg>
            </span>
            <input
              type="text"
              className={styles.aiQueryInput}
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              onKeyDown={handleAiKeyDown}
              disabled={aiLoading}
              placeholder={t("dashboard.searchPlaceholder")}
              aria-label={t("common.search")}
              autoComplete="off"
            />
            <button
              type="button"
              className={styles.aiQueryButton}
              onClick={handleAiSubmit}
              disabled={aiLoading || !query.trim()}
              aria-label={t("common.submit")}
            >
              <svg
                width="18"
                height="18"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
              >
                <path d="M22 2L11 13" />
                <path d="M22 2l-7 20-4-9-9-4 20-7z" />
              </svg>
            </button>
          </div>
        </div>
      </main>

      {/* ── AI Response panel (fixed above the AI bar) ──────── */}
      {hasAiContent && (
        <div className={styles.aiResponsePanel}>
          {/* Loading state */}
          {aiLoading && (
            <div className={styles.aiLoadingContainer}>
              <Spinner size="md" />
              <p className={styles.aiLoadingText}>{t("dashboard.loading")}</p>
            </div>
          )}

          {/* Error state */}
          {aiError && !aiLoading && (
            <div className={styles.aiErrorCard} role="alert">
              <p className={styles.aiErrorMessage}>{aiError}</p>
              <button
                type="button"
                className={styles.aiRetryButton}
                onClick={handleAiSubmit}
              >
                {t("dashboard.retry")}
              </button>
            </div>
          )}

          {/* Response state */}
          {response && !aiLoading && (
            <AiResponseDisplay response={response} onClear={clearResponse} />
          )}
        </div>
      )}
    </div>
  );
}
