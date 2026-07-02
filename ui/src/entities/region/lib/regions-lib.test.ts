import { describe, it, expect } from "vitest";
import { filterByLowConnectivity, sortByConcentration, getWeakestConnectivity } from "./regions-lib.ts";
import type { Region } from "../model/types.ts";

function makeRegion(overrides: Partial<Region> & { id: string }): Region {
  return {
    name: "Test",
    lat: 0,
    lng: 0,
    concentration: 50,
    connectivity: 50,
    indicators: { antennas: 0, averageUsers: 0, averageDropPct: 0, averageCongestion: 0 },
    ...overrides,
  };
}

describe("filterByLowConnectivity", () => {
  const regions: readonly Region[] = [
    makeRegion({ id: "r1", connectivity: 30 }),
    makeRegion({ id: "r2", connectivity: 60 }),
    makeRegion({ id: "r3", connectivity: 20 }),
    makeRegion({ id: "r4", connectivity: 80 }),
  ];

  it("filters regions below default threshold (50)", () => {
    const result = filterByLowConnectivity(regions);
    expect(result).toHaveLength(2);
    expect(result.map((r) => r.id)).toEqual(["r1", "r3"]);
  });

  it("filters regions below custom threshold", () => {
    const result = filterByLowConnectivity(regions, 25);
    expect(result).toHaveLength(1);
    expect(result[0].id).toBe("r3");
  });

  it("returns empty array when no match", () => {
    const result = filterByLowConnectivity(regions, 10);
    expect(result).toHaveLength(0);
  });
});

describe("sortByConcentration", () => {
  const regions: readonly Region[] = [
    makeRegion({ id: "r1", concentration: 30 }),
    makeRegion({ id: "r2", concentration: 80 }),
    makeRegion({ id: "r3", concentration: 50 }),
  ];

  it("sorts descending by default", () => {
    const result = sortByConcentration(regions);
    expect(result.map((r) => r.id)).toEqual(["r2", "r3", "r1"]);
  });

  it("sorts ascending when specified", () => {
    const result = sortByConcentration(regions, "asc");
    expect(result.map((r) => r.id)).toEqual(["r1", "r3", "r2"]);
  });

  it("does not mutate the original array", () => {
    const original = [...regions];
    sortByConcentration(regions);
    expect(regions.map((r) => r.id)).toEqual(original.map((r) => r.id));
  });
});

describe("getWeakestConnectivity", () => {
  const regions: readonly Region[] = [
    makeRegion({ id: "r1", connectivity: 80 }),
    makeRegion({ id: "r2", connectivity: 20 }),
    makeRegion({ id: "r3", connectivity: 10 }),
    makeRegion({ id: "r4", connectivity: 50 }),
    makeRegion({ id: "r5", connectivity: 5 }),
  ];

  it("returns bottom 5 by default", () => {
    const result = getWeakestConnectivity(regions);
    expect(result).toHaveLength(5);
    expect(result.map((r) => r.id)).toEqual(["r5", "r3", "r2", "r4", "r1"]);
  });

  it("returns bottom N when count is specified", () => {
    const result = getWeakestConnectivity(regions, 2);
    expect(result).toHaveLength(2);
    expect(result.map((r) => r.id)).toEqual(["r5", "r3"]);
  });

  it("does not mutate the original array", () => {
    const original = [...regions];
    getWeakestConnectivity(regions);
    expect(regions.map((r) => r.id)).toEqual(original.map((r) => r.id));
  });
});
