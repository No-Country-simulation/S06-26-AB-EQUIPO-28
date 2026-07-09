// ---------------------------------------------------------------------------
// AppRouter — React Router v6+ application router.
//
// Defines the top-level route table.  All routes are wrapped in AppLayout
// which provides the persistent navbar shell.  Unknown paths and the root
// ("/") redirect to the panel — the primary interface.
//
// Route structure:
//   /           → redirect → /panel
//   /panel      → PanelDemoPage (unified map + dashboard view)
//   /metodologia → MethodologyPage
//   *           → redirect → /panel
// ---------------------------------------------------------------------------

import {
  BrowserRouter,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import { MethodologyPage } from "@/pages/methodology";
import { PanelDemoPage } from "@/pages/panel";
import { AppLayout } from "@/app/layout";

export function AppRouter() {
  return (
    <BrowserRouter>
      <AppLayout>
        <Routes>
          <Route
            path="/"
            element={<Navigate to="/panel" replace />}
          />
          <Route path="/metodologia" element={<MethodologyPage />} />
          <Route path="/panel" element={<PanelDemoPage />} />
          <Route
            path="*"
            element={<Navigate to="/panel" replace />}
          />
        </Routes>
      </AppLayout>
    </BrowserRouter>
  );
}
