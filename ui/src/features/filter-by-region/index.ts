// ---------------------------------------------------------------------------
// Public API — src/features/filter-by-region
//
// The region filter feature allows users to pick a specific geographic zone
// and see an active badge that reflects the current dashboard scope.
// ---------------------------------------------------------------------------

export { RegionSelect } from "./ui/RegionSelect.tsx";
export { ActiveRegionBadge } from "./ui/ActiveRegionBadge.tsx";
export { useRegionFilter } from "./model/useRegionFilter.ts";
