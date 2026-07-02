// ---------------------------------------------------------------------------
// AiQueryDashboard — Composed AI query widget.
//
// Manages the full user flow: empty state → input → loading spinner →
// structured response (or error with retry).
// ---------------------------------------------------------------------------

import type { AiAgentRepository } from "@/entities/ai-agent";
import { Spinner } from "@/shared/ui";
import { useLanguage } from "@/shared/lib/i18n";
import { useAskAi } from "../model/useAskAi.ts";
import { AiSearchInput } from "./AiSearchInput.tsx";
import { AiResponseDisplay } from "./AiResponseDisplay.tsx";
import styles from "./AiQueryDashboard.module.css";

interface AiQueryDashboardProps {
  repository: AiAgentRepository;
  region?: string | null;
  indicator?: string | null;
}

export function AiQueryDashboard({
  repository,
  region,
  indicator,
}: AiQueryDashboardProps) {
  const { t } = useLanguage();
  const {
    query,
    setQuery,
    submit,
    response,
    isLoading,
    error,
    clearResponse,
  } = useAskAi(repository, { region, indicator });

  return (
    <div className={styles.dashboard}>
      <AiSearchInput
        value={query}
        onChange={setQuery}
        onSubmit={submit}
        disabled={isLoading}
      />

      {/* ── Loading state ──────────────────────────────────────────── */}
      {isLoading && (
        <div className={styles.loadingContainer}>
          <Spinner size="lg" />
          <p className={styles.loadingText}>{t("dashboard.loading")}</p>
        </div>
      )}

      {/* ── Error state ────────────────────────────────────────────── */}
      {error && !isLoading && (
        <div className={styles.errorCard} role="alert">
          <div className={styles.errorHeader}>
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
              <circle cx="12" cy="12" r="10" />
              <line x1="15" y1="9" x2="9" y2="15" />
              <line x1="9" y1="9" x2="15" y2="15" />
            </svg>
            <span className={styles.errorTitle}>{t("common.error")}</span>
          </div>
          <p className={styles.errorMessage}>{error}</p>
          <button
            type="button"
            className={styles.retryButton}
            onClick={submit}
          >
            {t("dashboard.retry")}
          </button>
        </div>
      )}

      {/* ── Response state ─────────────────────────────────────────── */}
      {response && !isLoading && (
        <AiResponseDisplay response={response} onClear={clearResponse} />
      )}
    </div>
  );
}
