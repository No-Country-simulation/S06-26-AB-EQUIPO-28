// ---------------------------------------------------------------------------
// MapLibreMap — Actual MapLibre GL JS map with antenna markers and
// concentration pins.
//
// Intentionally a separate module so InteractiveMapWidget can use
// React.lazy + dynamic import.  If maplibre-gl is not installed, the
// import fails and the placeholder is shown instead.
//
// Map tiles: OpenFreeMap (https://tiles.openfreemap.org/styles/liberty).
// Markers:  antenna circles (coloured by load level) + concentration
//           circles (sized by intensity).
//
// Data lifecycle:
//   1. Map initialised once on mount (useEffect with empty deps).
//   2. GeoJSON sources are created in the "load" handler.
//   3. When `antennas` or `pins` change, sources are updated via
//      map.getSource(id).setData(...) — no map recreation needed.
//
// Null safety: all coordinates and numeric values coming from domain
// types are guaranteed to be number (mappers use ?? 0 fallback).  If
// data is empty, the map renders with no markers — no crash.
//
// Uses a minimal interface shim rather than importing maplibre-gl types
// directly so the module can compile even without the package installed.
// ---------------------------------------------------------------------------

import { useEffect, useRef } from "react";
import type { Antenna, ConcentrationPin } from "@/entities/mobility-data";
import type { Viewport } from "../model/useMapViewport.ts";

export interface PopupStrings {
  antennaLabel: string;
  loadLabel: string;
  concentrationPoint: string;
  intensityLabel: string;
}

export interface MapLibreMapProps {
  antennas: Antenna[];
  pins: ConcentrationPin[];
  viewport: Viewport;
  showAntennas?: boolean;
  popupStrings?: PopupStrings;
}

// Load colours for antenna load levels (matches DESIGN.md palette).
const LOAD_COLORS: Record<string, string> = {
  HIGH: "#B91C1C",
  MEDIUM: "#92400E",
  LOW: "#065F46",
};

// -----------------------------------------------------------------------
// Minimal interface shim for the subset of MapLibre API we use.
// Avoids static dependency on maplibre-gl types so the module compiles
// without the package being installed.
// -----------------------------------------------------------------------

interface GeoJSONFeature {
  type: "Feature";
  geometry: { type: "Point"; coordinates: number[] };
  properties?: Record<string, unknown>;
}

interface GeoJSONSourceShim {
  setData(data: { type: string; features: GeoJSONFeature[] }): void;
  getClusterExpansionZoom(
    clusterId: number,
    callback: (err: Error | null, zoom: number) => void,
  ): void;
}

interface MapClickEvent {
  features?: GeoJSONFeature[];
  point?: { x: number; y: number };
}

interface MapShim {
  on(event: string, layerOrHandler: string | ((e: MapClickEvent) => void), handler?: (e: MapClickEvent) => void): void;
  addSource(id: string, source: Record<string, unknown>): void;
  addLayer(layer: {
    id: string;
    type: string;
    source: string;
    filter?: unknown[];
    layout?: Record<string, unknown>;
    paint?: Record<string, unknown>;
  }): void;
  getSource(id: string): GeoJSONSourceShim;
  setLayoutProperty(layer: string, property: string, value: unknown): void;
  queryRenderedFeatures(point: { x: number; y: number }, params?: { layers?: string[] }): GeoJSONFeature[];
  easeTo(params: { center: number[]; zoom: number }): void;
  addControl(control: unknown, position: string): void;
  getCanvas(): { style: { cursor: string } };
  remove(): void;
}

// -----------------------------------------------------------------------
// Helper — builds GeoJSON FeatureCollection from antennas
// -----------------------------------------------------------------------

function buildAntennaFeatures(antennas: Antenna[]): {
  type: "FeatureCollection";
  features: GeoJSONFeature[];
} {
  return {
    type: "FeatureCollection",
    features: antennas.map((a) => ({
      type: "Feature",
      geometry: {
        type: "Point",
        coordinates: [a.lng ?? 0, a.lat ?? 0],
      },
      properties: {
        id: a.id,
        name: a.name,
        loadLevel: a.loadLevel,
      },
    })),
  };
}

// -----------------------------------------------------------------------
// Helper — builds GeoJSON FeatureCollection from concentration pins
// -----------------------------------------------------------------------

function buildPinFeatures(pins: ConcentrationPin[]): {
  type: "FeatureCollection";
  features: GeoJSONFeature[];
} {
  return {
    type: "FeatureCollection",
    features: pins.map((p) => ({
      type: "Feature",
      geometry: {
        type: "Point",
        coordinates: [p.lng ?? 0, p.lat ?? 0],
      },
      properties: {
        id: p.id,
        type: p.type,
        intensity: p.intensity ?? 0,
      },
    })),
  };
}

export function MapLibreMap({ antennas, pins, viewport, showAntennas = true, popupStrings }: MapLibreMapProps) {
  console.log("[MapLibreMap] render — antennas:", antennas.length, "pins:", pins.length, "showAntennas:", showAntennas);
  const containerRef = useRef<HTMLDivElement>(null);
  const mapRef = useRef<MapShim | null>(null);
  // Keep refs to the LATEST data so the async "load" handler can read them.
  const showAntennasRef = useRef(showAntennas);
  showAntennasRef.current = showAntennas;
  const antennasRef = useRef(antennas);
  antennasRef.current = antennas;
  const pinsRef = useRef(pins);
  pinsRef.current = pins;

  // ── Phase 1: initialise map once on mount ───────────────────────────
  useEffect(() => {
    let map: MapShim | null = null;
    let cleanupCalled = false;
    let currentPopup: { remove: () => void } | null = null;

    async function initMap() {
      try {
        // Dynamic import — will reject if maplibre-gl is not installed.
        const maplibregl = await import("maplibre-gl");
        await import("maplibre-gl/dist/maplibre-gl.css");

        if (!containerRef.current || cleanupCalled) return;

        map = new maplibregl.Map({
          container: containerRef.current,
          style: "https://basemaps.cartocdn.com/gl/positron-gl-style/style.json",
          center: [viewport.lng, viewport.lat],
          zoom: viewport.zoom,
          attributionControl: false,
        }) as unknown as MapShim;

        mapRef.current = map;

        const navControl = new maplibregl.NavigationControl();
        map.addControl(navControl, "top-right");

        map.on("load", () => {
          if (cleanupCalled || !map) return;
          console.log("[MapLibreMap] map LOAD — antennasRef:", antennasRef.current.length, "pinsRef:", pinsRef.current.length);

          /* ── Antenna markers ──────────────────────────────────── */
          // Use refs so we get the LATEST data even if the load handler
          // fires after data has already been fetched.
          map.addSource("antennas", {
            type: "geojson",
            data: buildAntennaFeatures(antennasRef.current),
          });

          map.addLayer({
            id: "antennas-layer",
            type: "circle",
            source: "antennas",
            paint: {
              "circle-radius": 8,
              "circle-color": [
                "match",
                ["get", "loadLevel"],
                "HIGH",
                LOAD_COLORS.HIGH,
                "MEDIUM",
                LOAD_COLORS.MEDIUM,
                "LOW",
                LOAD_COLORS.LOW,
                "#6B7280",
              ],
              "circle-opacity": 0.8,
              "circle-stroke-width": 2,
              "circle-stroke-color": "#ffffff",
            },
          });

          // Apply initial visibility based on the CURRENT showAntennas value
          if (!showAntennasRef.current) {
            map.setLayoutProperty("antennas-layer", "visibility", "none");
          }

          /* ── Concentration pins ───────────────────────────────── */
          const currentPins = pinsRef.current;
          map.addSource("pins", {
            type: "geojson",
            data: buildPinFeatures(currentPins),
            cluster: currentPins.length >= 100,
            clusterMaxZoom: 14,
            clusterRadius: 50,
          });

          map.addLayer({
            id: "pins-layer",
            type: "circle",
            source: "pins",
            paint: {
              "circle-radius": [
                "interpolate",
                ["linear"],
                ["get", "intensity"],
                0,
                4,
                50,
                10,
                100,
                18,
              ],
              "circle-color": [
                "interpolate",
                ["linear"],
                ["get", "intensity"],
                0,
                "#FEF3C7",
                25,
                "#FCD34D",
                50,
                "#F97316",
                75,
                "#DC2626",
              ],
              "circle-opacity": 0.7,
              "circle-stroke-width": 1,
              "circle-stroke-color": "#ffffff",
            },
          });

          /* ── Pin cluster circles (only rendered when clustering is active) ── */
          map.addLayer({
            id: "pins-cluster-layer",
            type: "circle",
            source: "pins",
            filter: ["has", "point_count"],
            paint: {
              "circle-color": [
                "step",
                ["get", "point_count"],
                "#FCD34D",   // < 10 points
                30, "#F97316",  // < 30 points
                100, "#DC2626", // >= 100 points
              ],
              "circle-radius": [
                "step",
                ["get", "point_count"],
                20,      // < 10 points
                30, 30,  // < 30 points
                100, 40, // >= 100 points
              ],
              "circle-opacity": 0.7,
              "circle-stroke-width": 2,
              "circle-stroke-color": "#ffffff",
            },
          });

          /* ── Cluster count labels ─────────────────────────────── */
          map.addLayer({
            id: "pins-cluster-count",
            type: "symbol",
            source: "pins",
            filter: ["has", "point_count"],
            layout: {
              "text-field": ["get", "point_count"],
              "text-size": 14,
            },
            paint: {
              "text-color": "#ffffff",
            },
          });

          /* ── Click popups ─────────────────────────────────────── */
          map.on(
            "click",
            "antennas-layer",
            (e: MapClickEvent) => {
              if (!e.features?.[0] || !map) return;
              const feat = e.features[0];
              const props = feat.properties;
              const coords = feat.geometry.coordinates.slice() as [number, number];

              // Remove any existing popup before adding a new one
              if (currentPopup) currentPopup.remove();
              const popup = new maplibregl.Popup()
                .setLngLat(coords)
                .setHTML(
                  `<strong>${String(props?.name ?? popupStrings?.antennaLabel ?? "Antena")}</strong><br/>` +
                    `${popupStrings?.loadLabel ?? "Carga"}: ${String(props?.loadLevel ?? "N/A")}`,
                );
              // @ts-ignore — MapShim vs ambient Map type conflict
              popup.addTo(map);
              currentPopup = popup as unknown as { remove: () => void };
            },
          );

          map.on(
            "click",
            "pins-layer",
            (e: MapClickEvent) => {
              if (!e.features?.[0] || !map) return;
              const feat = e.features[0];
              const props = feat.properties;
              const coords = feat.geometry.coordinates.slice() as [number, number];

              // Remove any existing popup before adding a new one
              if (currentPopup) currentPopup.remove();
              const popup = new maplibregl.Popup()
                .setLngLat(coords)
                .setHTML(
                  `<strong>${popupStrings?.concentrationPoint ?? "Punto de concentración"}</strong><br/>` +
                    `${popupStrings?.intensityLabel ?? "Intensidad"}: ${String(props?.intensity ?? "N/A")}`,
                );
              // @ts-ignore — MapShim vs ambient Map type conflict
              popup.addTo(map);
              currentPopup = popup as unknown as { remove: () => void };
            },
          );

          /* ── Cluster click — zoom to cluster ──────────────────── */
          map.on("click", "pins-cluster-layer", (e: MapClickEvent) => {
            if (!e.features?.[0] || !map) return;
            const features = map.queryRenderedFeatures(e.point ?? { x: 0, y: 0 }, {
              layers: ["pins-cluster-layer"],
            });
            const clusterId = features[0]?.properties?.cluster_id as number | undefined;
            if (clusterId == null) return;
            const source = map.getSource("pins");
            source.getClusterExpansionZoom(clusterId, (err, zoom) => {
              if (err || !map) return;
              map.easeTo({
                center: features[0].geometry.coordinates,
                zoom,
              });
            });
          });

          /* ── Hover cursor ─────────────────────────────────────── */
          map.on("mouseenter", "antennas-layer", () => {
            if (map) map.getCanvas().style.cursor = "pointer";
          });
          map.on("mouseleave", "antennas-layer", () => {
            if (map) map.getCanvas().style.cursor = "";
          });
          map.on("mouseenter", "pins-layer", () => {
            if (map) map.getCanvas().style.cursor = "pointer";
          });
          map.on("mouseleave", "pins-layer", () => {
            if (map) map.getCanvas().style.cursor = "";
          });

          /* ── Cluster hover cursor ─────────────────────────────── */
          map.on("mouseenter", "pins-cluster-layer", () => {
            if (map) map.getCanvas().style.cursor = "pointer";
          });
          map.on("mouseleave", "pins-cluster-layer", () => {
            if (map) map.getCanvas().style.cursor = "";
          });
        });
      } catch {
        // maplibre-gl not installed — parent lazy-load handles fallback.
      }
    }

    initMap();

    return () => {
      cleanupCalled = true;
      if (map) {
        map.remove();
        mapRef.current = null;
      }
    };
    // Re-initialise only on mount/unmount.
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // ── Phase 2: update GeoJSON sources when data changes ──────────────
  // Uses the existing map reference — does NOT recreate the map.
  useEffect(() => {
    const map = mapRef.current;
    if (!map) { console.log("[MapLibreMap] Phase2: map not ready yet"); return; }

    try {
      const antennaSource = map.getSource("antennas");
      if (antennaSource) {
        const features = buildAntennaFeatures(antennas);
        console.log("[MapLibreMap] Phase2: updating antennas source with", antennas.length, "items,", features.features.length, "features");
        antennaSource.setData(features);
      } else {
        console.log("[MapLibreMap] Phase2: antenna source not found (map may not have loaded yet)");
      }
    } catch (e) {
      console.warn("[MapLibreMap] Phase2: antenna update error:", e);
    }

    try {
      const pinSource = map.getSource("pins");
      if (pinSource) {
        pinSource.setData(buildPinFeatures(pins));
      }
    } catch {
      // Source not yet added — skip.
    }
  }, [antennas, pins]);

  // ── Phase 3: toggle antenna layer visibility ───────────────────────
  useEffect(() => {
    const map = mapRef.current;
    if (!map) return;

    try {
      map.setLayoutProperty("antennas-layer", "visibility", showAntennas ? "visible" : "none");
    } catch {
      // Layer not yet added — skip.
    }
  }, [showAntennas]);

  return (
    <div
      ref={containerRef}
      style={{ width: "100%", height: "100%", minHeight: "400px" }}
    />
  );
}
