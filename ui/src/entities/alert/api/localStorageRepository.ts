import type { AlertThreshold, AlertEvent } from "../model/types.ts";
import { DEFAULT_THRESHOLDS } from "../model/defaults.ts";
import type { AlertRepository } from "./port.ts";

const THRESHOLDS_KEY = "alert_thresholds";
const HISTORY_KEY = "alert_history";



function loadThresholds(): AlertThreshold[] {
  try {
    const raw = localStorage.getItem(THRESHOLDS_KEY);
    if (raw) {
      const parsed = JSON.parse(raw) as AlertThreshold[];
      if (Array.isArray(parsed) && parsed.length > 0) return parsed;
    }
  } catch {
  }
  return DEFAULT_THRESHOLDS;
}

function saveThresholds(thresholds: AlertThreshold[]): void {
  localStorage.setItem(THRESHOLDS_KEY, JSON.stringify(thresholds));
}

function loadHistory(): AlertEvent[] {
  try {
    const raw = localStorage.getItem(HISTORY_KEY);
    if (raw) {
      const parsed = JSON.parse(raw) as AlertEvent[];
      if (Array.isArray(parsed)) return parsed;
    }
  } catch {
  }
  return [];
}

function saveHistory(history: AlertEvent[]): void {
  localStorage.setItem(HISTORY_KEY, JSON.stringify(history));
}

export const alertRepository: AlertRepository = {
  getThresholds(): AlertThreshold[] {
    return loadThresholds();
  },

  setThresholds(thresholds: AlertThreshold[]): void {
    saveThresholds(thresholds);
  },

  getHistory(): AlertEvent[] {
    return loadHistory();
  },

  addEvent(event: AlertEvent): void {
    const history = loadHistory();
    history.unshift(event);
    if (history.length > 50) history.length = 50;
    saveHistory(history);
  },

  acknowledgeEvent(id: string): void {
    const history = loadHistory();
    const idx = history.findIndex((e) => e.id === id);
    if (idx !== -1) {
      history[idx] = { ...history[idx], acknowledged: true };
      saveHistory(history);
    }
  },

  clearHistory(): void {
    saveHistory([]);
  },
};
