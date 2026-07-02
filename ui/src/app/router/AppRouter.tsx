// ---------------------------------------------------------------------------
// AppRouter — React Router v6+ application router.
//
// Defines the top-level route table.  All routes are wrapped in AppLayout
// which provides the persistent navbar shell.  Unknown paths and the root
// ("/") redirect to the map view — the primary interface.
//
// Route structure:
//   /           → redirect → /mapa
//   /mapa       → MapViewPage (map-centric main view)
//   /metodologia → MethodologyPage
//   *           → redirect → /mapa
// ---------------------------------------------------------------------------

import {
  BrowserRouter,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import { MapViewPage } from "@/pages/map-view";
import { MethodologyPage } from "@/pages/methodology";
import { AppLayout } from "@/app/layout";

export function AppRouter() {
  return (
    <BrowserRouter>
      <AppLayout>
        <Routes>
          <Route
            path="/"
            element={<Navigate to="/mapa" replace />}
          />
          <Route path="/mapa" element={<MapViewPage />} />
          <Route path="/metodologia" element={<MethodologyPage />} />
          <Route
            path="*"
            element={<Navigate to="/mapa" replace />}
          />
        </Routes>
      </AppLayout>
    </BrowserRouter>
  );
}
