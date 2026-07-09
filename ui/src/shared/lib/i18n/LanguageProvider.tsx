// ---------------------------------------------------------------------------
// LanguageProvider — React context for i18n with Spanish as default.
//
// Provides `t()` translate function and `setLocale()` to switch between
// es, pt and en. Falls back to Spanish for missing keys.
// ---------------------------------------------------------------------------

import {
  useState,
  useCallback,
  useMemo,
  useEffect,
  type ReactNode,
} from "react";
import { translations, type Locale, type TranslationKey } from "./translations.ts";
import { LanguageContext, type TranslationParams } from "./useLanguage.ts";

interface LanguageProviderProps {
  readonly children: ReactNode;
}

export const LOCALE_STORAGE_KEY = "appbit_locale";
const DEFAULT_LOCALE: Locale = "es";

function loadLocale(): Locale {
  try {
    const saved = localStorage.getItem(LOCALE_STORAGE_KEY) as Locale | null;
    if (saved && saved in translations) return saved;
  } catch { /* localStorage unavailable */ }
  return DEFAULT_LOCALE;
}

function interpolate(template: string, params?: TranslationParams): string {
  if (!params) return template;
  return Object.entries(params).reduce(
    (result, [key, value]) => result.replaceAll(`{${key}}`, String(value)),
    template,
  );
}

export function LanguageProvider({ children }: LanguageProviderProps) {
  const [locale, setLocaleState] = useState<Locale>(loadLocale);

  useEffect(() => {
    document.documentElement.lang = locale;
  }, [locale]);

  const setLocale = useCallback((next: Locale) => {
    setLocaleState(next);
    try { localStorage.setItem(LOCALE_STORAGE_KEY, next); } catch { /* noop */ }
    document.documentElement.lang = next;
  }, []);

  const t = useCallback(
    (key: TranslationKey, params?: TranslationParams): string => {
      const dict = translations[locale] ?? translations.es;
      const template = dict[key] ?? translations.es[key] ?? key;
      return interpolate(template, params);
    },
    [locale],
  );

  const ctxValue = useMemo(() => ({ locale, setLocale, t }), [locale, setLocale, t]);

  return (
    <LanguageContext.Provider value={ctxValue}>
      {children}
    </LanguageContext.Provider>
  );
}
