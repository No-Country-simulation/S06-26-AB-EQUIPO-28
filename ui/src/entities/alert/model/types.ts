import type { IndicatorId } from "@/entities/indicator";

export type AlertOperator = "lt" | "lte";

export interface AlertThreshold {
  indicatorId: IndicatorId;
  regionId: string | null;
  operator: AlertOperator;
  value: number;
  enabled: boolean;
}

export interface AlertEvent {
  id: string;
  threshold: AlertThreshold;
  currentValue: number;
  currentRegionName: string;
  triggeredAt: number;
  acknowledged: boolean;
}
