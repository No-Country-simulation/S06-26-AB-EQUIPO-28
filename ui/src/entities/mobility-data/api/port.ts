import type { Antenna, ConcentrationPin, ConcentrationLegend } from "../model/types.ts";

export interface MobilityDataRepository {
  getAntennas(params?: {
    regionId?: string;
    page?: number;
    size?: number;
  }): Promise<Antenna[]>;

  getConcentration(params?: {
    regionId?: string;
    cluster?: string;
    startDate?: string;
    endDate?: string;
    period?: string;
    page?: number;
    size?: number;
  }): Promise<{
    metric: string;
    legend: ConcentrationLegend[];
    pins: ConcentrationPin[];
  }>;
}
