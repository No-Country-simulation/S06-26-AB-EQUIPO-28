// ---------------------------------------------------------------------------
// useDashboard — Orchestration hook for the AI Query Dashboard.
//
// Wires AI query + region filter + indicator selection together, injecting
// the currently selected region and indicator into every natural-language
// question so the backend receives the full filter context.
//
// Uses plain React state / callbacks throughout (no TanStack Query, no
// react-router).
// ---------------------------------------------------------------------------

import { useState, useCallback, useEffect } from "react";
import type { AiAgentRepository, AiResponse } from "@/entities/ai-agent";
import type { RegionRepository, Region } from "@/entities/region";
import { useRegionFilter } from "@/features/filter-by-region";

// ── SessionStorage keys for AI query persistence ────────────────────────
const STORAGE_KEY_RESPONSE = "appbit_ai_response";
const STORAGE_KEY_ERROR = "appbit_ai_error";

function loadFromStorage<T>(key: string): T | null {
  try {
    const raw = sessionStorage.getItem(key);
    return raw ? (JSON.parse(raw) as T) : null;
  } catch {
    return null;
  }
}

function saveToStorage<T>(key: string, value: T | null): void {
  try {
    if (value === null) {
      sessionStorage.removeItem(key);
    } else {
      sessionStorage.setItem(key, JSON.stringify(value));
    }
  } catch {
    // Storage full or unavailable — silently ignore
  }
}

export interface DashboardState {
  readonly selectedIndicator: string | null;
  readonly setSelectedIndicator: (id: string | null) => void;

  readonly askAi: {
    readonly query: string;
    readonly setQuery: (q: string) => void;
    readonly submit: () => Promise<void>;
    readonly response: AiResponse | null;
    readonly isLoading: boolean;
    readonly error: string | null;
    readonly clearResponse: () => void;
  };

  readonly regionFilter: {
    readonly regions: readonly Region[];
    readonly selectedRegionId: string | null;
    readonly setSelectedRegion: (id: string | null) => void;
    readonly isLoading: boolean;
    readonly selectedRegion: Region | null;
  };
}

export function useDashboard(
  aiRepository: AiAgentRepository,
  regionRepository: RegionRepository,
): DashboardState {
  // ── Indicator selection ────────────────────────────────────────────
  const [selectedIndicator, setSelectedIndicator] = useState<string | null>(
    null,
  );

  // ── Region filter (delegated to existing feature hook) ─────────────
  const regionFilter = useRegionFilter(regionRepository);

  // ── AI query state (persisted to sessionStorage across navigations) ──
  const [query, setQuery] = useState("");
  const [response, setResponse] = useState<AiResponse | null>(
    loadFromStorage<AiResponse>(STORAGE_KEY_RESPONSE),
  );
  const [isQueryLoading, setIsQueryLoading] = useState(false);
  const [error, setError] = useState<string | null>(
    loadFromStorage<string>(STORAGE_KEY_ERROR),
  );

  // Persist response/error to sessionStorage whenever they change
  useEffect(() => {
    saveToStorage(STORAGE_KEY_RESPONSE, response);
  }, [response]);

  useEffect(() => {
    saveToStorage(STORAGE_KEY_ERROR, error);
  }, [error]);

  const submit = useCallback(async () => {
    const trimmed = query.trim();
    if (!trimmed) return;

    setIsQueryLoading(true);
    setError(null);

    try {
      const result = await aiRepository.askQuery({
        question: trimmed,
        indicator: selectedIndicator ?? undefined,
        region: regionFilter.selectedRegionId ?? undefined,
      });
      setResponse(result);
      setQuery("");
    } catch (err) {
      const message =
        err instanceof Error
          ? err.message
          : "Ocurrió un error al procesar la consulta.";
      setError(message);
    } finally {
      setIsQueryLoading(false);
    }
  }, [query, aiRepository, selectedIndicator, regionFilter.selectedRegionId]);

  const clearResponse = useCallback(() => {
    setResponse(null);
    setError(null);
  }, []);

  return {
    selectedIndicator,
    setSelectedIndicator,

    askAi: {
      query,
      setQuery,
      submit,
      response,
      isLoading: regionFilter.isLoading || isQueryLoading,
      error,
      clearResponse,
    },

    regionFilter: {
      regions: regionFilter.regions,
      selectedRegionId: regionFilter.selectedRegionId,
      setSelectedRegion: regionFilter.setSelectedRegion,
      isLoading: regionFilter.isLoading,
      selectedRegion: regionFilter.selectedRegion,
    },
  };
}
