// ---------------------------------------------------------------------------
// App — Root application component.
//
// Mounts the dependency-injection providers and the router.
// This is the single entry point consumed by main.tsx.
// ---------------------------------------------------------------------------

import { AppProviders } from "./providers";
import { AppRouter } from "./router";

export function App() {
  return (
    <AppProviders>
      <AppRouter />
    </AppProviders>
  );
}
