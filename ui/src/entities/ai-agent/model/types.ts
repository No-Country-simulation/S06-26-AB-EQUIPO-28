// ---------------------------------------------------------------------------
// AI Agent entity — immutable domain types for conversational analysis.
// ---------------------------------------------------------------------------

export interface AiQuery {
  readonly question: string;
  readonly indicator?: string;
  readonly region?: string;
  /** ISO language code ("es" | "pt" | "en") the answer should be written in. */
  readonly language?: string;
}

export interface AiDataItem {
  readonly region: string;
  readonly value: number | string;
  readonly source: string;
}

export interface AiResponse {
  readonly summary: string;
  readonly data: readonly AiDataItem[];
  readonly sources: readonly string[];
}
