import type { ReactNode } from "react";

interface MapContainerProps {
  children: ReactNode;
  className?: string;
}

export function MapContainer({ children, className }: MapContainerProps) {
  return (
    <div className={`relative h-full min-h-[300px] overflow-hidden rounded-xl ${className ?? ""}`}>
      {children}
    </div>
  );
}
