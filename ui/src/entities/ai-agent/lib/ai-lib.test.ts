import { describe, it, expect } from "vitest";
import { hasData, getDataByRegion } from "./ai-lib.ts";
import type { AiResponse } from "../model/types.ts";

const baseResponse: AiResponse = {
  summary: "summary text",
  data: [],
  sources: [],
};

describe("hasData", () => {
  it("returns false when data array is empty", () => {
    expect(hasData(baseResponse)).toBe(false);
  });

  it("returns true when data items exist", () => {
    const response: AiResponse = {
      ...baseResponse,
      data: [{ region: "NORTE", value: 50, source: "test" }],
    };
    expect(hasData(response)).toBe(true);
  });
});

describe("getDataByRegion", () => {
  const response: AiResponse = {
    ...baseResponse,
    data: [
      { region: "NORTE", value: 80, source: "mock" },
      { region: "SUR", value: 30, source: "mock" },
    ],
  };

  it("finds a data item by region", () => {
    const item = getDataByRegion(response, "NORTE");
    expect(item).toBeDefined();
    expect(item?.value).toBe(80);
  });

  it("returns undefined when region not found", () => {
    const item = getDataByRegion(response, "UNKNOWN");
    expect(item).toBeUndefined();
  });

  it("is case-sensitive", () => {
    const item = getDataByRegion(response, "norte");
    expect(item).toBeUndefined();
  });
});
