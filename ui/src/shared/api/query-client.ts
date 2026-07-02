import { QueryClient } from "@tanstack/react-query";

/**
 * Shared QueryClient for the whole application.
 *
 * - staleTime 30s: data is considered fresh for 30 s after fetch.
 * - retry 1: single retry on failure (hackathon pace — fast feedback).
 * - refetchOnWindowFocus false: avoids surprise re-fetches during demos.
 */
export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 30_000,
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
});
