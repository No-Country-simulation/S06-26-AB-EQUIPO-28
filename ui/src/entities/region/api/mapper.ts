import type { MapRegionItem } from "../../../shared/api/index.ts";
import type { Region } from "../model/types.ts";
import { formatRegionName } from "@/shared/lib/formatters/index.ts";

/**
 * Creates a stable string id from a region name.
 * Strips connectivity suffixes (e.g. " - Alto") before normalising,
 * so region IDs never contain the suffix and match antenna cluster values.
 */
function toRegionId(name: string): string {
  return name
    .trim()
    .replace(/\s*[-–—]\s*(Alto|Medio|Bajo|High|Medium|Low)$/i, "")
    .toLowerCase()
    .replace(/\s+/g, "-");
}

/**
 * Converts a backend `MapRegionItem` DTO into a `Region` domain model.
 */
export function toRegion(dto: MapRegionItem): Region {
  return {
    id: toRegionId(dto.name),
    name: formatRegionName(dto.name),
    lat: dto.lat,
    lng: dto.lng,
    concentration: Math.min(100, Math.round(dto.concentration / 10)),
    connectivity: dto.connectivity,
    indicators: {
      antennas: dto.indicators.antennas,
      averageUsers: dto.indicators.averageUsers,
      averageDropPct: dto.indicators.averageDropPct,
      averageCongestion: dto.indicators.averageCongestion,
    },
  };
}
