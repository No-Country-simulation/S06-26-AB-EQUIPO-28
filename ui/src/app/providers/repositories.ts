// ---------------------------------------------------------------------------
// Repository adapters (dependency injection wiring).
//
// These adapters implement the entity port interfaces using fetch() against
// the Vite dev proxy (/api/v1/...).
// ---------------------------------------------------------------------------

import type { AiAgentRepository } from "@/entities/ai-agent";
import { toAiResponse, toAskAiRequest } from "@/entities/ai-agent";
import type { IndicatorRepository } from "@/entities/indicator";
import type { MentalHealthRepository } from "@/entities/mental-health";
import {
  toMentalHealthReport,
  toMentalHealthRegionDetail,
} from "@/entities/mental-health";
import type { MobilityDataRepository } from "@/entities/mobility-data";
import {
  toAntennaList,
  toConcentrationData,
} from "@/entities/mobility-data";
import type { RegionRepository } from "@/entities/region";
import { toRegion } from "@/entities/region";
import type {
  DataQueryResponse,
  MapRegionItem,
  AntennaItem,
  ConcentrationItem,
  HealthReportResource,
  VulnerableRegionItem,
} from "@/shared/api";

// ---------------------------------------------------------------------------
// Helper — extract array from paginated or wrapped API responses.
//
// Handles (in order of precedence):
//   field && body[field]           → explicit field lookup (e.g. "regions")
//   body.content                   → Spring Boot Page shape
//   body.items                     → legacy MSW wrapper
//   body.regionSummaries           → health-report response shape
//   [...]                          → Direct array
// ---------------------------------------------------------------------------
function resolveArray<T>(body: unknown, field?: string): T[] {
  if (Array.isArray(body)) return body as T[];
  if (body && typeof body === "object") {
    const obj = body as Record<string, unknown>;
    if (field && Array.isArray(obj[field])) return obj[field] as T[];
    if (Array.isArray(obj.content)) {
      console.log("[resolveArray] found .content with", obj.content.length, "items");
      return obj.content as T[];
    }
    if (Array.isArray(obj.items)) return obj.items as T[];
    if (Array.isArray(obj.regionSummaries)) return obj.regionSummaries as T[];
  }
  console.warn("[repositories] Unexpected API response shape — no array found", JSON.stringify(body).slice(0, 500));
  return [];
}

/**
 * Fetches all pages from a Spring Boot paginated endpoint.
 * Uses size=100 (backend max) and iterates up to maxPages pages.
 * Returns all items concatenated.
 */
async function fetchAllPages<T>(
  baseUrl: string,
  searchParams: URLSearchParams,
  maxPages: number = 20,
  pageSize?: number,
): Promise<T[]> {
  const PAGE_SIZE = String(pageSize ?? 100);
  searchParams.delete("size"); // Avoid duplicates
  searchParams.set("size", PAGE_SIZE);

  let allItems: T[] = [];
  let currentPage = 0;
  let totalPages = 1;

  while (currentPage < totalPages && currentPage < maxPages) {
    const pageParams = new URLSearchParams(searchParams);
    pageParams.set("page", String(currentPage));
    const qs = pageParams.toString();
    const url = `${baseUrl}${qs ? `?${qs}` : ""}`;

    const res = await fetch(url);
    if (res.status === 404) return []; // Endpoint not available — return empty
    if (!res.ok) throw new Error(`Failed to fetch (${res.status})`);

    const body = await res.json();
    const items = resolveArray<T>(body);
    allItems = allItems.concat(items);

    // Check if there are more pages
    if (body && typeof body === "object") {
      const obj = body as Record<string, unknown>;
      if (typeof obj.totalPages === "number") {
        totalPages = obj.totalPages;
      }
    }

    currentPage++;
  }

  return allItems;
}

// ---------------------------------------------------------------------------
// RegionRepository — adapter
// ---------------------------------------------------------------------------

export const regionRepository: RegionRepository = {
  async getRegions() {
    const res = await fetch("/api/v1/map/regions");
    if (res.status === 404) return []; // Not available — return empty
    if (!res.ok)
      throw new Error(`Failed to fetch regions (${res.status})`);

    const body = await res.json();
    const items = resolveArray<MapRegionItem>(body, "regions");
    return items.map(toRegion);
  },
};

// ---------------------------------------------------------------------------
// AiAgentRepository — adapter
// ---------------------------------------------------------------------------

export const aiAgentRepository: AiAgentRepository = {
  async askQuery(query) {
    const requestBody = toAskAiRequest(query);

    const res = await fetch("/api/v1/data/query", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(requestBody),
    });

    if (!res.ok) throw new Error(`AI query failed (${res.status})`);

    const body = (await res.json()) as DataQueryResponse;
    return toAiResponse(body);
  },
};

// ---------------------------------------------------------------------------
// MobilityDataRepository — adapter
// ---------------------------------------------------------------------------

export const mobilityDataRepository: MobilityDataRepository = {
  async getAntennas(params) {
    const searchParams = new URLSearchParams();
    // NOTE: Backend AntennaController only accepts page/size — no cluster filter.
    // Region filtering is done client-side after fetch.

    console.log("[repo] getAntennas: fetching from /api/v1/telemetry/antennas");
    const items = await fetchAllPages<AntennaItem>(
      "/api/v1/telemetry/antennas",
      searchParams,
      7,  // 132 antennas ÷ 20 per page ≈ 7 pages
      20, // antennas rejects size > 20
    );
    console.log("[repo] getAntennas: raw items from API:", items.length);
    if (items.length > 0) {
      console.log("[repo] getAntennas: sample item:", JSON.stringify(items[0]));
    }

    const allAntennas = toAntennaList(items);
    console.log("[repo] getAntennas: mapped to domain:", allAntennas.length);
    if (allAntennas.length > 0) {
      console.log("[repo] getAntennas: sample domain:", JSON.stringify(allAntennas[0]));
    }

    // Client-side region filter (backend doesn't support cluster param)
    if (params?.regionId) {
      const filtered = allAntennas.filter((a) => a.regionId === params.regionId);
      console.log("[repo] getAntennas: filtered by region", params.regionId, "→", filtered.length);
      return filtered;
    }
    return allAntennas;
  },

  async getConcentration(params) {
    const searchParams = new URLSearchParams();
    // Map domain regionId → backend cluster parameter
    const cluster = params?.cluster ?? params?.regionId;
    if (cluster) searchParams.set("cluster", cluster);
    if (params?.startDate) searchParams.set("startDate", params.startDate);
    if (params?.endDate) searchParams.set("endDate", params.endDate);
    if (params?.period) searchParams.set("period", params.period.toUpperCase());

    try {
      // 7920 items total. Fetch 10 pages (1000 items) max to avoid OOM.
      // Enough for meaningful clustering without killing the backend.
      const items = await fetchAllPages<ConcentrationItem>(
        "/api/v1/telemetry/concentration",
        searchParams,
        10,
      );
      return toConcentrationData(items);
    } catch (err) {
      // Endpoint not available — return empty gracefully
      return { metric: "NETWORK_CONCENTRATION", legend: [], pins: [] };
    }
  },
};

// ---------------------------------------------------------------------------
// IndicatorRepository — real adapter for /api/v1/inclusion/health-report
//
// Maps the backend HealthReportResource into 5 IndicatorValues so the
// existing MentalHealthWidget renders real data.  Region filtering is
// supported via regionId → regionName substring matching.
// ---------------------------------------------------------------------------

/** Attempts to match our internal region IDs (REG-NORTH-A, etc.)
 *  to the health-report's regionName (RESIDENCIAL_NORTE, etc.). */
function matchRegion(regionId: string, regionName: string): boolean {
  const normalizedId = regionId
    .replace(/^REG-/, "")
    .replace(/-[A-Z]$/, "")
    .replace(/-/g, "_")
    .toUpperCase();
  return (
    regionName.includes(normalizedId) || normalizedId.includes(regionName)
  );
}

export const indicatorRepository: IndicatorRepository = {
  async getIndicators(regionId: string) {
    try {
      const res = await fetch("/api/v1/inclusion/health-report", {
        signal: AbortSignal.timeout(5000),
      });
      if (!res.ok) return [];
      const body = (await res.json()) as HealthReportResource;

      // If a specific region is selected, find its data
      if (regionId) {
        const match = body.regionSummaries.find((r) =>
          matchRegion(regionId, r.regionName),
        );
        if (match) {
          return [
            { indicatorId: "MENTAL_HEALTH_ACCESS", value: match.vulnerabilityScore, trend: "STABLE" },
            { indicatorId: "TRAINING_COVERAGE", value: Math.round(match.vulnerablePercentage), trend: "STABLE" },
            { indicatorId: "EMPLOYABILITY_GAP", value: match.vulnerabilityScore, trend: "STABLE" },
            { indicatorId: "MENTORSHIP_PROGRAMS", value: Math.max(0, 50 - match.vulnerabilityScore), trend: "STABLE" },
            { indicatorId: "STRUCTURED_EXPERIENCES", value: Math.max(0, match.vulnerabilityScore - 10), trend: "STABLE" },
          ];
        }
      }

      // No region filter → use aggregate metadata
      const meta = body.metadata;
      return [
        { indicatorId: "MENTAL_HEALTH_ACCESS", value: meta.averageVulnerabilityScore, trend: "STABLE" },
        { indicatorId: "TRAINING_COVERAGE", value: Math.round((meta.totalVulnerablePopulation / meta.totalPopulation) * 100), trend: "STABLE" },
        { indicatorId: "EMPLOYABILITY_GAP", value: meta.averageVulnerabilityScore, trend: "STABLE" },
        { indicatorId: "MENTORSHIP_PROGRAMS", value: Math.max(0, 50 - meta.averageVulnerabilityScore), trend: "STABLE" },
        { indicatorId: "STRUCTURED_EXPERIENCES", value: Math.max(0, meta.averageVulnerabilityScore - 10), trend: "STABLE" },
      ];
    } catch {
      return [];
    }
  },
};

// ---------------------------------------------------------------------------
// MentalHealthRepository — adapter for /api/v1/inclusion/health-report
// ---------------------------------------------------------------------------

export const mentalHealthRepository: MentalHealthRepository = {
  async getReport(params) {
    const searchParams = new URLSearchParams();
    if (params?.reportPeriod) searchParams.set("reportPeriod", params.reportPeriod);
    if (params?.includePriorityOnly) searchParams.set("includePriorityOnly", "true");

    const qs = searchParams.toString();
    const url = `/api/v1/inclusion/health-report${qs ? `?${qs}` : ""}`;

    const res = await fetch(url);
    if (res.status === 404) {
      throw new Error("El reporte de salud mental no está disponible en este entorno.");
    }
    if (!res.ok)
      throw new Error(`Error al obtener reporte de salud mental (${res.status})`);

    const body = (await res.json()) as HealthReportResource;
    return toMentalHealthReport(body);
  },

  async getVulnerableRegions(params) {
    const searchParams = new URLSearchParams();
    if (params?.minVulnerabilityScore != null)
      searchParams.set("minVulnerabilityScore", String(params.minVulnerabilityScore));
    if (params?.maxResults != null)
      searchParams.set("maxResults", String(params.maxResults));
    if (params?.poorConnectivityOnly)
      searchParams.set("poorConnectivityOnly", "true");

    const qs = searchParams.toString();
    const url = `/api/v1/inclusion/vulnerable-regions${qs ? `?${qs}` : ""}`;

    const res = await fetch(url);
    if (res.status === 404) return [];
    if (!res.ok)
      throw new Error(`Error al obtener regiones vulnerables (${res.status})`);

    const body = await res.json();
    const items = Array.isArray(body) ? body : (body as { content?: unknown[] }).content ?? [];
    return items.map((item: unknown) => toMentalHealthRegionDetail(item as VulnerableRegionItem));
  },
};
