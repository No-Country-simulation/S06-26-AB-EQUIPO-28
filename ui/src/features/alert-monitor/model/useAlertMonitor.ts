import { useCallback, useEffect, useRef } from "react";
import type { IndicatorValue } from "@/entities/indicator";
import { getIndicatorMeta } from "@/entities/indicator";
import type { AlertEvent } from "@/entities/alert";
import { alertRepository } from "@/entities/alert/api/localStorageRepository.ts";
import { notificationService } from "@/shared/lib/notifications";

const FIRED_KEY = "alert_fired_ids";

function loadFiredIds(): Set<string> {
  try {
    const raw = sessionStorage.getItem(FIRED_KEY);
    if (raw) return new Set(JSON.parse(raw) as string[]);
  } catch {
  }
  return new Set();
}

function saveFiredIds(ids: Set<string>): void {
  sessionStorage.setItem(FIRED_KEY, JSON.stringify([...ids]));
}

export function useAlertMonitor(
  indicators: IndicatorValue[],
  regionId: string | null,
  regionName: string | undefined,
) {
  const firedIdsRef = useRef<Set<string>>(loadFiredIds());

  const evaluate = useCallback(() => {
    const thresholds = alertRepository.getThresholds();
    const firedIds = firedIdsRef.current;

    for (const indicator of indicators) {
      for (const threshold of thresholds) {
        if (!threshold.enabled) continue;
        if (threshold.indicatorId !== indicator.indicatorId) continue;
        if (threshold.regionId !== null && threshold.regionId !== regionId) continue;

        const meta = getIndicatorMeta(indicator.indicatorId);
        const alertKey = `${threshold.indicatorId}-${regionId ?? "__global__"}-${threshold.value}`;

        if (firedIds.has(alertKey)) continue;

        const passes =
          threshold.operator === "lt"
            ? indicator.value < threshold.value
            : indicator.value <= threshold.value;

        if (!passes) continue;

        firedIds.add(alertKey);
        saveFiredIds(firedIds);

        const regionLabel = regionName ?? "Todas as regiões";
        const message =
          `${meta.label} en ${regionLabel} está por debajo del umbral ` +
          `(${indicator.value} < ${threshold.value})`;

        notificationService.emit("warning", message);

        const event: AlertEvent = {
          id: `${alertKey}-${Date.now()}`,
          threshold: { ...threshold },
          currentValue: indicator.value,
          currentRegionName: regionLabel,
          triggeredAt: Date.now(),
          acknowledged: false,
        };
        alertRepository.addEvent(event);
      }
    }
  }, [indicators, regionId, regionName]);

  useEffect(() => {
    evaluate();
  }, [evaluate]);

  const unacknowledgedCount =
    alertRepository.getHistory().filter((e) => !e.acknowledged).length;

  return { unacknowledgedCount, acknowledgeEvent: alertRepository.acknowledgeEvent };
}
