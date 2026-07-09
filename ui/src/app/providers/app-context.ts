import { createContext, use } from "react";
import type { AiAgentRepository } from "@/entities/ai-agent";
import type { EmployabilityRepository } from "@/entities/employability";
import type { IndicatorRepository } from "@/entities/indicator";
import type { MentalHealthRepository } from "@/entities/mental-health";
import type { MentorshipRepository } from "@/entities/mentorship";
import type { MobilityDataRepository } from "@/entities/mobility-data";
import type { RegionRepository } from "@/entities/region";

export interface AppContextValue {
  readonly regionRepository: RegionRepository;
  readonly aiAgentRepository: AiAgentRepository;
  readonly mobilityDataRepository: MobilityDataRepository;
  readonly indicatorRepository: IndicatorRepository;
  readonly mentalHealthRepository: MentalHealthRepository;
  readonly mentorshipRepository: MentorshipRepository;
  readonly employabilityRepository: EmployabilityRepository;
}

export const AppContext = createContext<AppContextValue | null>(null);

export function useAppContext(): AppContextValue {
  const ctx = use(AppContext);
  if (ctx === null) {
    throw new Error(
      "useAppContext must be used within an <AppProviders> wrapper.",
    );
  }
  return ctx;
}
