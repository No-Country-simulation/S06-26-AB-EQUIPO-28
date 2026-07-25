import { X, Signal } from "lucide-react"
import { Card } from "@/shared/ui"
import { useLanguage } from "@/shared/lib/i18n"

interface FloatingAntennaCardProps {
  nombre?: string | null
  municipio?: string | null
  poblacion?: string | null
  cobertura?: string | null
  brecha?: string | null
  antenas?: string | null
  indicadores?: { key: string; label: string; valor: number; unidad: string }[]
  onClose?: () => void
}

function brechaColor(level: string): string {
  switch (level) {
    case "alta":
    case "high":
      return "bg-red-500/15 text-red-600 border-red-500/30"
    case "media":
    case "medium":
      return "bg-amber-500/15 text-amber-600 border-amber-500/30"
    default:
      return "bg-sky-500/15 text-sky-600 border-sky-500/30"
  }
}

export function FloatingAntennaCard({
  nombre,
  municipio,
  poblacion,
  cobertura,
  brecha,
  antenas,
  indicadores,
  onClose,
}: FloatingAntennaCardProps) {
  const { t } = useLanguage()

  if (!nombre) return null

  return (
    <Card className="w-72 p-0 shadow-lg backdrop-blur-sm animate-in slide-in-from-right-4 fade-in duration-200">
      <div className="flex items-center justify-between border-b border-border px-4 py-3">
        <div className="flex items-center gap-2 min-w-0">
          <Signal className="h-4 w-4 shrink-0 text-primary" />
          <h3 className="truncate text-sm font-semibold">{nombre}</h3>
        </div>
        {onClose && (
          <button
            type="button"
            onClick={onClose}
            className="ml-2 shrink-0 rounded-full p-1 text-muted-foreground transition-colors hover:bg-muted hover:text-foreground"
          >
            <X className="h-3.5 w-3.5" />
          </button>
        )}
      </div>

      <div className="px-4 py-3 space-y-3">
        {municipio && (
          <p className="text-xs text-muted-foreground">{municipio}</p>
        )}

        <div className="grid grid-cols-3 gap-2 text-center">
          <div className="rounded-lg bg-secondary/40 p-2">
            <div className="font-mono text-base font-semibold">{poblacion ?? "—"}</div>
            <div className="text-[10px] text-muted-foreground">{t("panel.detail.people")}</div>
          </div>
          <div className="rounded-lg bg-secondary/40 p-2">
            <div className="font-mono text-base font-semibold">{cobertura ?? "—"}</div>
            <div className="text-[10px] text-muted-foreground">{t("panel.detail.coverage")}</div>
          </div>
          <div className="rounded-lg bg-secondary/40 p-2">
            <div className="font-mono text-base font-semibold">{antenas ?? "—"}</div>
            <div className="text-[10px] text-muted-foreground">{t("panel.detail.antennas")}</div>
          </div>
        </div>

        {brecha && (
          <div className="flex items-center justify-between">
            <span className="text-xs text-muted-foreground">{t("panel.detail.gapLabel")}</span>
            <span className={`inline-flex items-center rounded-full border px-2 py-0.5 text-[10px] font-medium ${brechaColor(brecha)}`}>
              {brecha}
            </span>
          </div>
        )}

        {indicadores && indicadores.length > 0 && (
          <div className="space-y-2 border-t border-border pt-3">
            <div className="text-[10px] font-medium text-muted-foreground uppercase tracking-wide">
              {t("panel.detail.indicators")}
            </div>
            {indicadores.slice(0, 4).map((ind) => (
              <div key={ind.key} className="flex items-center justify-between text-xs">
                <span className="truncate text-muted-foreground">{ind.label}</span>
                <span className="ml-2 shrink-0 font-mono text-foreground">
                  {ind.valor}{ind.unidad}
                </span>
              </div>
            ))}
          </div>
        )}
      </div>
    </Card>
  )
}
