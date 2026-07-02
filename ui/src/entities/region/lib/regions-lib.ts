import type { Region } from "../model/types.ts";

/**
 * Filters a region list to only those with connectivity below a threshold.
 * Regions with low connectivity are "vulnerable" in the new model.
 */
export function filterByLowConnectivity(
  regions: readonly Region[],
  threshold: number = 50,
): Region[] {
  return regions.filter((r) => r.connectivity < threshold);
}

/**
 * Returns a new array sorted by `concentration` (descending by default).
 */
export function sortByConcentration(
  regions: readonly Region[],
  order: "asc" | "desc" = "desc",
): Region[] {
  const sorted = [...regions].sort((a, b) => a.concentration - b.concentration);
  return order === "desc" ? sorted.reverse() : sorted;
}

/**
 * Returns regions with the weakest connectivity (bottom N).
 */
export function getWeakestConnectivity(
  regions: readonly Region[],
  count: number = 5,
): Region[] {
  return [...regions]
    .sort((a, b) => a.connectivity - b.connectivity)
    .slice(0, count);
}
