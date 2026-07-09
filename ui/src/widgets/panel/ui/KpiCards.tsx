import { Users, RadioTower, SignalHigh, TriangleAlert } from "lucide-react"
import { Card } from "@/shared/ui"

interface KpiItem {
  label: string
  value: string
  tone?: string
}

interface KpiCardsProps {
  items: KpiItem[]
}

export function KpiCards({ items }: KpiCardsProps) {
  const icons = [Users, RadioTower, SignalHigh, TriangleAlert]

  return (
    <div className="grid grid-cols-2 gap-3 lg:grid-cols-4">
      {items.map((it, i) => {
        const Icon = icons[i % icons.length]
        return (
          <Card key={it.label} className="gap-2 p-4">
            <div className="flex items-center gap-2 text-xs text-muted-foreground">
              <Icon className={`h-4 w-4 ${it.tone ?? "text-primary"}`} />
              <span className="text-pretty">{it.label}</span>
            </div>
            <div className="font-mono text-2xl font-semibold tracking-tight">{it.value}</div>
          </Card>
        )
      })}
    </div>
  )
}
