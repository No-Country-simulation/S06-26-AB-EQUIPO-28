import { Bar, BarChart, CartesianGrid, Legend, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts"
import { Card } from "@/shared/ui"
import { Badge } from "@/shared/ui"
import { useLanguage } from "@/shared/lib/i18n"

interface RegionSummary {
  id: string
  nombre: string
  label: string
  tipo?: "real" | "emulado"
  metricas: { label: string; value: string }[]
}

interface ComparativoProps {
  regiones: RegionSummary[]
  chartData: Record<string, string | number>[]
  chartConfig?: {
    colors?: string[]
    indicadorKey?: string
  }
  title?: string
  description?: string
}

export function Comparativo({
  regiones,
  chartData,
  chartConfig,
  title,
  description,
}: ComparativoProps) {
  const { t } = useLanguage()
  const colores = chartConfig?.colors ?? ["#38bdf8", "#f5b642"]
  const resolvedTitle = title ?? t("panel.comparative.title")

  return (
    <div className="flex flex-col gap-4">
      <div className="grid gap-3 md:grid-cols-2">
        {regiones.map((d) => (
          <Card key={d.id} className="gap-3 p-5">
            <div className="flex items-center justify-between">
              <div>
                <h3 className="text-sm font-semibold">{d.label}</h3>
                {d.nombre && <p className="text-xs text-muted-foreground text-pretty">{d.nombre}</p>}
              </div>
              {d.tipo && (
                <Badge variant={d.tipo === "real" ? "default" : "outline"} className="text-[10px]">
                  {d.tipo === "real" ? t("panel.data.real") : t("panel.data.emulated")}
                </Badge>
              )}
            </div>
            <div className="grid grid-cols-2 gap-2 text-sm">
              {d.metricas.map((m) => (
                <div key={m.label} className="rounded-lg bg-secondary/40 px-3 py-2">
                  <div className="font-mono text-base font-semibold">{m.value}</div>
                  <div className="text-[10px] text-muted-foreground">{m.label}</div>
                </div>
              ))}
            </div>
          </Card>
        ))}
      </div>

      {chartData.length > 0 && (
        <Card className="gap-3 p-5">
          <h3 className="text-sm font-semibold">{resolvedTitle}</h3>
          {description && (
            <p className="text-xs text-muted-foreground">{description}</p>
          )}
          <div className="h-72 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={chartData} margin={{ top: 8, right: 8, left: -16, bottom: 0 }}>
                <CartesianGrid strokeDasharray="3 3" stroke="var(--border)" vertical={false} />
                <XAxis
                  dataKey={chartConfig?.indicadorKey ?? "indicador"}
                  tick={{ fontSize: 11, fill: "var(--muted-foreground)" }}
                  tickLine={false}
                  axisLine={false}
                />
                <YAxis tick={{ fontSize: 11, fill: "var(--muted-foreground)" }} tickLine={false} axisLine={false} />
                <Tooltip
                  contentStyle={{
                    background: "var(--popover)",
                    border: "1px solid var(--border)",
                    borderRadius: 8,
                    fontSize: 12,
                    color: "var(--popover-foreground)",
                  }}
                  cursor={{ fill: "var(--muted)", opacity: 0.3 }}
                />
                <Legend wrapperStyle={{ fontSize: 12 }} />
                {regiones.map((d, i) => (
                  <Bar key={d.id} dataKey={d.label} fill={colores[i % colores.length]} radius={[4, 4, 0, 0]} />
                ))}
              </BarChart>
            </ResponsiveContainer>
          </div>
        </Card>
      )}
    </div>
  )
}
