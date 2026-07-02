import type { IndicatorValue } from "../model/types.ts";

export interface IndicatorRepository {
  getIndicators(regionId: string): Promise<IndicatorValue[]>;
}
