// ---------------------------------------------------------------------------
// useMapData — Fetches antennas and concentration data for the map.
//
// Takes a MobilityDataRepository port and loads both data sets on mount
// with proper cleanup.  Uses plain useState + useEffect (no TanStack Query,
// no react-router).
//
// Handles endpoint errors independently: if one endpoint fails (e.g. 404),
// the other's data is still shown.  All nullable fields from the backend
// are defaulted to 0 via the mapper (?? 0) so the map never receives null
// coordinates or intensity values.
// ---------------------------------------------------------------------------

import { useState, useEffect, useCallback } from "react";
import type {
  Antenna,
  ConcentrationPin,
  ConcentrationLegend,
  MobilityDataRepository,
} from "@/entities/mobility-data";

export interface MapDataState {
  readonly antennas: Antenna[];
  readonly pins: ConcentrationPin[];
  readonly legend: ConcentrationLegend[];
  readonly isLoading: boolean;
  readonly error: string | null;
}

// Module-level caches to avoid refetching on tab switches.
// Concentration is keyed by period so changing period triggers a fresh fetch.
const concentrationCache = new Map<string, { pins: ConcentrationPin[]; legend: ConcentrationLegend[] }>();
const antennaCache = new Map<string, Antenna[]>();

export function useMapData(
  repository: MobilityDataRepository,
  regionId?: string | null,
  period?: string,
  vulnerableOnly?: boolean,
): MapDataState {
  const [antennas, setAntennas] = useState<Antenna[]>(() => {
    const key = regionId ?? "__all__";
    return antennaCache.get(key) ?? [];
  });
  const [pins, setPins] = useState<ConcentrationPin[]>(() => {
    const cached = period ? concentrationCache.get(period) : undefined;
    return cached?.pins ?? [];
  });
  const [legend, setLegend] = useState<ConcentrationLegend[]>(() => {
    const cached = period ? concentrationCache.get(period) : undefined;
    return cached?.legend ?? [];
  });
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const load = useCallback(async (cancelled: { current: boolean }) => {
    setIsLoading(true);
    setError(null);

    const antennaParams = regionId ? { regionId } : undefined;
    const errors: string[] = [];

    // Fetch antennas — filtered by region, cached per region key
    const antennaKey = regionId ?? "__all__";
    if (antennaCache.has(antennaKey)) {
      if (!cancelled.current) {
        const cachedAntennas = antennaCache.get(antennaKey)!;
        const filteredAntennas = vulnerableOnly
          ? cachedAntennas.filter((a) => a.loadLevel === "HIGH")
          : cachedAntennas;
        console.log("[useMapData] antennas from CACHE:", filteredAntennas.length, "region:", antennaKey);
        setAntennas(filteredAntennas);
      }
    } else {
      try {
        console.log("[useMapData] fetching antennas for region:", antennaKey);
        const antennaData = await repository.getAntennas(antennaParams);
        console.log("[useMapData] antennas fetched:", antennaData.length, "region:", antennaKey);
        if (!cancelled.current) {
          antennaCache.set(antennaKey, antennaData);
          const filteredAntennas = vulnerableOnly
            ? antennaData.filter((a) => a.loadLevel === "HIGH")
            : antennaData;
          setAntennas(filteredAntennas);
        }
      } catch (err) {
        console.error("[useMapData] antenna fetch FAILED:", err);
        errors.push(
          `Antenas: ${err instanceof Error ? err.message : "error al cargar"}`,
        );
        if (!cancelled.current) setAntennas([]);
      }
    }

    // Fetch concentration — ALWAYS fetch ALL data (no region filter)
    // The concentration heatmap shows across all regions regardless of selection.
    // Cache is keyed by period so changing the period triggers a fresh fetch.
    const concentrationKey = period ?? "__all__";
    const cached = concentrationCache.get(concentrationKey);
    if (cached) {
      if (!cancelled.current) {
        const filteredPins = vulnerableOnly
          ? cached.pins.filter((p) => p.intensity >= 66)
          : cached.pins;
        setPins(filteredPins);
        setLegend(cached.legend);
      }
    } else {
      try {
        const concentrationParams = period ? { period } : undefined;
        const concentrationData = await repository.getConcentration(concentrationParams);
        if (!cancelled.current) {
          concentrationCache.set(concentrationKey, { pins: concentrationData.pins, legend: concentrationData.legend });
          const filteredPins = vulnerableOnly
            ? concentrationData.pins.filter((p) => p.intensity >= 66)
            : concentrationData.pins;
          setPins(filteredPins);
          setLegend(concentrationData.legend);
        }
      } catch (err) {
        errors.push(
          `Concentración: ${err instanceof Error ? err.message : "error al cargar"}`,
        );
        if (!cancelled.current) {
          setPins([]);
          setLegend([]);
        }
      }
    }

    if (cancelled.current) return;

    if (errors.length > 0) {
      setError(
        errors.length === 2
          ? "No fue posible cargar los datos del mapa. Verifique que los servicios de telemetría estén disponibles."
          : errors.join("; "),
      );
    }

    setIsLoading(false);
  }, [repository, regionId, period, vulnerableOnly]);

  useEffect(() => {
    const cancelled = { current: false };

    load(cancelled);

    return () => {
      cancelled.current = true;
    };
  }, [load]);

  return {
    antennas,
    pins,
    legend,
    isLoading,
    error,
  } as const;
}
