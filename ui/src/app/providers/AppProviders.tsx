// ---------------------------------------------------------------------------
// AppProviders — React Context provider for dependency injection.
//
// Wires all repository adapters into a single static DI container via React
// Context.  Components access repositories through the `useAppContext()` hook
// rather than importing adapters directly, keeping the rest of the app
// decoupled from infrastructure.
//
// This is the only place where concrete adapter modules are imported.
// The rest of the application talks only to port interfaces.
// ---------------------------------------------------------------------------

import {
  createContext,
  useContext,
  type ReactNode,
} from "react";
import { LanguageProvider } from "@/shared/lib/i18n";
import type { AiAgentRepository } from "@/entities/ai-agent";
import type { IndicatorRepository } from "@/entities/indicator";
import type { MentalHealthRepository } from "@/entities/mental-health";
import type { MobilityDataRepository } from "@/entities/mobility-data";
import type { RegionRepository } from "@/entities/region";
import {
  regionRepository,
  aiAgentRepository,
  mobilityDataRepository,
  indicatorRepository,
  mentalHealthRepository,
} from "./repositories.ts";

// ---------------------------------------------------------------------------
// Context value shape
// ---------------------------------------------------------------------------

export interface AppContextValue {
  readonly regionRepository: RegionRepository;
  readonly aiAgentRepository: AiAgentRepository;
  readonly mobilityDataRepository: MobilityDataRepository;
  readonly indicatorRepository: IndicatorRepository;
  readonly mentalHealthRepository: MentalHealthRepository;
}

const AppContext = createContext<AppContextValue | null>(null);

// ---------------------------------------------------------------------------
// Provider component
// ---------------------------------------------------------------------------

interface AppProvidersProps {
  readonly children: ReactNode;
}

export function AppProviders({ children }: AppProvidersProps) {
  return (
    <LanguageProvider>
      <AppContext.Provider
        value={{
          regionRepository,
          aiAgentRepository,
          mobilityDataRepository,
          indicatorRepository,
          mentalHealthRepository,
        }}
      >
        {children}
      </AppContext.Provider>
    </LanguageProvider>
  );
}

// ---------------------------------------------------------------------------
// Consumer hook
// ---------------------------------------------------------------------------

export function useAppContext(): AppContextValue {
  const ctx = useContext(AppContext);
  if (ctx === null) {
    throw new Error(
      "useAppContext must be used within an <AppProviders> wrapper.",
    );
  }
  return ctx;
}
