import { createContext, use } from "react";
import type { Locale, TranslationKey } from "./translations.ts";

export type TranslationParams = Record<string, string | number>;

export interface LanguageContextValue {
  locale: Locale;
  setLocale: (locale: Locale) => void;
  t: (key: TranslationKey, params?: TranslationParams) => string;
}

export const LanguageContext = createContext<LanguageContextValue | null>(null);

export function useLanguage(): LanguageContextValue {
  const ctx = use(LanguageContext);
  if (ctx === null) {
    throw new Error("useLanguage must be used within a <LanguageProvider>");
  }
  return ctx;
}
