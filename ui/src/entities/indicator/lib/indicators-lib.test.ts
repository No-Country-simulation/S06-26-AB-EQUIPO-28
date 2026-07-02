import { describe, it, expect } from "vitest";
import { getIndicatorMeta, getColorForScore } from "./indicators-lib.ts";

describe("getIndicatorMeta", () => {
  it("returns metadata for EMPLOYABILITY_GAP", () => {
    const meta = getIndicatorMeta("EMPLOYABILITY_GAP");
    expect(meta.label).toBe("Brecha de Empleabilidad");
    expect(meta.color).toBe("#B91C1C");
    expect(meta.icon).toBe("briefcase");
  });

  it("returns metadata for MENTAL_HEALTH_ACCESS", () => {
    const meta = getIndicatorMeta("MENTAL_HEALTH_ACCESS");
    expect(meta.label).toBe("Acceso a Salud Mental");
    expect(meta.color).toBe("#1D4ED8");
  });

  it("returns metadata for all known indicator IDs", () => {
    const ids = [
      "EMPLOYABILITY_GAP",
      "TRAINING_COVERAGE",
      "MENTAL_HEALTH_ACCESS",
      "MENTORSHIP_PROGRAMS",
      "STRUCTURED_EXPERIENCES",
    ] as const;

    for (const id of ids) {
      const meta = getIndicatorMeta(id);
      expect(meta.id).toBe(id);
      expect(meta.label).toBeTruthy();
      expect(meta.color).toBeTruthy();
    }
  });
});

describe("getColorForScore", () => {
  it("returns red for scores below 0.33", () => {
    expect(getColorForScore(0)).toBe("#B91C1C");
    expect(getColorForScore(0.15)).toBe("#B91C1C");
    expect(getColorForScore(0.32)).toBe("#B91C1C");
  });

  it("returns yellow for scores between 0.33 and 0.66", () => {
    expect(getColorForScore(0.33)).toBe("#92400E");
    expect(getColorForScore(0.5)).toBe("#92400E");
    expect(getColorForScore(0.65)).toBe("#92400E");
  });

  it("returns green for scores above 0.66", () => {
    expect(getColorForScore(0.66)).toBe("#166534");
    expect(getColorForScore(0.8)).toBe("#166534");
    expect(getColorForScore(1)).toBe("#166534");
  });

  it("clamps values outside [0, 1]", () => {
    expect(getColorForScore(-0.5)).toBe("#B91C1C");
    expect(getColorForScore(1.5)).toBe("#166534");
  });
});
