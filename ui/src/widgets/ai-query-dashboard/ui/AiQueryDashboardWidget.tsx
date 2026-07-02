// ---------------------------------------------------------------------------
// AiQueryDashboardWidget — Composed widget for the AI query dashboard.
//
// Wires region filtering, indicator selection, and the AI query panel into
// a single-column responsive layout with a horizontal filter bar, stats
// row, and an empty-data banner when no region data is available.
//
// Props accept repository ports so the consumer (app layer) can inject real
// or mock adapters without this widget knowing about infrastructure.
// ---------------------------------------------------------------------------

import { useMemo } from "react";
import { useLanguage } from "@/shared/lib/i18n";
import type { AiAgentRepository } from "@/entities/ai-agent";
import type { RegionRepository, Region } from "@/entities/region";
import { AiQueryDashboard } from "@/features/ask-ai-query";
import { RegionSelect } from "@/features/filter-by-region";
import { useDashboard } from "../model/useDashboard.ts";
import { DashboardHeader } from "./DashboardHeader.tsx";
import { IndicatorSelector } from "./IndicatorSelector.tsx";
import styles from "./AiQueryDashboardWidget.module.css";

interface AiQueryDashboardWidgetProps {
  aiRepository: AiAgentRepository;
  regionRepository: RegionRepository;
}

export function AiQueryDashboardWidget({
  aiRepository,
  regionRepository,
}: AiQueryDashboardWidgetProps) {
  const { t } = useLanguage();
  const { selectedIndicator, setSelectedIndicator, regionFilter } =
    useDashboard(aiRepository, regionRepository);

  const priorityCount = useMemo(
    () => regionFilter.regions.filter((r) => r.connectivity < 50).length,
    [regionFilter.regions],
  );

  const totalUsers = useMemo(
    () => regionFilter.regions.reduce((sum, r) => sum + r.indicators.averageUsers, 0),
    [regionFilter.regions],
  );

  const avgConcentration = useMemo(() => {
    const regions = regionFilter.regions;
    if (regions.length === 0) return 0;
    const total = regions.reduce((sum, r) => sum + r.concentration, 0);
    return Math.round(total / regions.length);
  }, [regionFilter.regions]);

  // ── Empty-data detection ──────────────────────────────────────────
  const isDataEmpty = useMemo(
    () =>
      regionFilter.regions.length === 0 &&
      priorityCount === 0 &&
      totalUsers === 0 &&
      avgConcentration === 0,
    [regionFilter.regions.length, priorityCount, totalUsers, avgConcentration],
  );

  return (
    <section className={styles.widget} aria-label={t("dashboard.title")}>
      <DashboardHeader />

      {/* ── Horizontal filter bar ─────────────────────────────────── */}
      <div className={styles.filterBar}>
        <RegionSelect
          regions={regionFilter.regions as Region[]}
          value={regionFilter.selectedRegionId}
          onChange={regionFilter.setSelectedRegion}
        />
        <IndicatorSelector
          selected={selectedIndicator}
          onChange={setSelectedIndicator}
        />
      </div>

      {/* ── Stats row ──────────────────────────────────────────── */}
      <div className={styles.statsRow}>
        <div className={styles.statCard}>
          <span className={styles.statCardValue}>
            {regionFilter.regions.length}
          </span>
          <span className={styles.statCardLabel}>{t("dashboard.regions")}</span>
        </div>
        <div className={styles.statCard}>
          <span className={styles.statCardValue}>{priorityCount}</span>
          <span className={styles.statCardLabel}>{t("dashboard.statsPrioritarias")}</span>
        </div>
        <div className={styles.statCard}>
          <span className={styles.statCardValue}>
            {totalUsers >= 1_000_000
              ? `${(totalUsers / 1_000_000).toFixed(1)}M`
              : totalUsers >= 1_000
                ? `${(totalUsers / 1_000).toFixed(0)}K`
                : totalUsers}
          </span>
          <span className={styles.statCardLabel}>{t("dashboard.statsPopulation")}</span>
        </div>
        <div className={styles.statCard}>
          <span className={styles.statCardValue}>{avgConcentration}</span>
          <span className={styles.statCardLabel}>{t("dashboard.statsScore")}</span>
        </div>
      </div>

      {/* ── Empty-data banner ─────────────────────────────────────── */}
      {isDataEmpty && (
        <div className={styles.emptyBanner} role="status">
          {t("dashboard.emptyBanner")}
        </div>
      )}

      {/* ── AI Query panel ─────────────────────────────────────────── */}
      <AiQueryDashboard
        repository={aiRepository}
        region={regionFilter.selectedRegionId}
        indicator={selectedIndicator}
      />
    </section>
  );
}
