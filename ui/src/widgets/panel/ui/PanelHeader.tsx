import { Activity, Globe, Languages } from "lucide-react"
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

const LANGUAGE_OPTIONS = [
  { value: "es", label: "Español" },
  { value: "pt", label: "Português" },
  { value: "en", label: "English" },
]

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
    <header className="z-20 shrink-0 border-b border-border bg-background">
      <div className="mx-auto flex max-w-350 flex-wrap items-center gap-3 px-4 py-3 md:px-6">
        <div className="flex items-center gap-2.5">
          <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-primary text-primary-foreground">
            <Activity className="h-5 w-5" />
          </div>
          <div className="min-w-0">
            <h1 className="truncate text-sm font-semibold leading-tight md:text-base">{title ?? t("panel.title")}</h1>
            <p className="hidden truncate text-[11px] text-muted-foreground sm:block">{subtitle ?? t("panel.subtitle")}</p>
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
            <Select
              items={LANGUAGE_OPTIONS}
              value={locale}
              onValueChange={(v) => v && onLocaleChange(v)}
            >
              <SelectTrigger className="w-25" aria-label={t("panel.language")}>
                <Languages className="h-4 w-4 shrink-0 text-primary" />
                <SelectValue>{() => locale.toUpperCase()}</SelectValue>
              </SelectTrigger>
              <SelectContent>
                {LANGUAGE_OPTIONS.map((lang) => (
                  <SelectItem key={lang.value} value={lang.value}>
                    {lang.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          )}

          {children}
        </div>
      </div>
    </header>
  )
}
