import { describe, it, expect } from "vitest";
import { filterByLoadLevel, getHotspots, getPinColor } from "./mobility-lib.ts";
import type { Antenna, ConcentrationPin } from "../model/types.ts";

function makeAntenna(overrides: Partial<Antenna> & { id: string }): Antenna {
  return {
    name: "Antenna",
    lat: 0,
    lng: 0,
    regionId: "r1",
    loadLevel: "MEDIUM",
    ...overrides,
  };
}

describe("filterByLoadLevel", () => {
  const antennas: readonly Antenna[] = [
    makeAntenna({ id: "a1", loadLevel: "HIGH" }),
    makeAntenna({ id: "a2", loadLevel: "MEDIUM" }),
    makeAntenna({ id: "a3", loadLevel: "HIGH" }),
    makeAntenna({ id: "a4", loadLevel: "LOW" }),
  ];

  it("filters by HIGH load level", () => {
    const result = filterByLoadLevel(antennas, "HIGH");
    expect(result).toHaveLength(2);
    expect(result.map((a) => a.id)).toEqual(["a1", "a3"]);
  });

  it("filters by LOW load level", () => {
    const result = filterByLoadLevel(antennas, "LOW");
    expect(result).toHaveLength(1);
    expect(result[0].id).toBe("a4");
  });

  it("returns empty when no match", () => {
    const result = filterByLoadLevel([], "HIGH");
    expect(result).toHaveLength(0);
  });
});

describe("getHotspots", () => {
  const pins: readonly ConcentrationPin[] = [
    { id: "p1", type: "HOTSPOT", lat: 0, lng: 0, intensity: 80 },
    { id: "p2", type: "COVERAGE_GAP", lat: 0, lng: 0, intensity: 60 },
    { id: "p3", type: "HOTSPOT", lat: 0, lng: 0, intensity: 90 },
    { id: "p4", type: "ANTENNA", lat: 0, lng: 0, intensity: 30 },
  ];

  it("returns only HOTSPOT pins", () => {
    const result = getHotspots(pins);
    expect(result).toHaveLength(2);
    expect(result.map((p) => p.id)).toEqual(["p1", "p3"]);
  });

  it("returns empty array when no hotspots", () => {
    const result = getHotspots([]);
    expect(result).toHaveLength(0);
  });
});

describe("getPinColor", () => {
  it("returns light amber for intensity < 25", () => {
    expect(getPinColor(0)).toBe("#FEF3C7");
    expect(getPinColor(24)).toBe("#FEF3C7");
  });

  it("returns amber for 25-49", () => {
    expect(getPinColor(25)).toBe("#FCD34D");
    expect(getPinColor(49)).toBe("#FCD34D");
  });

  it("returns orange for 50-74", () => {
    expect(getPinColor(50)).toBe("#F97316");
    expect(getPinColor(74)).toBe("#F97316");
  });

  it("returns red for 75-100", () => {
    expect(getPinColor(75)).toBe("#DC2626");
    expect(getPinColor(100)).toBe("#DC2626");
  });

  it("clamps values outside [0, 100]", () => {
    expect(getPinColor(-10)).toBe("#FEF3C7");
    expect(getPinColor(150)).toBe("#DC2626");
  });
});
