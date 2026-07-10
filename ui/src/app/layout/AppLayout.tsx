import { type ReactNode } from "react";
import { useLocation } from "react-router-dom";
import { Navbar } from "./Navbar.tsx";

interface AppLayoutProps {
  readonly children: ReactNode;
}

export function AppLayout({ children }: AppLayoutProps) {
  // The Navbar hides itself on /panel (the page brings its own header), so
  // only offset the fixed 4rem navbar on routes where it actually renders.
  const location = useLocation();
  const hasNavbar = !location.pathname.startsWith("/panel");

  return (
    <div className="flex min-h-screen flex-col">
      <Navbar />
      <main className={`flex flex-1 flex-col ${hasNavbar ? "mt-16" : ""}`}>
        {children}
      </main>
    </div>
  );
}
