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

import { type ReactNode } from "react";
import { LanguageProvider } from "@/shared/lib/i18n";
import { AppContext, type AppContextValue } from "./app-context.ts";
import {
  regionRepository,
  aiAgentRepository,
  mobilityDataRepository,
  indicatorRepository,
  mentalHealthRepository,
  mentorshipRepository,
  employabilityRepository,
} from "./repositories.ts";

const APP_CONTEXT_VALUE: AppContextValue = {
  regionRepository,
  aiAgentRepository,
  mobilityDataRepository,
  indicatorRepository,
  mentalHealthRepository,
  mentorshipRepository,
  employabilityRepository,
};

interface AppProvidersProps {
  readonly children: ReactNode;
}

export function AppProviders({ children }: AppProvidersProps) {
  return (
    <LanguageProvider>
      <AppContext.Provider value={APP_CONTEXT_VALUE}>
        {children}
      </AppContext.Provider>
    </LanguageProvider>
  );
}
