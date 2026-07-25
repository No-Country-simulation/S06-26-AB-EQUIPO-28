import { useState, useEffect, useRef } from "react";
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

const concentrationCache = new Map<string, { pins: ConcentrationPin[]; legend: ConcentrationLegend[] }>();
const antennaCache = new Map<string, Antenna[]>();

const MAX_RETRIES = 1;
const RETRY_DELAY_MS = 1500;

async function withRetry<T>(fn: () => Promise<T>, retries: number = MAX_RETRIES): Promise<T> {
  let lastError: unknown;
  for (let attempt = 0; attempt <= retries; attempt++) {
    try {
      return await fn();
    } catch (err) {
      lastError = err;
      if (attempt < retries) {
        await new Promise((r) => setTimeout(r, RETRY_DELAY_MS * (attempt + 1)));
      }
    }
  }
  throw lastError;
}

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
  const fetchingRef = useRef(false);

  const antennaKey = regionId ?? "__all__";
  const concentrationKey = period ?? "__all__";

  useEffect(() => {
    if (fetchingRef.current) return;
    fetchingRef.current = true;

    let cancelled = false;

    async function load() {
      setIsLoading(true);
      setError(null);

      const antennaCached = antennaCache.has(antennaKey);
      const concentrationCached = concentrationCache.has(concentrationKey);

      // Fetch both in parallel if not cached
      const promises: Promise<void>[] = [];

      if (!antennaCached) {
        promises.push(
          withRetry(() => repository.getAntennas(regionId ? { regionId } : undefined))
            .then((data) => {
              if (!cancelled) {
                antennaCache.set(antennaKey, data);
                setAntennas(data);
              }
            })
            .catch(() => {
              if (!cancelled) setAntennas([]);
            })
        );
      }

      if (!concentrationCached) {
        promises.push(
          withRetry(() => repository.getConcentration(period ? { period } : undefined))
            .then((data) => {
              if (!cancelled) {
                concentrationCache.set(concentrationKey, { pins: data.pins, legend: data.legend });
                setPins(vulnerableOnly ? data.pins.filter((p) => p.intensity >= 66) : data.pins);
                setLegend(data.legend);
              }
            })
            .catch(() => {
              if (!cancelled) { setPins([]); setLegend([]); }
            })
        );
      }

      // Apply cached data immediately
      if (antennaCached) {
        setAntennas(antennaCache.get(antennaKey)!);
      }
      if (concentrationCached) {
        const cached = concentrationCache.get(concentrationKey)!;
        setPins(vulnerableOnly ? cached.pins.filter((p) => p.intensity >= 66) : cached.pins);
        setLegend(cached.legend);
      }

      if (promises.length > 0) {
        await Promise.allSettled(promises);
      }

      if (!cancelled) {
        setIsLoading(false);
      }
      fetchingRef.current = false;
    }

    load();
    return () => { cancelled = true; fetchingRef.current = false; };
  }, [repository, antennaKey, concentrationKey, vulnerableOnly, regionId, period]);

  return { antennas, pins, legend, isLoading, error } as const;
}
