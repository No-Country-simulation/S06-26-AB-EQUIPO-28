import type { Region } from "../model/types.ts";

export interface RegionRepository {
  getRegions(): Promise<Region[]>;
}
