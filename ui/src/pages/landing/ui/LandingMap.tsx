import { useEffect, useRef, useState } from "react";
import maplibregl, { Map } from "maplibre-gl";
import "maplibre-gl/dist/maplibre-gl.css";

type LngLatLike = [number, number] | { lng: number; lat: number };

interface LandingMapProps {
  center?: LngLatLike;
  zoom?: number;
  initialView?: { center: LngLatLike; zoom: number };
  className?: string;
  style?: React.CSSProperties;
  interactive?: boolean;
}

const DEFAULT_CENTER: [number, number] = [-58.3816, -34.6037];
const DEFAULT_ZOOM = 10;

function toCoords(ll: LngLatLike): [number, number] {
  return Array.isArray(ll) ? ll : [ll.lng, ll.lat];
}

export function LandingMap({
  center = DEFAULT_CENTER,
  zoom = DEFAULT_ZOOM,
  initialView,
  className = "",
  style,
  interactive = false,
}: LandingMapProps) {
  const containerRef = useRef<HTMLDivElement>(null);
  const mapRef = useRef<Map | null>(null);
  const [mapError, setMapError] = useState(false);

  const view = initialView ?? { center, zoom };

  useEffect(() => {
    if (!containerRef.current || mapRef.current) return;

    let cancelled = false;
    let map: Map | null = null;

    try {
      map = new maplibregl.Map({
        container: containerRef.current,
        style: "https://demotiles.maplibre.org/style.json",
        center: toCoords(view.center),
        zoom: view.zoom,
        attributionControl: false,
      });

      const AttributionControl = (maplibregl as any).AttributionControl;
      if (AttributionControl) {
        map.addControl(new AttributionControl({ compact: true }), "bottom-right");
      }

      map.on("load", () => {
        if (cancelled || !map) return;

        const coords = toCoords(view.center);

        map.addSource("center-point", {
          type: "geojson",
          data: {
            type: "Feature",
            geometry: { type: "Point", coordinates: coords },
            properties: {},
          },
        });

        map.addLayer({
          id: "center-pulse",
          type: "circle",
          source: "center-point",
          paint: {
            "circle-radius": 16,
            "circle-color": "#0891b2",
            "circle-opacity": 0.2,
          },
        });

        map.addLayer({
          id: "center-point-layer",
          type: "circle",
          source: "center-point",
          paint: {
            "circle-radius": 6,
            "circle-color": "#0891b2",
            "circle-stroke-width": 2,
            "circle-stroke-color": "#ffffff",
          },
        });

        if (!interactive) {
          (map as any).dragRotate?.disable();
          (map as any).touchZoomRotate?.disableRotation?.();
        }
      });

      map.on("error", () => {
        if (!cancelled) setMapError(true);
      });

      mapRef.current = map;
    } catch {
      if (!cancelled) setMapError(true);
    }

    return () => {
      cancelled = true;
      map?.remove();
      mapRef.current = null;
    };
  }, [view.center, view.zoom, interactive]);

  if (mapError) {
    return (
      <div
        ref={containerRef}
        className={`relative rounded-2xl bg-muted/50 border border-border/50 flex items-center justify-center ${className}`}
        style={{ ...style, width: "100%", height: "100%" }}
        role="img"
        aria-label="Error cargando el mapa"
      >
        <div className="text-center p-6 text-muted-foreground">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" className="mx-auto mb-3 opacity-50">
            <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" />
            <circle cx="12" cy="10" r="3" />
          </svg>
          <p className="text-sm">No se pudo cargar el mapa</p>
        </div>
      </div>
    );
  }

  return (
    <div
      ref={containerRef}
      className={`relative rounded-2xl overflow-hidden bg-muted/30 ${className}`}
      style={{ ...style, width: "100%", height: "100%" }}
      role="img"
      aria-label={interactive ? "Mapa interactivo del territorio" : "Vista previa del mapa del territorio"}
    />
  );
}