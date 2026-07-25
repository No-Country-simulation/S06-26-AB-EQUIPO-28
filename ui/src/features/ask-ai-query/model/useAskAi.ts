// ---------------------------------------------------------------------------
// useAskAi — Orchestration hook for the AI query feature.
//
// Takes an AiAgentRepository port and manages the full lifecycle of a
// conversational query: text input → POST → structured response.
// Uses plain useState/useCallback (no TanStack Query, no react-router).
// ---------------------------------------------------------------------------

import { useState, useCallback, useRef, useEffect } from "react";
import type { AiAgentRepository, AiResponse } from "@/entities/ai-agent";

export function useAskAi(
  repository: AiAgentRepository,
  options?: {
    region?: string | null;
    indicator?: string | null;
    /** Language code the AI should answer in (defaults to "es" at the adapter). */
    language?: string;
    /** Localized message shown when the request fails without a message. */
    errorFallback?: string;
    /** Request timeout in ms (default 90000). */
    timeoutMs?: number;
  },
) {
  const [query, setQuery] = useState(() => {
    try {
      return sessionStorage.getItem("ai-query") ?? "";
    } catch {
      return "";
    }
  });
  const [response, setResponse] = useState<AiResponse | null>(() => {
    try {
      const saved = sessionStorage.getItem("ai-response");
      return saved ? JSON.parse(saved) : null;
    } catch {
      return null;
    }
  });
  const [lastQuestion, setLastQuestion] = useState<string>(() => {
    try {
      return sessionStorage.getItem("ai-last-question") ?? "";
    } catch {
      return "";
    }
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const isLoadingRef = useRef(false);
  const abortRef = useRef<AbortController | null>(null);

  // Cleanup on unmount — abort any in-flight request
  useEffect(() => {
    return () => {
      abortRef.current?.abort();
    };
  }, []);

  const submit = useCallback(async () => {
    const trimmed = query.trim();
    if (!trimmed) return;
    if (isLoadingRef.current) return; // prevent double-submit while in-flight

    // Abort previous request if still in-flight
    abortRef.current?.abort();
    const abortController = new AbortController();
    abortRef.current = abortController;

    isLoadingRef.current = true;
    setIsLoading(true);
    setError(null);

    // Timeout
    const timeoutMs = options?.timeoutMs ?? 90000;
    const timeoutId = setTimeout(() => abortController.abort(), timeoutMs);

    try {
      const result = await repository.askQuery(
        {
          question: trimmed,
          indicator: options?.indicator ?? undefined,
          region: options?.region ?? undefined,
          language: options?.language,
        },
        abortController.signal,
      );

      if (abortController.signal.aborted) return; // already aborted, disregard

      setResponse(result);
      setLastQuestion(trimmed);
      setQuery("");
    } catch (err) {
      if (abortController.signal.aborted) {
        // Don't set error for aborted/timeout requests — the user cancelled
        return;
      }
      const message =
        err instanceof Error
          ? err.message
          : (options?.errorFallback ?? "Ocurrió un error al procesar la consulta.");
      setError(message);
    } finally {
      clearTimeout(timeoutId);
      if (abortRef.current === abortController) {
        abortRef.current = null;
      }
      setIsLoading(false);
      isLoadingRef.current = false;
    }
  }, [query, repository, options?.region, options?.indicator, options?.language, options?.errorFallback, options?.timeoutMs]);

  const cancel = useCallback(() => {
    abortRef.current?.abort();
  }, []);

  const clearResponse = useCallback(() => {
    setResponse(null);
    setError(null);
    setLastQuestion("");
    try {
      sessionStorage.removeItem("ai-response");
      sessionStorage.removeItem("ai-query");
      sessionStorage.removeItem("ai-last-question");
    } catch {
      // sessionStorage unavailable — ignore
    }
  }, []);

  // Persist response to sessionStorage
  useEffect(() => {
    try {
      if (response) {
        sessionStorage.setItem("ai-response", JSON.stringify(response));
      } else {
        sessionStorage.removeItem("ai-response");
      }
    } catch {
      // sessionStorage unavailable — ignore
    }
  }, [response]);

  // Persist the question that produced the current response
  useEffect(() => {
    try {
      if (lastQuestion) {
        sessionStorage.setItem("ai-last-question", lastQuestion);
      } else {
        sessionStorage.removeItem("ai-last-question");
      }
    } catch {
      // sessionStorage unavailable — ignore
    }
  }, [lastQuestion]);

  // Persist query text to sessionStorage
  useEffect(() => {
    try {
      if (query) {
        sessionStorage.setItem("ai-query", query);
      } else {
        sessionStorage.removeItem("ai-query");
      }
    } catch {
      // sessionStorage unavailable — ignore
    }
  }, [query]);

  return {
    query,
    setQuery,
    submit,
    cancel,
    response,
    lastQuestion,
    isLoading,
    error,
    clearResponse,
  } as const;
}
