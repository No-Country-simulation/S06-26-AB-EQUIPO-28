// ---------------------------------------------------------------------------
// KeyFindingItem — Displays a single finding with a claim and an inline
// metric badge (bg #EFF6FF, text #1D4ED8, border-radius 6 px).
// ---------------------------------------------------------------------------

import type { AiDataItem } from "@/entities/ai-agent";
import styles from "./KeyFindingItem.module.css";

interface KeyFindingItemProps {
  claim: string;
  metric: AiDataItem;
}

export function KeyFindingItem({ claim, metric }: KeyFindingItemProps) {
  return (
    <div className={styles.item}>
      <p className={styles.claim}>{claim}</p>
      <span className={styles.metricBadge}>
        {metric.value}&nbsp;{metric.source}
      </span>
    </div>
  );
}
