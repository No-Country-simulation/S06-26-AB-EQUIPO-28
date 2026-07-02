// ---------------------------------------------------------------------------
// MapContainer — A simple wrapper div for the map area.
//
// Sets height, min-height, border-radius, and overflow hidden so both the
// live MapLibre instance and the placeholder share the same visual frame.
// ---------------------------------------------------------------------------

import type { ReactNode } from "react";
import styles from "./MapContainer.module.css";

interface MapContainerProps {
  children: ReactNode;
  className?: string;
}

export function MapContainer({ children, className }: MapContainerProps) {
  return (
    <div className={`${styles.container} ${className ?? ""}`}>
      {children}
    </div>
  );
}
