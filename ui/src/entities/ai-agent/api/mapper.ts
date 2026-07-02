import type {
  DataQueryRequest,
  DataQueryResponse,
} from "../../../shared/api/index.ts";
import type { AiQuery, AiResponse } from "../model/types.ts";

/**
 * Converts a domain `AiQuery` into a backend `DataQueryRequest` DTO.
 *
 * The new backend expects `{ query, filters: { region, indicator }, language }`.
 */
export function toAskAiRequest(query: AiQuery): DataQueryRequest {
  const filters: { region?: string; indicator?: string } = {};
  if (query.region) filters.region = query.region;
  if (query.indicator) filters.indicator = query.indicator;

  return {
    query: query.question,
    filters: Object.keys(filters).length > 0 ? filters : undefined,
    language: "es",
  };
}

/**
 * Converts a backend `DataQueryResponse` DTO into a domain `AiResponse`.
 *
 * The backend returns `{ aiResponse, data: [...], sources: [...] }`.
 */
export function toAiResponse(dto: DataQueryResponse): AiResponse {
  return {
    summary: dto.aiResponse,
    data: dto.data.map((item) => ({
      region: item.region,
      value: item.value,
      source: item.source,
    })),
    sources: dto.sources,
  };
}
