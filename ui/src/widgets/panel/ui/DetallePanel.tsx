import { Card } from "@/shared/ui"
import { Badge } from "@/shared/ui"
import { useLanguage } from "@/shared/lib/i18n"

interface ServicioIndicador {
  key: string
  label: string
  valor: number
  unidad: string
  max: number
}

interface DetallePanelProps {
  nombre?: string
  municipio?: string
  brecha?: string
  poblacion?: string
  cobertura?: string
  antenas?: string
  mixRed?: { g5: number; g4: number; g3: number }
  indicadores?: ServicioIndicador[]
  emptyMessage?: string
}

function barColor(pct: number) {
  if (pct >= 0.66) return "var(--primary)"
  if (pct >= 0.33) return "var(--accent)"
  return "var(--alert)"
}

export function DetallePanel({
  nombre,
  municipio,
  brecha,
  poblacion,
  cobertura,
  antenas,
  mixRed,
  indicadores,
  emptyMessage,
}: DetallePanelProps) {
  const { t } = useLanguage()
  const resolvedEmptyMessage = emptyMessage ?? t("panel.detail.empty")

  if (!nombre) {
    return (
      <Card className="flex h-full items-center justify-center p-6 text-center text-sm text-muted-foreground">
        {resolvedEmptyMessage}
      </Card>
    )
  }

  return (
    <Card className="flex flex-col gap-4 p-5">
      <div className="flex items-start justify-between gap-3">
        <div>
          <h2 className="text-base font-semibold leading-tight text-balance">{nombre}</h2>
          {municipio && <p className="text-xs text-muted-foreground">{municipio}</p>}
        </div>
        {brecha && (
          <Badge variant="outline" className="shrink-0">
            {t("panel.detail.gap", { level: brecha })}
          </Badge>
        )}
      </div>

      <div className="grid grid-cols-3 gap-2 text-center">
        <div className="rounded-lg bg-secondary/40 p-2">
          <div className="font-mono text-lg font-semibold">{poblacion ?? "—"}</div>
          <div className="text-[10px] text-muted-foreground">{t("panel.detail.people")}</div>
        </div>
        <div className="rounded-lg bg-secondary/40 p-2">
          <div className="font-mono text-lg font-semibold">{cobertura ?? "—"}</div>
          <div className="text-[10px] text-muted-foreground">{t("panel.detail.coverage")}</div>
        </div>
        <div className="rounded-lg bg-secondary/40 p-2">
          <div className="font-mono text-lg font-semibold">{antenas ?? "—"}</div>
          <div className="text-[10px] text-muted-foreground">{t("panel.detail.antennas")}</div>
        </div>
      </div>

      {mixRed && (
        <div>
          <div className="mb-1.5 text-xs font-medium text-muted-foreground">{t("panel.detail.networkMix")}</div>
          <div className="flex h-3 overflow-hidden rounded-full">
            <div style={{ width: `${mixRed.g5}%`, background: "var(--primary)" }} title={`5G ${mixRed.g5}%`} />
            <div style={{ width: `${mixRed.g4}%`, background: "var(--accent)" }} title={`4G ${mixRed.g4}%`} />
            <div style={{ width: `${mixRed.g3}%`, background: "var(--muted-foreground)" }} title={`3G ${mixRed.g3}%`} />
          </div>
          <div className="mt-1 flex justify-between text-[10px] text-muted-foreground">
            <span>5G {mixRed.g5}%</span>
            <span>4G {mixRed.g4}%</span>
            <span>3G {mixRed.g3}%</span>
          </div>
        </div>
      )}

      {indicadores && indicadores.length > 0 && (
        <div className="flex flex-col gap-2.5">
          <div className="text-xs font-medium text-muted-foreground">{t("panel.detail.indicators")}</div>
          {indicadores.map((s) => {
            const pct = Math.min(1, s.valor / s.max)
            return (
              <div key={s.key}>
                <div className="mb-1 flex items-center justify-between text-xs">
                  <span>{s.label}</span>
                  <span className="font-mono text-muted-foreground">
                    {s.valor} <span className="opacity-60">{s.unidad}</span>
                  </span>
                </div>
                <div className="h-2 overflow-hidden rounded-full bg-secondary/60">
                  <div className="h-full rounded-full" style={{ width: `${pct * 100}%`, background: barColor(pct) }} />
                </div>
              </div>
            )
          })}
        </div>
      )}
    </Card>
  )
}
