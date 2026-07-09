export type Period = "dawn" | "morning" | "afternoon" | "night";

export const PERIOD_TRANSLATION_KEY: Record<Period, string> = {
  dawn: "map.period.dawn",
  morning: "map.period.morning",
  afternoon: "map.period.afternoon",
  night: "map.period.night",
};
