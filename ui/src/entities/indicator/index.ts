// ---------------------------------------------------------------------------
// Indicator entity — public API barrel.
// ---------------------------------------------------------------------------

export type { IndicatorId, Trend, IndicatorMeta, IndicatorValue } from "./model/types.ts";
export type { IndicatorRepository } from "./api/port.ts";
export { toIndicatorValue } from "./api/mapper.ts";
export {
  getIndicatorMeta,
  ALL_INDICATORS,
  getColorForScore,
} from "./lib/indicators-lib.ts";
