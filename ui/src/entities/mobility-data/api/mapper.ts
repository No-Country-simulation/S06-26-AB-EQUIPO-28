import type {
  AntennaItem,
  ConcentrationItem,
} from "../../../shared/api/index.ts";
import type {
  Antenna,
  ConcentrationLegend,
  ConcentrationPin,
} from "../model/types.ts";

/**
 * Normalises a raw backend cluster name to a stable region ID.
 * Strips connectivity suffixes and lowercases with hyphens,
 * matching the format produced by region's toRegionId().
 */
function normaliseRegionId(cluster: string): string {
  return cluster
    .trim()
    .replace(/\s*[-–—]\s*(Alto|Medio|Bajo|High|Medium|Low)$/i, "")
    .toLowerCase()
    .replace(/\s+/g, "-");
}

/**
 * Converts a backend `AntennaItem` DTO into a domain `Antenna` model.
 *
 * Backend shape: { ecgi, cluster, municipality, latitude, longitude }
 * Domain shape:  { id, name, lat, lng, regionId, loadLevel }
 */
export function toAntenna(dto: AntennaItem): Antenna {
  return {
    id: dto.ecgi,
    name: `Antenna ${dto.ecgi}`,
    lat: dto.latitude ?? 0,
    lng: dto.longitude ?? 0,
    regionId: normaliseRegionId(dto.cluster),
    loadLevel: "MEDIUM",
  };
}

/**
 * Converts a backend antenna resource into an array of domain `Antenna` models.
 */
export function toAntennaList(items: readonly AntennaItem[]): Antenna[] {
  return items.map(toAntenna);
}

/**
 * Converts a backend `ConcentrationItem` array into the domain shape.
 *
 * Backend returns raw record-level data.  This mapper derives pins from
 * latitude/longitude coordinates and normalises `userCount` into a 0–100
 * intensity scale.
 */
export function toConcentrationData(
  items: readonly ConcentrationItem[],
): {
  metric: string;
  legend: ConcentrationLegend[];
  pins: ConcentrationPin[];
} {
  // Normalise intensity from userCount
  const userCounts = items
    .map((i) => i.userCount)
    .filter((c): c is number => c != null);
  const maxUserCount =
    userCounts.length > 0 ? Math.max(...userCounts, 1) : 1;

  const pins: ConcentrationPin[] = items.map((item) => ({
    id: item.ecgi,
    type: "HOTSPOT" as const,
    lat: item.latitude ?? 0,
    lng: item.longitude ?? 0,
    intensity: Math.min(
      100,
      Math.round(((item.userCount ?? 0) / maxUserCount) * 100),
    ),
  }));

  return {
    metric: "NETWORK_CONCENTRATION",
    legend: [
      { label: "Baja", min: 0, max: 33, color: "#FEF3C7" },
      { label: "Media", min: 33, max: 66, color: "#F97316" },
      { label: "Alta", min: 66, max: 100, color: "#DC2626" },
    ],
    pins,
  };
}
