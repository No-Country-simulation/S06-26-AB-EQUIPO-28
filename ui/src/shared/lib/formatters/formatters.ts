// ---------------------------------------------------------------------------
// formatters — Locale-aware UI formatting helpers for pt-BR
// ---------------------------------------------------------------------------

const NUMBER_FORMAT = new Intl.NumberFormat("pt-BR");
const PERCENT_FORMAT = new Intl.NumberFormat("pt-BR", {
  style: "percent",
  maximumFractionDigits: 1,
});
const DATE_FORMAT: Intl.DateTimeFormatOptions = {
  day: "2-digit",
  month: "2-digit",
  year: "numeric",
};

/** Known region-name normalisations (snake_case → Title Case with accents). */
const REGION_OVERRIDES: Record<string, string> = {
  sao_paulo: "São Paulo",
  rio_de_janeiro: "Rio de Janeiro",
  "belo_horizonte": "Belo Horizonte",
  "brasilia": "Brasília",
  "distrito_federal": "Distrito Federal",
  "sao_luis": "São Luís",
  "porto_alegre": "Porto Alegre",
  "salvador": "Salvador",
  "fortaleza": "Fortaleza",
  "recife": "Recife",
  "curitiba": "Curitiba",
  "manaus": "Manaus",
  "belem": "Belém",
  "vitoria": "Vitória",
  "nordeste": "Nordeste",
  "norte": "Norte",
  "centro_oeste": "Centro-Oeste",
  "sudeste": "Sudeste",
  "sul": "Sur",
};

/**
 * English area/region codes → Spanish display names.
 * Used to decode backend REG-CODE patterns.
 */
const REGION_CODE_MAP: Record<string, string> = {
  NORTH: "Norte",
  SOUTH: "Sur",
  CENTER: "Centro",
  EAST: "Este",
  WEST: "Oeste",
};

/**
 * Portuguese → Spanish direction word translations.
 * Applied after the fallback title-case transform.
 */
const PT_TO_ES_DIRECTIONS: Record<string, string> = {
  sul: "Sur",
  leste: "Este",
};

export function formatNumber(value: number): string {
  return NUMBER_FORMAT.format(value);
}

export function formatPercentage(value: number): string {
  return PERCENT_FORMAT.format(value);
}

export function formatDate(date: string | Date): string {
  const d = typeof date === "string" ? new Date(date) : date;
  return d.toLocaleDateString("pt-BR", DATE_FORMAT);
}

export function formatRegionName(name: string): string {
  const trimmed = name.trim();
  if (!trimmed) return name;

  // Strip connectivity suffix if present (e.g. "Region X — Alto" → "Region X")
  // Handles: hyphen (-), en-dash (–), em-dash (—), and case variations
  const cleaned = trimmed
    .replace(/\s*[-–—]\s*(Alto|Medio|Bajo|High|Medium|Low)\s*$/i, "")
    .trim();

  // ── Pattern 1: "REG-AREA-X" or "REG-AREA" (uppercase codes) ─────────────
  const regCodeMatch = cleaned.match(/^REG[-_]([A-Z]+)(?:[-_]([A-Z]))?$/);
  if (regCodeMatch) {
    const areaCode = regCodeMatch[1];
    const suffix = regCodeMatch[2] ?? "";
    const area = REGION_CODE_MAP[areaCode] ?? areaCode.charAt(0) + areaCode.slice(1).toLowerCase();
    return suffix ? `Región ${area} ${suffix}` : `Región ${area}`;
  }

  // ── Pattern 2: "cluster_1", "cluster-2" etc ─────────────────────────────
  const clusterMatch = cleaned.match(/^cluster[-_](\d+)$/i);
  if (clusterMatch) {
    return `Cluster ${clusterMatch[1]}`;
  }

  // ── Normalise to lowercase for override lookup ──────────────────────────
  const lower = cleaned.toLowerCase();

  if (REGION_OVERRIDES[lower]) {
    return REGION_OVERRIDES[lower];
  }

  // ── Snake_case / kebab-case fallback ────────────────────────────────────
  // Replace hyphens and underscores with spaces, then title-case each word.
  const parts = lower.split(/[-_]+/).filter(Boolean);
  if (parts.length > 1 || lower.includes("_") || lower.includes("-")) {
    const titled = parts
      .map((seg) => seg.charAt(0).toUpperCase() + seg.slice(1))
      .join(" ");
    return applyPortugueseTranslations(titled);
  }

  // ── Single-word fallback ────────────────────────────────────────────────
  // Capitalise the first letter, keep the rest as-is for known words
  // or lowercase the rest for uppercase codes that didn't match pattern 1.
  const single = lower.charAt(0).toUpperCase() + lower.slice(1);
  return applyPortugueseTranslations(single);
}

/**
 * Applies Portuguese→Spanish direction word translations on a
 * space-separated title-cased string (fallback path only).
 */
function applyPortugueseTranslations(text: string): string {
  return text
    .split(" ")
    .map((word) => PT_TO_ES_DIRECTIONS[word.toLowerCase()] ?? word)
    .join(" ");
}

export function truncate(text: string, maxLength: number): string {
  if (text.length <= maxLength) return text;
  return text.slice(0, maxLength).trimEnd() + "…";
}
