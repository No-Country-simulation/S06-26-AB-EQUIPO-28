// ---------------------------------------------------------------------------
// SidePanel — Selected region card, legend, and nearby regions list.
//
// Renders inside the desktop side panel.
// On mobile, the right panel is hidden — users scroll to see indicators.
// ---------------------------------------------------------------------------

import { useLanguage } from "@/shared/lib/i18n";
import type { Region } from "@/entities/region";
import styles from "./SidePanel.module.css";

// ── Helpers ────────────────────────────────────────────────────────────

function formatPopulation(n: number): string {
  if (n >= 1_000_000) return `${(n / 1_000_000).toFixed(1)}M`;
  if (n >= 1_000) return `${(n / 1_000).toFixed(1)}K`;
  return String(n);
}

function getConnectivityLabel(level: number, t: (key: string) => string): string {
  if (level >= 66) return t("severity.high");
  if (level >= 33) return t("severity.medium");
  return t("severity.low");
}

function getConnectivityColor(level: number): string {
  if (level >= 66) return "#22C55E";
  if (level >= 33) return "#F59E0B";
  return "#EF4444";
}

function getConcentrationBarColor(score: number): string {
  if (score > 66) return "#EF4444";  // RED for HIGH concentration (bad)
  if (score > 33) return "#F59E0B";  // AMBER for MEDIUM
  return "#22C55E";                   // GREEN for LOW concentration (good)
}

// ── Props ──────────────────────────────────────────────────────────────

interface SidePanelContentProps {
  selectedRegion: Region | null;
  regions: Region[];
  selectedRegionId: string | null;
}

// ── Desktop side panel ─────────────────────────────────────────────────

interface SidePanelProps extends SidePanelContentProps {
  className?: string;
}

export function SidePanel(props: SidePanelProps) {
  const { className = "" } = props;
  return (
    <aside className={`${styles.panel} ${className}`}>
      <SidePanelContent {...props} />
    </aside>
  );
}

// ── Shared content ─────────────────────────────────────────────────────

function SidePanelContent({
  selectedRegion,
  regions,
  selectedRegionId,
}: SidePanelContentProps) {
  const { t } = useLanguage();

  const nearbyRegions = selectedRegionId
    ? regions
        .filter((r) => r.id !== selectedRegionId)
        .sort((a, b) => b.concentration - a.concentration)
        .slice(0, 3)
    : [];

  return (
    <>
      {/* ── Region Info section (always visible at top) ───────────── */}
      <div className={styles.section}>
        {selectedRegion ? (
          <SelectedRegionCard region={selectedRegion} />
        ) : (
          <div className={styles.noSelection}>
            <p>{t("map.selectRegion")}</p>
          </div>
        )}

        <LegendSection />
      </div>

      {/* ── Divider ───────────────────────────────────────────────── */}
      {nearbyRegions.length > 0 && <div className={styles.divider} />}

      {/* ── Nearby regions section (scrollable below) ─────────────── */}
      {nearbyRegions.length > 0 && (
        <div className={styles.section}>
          <NearbyRegions regions={nearbyRegions} />
        </div>
      )}
    </>
  );
}

// ── Selected region card ───────────────────────────────────────────────

function SelectedRegionCard({ region }: { region: Region }) {
  const { t } = useLanguage();

  return (
    <div className={styles.regionCard}>
      <h3 className={styles.regionName}>{region.name}</h3>
      <p className={styles.population}>
        {t("map.population")}: {formatPopulation(region.indicators.averageUsers)}
      </p>

      <div className={styles.scoreSection}>
        <span className={styles.scoreLabel}>
                {region.concentration}/100 {t("map.vulnerability")}
        </span>
        <div className={styles.barContainer}>
          <div
            className={styles.barFill}
            style={{
              width: `${region.concentration}%`,
              backgroundColor: getConcentrationBarColor(region.concentration),
            }}
          />
        </div>
      </div>

      <div className={styles.badgeRow}>
        <span className={styles.badgeLabel}>
          {t("map.connectivity")}:{" "}
        </span>
        <span className={styles.badgeValue}>
          <span
            className={styles.dot}
            style={{
              backgroundColor: getConnectivityColor(region.connectivity),
            }}
            aria-hidden="true"
          />
          {getConnectivityLabel(region.connectivity, t)}
        </span>
      </div>

      {region.connectivity < 50 && (
        <div className={styles.priorityBadge}>
          {t("map.priority")}
        </div>
      )}
    </div>
  );
}

// ── Legend section ─────────────────────────────────────────────────────

function LegendSection() {
  const { t } = useLanguage();

  const items = [
    { color: "#92400E", label: t("map.legend.antenna") },
    { color: "#F59E0B", label: t("map.legend.highConcentration") },
    { color: "#EF4444", label: t("map.legend.vulnerableRegion") },
  ];

  return (
    <div className={styles.legendSection}>
      <h4 className={styles.legendTitle}>{t("map.legend.title")}</h4>
      <ul className={styles.legendList}>
        {items.map((item, i) => (
          <li key={i} className={styles.legendItem}>
            <span
              className={styles.legendDot}
              style={{ backgroundColor: item.color }}
              aria-hidden="true"
            />
            <span className={styles.legendLabel}>{item.label}</span>
          </li>
        ))}
      </ul>
      <div className={styles.heatGradient}>
        <span className={styles.heatLabel}>{t("map.legend.low")}</span>
        <div className={styles.gradientBar} />
        <span className={styles.heatLabel}>{t("map.legend.high")}</span>
      </div>
    </div>
  );
}

// ── Nearby regions list ────────────────────────────────────────────────

function NearbyRegions({ regions }: { regions: Region[] }) {
  const { t } = useLanguage();

  return (
    <div className={styles.nearbySection}>
      <h4 className={styles.nearbyTitle}>{t("map.nearbyRegions")}</h4>
      <ul className={styles.nearbyList}>
        {regions.map((region) => (
          <li key={region.id} className={styles.nearbyItem}>
            <div className={styles.nearbyInfo}>
              <span className={styles.nearbyName}>{region.name}</span>
              <span
                className={styles.nearbyScore}
                style={{
                  color: getConcentrationBarColor(region.concentration),
                }}
              >
          {region.concentration}/100 {t("map.vulnerability")}
              </span>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
