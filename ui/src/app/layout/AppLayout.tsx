// ---------------------------------------------------------------------------
// AppLayout — Application shell with persistent navbar.
//
// Wraps every routed page in a flex column that fills the full viewport.
// The Navbar is fixed at top, and the main content area sits below it
// (64 px offset for the fixed nav).
// ---------------------------------------------------------------------------

import { type ReactNode } from "react";
import { Navbar } from "./Navbar.tsx";
import styles from "./AppLayout.module.css";

interface AppLayoutProps {
  readonly children: ReactNode;
}

export function AppLayout({ children }: AppLayoutProps) {
  return (
    <div className={styles.layout}>
      <Navbar />
      <main className={styles.content}>{children}</main>
    </div>
  );
}
