// ---------------------------------------------------------------------------
// useMapViewport — Hook for managing the map viewport state.
//
// Default view: centred on Florianópolis (-27.595, -48.548) at zoom 11.
// Returns a stable setViewport callback via useCallback.
// ---------------------------------------------------------------------------

import { useState, useCallback } from "react";

export interface Viewport {
  readonly lat: number;
  readonly lng: number;
  readonly zoom: number;
}

const DEFAULT_VIEWPORT: Viewport = {
  lat: -27.595,
  lng: -48.548,
  zoom: 11,
};

export function useMapViewport(initial?: Partial<Viewport>) {
  const [viewport, setViewportState] = useState<Viewport>({
    lat: initial?.lat ?? DEFAULT_VIEWPORT.lat,
    lng: initial?.lng ?? DEFAULT_VIEWPORT.lng,
    zoom: initial?.zoom ?? DEFAULT_VIEWPORT.zoom,
  });

  const setViewport = useCallback((next: Viewport) => {
    setViewportState(next);
  }, []);

  return {
    viewport,
    setViewport,
  } as const;
}
