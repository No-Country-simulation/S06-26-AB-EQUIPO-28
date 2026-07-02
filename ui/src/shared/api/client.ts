import axios from "axios";

/**
 * Shared Axios client for all backend API calls.
 *
 * baseURL defaults to http://localhost:8089/api/v1.
 * Override via VITE_API_BASE_URL env variable.
 */
export const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api/v1",
  timeout: 15_000,
  headers: {
    "Content-Type": "application/json",
  },
});

// ---------------------------------------------------------------------------
// Request interceptor — extend here for auth headers, tracing, etc.
// ---------------------------------------------------------------------------
apiClient.interceptors.request.use((config) => {
  // Future: attach Bearer token, X-Request-ID, etc.
  return config;
});

// ---------------------------------------------------------------------------
// Response interceptor — normalise errors to a consistent shape.
// ---------------------------------------------------------------------------
interface NormalizedError {
  status: number;
  title: string;
  detail: string;
}

apiClient.interceptors.response.use(
  (response) => response,
  (error: unknown) => {
    if (axios.isAxiosError(error) && error.response) {
      const { status, data } = error.response;
      const normalized: NormalizedError = {
        status,
        title: (data as { title?: string })?.title ?? "Request failed",
        detail:
          (data as { detail?: string })?.detail ??
          error.message ??
          "Unknown error",
      };
      return Promise.reject(normalized);
    }

    // Network / timeout errors
    const fallback: NormalizedError = {
      status: 0,
      title: "Network error",
      detail:
        error instanceof Error ? error.message : "Could not reach the server",
    };
    return Promise.reject(fallback);
  },
);
