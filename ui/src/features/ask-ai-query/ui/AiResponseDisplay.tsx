// ---------------------------------------------------------------------------
// AiResponseDisplay — Renders the structured AI response from the
// /api/v1/data/query endpoint.
//
// Sections: summary (markdown), data table, and sources.
// A "Nueva consulta" button clears and starts over.
// ---------------------------------------------------------------------------

import type { AiResponse } from "@/entities/ai-agent";
import { useLanguage } from "@/shared/lib/i18n";
import { MarkdownRenderer } from "@/shared/ui";
import { ResultCard } from "./ResultCard.tsx";
import styles from "./AiResponseDisplay.module.css";

interface AiResponseDisplayProps {
  response: AiResponse;
  onClear: () => void;
}

export function AiResponseDisplay({
  response,
  onClear,
}: AiResponseDisplayProps) {
  const { t } = useLanguage();
  return (
    <div className={styles.container}>
      {/* ── Summary ────────────────────────────────────────────────── */}
      <ResultCard
        title={t("dashboard.summary")}
        icon={
          <svg
            width="20"
            height="20"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
          >
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
            <polyline points="14 2 14 8 20 8" />
            <line x1="16" y1="13" x2="8" y2="13" />
            <line x1="16" y1="17" x2="8" y2="17" />
          </svg>
        }
      >
        <MarkdownRenderer content={response.summary} />
      </ResultCard>

      {/* ── Data table ───────────────────────────────────────────── */}
      {response.data.length > 0 && (
        <ResultCard
          title={t("dashboard.keyFindings")}
          icon={
            <svg
              width="20"
              height="20"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <circle cx="11" cy="11" r="8" />
              <path d="M21 21l-4.35-4.35" />
            </svg>
          }
        >
          <table className={styles.dataTable}>
            <thead>
              <tr>
                <th>{t("map.region")}</th>
                <th>{t("dashboard.vulnerabilityScore")}</th>
              </tr>
            </thead>
            <tbody>
              {response.data.map((item, index) => (
                <tr key={`data-${index}`}>
                  <td>{item.region === "All" ? t("dashboard.allRegions") : item.region}</td>
                  <td>{item.value}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </ResultCard>
      )}

      {/* ── Sources ───────────────────────────────────────────────── */}
      {response.sources.length > 0 && (
        <ResultCard
          title={t("dashboard.sources")}
          icon={
            <svg
              width="20"
              height="20"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20" />
              <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z" />
            </svg>
          }
        >
          <ul className={styles.sourcesList}>
            {response.sources.map((source, index) => (
              <li key={`src-${index}`} className={styles.sourceItem}>
                <span className={styles.sourceTitle}>{source}</span>
              </li>
            ))}
          </ul>
        </ResultCard>
      )}

      {/* ── New query button ──────────────────────────────────────── */}
      <div className={styles.clearButtonWrapper}>
        <button
          type="button"
          className={styles.clearButton}
          onClick={onClear}
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
          >
            <polyline points="1 4 1 10 7 10" />
            <path d="M3.51 15a9 9 0 1 0 2.13-9.36L1 10" />
          </svg>
          {t("dashboard.newQuery")}
        </button>
      </div>
    </div>
  );
}
