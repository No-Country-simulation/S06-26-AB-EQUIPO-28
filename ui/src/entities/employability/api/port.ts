import type {
  MobilityODPair,
  TravelTime,
  EmployabilityGap,
} from "../model/types.ts";

export interface EmployabilityRepository {
  /** Gets the full origin-destination mobility matrix. */
  getOdMatrix(): Promise<readonly MobilityODPair[]>;

  /** Gets aggregated travel-time metrics between clusters. */
  getTravelTimes(): Promise<readonly TravelTime[]>;

  /** Gets employability gap analysis per cluster. */
  getGaps(): Promise<readonly EmployabilityGap[]>;
}