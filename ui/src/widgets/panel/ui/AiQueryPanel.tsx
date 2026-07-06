import { Sparkles, ArrowUp, FileText, RotateCcw } from "lucide-react"
import { Card } from "@/shared/ui"
import { Badge } from "@/shared/ui"
import { Button } from "@/shared/ui"
import { MarkdownRenderer } from "@/shared/ui"
import { useLanguage } from "@/shared/lib/i18n"

interface DatoItem {
  region: string
  valor: number | string
  fuente: string
}

interface AiQueryResponse {
  respuesta_ia: string
  titulo: string
  metrica: string
  datos: DatoItem[]
  fuentes: string[]
}

interface AiQueryPanelProps {
  consulta: string
  onConsultaChange: (value: string) => void
  onSubmit: () => void
  loading?: boolean
  error?: string | null
  response?: AiQueryResponse | null
  pregunta?: string
  onClear?: () => void
  sugerencias?: string[]
  placeholder?: string
  title?: string
  subtitle?: string
}

/** The backend falls back to a single generic row (region "all") when the
 * question can't be broken down per-region — that placeholder carries no
 * real information, so we skip rendering the table for it. */
function hasStructuredData(datos: DatoItem[]): boolean {
  if (datos.length === 0) return false
  if (datos.length === 1 && datos[0].region.toLowerCase() === "all") return false
  return true
}

function formatValor(valor: number | string): string {
  return typeof valor === "number" ? valor.toLocaleString() : valor
}

export function AiQueryPanel({
  consulta,
  onConsultaChange,
  onSubmit,
  loading = false,
  error,
  response,
  pregunta,
  onClear,
  sugerencias = [],
  placeholder,
  title,
  subtitle,
}: AiQueryPanelProps) {
  const { t } = useLanguage()
  const resolvedPlaceholder = placeholder ?? t("panel.queryPlaceholder")
  const resolvedTitle = title ?? t("panel.queryTitle")
  const resolvedSubtitle = subtitle ?? t("panel.querySubtitle")

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === "Enter" && !e.shiftKey && !e.nativeEvent.isComposing) {
      e.preventDefault()
      onSubmit()
    }
  }

  return (
    <Card className="flex flex-col gap-4 p-5">
      <div className="flex items-center gap-2">
        <div className="flex h-8 w-8 items-center justify-center rounded-md bg-primary/15 text-primary">
          <Sparkles className="h-4 w-4" />
        </div>
        <div>
          <h2 className="text-sm font-semibold leading-tight">{resolvedTitle}</h2>
          <p className="text-xs text-muted-foreground">{resolvedSubtitle}</p>
        </div>
      </div>

      <div className="flex items-end gap-2 rounded-lg border border-border bg-background/60 p-2">
        <textarea
          value={consulta}
          onChange={(e) => onConsultaChange(e.target.value)}
          onKeyDown={handleKeyDown}
          rows={2}
          placeholder={resolvedPlaceholder}
          className="min-h-10 flex-1 resize-none bg-transparent px-2 py-1.5 text-sm outline-none placeholder:text-muted-foreground"
        />
        <Button size="icon" onClick={onSubmit} disabled={loading || !consulta.trim()} aria-label={t("panel.querySubmit")}>
          {loading ? (
            <svg className="h-4 w-4 animate-spin" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="3" opacity="0.25" />
              <path d="M12 2a10 10 0 0 1 10 10" stroke="currentColor" strokeWidth="3" strokeLinecap="round" />
            </svg>
          ) : (
            <ArrowUp className="h-4 w-4" />
          )}
        </Button>
      </div>

      {!response && !loading && sugerencias.length > 0 && (
        <div className="flex flex-wrap gap-2">
          {sugerencias.map((s) => (
            <button
              key={s}
              onClick={() => onConsultaChange(s)}
              type="button"
              className="rounded-full border border-border bg-secondary/50 px-3 py-1.5 text-left text-xs text-muted-foreground transition-colors hover:border-primary/50 hover:text-foreground"
            >
              {s}
            </button>
          ))}
        </div>
      )}

      {error && <p className="text-sm text-destructive">{error}</p>}

      {response && (
        <div className="flex flex-col gap-3">
          {pregunta && (
            <div className="flex items-start gap-2 rounded-lg bg-muted/60 px-3 py-2 text-xs text-muted-foreground">
              <span className="font-semibold text-foreground shrink-0">{t("panel.queryYouAsked")}:</span>
              <span>{pregunta}</span>
            </div>
          )}

          <div className="rounded-lg border border-primary/25 bg-primary/5 p-4">
            <div className="mb-1.5 flex items-center gap-2 text-xs font-medium text-primary">
              <Sparkles className="h-3.5 w-3.5" /> {t("panel.queryAgentResponse")}
            </div>
            <MarkdownRenderer content={response.respuesta_ia} />
          </div>

          {hasStructuredData(response.datos) && (
            <div>
              <div className="mb-2 flex items-center gap-2 text-xs font-medium text-muted-foreground">
                <FileText className="h-3.5 w-3.5" /> {response.titulo} · {response.metrica}
              </div>
              <div className="overflow-hidden rounded-lg border border-border">
                <table className="w-full text-sm">
                  <thead className="bg-secondary/50 text-xs text-muted-foreground">
                    <tr>
                      <th className="px-3 py-2 text-left font-medium">{t("panel.queryTable.region")}</th>
                      <th className="px-3 py-2 text-right font-medium">{t("panel.queryTable.value")}</th>
                      <th className="px-3 py-2 text-left font-medium">{t("panel.queryTable.source")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {response.datos.map((d, i) => (
                      <tr key={d.region} className={i % 2 ? "bg-background/40" : ""}>
                        <td className="px-3 py-2">{d.region}</td>
                        <td className="px-3 py-2 text-right font-mono">{formatValor(d.valor)}</td>
                        <td className="px-3 py-2 text-xs text-muted-foreground">{d.fuente}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          <div className="flex flex-wrap items-center justify-between gap-2">
            <div className="flex flex-wrap items-center gap-1.5">
              <span className="text-xs text-muted-foreground">{t("panel.querySources")}</span>
              {response.fuentes.map((f) => (
                <Badge key={f} variant="outline" className="text-[10px] font-normal">
                  {f}
                </Badge>
              ))}
            </div>

            {onClear && (
              <button
                type="button"
                onClick={onClear}
                className="inline-flex items-center gap-1.5 rounded-lg border border-border bg-card px-2.5 py-1.5 text-xs font-medium text-muted-foreground transition-colors hover:bg-muted hover:text-foreground"
              >
                <RotateCcw className="h-3.5 w-3.5" />
                {t("panel.queryClear")}
              </button>
            )}
          </div>
        </div>
      )}
    </Card>
  )
}
