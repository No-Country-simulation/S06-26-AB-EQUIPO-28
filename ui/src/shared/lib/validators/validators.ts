// ---------------------------------------------------------------------------
// validators — Simple validation helpers
// ---------------------------------------------------------------------------

const UUID_RE =
  /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;

export function isNonEmpty(value: string): boolean {
  return value.trim().length > 0;
}

export function isValidRegionId(value: string): boolean {
  return UUID_RE.test(value);
}
