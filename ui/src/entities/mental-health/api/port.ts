import type { MentalHealthReport, MentalHealthRegionDetail } from "../model/types.ts";

export interface MentalHealthRepository {
  /** Gets the full mental health report with metadata */
  getReport(params?: {
    reportPeriod?: string;
    includePriorityOnly?: boolean;
  }): Promise<MentalHealthReport>;

  /** Gets detailed vulnerable regions list */
  getVulnerableRegions(params?: {
    minVulnerabilityScore?: number;
    maxResults?: number;
    poorConnectivityOnly?: boolean;
  }): Promise<MentalHealthRegionDetail[]>;
}
