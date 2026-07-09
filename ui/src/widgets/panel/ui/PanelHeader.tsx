import { Activity, Globe } from "lucide-react"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/shared/ui"
import { useLanguage } from "@/shared/lib/i18n"
import type { ReactNode } from "react"

interface RegionOption {
  id: string
  label: string
}

interface PanelHeaderProps {
  title?: string
  subtitle?: string
  regions: RegionOption[]
  selectedRegionId: string
  onRegionChange: (id: string) => void
  locale?: string
  onLocaleChange?: (locale: string) => void
  children?: ReactNode
}

export function PanelHeader({
  title,
  subtitle,
  regions,
  selectedRegionId,
  onRegionChange,
  locale,
  onLocaleChange,
  children,
}: PanelHeaderProps) {
  const { t } = useLanguage()

  return (
    <header className="sticky top-0 z-20 border-b border-border bg-background/85 backdrop-blur">
      <div className="mx-auto flex max-w-350 flex-wrap items-center gap-3 px-4 py-3 md:px-6">
        <div className="flex items-center gap-2.5">
          <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-primary text-primary-foreground">
            <Activity className="h-5 w-5" />
          </div>
          <div>
            <h1 className="text-sm font-semibold leading-tight md:text-base">{title ?? t("panel.title")}</h1>
            <p className="text-[11px] text-muted-foreground">{subtitle ?? t("panel.subtitle")}</p>
          </div>
        </div>

        <div className="ml-auto flex flex-wrap items-center gap-2">
          <Select
            items={regions.map((r) => ({ value: r.id, label: r.label }))}
            value={selectedRegionId}
            onValueChange={(v) => v && onRegionChange(v)}
          >
            <SelectTrigger className="w-37.5 sm:w-52.5" aria-label={t("panel.region")}>
              <Globe className="h-4 w-4 shrink-0 text-primary" />
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              {regions.map((r) => (
                <SelectItem key={r.id} value={r.id}>
                  {r.label}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>

          {locale && onLocaleChange && (
            <Select value={locale} onValueChange={(v) => v && onLocaleChange(v)}>
              <SelectTrigger className="w-23" aria-label={t("panel.language")}>
                <SelectValue>{() => locale.toUpperCase()}</SelectValue>
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="es">ES</SelectItem>
                <SelectItem value="pt">PT</SelectItem>
                <SelectItem value="en">EN</SelectItem>
              </SelectContent>
            </Select>
          )}

          {children}
        </div>
      </div>
    </header>
  )
}
