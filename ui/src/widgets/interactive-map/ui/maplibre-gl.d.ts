// ---------------------------------------------------------------------------
// Ambient declaration for maplibre-gl.
//
// maplibre-gl is dynamically imported at runtime by MapLibreMap.tsx —
// it may or may not be installed.  This declaration lets TypeScript compile
// the widget even when the package is absent.  When the package IS present,
// its own type declarations take precedence over this ambient shim.
// ---------------------------------------------------------------------------

declare module "maplibre-gl" {
  export class Map {
    constructor(options: {
      container: HTMLElement;
      style: string;
      center: [number, number];
      zoom: number;
      attributionControl?: boolean;
    });
    on(event: string, layerOrHandler: string | ((e: unknown) => void), handler?: (e: unknown) => void): void;
    addSource(id: string, source: Record<string, unknown>): void;
    addLayer(layer: Record<string, unknown>): void;
    addControl(control: unknown, position: string): void;
    getCanvas(): { style: { cursor: string } };
    remove(): void;
  }

  export class NavigationControl {
    constructor();
  }

  export class Popup {
    constructor();
    setLngLat(lnglat: [number, number]): this;
    setHTML(html: string): this;
    addTo(map: Map): this;
  }
}

declare module "maplibre-gl/dist/maplibre-gl.css" {
  const _: string;
  export default _;
}
