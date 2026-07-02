// ---------------------------------------------------------------------------
// clsx — Tiny classname utility (zero dependencies)
// ---------------------------------------------------------------------------

export type ClassValue = string | boolean | null | undefined;

export function clsx(...classes: ClassValue[]): string {
  return classes.filter(Boolean).join(" ");
}
