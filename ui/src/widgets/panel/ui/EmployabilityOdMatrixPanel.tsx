// ---------------------------------------------------------------------------
// EmployabilityOdMatrixPanel — Top 10 mobility corridors by unique users.
//
// Sorts the OD matrix desc by uniqueUsers, takes the top 10 and renders a
// Table with origin → destination, unique users, total trips, average
// distance and predominant period.
// ---------------------------------------------------------------------------

import { useMemo } from "react";
import { ArrowRight, Route } from "lucide-react";
import { Card, Table } from "@/shared/ui";
import { useLanguage, formatLocaleNumber } from "@/shared/lib/i18n";
import type { MobilityODPair } from "@/entities/employability";

interface EmployabilityOdMatrixPanelProps {
  odMatrix: readonly MobilityODPair[];
  title?: string;
  description?: string;
  emptyMessage?: string;
}

const TOP_N = 10;

export function EmployabilityOdMatrixPanel({
  odMatrix,
  title,
  description,
  emptyMessage,
}: EmployabilityOdMatrixPanelProps) {
  const { t, locale } = useLanguage();
  const resolvedTitle = title ?? t("panel.employability.odMatrix");
  const resolvedEmpty = emptyMessage ?? t("common.noData");

  const topCorridors = useMemo(() => {
    return [...odMatrix]
      .sort((a, b) => b.uniqueUsers - a.uniqueUsers)
      .slice(0, TOP_N);
  }, [odMatrix]);

  const columns = [
    { key: "origin", label: t("panel.employability.origin"), width: "28%" },
    { key: "destination", label: t("panel.employability.destination"), width: "28%" },
    { key: "uniqueUsers", label: t("panel.employability.uniqueUsers"), width: "14%" },
    { key: "totalTrips", label: t("panel.employability.totalTrips"), width: "12%" },
    { key: "avgDistance", label: t("panel.employability.avgDistance"), width: "12%" },
    { key: "period", label: t("panel.employability.period"), width: "6%" },
  ];

  const rows = topCorridors.map((pair) => ({
    origin: `${pair.originCluster}${pair.sameCluster ? ` ${t("panel.employability.sameCluster")}` : ""}`,
    destination: `${pair.destinationCluster}${pair.sameCluster ? ` ${t("panel.employability.sameCluster")}` : ""}`,
    uniqueUsers: formatLocaleNumber(pair.uniqueUsers, locale),
    totalTrips: formatLocaleNumber(pair.totalTrips, locale),
    avgDistance: `${pair.averageDistanceKm.toFixed(1)} km`,
    period: pair.predominantPeriod,
  }));

  return (
    <Card className="flex h-full flex-col gap-3 p-5">
      <div className="flex items-center gap-2">
        <Route className="h-4 w-4 text-primary" />
        <h2 className="text-sm font-semibold">{resolvedTitle}</h2>
      </div>
      {description && <p className="text-xs text-muted-foreground">{description}</p>}

      {topCorridors.length === 0 ? (
        <div className="flex flex-1 items-center justify-center py-8 text-center text-sm text-muted-foreground">
          {resolvedEmpty}
        </div>
      ) : (
        <div className="flex-1 overflow-hidden">
          <Table columns={columns} data={rows} />
          <div className="mt-2 flex items-center gap-1 text-[10px] text-muted-foreground">
            <ArrowRight className="h-3 w-3" />
            {t("panel.employability.topNBy")}: {t("panel.employability.uniqueUsers")}
          </div>
        </div>
      )}
    </Card>
  );
}
