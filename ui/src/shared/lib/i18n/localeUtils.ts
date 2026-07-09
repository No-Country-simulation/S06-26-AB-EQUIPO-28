import type { Locale } from "./translations.ts";

const LOCALE_TO_BCP47: Record<Locale, string> = {
  es: "es-AR",
  pt: "pt-BR",
  en: "en-US",
};

export function localeToBcp47(locale: Locale): string {
  return LOCALE_TO_BCP47[locale] ?? LOCALE_TO_BCP47.es;
}

export function formatLocaleNumber(value: number, locale: Locale): string {
  return value.toLocaleString(localeToBcp47(locale));
}

export function formatLocaleDate(value: Date | string | number, locale: Locale): string {
  const date = value instanceof Date ? value : new Date(value);
  return date.toLocaleDateString(localeToBcp47(locale));
}
