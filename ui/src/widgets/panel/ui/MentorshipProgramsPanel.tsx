// ---------------------------------------------------------------------------
// MentorshipProgramsPanel — Scrollable list of mentorship programs.
//
// Each card shows program id, name, organization, focus area + modality
// badges, cluster, capacity utilization and active status. Includes a simple
// cluster filter via Select.
// ---------------------------------------------------------------------------

import { useMemo, useState } from "react";
import { GraduationCap, Building2 } from "lucide-react";
import { Card, Badge, ScrollArea, Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/shared/ui";
import { cn } from "@/shared/lib/cn";
import { useLanguage } from "@/shared/lib/i18n";
import type {
  MentorshipProgram,
  MentorshipClusterSummary,
} from "@/entities/mentorship";

interface MentorshipProgramsPanelProps {
  programs: readonly MentorshipProgram[];
  clusters: readonly MentorshipClusterSummary[];
  title?: string;
  description?: string;
  emptyMessage?: string;
}

export function MentorshipProgramsPanel({
  programs,
  clusters,
  title,
  description,
  emptyMessage,
}: MentorshipProgramsPanelProps) {
  const { t } = useLanguage();
  const resolvedTitle = title ?? t("panel.mentorship.programs");
  const resolvedEmpty = emptyMessage ?? t("common.noData");

  const [selectedCluster, setSelectedCluster] = useState<string>("__all__");

  const clusterOptions = useMemo(() => {
    const names = new Set<string>();
    clusters.forEach((c) => names.add(c.clusterName));
    programs.forEach((p) => names.add(p.clusterName));
    return Array.from(names).sort();
  }, [clusters, programs]);

  const filteredPrograms = useMemo(() => {
    if (selectedCluster === "__all__") return programs;
    return programs.filter((p) => p.clusterName === selectedCluster);
  }, [programs, selectedCluster]);

  const utilizationPct = (p: MentorshipProgram): number => {
    if (p.totalCapacity <= 0) return 0;
    return Math.min(100, (p.activeMentees / p.totalCapacity) * 100);
  };

  return (
    <Card className="flex h-full flex-col gap-3 p-5">
      <div className="flex items-center justify-between gap-2">
        <div className="flex items-center gap-2">
          <GraduationCap className="h-4 w-4 text-primary" />
          <h2 className="text-sm font-semibold">{resolvedTitle}</h2>
        </div>
        {clusterOptions.length > 0 && (
          <Select
            items={[
              { value: "__all__", label: t("panel.mentorship.allClusters") },
              ...clusterOptions.map((c) => ({ value: c, label: c })),
            ]}
            value={selectedCluster}
            onValueChange={(v: string | null) => v && setSelectedCluster(v)}
          >
            <SelectTrigger className="w-40" aria-label={t("panel.mentorship.clusterFilter")}>
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="__all__">{t("panel.mentorship.allClusters")}</SelectItem>
              {clusterOptions.map((c) => (
                <SelectItem key={c} value={c}>
                  {c}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        )}
      </div>
      {description && <p className="text-xs text-muted-foreground">{description}</p>}

      {filteredPrograms.length === 0 ? (
        <div className="flex flex-1 items-center justify-center py-8 text-center text-sm text-muted-foreground">
          {resolvedEmpty}
        </div>
      ) : (
        <ScrollArea className="-mx-1 flex-1">
          <div className="flex flex-col gap-2 px-1">
            {filteredPrograms.map((p) => (
              <div
                key={p.programId}
                className="rounded-lg border border-border bg-background/40 p-3 transition-colors hover:border-primary/40"
              >
                <div className="flex items-start justify-between gap-2">
                  <div className="min-w-0">
                    <h3 className="truncate text-sm font-medium">{p.name}</h3>
                    <p className="mt-0.5 inline-flex items-center gap-1 truncate text-xs text-muted-foreground">
                      <Building2 className="h-3 w-3" />
                      {p.organization}
                    </p>
                  </div>
                  <Badge
                    variant="outline"
                    className={cn(
                      "text-[10px]",
                      p.isActive
                        ? "border-emerald-500/40 bg-emerald-500/10 text-emerald-600 dark:text-emerald-500"
                        : "border-border bg-muted text-muted-foreground",
                    )}
                  >
                    {p.isActive ? t("panel.mentorship.active") : t("panel.mentorship.inactive")}
                  </Badge>
                </div>

                <div className="mt-2 flex flex-wrap items-center gap-1.5">
                  <Badge variant="secondary" className="text-[10px]">
                    {p.focusArea}
                  </Badge>
                  <Badge variant="outline" className="text-[10px]">
                    {p.modality}
                  </Badge>
                  <span className="text-xs text-muted-foreground">{p.clusterName}</span>
                </div>

                <div className="mt-2.5">
                  <div className="mb-1 flex items-center justify-between text-xs">
                    <span className="text-muted-foreground">
                      {t("panel.mentorship.capacity")}
                    </span>
                    <span className="font-mono font-medium">
                      {p.activeMentees} / {p.totalCapacity}
                    </span>
                  </div>
                  <div className="h-2 overflow-hidden rounded-full bg-secondary/60">
                    <div
                      className="h-full rounded-full bg-primary transition-all"
                      style={{ width: `${utilizationPct(p)}%` }}
                    />
                  </div>
                </div>
              </div>
            ))}
          </div>
        </ScrollArea>
      )}
    </Card>
  );
}
