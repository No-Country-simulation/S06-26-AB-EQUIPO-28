// ---------------------------------------------------------------------------
// EmployabilityGapsPanel — Scrollable list of per-cluster employability gaps.
//
// Each card shows the cluster name, severity badge, gap score bar, telemetry
// coverage status, demographic breakdown, distance to nearest hub, primary
// factors and municipalities. Mirrors the visual style of ZonasPanel.
// ---------------------------------------------------------------------------

import {
  Briefcase,
  TriangleAlert,
  WifiOff,
  Users as UsersIcon,
} from "lucide-react";
import { Card, Badge, ScrollArea } from "@/shared/ui";
import { cn } from "@/shared/lib/cn";
import { useLanguage, formatLocaleNumber } from "@/shared/lib/i18n";
import type {
  EmployabilityGap,
  EmployabilityGapSeverity,
} from "@/entities/employability";

interface EmployabilityGapsPanelProps {
  gaps: readonly EmployabilityGap[];
  title?: string;
  description?: string;
  emptyMessage?: string;
}

const severityClass: Record<EmployabilityGapSeverity, string> = {
  CRITICAL: "border-[color:var(--alert)]/40 bg-[color:var(--alert)]/10 text-[color:var(--alert)]",
  HIGH: "border-amber-500/40 bg-amber-500/10 text-amber-600 dark:text-amber-500",
  MODERATE: "border-primary/40 bg-primary/10 text-primary",
  LOW: "border-emerald-500/40 bg-emerald-500/10 text-emerald-600 dark:text-emerald-500",
  NONE: "border-border bg-muted text-muted-foreground",
};

function barColor(severity: EmployabilityGapSeverity): string {
  switch (severity) {
    case "CRITICAL":
      return "var(--alert)";
    case "HIGH":
      return "#f59e0b";
    case "MODERATE":
      return "var(--primary)";
    case "LOW":
      return "#10b981";
    default:
      return "var(--muted-foreground)";
  }
}

export function EmployabilityGapsPanel({
  gaps,
  title,
  description,
  emptyMessage,
}: EmployabilityGapsPanelProps) {
  const { t, locale } = useLanguage();
  const resolvedTitle = title ?? t("panel.employability.gaps");
  const resolvedEmpty = emptyMessage ?? t("common.noData");

  return (
    <Card className="flex h-full flex-col gap-3 p-5">
      <div className="flex items-center gap-2">
        <Briefcase className="h-4 w-4 text-primary" />
        <h2 className="text-sm font-semibold">{resolvedTitle}</h2>
      </div>
      {description && <p className="text-xs text-muted-foreground">{description}</p>}

      {gaps.length === 0 ? (
        <div className="flex flex-1 items-center justify-center py-8 text-center text-sm text-muted-foreground">
          {resolvedEmpty}
        </div>
      ) : (
        <ScrollArea className="-mx-1 flex-1">
          <div className="flex flex-col gap-2 px-1">
            {gaps.map((gap) => (
              <div
                key={gap.cluster}
                className="rounded-lg border border-border bg-background/40 p-3 transition-colors hover:border-primary/40"
              >
                <div className="flex items-start justify-between gap-2">
                  <div className="min-w-0">
                    <h3 className="truncate text-sm font-medium">{gap.cluster}</h3>
                    {gap.municipalities.length > 0 && (
                      <p className="mt-0.5 truncate text-xs text-muted-foreground">
                        {gap.municipalities.join(", ")}
                      </p>
                    )}
                  </div>
                  <Badge variant="outline" className={cn("text-[10px]", severityClass[gap.gapSeverity])}>
                    {gap.gapSeverity}
                  </Badge>
                </div>

                <div className="mt-2.5">
                  <div className="mb-1 flex items-center justify-between text-xs">
                    <span className="text-muted-foreground">{t("panel.employability.gapScore")}</span>
                    <span className="font-mono font-semibold">{gap.gapScore.toFixed(1)}</span>
                  </div>
                  <div className="h-2 overflow-hidden rounded-full bg-secondary/60">
                    <div
                      className="h-full rounded-full transition-all"
                      style={{
                        width: `${Math.min(100, gap.gapScore)}%`,
                        background: barColor(gap.gapSeverity),
                      }}
                    />
                  </div>
                </div>

                <div className="mt-2.5 flex items-center gap-2">
                  {!gap.hasTelemetryCoverage && (
                    <Badge variant="outline" className="border-[color:var(--alert)]/40 bg-[color:var(--alert)]/10 text-[color:var(--alert)]">
                      <WifiOff className="h-3 w-3" />
                      {t("panel.employability.blindZone")}
                    </Badge>
                  )}
                  {gap.gapSeverity === "CRITICAL" && (
                    <Badge variant="outline" className="border-amber-500/40 bg-amber-500/10 text-amber-600 dark:text-amber-500">
                      <TriangleAlert className="h-3 w-3" />
                      {t("panel.metric.priority")}
                    </Badge>
                  )}
                </div>

                <div className="mt-2.5 grid grid-cols-3 gap-2 text-center">
                  <div className="rounded-lg bg-secondary/40 p-2">
                    <div className="font-mono text-base font-semibold">
                      {formatLocaleNumber(gap.citizenCount, locale)}
                    </div>
                    <div className="text-[10px] text-muted-foreground">citizens</div>
                  </div>
                  <div className="rounded-lg bg-secondary/40 p-2">
                    <div className="font-mono text-base font-semibold">
                      {formatLocaleNumber(gap.incomeDCount, locale)}
                    </div>
                    <div className="text-[10px] text-muted-foreground">income D</div>
                  </div>
                  <div className="rounded-lg bg-secondary/40 p-2">
                    <div className="font-mono text-base font-semibold">
                      {formatLocaleNumber(gap.youthCount18_24, locale)}
                    </div>
                    <div className="text-[10px] text-muted-foreground">youth 18-24</div>
                  </div>
                </div>

                <div className="mt-2 flex items-center justify-between text-xs text-muted-foreground">
                  <span className="inline-flex items-center gap-1">
                    <UsersIcon className="h-3 w-3" />
                    daytime avg:{" "}
                    <span className="font-mono font-medium text-foreground">
                      {formatLocaleNumber(gap.daytimeAvgUsers, locale)}
                    </span>
                  </span>
                  <span>
                    nearest hub:{" "}
                    <span className="font-mono font-medium text-foreground">
                      {gap.distanceToNearestHubKm.toFixed(1)} km
                    </span>
                  </span>
                </div>

                {gap.primaryFactors.length > 0 && (
                  <div className="mt-2.5 flex flex-wrap gap-1">
                    {gap.primaryFactors.map((f, i) => (
                      <Badge key={`${f}-${i}`} variant="secondary" className="text-[10px]">
                        {f}
                      </Badge>
                    ))}
                  </div>
                )}
              </div>
            ))}
          </div>
        </ScrollArea>
      )}
    </Card>
  );
}
