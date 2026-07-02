// ---------------------------------------------------------------------------
// Mobility Data entity — immutable types for Vísent CDRView data.
// ---------------------------------------------------------------------------

export type LoadLevel = "HIGH" | "MEDIUM" | "LOW";

export type PinType = "HOTSPOT" | "COVERAGE_GAP" | "ANTENNA";

export interface Antenna {
  readonly id: string;
  readonly name: string;
  readonly lat: number;
  readonly lng: number;
  readonly regionId: string;
  readonly loadLevel: LoadLevel;
}

export interface ConcentrationPin {
  readonly id: string;
  readonly type: PinType;
  readonly lat: number;
  readonly lng: number;
  readonly intensity: number; // 0-100
}

export interface ConcentrationLegend {
  readonly label: string;
  readonly min: number;
  readonly max: number;
  readonly color: string;
}
