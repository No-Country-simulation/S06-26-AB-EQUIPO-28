import type { AiQuery, AiResponse } from "../model/types.ts";

export interface AiAgentRepository {
  askQuery(query: AiQuery, signal?: AbortSignal): Promise<AiResponse>;
}
