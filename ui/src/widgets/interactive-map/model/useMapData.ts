import { useState, useEffect, useCallback, useRef } from "react";
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
      const errors: string[] = [];

      // Antennas
      if (antennaCache.has(antennaKey)) {
        if (!cancelled) setAntennas(antennaCache.get(antennaKey)!);
      } else {
        try {
          const data = await repository.getAntennas(regionId ? { regionId } : undefined);
          if (!cancelled) {
            antennaCache.set(antennaKey, data);
            setAntennas(data);
          }
        } catch {
          if (!cancelled) setAntennas([]);
          errors.push("Antenas");
        }
      }

      // Concentration
      const cached = concentrationCache.get(concentrationKey);
      if (cached) {
        if (!cancelled) {
          setPins(vulnerableOnly ? cached.pins.filter((p) => p.intensity >= 66) : cached.pins);
          setLegend(cached.legend);
        }
      } else {
        try {
          const data = await repository.getConcentration(period ? { period } : undefined);
          if (!cancelled) {
            concentrationCache.set(concentrationKey, { pins: data.pins, legend: data.legend });
            setPins(vulnerableOnly ? data.pins.filter((p) => p.intensity >= 66) : data.pins);
            setLegend(data.legend);
          }
        } catch {
          if (!cancelled) { setPins([]); setLegend([]); }
          errors.push("Concentración");
        }
      }

      if (!cancelled) {
        setError(errors.length > 0 ? `Error cargando: ${errors.join(", ")}` : null);
        setIsLoading(false);
      }
      fetchingRef.current = false;
    }

    load();
    return () => { cancelled = true; };
  }, [repository, antennaKey, concentrationKey, vulnerableOnly, regionId, period]);

  return { antennas, pins, legend, isLoading, error } as const;
}
