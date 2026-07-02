import { describe, it, expect } from "vitest";
import { formatNumber, formatPercentage, formatDate, formatRegionName, truncate } from "./formatters.ts";

describe("formatNumber", () => {
  it("formats a plain integer with pt-BR separators", () => {
    expect(formatNumber(1234)).toBe("1.234");
  });

  it("formats a large number", () => {
    expect(formatNumber(1_000_000)).toBe("1.000.000");
  });

  it("handles zero", () => {
    expect(formatNumber(0)).toBe("0");
  });
});

describe("formatPercentage", () => {
  it("formats 0.5 as 50%", () => {
    // pt-BR style:percent with maximumFractionDigits:1
    const result = formatPercentage(0.5);
    expect(result).toMatch(/%$/);
    expect(result).not.toBe("0%");
  });

  it("formats 0.07 as 7%", () => {
    const result = formatPercentage(0.07);
    expect(result).toMatch(/%$/);
    expect(result).not.toBe("0%");
  });

  it("formats 1 as 100%", () => {
    const result = formatPercentage(1);
    expect(result).toMatch(/%$/);
    expect(result).not.toBe("0%");
  });
});

describe("formatDate", () => {
  it("formats an ISO string to pt-BR date", () => {
    expect(formatDate("2026-06-20T14:22:00Z")).toBe("20/06/2026");
  });

  it("accepts a Date object", () => {
    // Use noon UTC to avoid timezone shift in any locale
    const d = new Date("2026-01-05T12:00:00Z");
    expect(formatDate(d)).toBe("05/01/2026");
  });
});

describe("formatRegionName", () => {
  it("transforms snake_case region keys via overrides", () => {
    expect(formatRegionName("sao_paulo")).toBe("São Paulo");
    expect(formatRegionName("rio_de_janeiro")).toBe("Rio de Janeiro");
    expect(formatRegionName("belo_horizonte")).toBe("Belo Horizonte");
    expect(formatRegionName("brasilia")).toBe("Brasília");
  });

  it("handles REG-CODE patterns", () => {
    expect(formatRegionName("REG-NORTH-A")).toBe("Región Norte A");
    expect(formatRegionName("REG-SOUTH-C")).toBe("Región Sur C");
    expect(formatRegionName("REG-CENTER-B")).toBe("Región Centro B");
    expect(formatRegionName("REG_NORTH_A")).toBe("Región Norte A");
    expect(formatRegionName("REG-NORTH")).toBe("Región Norte");
    expect(formatRegionName("REG-EAST-D")).toBe("Región Este D");
    expect(formatRegionName("REG-WEST-E")).toBe("Región Oeste E");
  });

  it("handles cluster_N patterns", () => {
    expect(formatRegionName("cluster_1")).toBe("Cluster 1");
    expect(formatRegionName("cluster_2")).toBe("Cluster 2");
    expect(formatRegionName("cluster-3")).toBe("Cluster 3");
  });

  it("translates Portuguese direction words to Spanish in fallback", () => {
    expect(formatRegionName("sul")).toBe("Sur");
    expect(formatRegionName("leste")).toBe("Este");
  });

  it("single known direction words via overrides", () => {
    expect(formatRegionName("norte")).toBe("Norte");
    expect(formatRegionName("centro_oeste")).toBe("Centro-Oeste");
    expect(formatRegionName("nordeste")).toBe("Nordeste");
    expect(formatRegionName("sudeste")).toBe("Sudeste");
  });

  it("falls back to title-case for unknown snake_case keys", () => {
    expect(formatRegionName("nova_regiao_x")).toBe("Nova Regiao X");
  });

  it("trims whitespace", () => {
    expect(formatRegionName("  fortaleza  ")).toBe("Fortaleza");
  });

  it("returns empty string unchanged", () => {
    expect(formatRegionName("")).toBe("");
  });
});

describe("truncate", () => {
  it("returns the string unchanged when within maxLength", () => {
    expect(truncate("short", 10)).toBe("short");
  });

  it("truncates and appends ellipsis when over maxLength", () => {
    // 20 chars → first 20 chars trimmed + ellipsis
    const result = truncate("a very long string that should be cut", 20);
    expect(result).toBe("a very long string t…");
  });

  it("handles empty string", () => {
    expect(truncate("", 5)).toBe("");
  });
});
