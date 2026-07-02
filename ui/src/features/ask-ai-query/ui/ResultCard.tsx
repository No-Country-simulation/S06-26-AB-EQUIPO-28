// ---------------------------------------------------------------------------
// ResultCard — Generic card with a coloured left border for structured
// result sections (summary, findings, actions, sources).
// ---------------------------------------------------------------------------

import type { ReactNode } from "react";
import styles from "./ResultCard.module.css";

interface ResultCardProps {
  title: string;
  children: ReactNode;
  icon?: ReactNode;
}

export function ResultCard({ title, children, icon }: ResultCardProps) {
  return (
    <div className={styles.card}>
      <div className={styles.header}>
        {icon && <span className={styles.icon}>{icon}</span>}
        <h3 className={styles.title}>{title}</h3>
      </div>
      <div className={styles.body}>{children}</div>
    </div>
  );
}
