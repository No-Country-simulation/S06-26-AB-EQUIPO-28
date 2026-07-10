// ---------------------------------------------------------------------------
// MentorshipGapsPanel — Scrollable list of per-cluster mentorship gaps.
//
// Each card shows the cluster name, severity badge, vulnerability score,
// connectivity, mentorship coverage status, matching programs and vulnerable
// population figures. Mirrors the visual style of ZonasPanel.
// ---------------------------------------------------------------------------

import { Users, SignalHigh, TriangleAlert, CheckCircle2, XCircle } from "lucide-react";
import { Card, Badge, ScrollArea } from "@/shared/ui";
import { cn } from "@/shared/lib/cn";
import { useLanguage, formatLocaleNumber } from "@/shared/lib/i18n";
import type {
  MentorshipGap,
  MentorshipGapSeverity,
} from "@/entities/mentorship";

interface MentorshipGapsPanelProps {
  gaps: readonly MentorshipGap[];
  title?: string;
  description?: string;
  emptyMessage?: string;
}

const severityClass: Record<MentorshipGapSeverity, string> = {
  CRITICAL: "border-[color:var(--alert)]/40 bg-[color:var(--alert)]/10 text-[color:var(--alert)]",
  HIGH: "border-amber-500/40 bg-amber-500/10 text-amber-600 dark:text-amber-500",
  MODERATE: "border-primary/40 bg-primary/10 text-primary",
  LOW: "border-emerald-500/40 bg-emerald-500/10 text-emerald-600 dark:text-emerald-500",
};

export function MentorshipGapsPanel({
  gaps,
  title,
  description,
  emptyMessage,
}: MentorshipGapsPanelProps) {
  const { t, locale } = useLanguage();
  const resolvedTitle = title ?? t("panel.mentorship.gaps");
  const resolvedEmpty = emptyMessage ?? t("common.noData");

  return (
    <Card className="flex h-full flex-col gap-3 p-5">
      <div className="flex items-center gap-2">
        <Users className="h-4 w-4 text-primary" />
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
                key={gap.clusterName}
                className="rounded-lg border border-border bg-background/40 p-3 transition-colors hover:border-primary/40"
              >
                <div className="flex items-start justify-between gap-2">
                  <div className="min-w-0">
                    <h3 className="truncate text-sm font-medium">{gap.clusterName}</h3>
                  </div>
                  <Badge variant="outline" className={cn("text-[10px]", severityClass[gap.gapSeverity])}>
                    {gap.gapSeverity}
                  </Badge>
                </div>

                <div className="mt-2 grid grid-cols-3 gap-2 text-center">
                  <div className="rounded-lg bg-secondary/40 p-2">
                    <div className="font-mono text-base font-semibold">
                      {gap.vulnerabilityScore.toFixed(1)}
                    </div>
                    <div className="text-[10px] text-muted-foreground">vulnerability</div>
                  </div>
                  <div className="rounded-lg bg-secondary/40 p-2">
                    <div className="inline-flex items-center gap-1 font-mono text-xs font-semibold">
                      <SignalHigh className="h-3 w-3" />
                      {gap.connectivityLevel}
                    </div>
                    <div className="text-[10px] text-muted-foreground">connectivity</div>
                  </div>
                  <div className="rounded-lg bg-secondary/40 p-2">
                    <div className="font-mono text-base font-semibold">
                      {gap.vulnerablePercentage.toFixed(0)}%
                    </div>
                    <div className="text-[10px] text-muted-foreground">% vuln.</div>
                  </div>
                </div>

                <div className="mt-2.5 flex items-center gap-2">
                  {gap.hasMentorshipPrograms ? (
                    <Badge variant="outline" className="border-emerald-500/40 bg-emerald-500/10 text-emerald-600 dark:text-emerald-500">
                      <CheckCircle2 className="h-3 w-3" />
                      {t("panel.mentorship.withPrograms")}
                    </Badge>
                  ) : (
                    <Badge variant="outline" className="border-[color:var(--alert)]/40 bg-[color:var(--alert)]/10 text-[color:var(--alert)]">
                      <XCircle className="h-3 w-3" />
                      {t("panel.mentorship.withoutPrograms")}
                    </Badge>
                  )}
                  {gap.isPriorityForIntervention && (
                    <Badge variant="outline" className="border-amber-500/40 bg-amber-500/10 text-amber-600 dark:text-amber-500">
                      <TriangleAlert className="h-3 w-3" />
                      {t("panel.metric.priority")}
                    </Badge>
                  )}
                </div>

                <div className="mt-2 flex items-center justify-between text-xs text-muted-foreground">
                  <span>
                    {t("panel.mentorship.vulnerablePop")}:{" "}
                    <span className="font-mono font-medium text-foreground">
                      {formatLocaleNumber(gap.vulnerablePopulationCount, locale)}
                    </span>{" "}
                    / {formatLocaleNumber(gap.totalPopulation, locale)}
                  </span>
                </div>

                {gap.matchingPrograms.length > 0 && (
                  <div className="mt-2.5 flex flex-wrap gap-1">
                    {gap.matchingPrograms.map((p) => (
                      <Badge key={p.programId} variant="secondary" className="text-[10px]">
                        {p.name}
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
