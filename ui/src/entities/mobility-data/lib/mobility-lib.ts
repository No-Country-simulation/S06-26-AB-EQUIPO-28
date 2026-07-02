import type { Antenna, ConcentrationPin, LoadLevel } from "../model/types.ts";

/**
 * Filters antennas to those matching a given load level.
 */
export function filterByLoadLevel(
  antennas: readonly Antenna[],
  level: LoadLevel,
): Antenna[] {
  return antennas.filter((a) => a.loadLevel === level);
}

/**
 * Returns only concentration pins of type `"HOTSPOT"`.
 */
export function getHotspots(
  pins: readonly ConcentrationPin[],
): ConcentrationPin[] {
  return pins.filter((p) => p.type === "HOTSPOT");
}

/**
 * Maps an intensity value (0-100) to a warm gradient hex colour.
 *
 * Warm palette contrasts with the blue ocean on the map tiles.
 *
 * - 0 – 25  → `"#FEF3C7"` (light amber)
 * - 25 – 50 → `"#FCD34D"` (amber)
 * - 50 – 75 → `"#F97316"` (orange)
 * - 75 – 100 → `"#DC2626"` (red)
 *
 * Values outside [0, 100] are clamped.
 */
export function getPinColor(intensity: number): string {
  const clamped = Math.max(0, Math.min(100, intensity));

  if (clamped < 25) return "#FEF3C7";
  if (clamped < 50) return "#FCD34D";
  if (clamped < 75) return "#F97316";
  return "#DC2626";
}
