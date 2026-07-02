import type { AiResponse } from "../model/types.ts";

/**
 * Returns `true` when the AI response includes data items.
 */
export function hasData(response: AiResponse): boolean {
  return response.data.length > 0;
}

/**
 * Returns the first data item matching the given `region`, or `undefined` when
 * none does.
 */
export function getDataByRegion(
  response: AiResponse,
  region: string,
): AiResponse["data"][number] | undefined {
  return response.data.find((item) => item.region === region);
}
