import type { AlertThreshold, AlertEvent } from "../model/types.ts";

export interface AlertRepository {
  getThresholds(): AlertThreshold[];
  setThresholds(thresholds: AlertThreshold[]): void;
  getHistory(): AlertEvent[];
  addEvent(event: AlertEvent): void;
  acknowledgeEvent(id: string): void;
  clearHistory(): void;
}
