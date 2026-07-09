import { type ReactNode } from "react";
import { Navbar } from "./Navbar.tsx";

interface AppLayoutProps {
  readonly children: ReactNode;
}

export function AppLayout({ children }: AppLayoutProps) {
  return (
    <div className="flex min-h-screen flex-col">
      <Navbar />
      <main className="mt-16 flex flex-1 flex-col">{children}</main>
    </div>
  );
}
