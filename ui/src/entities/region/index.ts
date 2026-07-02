// ---------------------------------------------------------------------------
// Region entity — public API barrel.
// ---------------------------------------------------------------------------

export type { Region, RegionIndicators } from "./model/types.ts";
export type { RegionRepository } from "./api/port.ts";
export { toRegion } from "./api/mapper.ts";
