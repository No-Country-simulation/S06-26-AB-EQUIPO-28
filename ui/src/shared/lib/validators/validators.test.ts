import { describe, it, expect } from "vitest";
import { isNonEmpty, isValidRegionId } from "./validators.ts";

describe("isNonEmpty", () => {
  it("returns true for a non-empty string", () => {
    expect(isNonEmpty("hello")).toBe(true);
  });

  it("returns false for an empty string", () => {
    expect(isNonEmpty("")).toBe(false);
  });

  it("returns false for whitespace-only string", () => {
    expect(isNonEmpty("   ")).toBe(false);
  });
});

describe("isValidRegionId", () => {
  it("returns true for a valid UUID", () => {
    expect(isValidRegionId("550e8400-e29b-41d4-a716-446655440000")).toBe(true);
  });

  it("returns false for an invalid string", () => {
    expect(isValidRegionId("not-a-uuid")).toBe(false);
  });

  it("returns false for an empty string", () => {
    expect(isValidRegionId("")).toBe(false);
  });

  it("returns true for uppercase hex UUID", () => {
    expect(isValidRegionId("550E8400-E29B-41D4-A716-446655440000")).toBe(true);
  });
});
