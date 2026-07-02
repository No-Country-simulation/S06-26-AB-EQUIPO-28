// ---------------------------------------------------------------------------
// AI Agent entity — public API barrel.
// ---------------------------------------------------------------------------

export type { AiQuery, AiDataItem, AiResponse } from "./model/types.ts";
export type { AiAgentRepository } from "./api/port.ts";
export { toAskAiRequest, toAiResponse } from "./api/mapper.ts";
