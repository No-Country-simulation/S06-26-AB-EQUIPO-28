import type { AiQuery, AiResponse } from "../model/types.ts";

export interface AiAgentRepository {
  askQuery(query: AiQuery): Promise<AiResponse>;
}
