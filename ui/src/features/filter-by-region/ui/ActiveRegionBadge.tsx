import { useLanguage } from "@/shared/lib/i18n";
import type { Region } from "@/entities/region";
import { Badge } from "@/shared/ui";

function getConnectivityLevel(connectivity: number): string {
  if (connectivity >= 66) return "HIGH";
  if (connectivity >= 33) return "MEDIUM";
  return "LOW";
}

const severityKey: Record<string, string> = {
  LOW: "severity.low",
  MEDIUM: "severity.medium",
  HIGH: "severity.high",
};

interface ActiveRegionBadgeProps {
  region: Region | null;
}

const severityBadgeVariant: Record<
  string,
  "default" | "secondary" | "destructive" | "outline" | "ghost" | "link"
> = {
  LOW: "default",
  MEDIUM: "outline",
  HIGH: "destructive",
};

export function ActiveRegionBadge({ region }: ActiveRegionBadgeProps) {
  const { t } = useLanguage();
  const level = region ? getConnectivityLevel(region.connectivity) : null;

  return (
    <div className="inline-flex items-center gap-2 rounded-lg bg-muted/50 px-3 py-1.5">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="text-muted-foreground shrink-0" aria-hidden="true">
        {region ? (
          <>
            <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" />
            <circle cx="12" cy="10" r="3" />
          </>
        ) : (
          <>
            <circle cx="12" cy="12" r="10" />
            <path d="M12 2a15 15 0 0 0 0 20 15 15 0 0 0 0-20z" />
          </>
        )}
      </svg>

      <span className="text-xs text-muted-foreground">
        {t("dashboard.viewing")}{" "}
        <strong className="text-foreground">{region ? region.name : t("dashboard.allRegions")}</strong>
      </span>

      {region && level && (
        <Badge variant={severityBadgeVariant[level] ?? "outline"}>
          {t(severityKey[level] ?? "severity.medium")}
        </Badge>
      )}
    </div>
  );
}
