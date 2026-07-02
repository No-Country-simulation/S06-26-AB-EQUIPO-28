// ---------------------------------------------------------------------------
// Region entity — immutable domain types for geographic zones.
// ---------------------------------------------------------------------------

export interface RegionIndicators {
  readonly antennas: number;
  readonly averageUsers: number;
  readonly averageDropPct: number;
  readonly averageCongestion: number;
}

export interface Region {
  readonly id: string;
  readonly name: string;
  readonly lat: number;
  readonly lng: number;
  readonly concentration: number;
  readonly connectivity: number;
  readonly indicators: RegionIndicators;
}
