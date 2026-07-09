import { TriangleAlert, MapPin } from "lucide-react"
import { Card } from "@/shared/ui"
import { Badge } from "@/shared/ui"
import { ScrollArea } from "@/shared/ui"
import { useLanguage } from "@/shared/lib/i18n"

interface ZonaItem {
  id: string
  nombre: string
  municipio?: string
  poblacion?: string
  cobertura?: string
  brecha?: string
}

interface ZonasPanelProps {
  zonas: ZonaItem[]
  seleccionado?: string | null
  onSelect?: (id: string) => void
  title?: string
  description?: string
}

export function ZonasPanel({
  zonas,
  seleccionado,
  onSelect,
  title,
  description,
}: ZonasPanelProps) {
  const { t } = useLanguage()
  const resolvedTitle = title ?? t("panel.zones.title")
  const resolvedDescription = description ?? t("panel.zones.description")

  const brechaColor: Record<string, string> = {
    alta: "border-[color:var(--alert)]/40 bg-[color:var(--alert)]/10 text-[color:var(--alert)]",
    media: "border-accent/40 bg-accent/10 text-accent",
    baja: "border-primary/40 bg-primary/10 text-primary",
    high: "border-[color:var(--alert)]/40 bg-[color:var(--alert)]/10 text-[color:var(--alert)]",
    medium: "border-accent/40 bg-accent/10 text-accent",
    low: "border-primary/40 bg-primary/10 text-primary",
  }

  return (
    <Card className="flex h-full flex-col gap-3 p-5">
      <div className="flex items-center gap-2">
        <TriangleAlert className="h-4 w-4 text-[color:var(--alert)]" />
        <h2 className="text-sm font-semibold">{resolvedTitle}</h2>
      </div>
      {resolvedDescription && (
        <p className="text-xs text-muted-foreground">{resolvedDescription}</p>
      )}
      <ScrollArea className="-mx-1 flex-1">
        <div className="flex flex-col gap-2 px-1">
          {zonas.map((c) => {
            const activo = seleccionado === c.id
            return (
              <button
                key={c.id}
                onClick={() => onSelect?.(c.id)}
                className={`flex items-center justify-between rounded-lg border p-3 text-left transition-colors ${
                  activo ? "border-primary bg-primary/10" : "border-border bg-background/40 hover:border-primary/40"
                }`}
              >
                <div className="min-w-0">
                  <div className="flex items-center gap-1.5 text-sm font-medium">
                    <MapPin className="h-3.5 w-3.5 shrink-0 text-muted-foreground" />
                    <span className="truncate">{c.nombre}</span>
                  </div>
                  {c.municipio && (
                    <div className="mt-0.5 text-xs text-muted-foreground">
                      {c.municipio}{c.poblacion ? ` · ${c.poblacion}` : ""}
                    </div>
                  )}
                </div>
                <div className="flex flex-col items-end gap-1">
                  {c.cobertura && (
                    <span className="font-mono text-sm font-semibold">{c.cobertura}</span>
                  )}
                  {c.brecha && (
                    <Badge variant="outline" className={`text-[10px] ${brechaColor[c.brecha] ?? ""}`}>
                      {c.brecha}
                    </Badge>
                  )}
                </div>
              </button>
            )
          })}
        </div>
      </ScrollArea>
    </Card>
  )
}
