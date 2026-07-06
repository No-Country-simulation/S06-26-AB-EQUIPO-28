// ---------------------------------------------------------------------------
// Public API — src/shared/lib/i18n
// ---------------------------------------------------------------------------

export { LanguageProvider, LOCALE_STORAGE_KEY } from "./LanguageProvider.tsx";
export { useLanguage } from "./useLanguage.ts";
export type { TranslationParams } from "./useLanguage.ts";
export type { Locale, TranslationKey } from "./translations.ts";
export { LOCALES } from "./translations.ts";
export { getDataSources, getAiMethodology } from "./methodologyContent.ts";
export { formatLocaleNumber, formatLocaleDate, localeToBcp47 } from "./localeUtils.ts";
