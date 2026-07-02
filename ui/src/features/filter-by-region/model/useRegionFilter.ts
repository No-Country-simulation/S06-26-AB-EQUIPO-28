// ---------------------------------------------------------------------------
// useRegionFilter — Orchestration hook for the region filter feature.
//
// Takes a RegionRepository port, loads the vulnerable regions list on
// mount, and exposes selection state.  Uses plain React state + effects
// (no TanStack Query, no react-router).
// ---------------------------------------------------------------------------

import { useState, useEffect, useCallback, useMemo } from "react";
import type { Region, RegionRepository } from "@/entities/region";

let cachedRegions: Region[] | null = null;

export function useRegionFilter(repository: RegionRepository) {
  const [regions, setRegions] = useState<Region[]>(cachedRegions ?? []);
  const [isLoading, setIsLoading] = useState(!cachedRegions);
  const [selectedRegionId, setSelectedRegionId] = useState<string | null>(
    null,
  );

  useEffect(() => {
    if (cachedRegions) {
      setRegions(cachedRegions);
      setIsLoading(false);
      return;
    }

    let cancelled = false;

    async function load() {
      setIsLoading(true);
      try {
        const data = await repository.getRegions();
        if (!cancelled) {
          cachedRegions = data;
          setRegions(data);
        }
      } catch {
        if (!cancelled) {
          setRegions([]);
        }
      } finally {
        if (!cancelled) {
          setIsLoading(false);
        }
      }
    }

    load();

    return () => {
      cancelled = true;
    };
  }, [repository]);

  const setSelectedRegion = useCallback((id: string | null) => {
    setSelectedRegionId(id);
  }, []);

  const selectedRegion = useMemo<Region | null>(() => {
    if (selectedRegionId === null) return null;
    return regions.find((r) => r.id === selectedRegionId) ?? null;
  }, [selectedRegionId, regions]);

  return {
    regions,
    selectedRegionId,
    setSelectedRegion,
    isLoading,
    selectedRegion,
  } as const;
}
