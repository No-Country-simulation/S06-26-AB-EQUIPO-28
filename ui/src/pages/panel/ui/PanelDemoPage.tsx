"use client"

import { useState, useCallback, useMemo } from "react"
import {
  useLanguage,
  formatLocaleNumber,
  formatLocaleDate,
  type Locale,
  type TranslationKey,
} from "@/shared/lib/i18n"
import { useAppContext } from "@/app/providers/app-context"
import { useRegionFilter } from "@/features/filter-by-region"
import { useAskAi } from "@/features/ask-ai-query"
import { useAlertMonitor, AlertHistoryPanel } from "@/features/alert-monitor"
import { useExportPdf } from "@/shared/lib/pdf-export"
import { PanelHeader, KpiCards, DetallePanel, ZonasPanel, Comparativo, AiQueryPanel, MentorshipGapsPanel, MentorshipProgramsPanel, EmployabilityGapsPanel, EmployabilityOdMatrixPanel } from "@/widgets/panel"
import { InteractiveMapWidget } from "@/widgets/interactive-map"
import type { RegionPoint } from "@/widgets/interactive-map"
import { Card, Spinner, Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/shared/ui"
import { MapIcon, BarChart3, Layers, TriangleAlert, Sparkles, Bell, FileDown, Users, Briefcase, LayoutDashboard } from "lucide-react"
import { type Period, PERIOD_TRANSLATION_KEY } from "../model/filter-bar-types.ts"
import { usePanelData } from "../model/usePanelData.ts"
import { useMentorshipData } from "../model/useMentorshipData.ts"
import { useEmployabilityData } from "../model/useEmployabilityData.ts"

type Section = "dashboard" | "mapa" | "consulta" | "comparativo" | "mentorias" | "empleabilidad"
type Vista = "vulnerabilidad" | "conectividad"

function vulnerabilityLevelToBrecha(level: string, t: (key: TranslationKey) => string): string {
  if (level === "CRITICAL" || level === "HIGH") return t("panel.gap.high")
  if (level === "MEDIUM") return t("panel.gap.medium")
  return t("panel.gap.low")
}

function connectivityToLabel(level: string, t: (key: TranslationKey) => string): string {
  if (level === "HIGH") return t("panel.connectivity.high")
  if (level === "MEDIUM") return t("panel.connectivity.medium")
  return t("panel.connectivity.low")
}

function scoreToBrecha(score: number, t: (key: TranslationKey) => string): string {
  if (score >= 66) return t("panel.gap.high")
  if (score >= 33) return t("panel.gap.medium")
  return t("panel.gap.low")
}

function scoreToConnectivity(score: number, t: (key: TranslationKey) => string): string {
  if (score >= 66) return t("panel.connectivity.high")
  if (score >= 33) return t("panel.connectivity.medium")
  return t("panel.connectivity.low")
}

function colorForValue(value: number): string {
  if (value >= 66) return "#ef5a4c"
  if (value >= 33) return "#f5b642"
  return "#38bdf8"
}

function SidebarItem({ icon: Icon, label, active, onClick }: { icon: React.ComponentType<{ className?: string }>; label: string; active: boolean; onClick: () => void }) {
  return (
    <button
      onClick={onClick}
      className={`flex w-full items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors ${
        active
          ? "bg-primary/10 text-primary"
          : "text-muted-foreground hover:bg-muted hover:text-foreground"
      }`}
    >
      <Icon className="h-4 w-4 shrink-0" />
      {label}
    </button>
  )
}

export function PanelDemoPage() {
  const { locale, setLocale, t } = useLanguage()
  const { regionRepository, aiAgentRepository, indicatorRepository, mentalHealthRepository, mobilityDataRepository, mentorshipRepository, employabilityRepository } = useAppContext()

  const { regions, selectedRegionId, setSelectedRegion, selectedRegion } = useRegionFilter(regionRepository)

  const { report, reportLoading, reportError, vulnerableRegions, vulnLoading, indicators } = usePanelData(
    mentalHealthRepository,
    indicatorRepository,
    selectedRegionId,
    t("panel.loadReportError"),
  )

  const mentorshipData = useMentorshipData(mentorshipRepository, t("panel.loadReportError"))
  const employabilityData = useEmployabilityData(employabilityRepository, t("panel.loadReportError"))

  const [selectedZoneName, setSelectedZoneName] = useState<string | null>(null)
  const [vista, setVista] = useState<Vista>("vulnerabilidad")
  const [section, setSection] = useState<Section>("dashboard")

  const [selectedPeriod, setSelectedPeriod] = useState<Period>("morning")
  const [showAntennas, setShowAntennas] = useState(true)
  const [highConcentrationOnly, setHighConcentrationOnly] = useState(false)
  const [alertHistoryOpen, setAlertHistoryOpen] = useState(false)

  const { query, setQuery, submit, response, lastQuestion, isLoading: aiLoading, error: aiError, clearResponse } =
    useAskAi(aiAgentRepository, { region: selectedRegionId, language: locale, errorFallback: t("dashboard.error") })

  const { unacknowledgedCount } = useAlertMonitor(indicators, selectedRegionId, selectedRegion?.name)

  const { exportPdf, exporting } = useExportPdf({
    region: selectedRegion,
    indicators,
    aiResponse: response,
    period: selectedPeriod,
    locale,
  })

  const handleAiSubmit = useCallback(() => {
    if (query.trim() && !aiLoading) submit()
  }, [query, aiLoading, submit])

  const vistas = useMemo(
    () => [
      { key: "vulnerabilidad" as const, label: t("panel.view.vulnerability") },
      { key: "conectividad" as const, label: t("panel.view.connectivity") },
    ],
    [t],
  )

  const sugerencias = useMemo(
    () => [
      t("panel.suggestion.concentration"),
      t("panel.suggestion.digitalGap"),
      t("panel.suggestion.training"),
      t("panel.suggestion.antennas"),
    ],
    [t],
  )

  const kpiItems = report
    ? [
        { label: t("panel.kpi.totalPopulation"), value: formatLocaleNumber(report.metadata.totalPopulation, locale), tone: "text-primary" } as const,
        { label: t("panel.kpi.vulnerabilityScore"), value: report.metadata.averageVulnerabilityScore.toFixed(1), tone: "text-primary" } as const,
        { label: t("panel.kpi.vulnerablePopulation"), value: formatLocaleNumber(report.metadata.totalVulnerablePopulation, locale), tone: "text-[color:var(--alert)]" } as const,
        { label: t("panel.kpi.priorityRegions"), value: String(report.metadata.priorityRegionCount), tone: "text-[color:var(--alert)]" } as const,
      ]
    : []

  const mapPoints = useMemo(() => {
    if (vulnerableRegions.length > 0) {
      const nameToRegion = new Map(regions.map((r) => [r.name, r]))
      return vulnerableRegions.map((vr) => {
        const region = nameToRegion.get(vr.regionName)
        const valor = vista === "vulnerabilidad" ? vr.vulnerabilityScore : vr.concentrationIndex
        return {
          id: vr.regionName,
          nombre: vr.regionName,
          lat: region?.lat ?? -27.595,
          lng: region?.lng ?? -48.548,
          poblacion: vr.totalPopulation,
          cobertura: connectivityToLabel(vr.connectivityLevel, t),
          brecha: vulnerabilityLevelToBrecha(vr.vulnerabilityLevel, t),
          valor,
        }
      })
    }
    return regions.map((r) => ({
      id: r.id,
      nombre: r.name,
      lat: r.lat,
      lng: r.lng,
      poblacion: r.indicators.averageUsers,
      cobertura: scoreToConnectivity(r.connectivity, t),
      brecha: scoreToBrecha(r.concentration, t),
      valor: vista === "vulnerabilidad" ? r.concentration : r.connectivity,
    }))
  }, [vulnerableRegions, regions, vista, t])

  const mapRegions: RegionPoint[] = useMemo(() => {
    const maxPop = mapPoints.length > 0 ? Math.max(...mapPoints.map((p) => p.poblacion)) : 1
    const minPop = mapPoints.length > 0 ? Math.min(...mapPoints.map((p) => p.poblacion)) : 0
    const popRange = maxPop - minPop || 1
    return mapPoints.map((p) => ({
      id: p.id,
      label: p.nombre,
      lat: p.lat,
      lng: p.lng,
      color: colorForValue(p.valor),
      radius: 8 + ((p.poblacion - minPop) / popRange) * 22,
      value: p.valor,
    }))
  }, [mapPoints])

  const zonaItems = vulnerableRegions.length > 0
    ? vulnerableRegions.map((vr) => ({
        id: vr.regionName,
        nombre: vr.regionName,
        poblacion: `${formatLocaleNumber(vr.totalPopulation, locale)} ${t("panel.peopleSuffix")}`,
        cobertura: connectivityToLabel(vr.connectivityLevel, t),
        brecha: vulnerabilityLevelToBrecha(vr.vulnerabilityLevel, t),
      }))
    : regions.map((r) => ({
        id: r.id,
        nombre: r.name,
        cobertura: scoreToConnectivity(r.connectivity, t),
        brecha: scoreToBrecha(r.concentration, t),
      }))

  const selectedMapPoint = selectedZoneName
    ? mapPoints.find((p) => p.id === selectedZoneName) ?? null
    : null

  const selectedVulnDetail = selectedZoneName
    ? vulnerableRegions.find((vr) => vr.regionName === selectedZoneName) ?? null
    : null

  const selectedRegionForDetail = selectedZoneName
    ? regions.find((r) => r.id === selectedZoneName || r.name === selectedZoneName) ?? null
    : null

  const indicatorLabels: Record<string, string> = {
    EMPLOYABILITY_GAP: t("indicator.employabilityGap"),
    TRAINING_COVERAGE: t("indicator.trainingCoverage"),
    MENTAL_HEALTH_ACCESS: t("indicator.mentalHealthAccess"),
    MENTORSHIP_PROGRAMS: t("indicator.mentorshipPrograms"),
    STRUCTURED_EXPERIENCES: t("indicator.structuredExperiences"),
  }

  const detailIndicadores = indicators.map((iv) => ({
    key: iv.indicatorId,
    label: indicatorLabels[iv.indicatorId] ?? iv.indicatorId,
    valor: iv.value,
    unidad: "%",
    max: 100,
  }))

  const comparativoRegiones = (report?.regionSummaries ?? []).map((rs) => ({
    id: rs.regionName,
    nombre: rs.regionName,
    label: rs.regionName,
    metricas: [
      { label: t("panel.metric.vulnerabilityScore"), value: rs.vulnerabilityScore.toFixed(1) },
      { label: t("panel.metric.vulnerablePct"), value: `${rs.vulnerablePercentage.toFixed(1)}%` },
      { label: t("panel.metric.connectivity"), value: connectivityToLabel(rs.connectivityLevel, t) },
      { label: t("panel.metric.priority"), value: rs.isPriorityForIntervention ? t("panel.metric.yes") : t("panel.metric.no") },
    ],
  }))

  const vulnerabilityScoreLabel = t("panel.metric.vulnerabilityScore")
  const vulnerablePctLabel = t("panel.metric.vulnerablePct")
  const priorityLabel = t("panel.metric.priority")

  const chartData = (report?.regionSummaries ?? []).map((rs) => ({
    indicador: rs.regionName,
    [vulnerabilityScoreLabel]: rs.vulnerabilityScore,
    [vulnerablePctLabel]: rs.vulnerablePercentage,
    [priorityLabel]: rs.isPriorityForIntervention ? 100 : 0,
  }))

  const aiPanelResponse = response
    ? {
        respuesta_ia: response.summary,
        titulo: t("panel.aiAnalysis.title"),
        metrica: selectedRegion
          ? t("panel.aiAnalysis.metric", { region: selectedRegion.name })
          : t("panel.aiAnalysis.metricAllRegions"),
        datos: response.data.map((d) => ({
          region: d.region,
          valor: d.value,
          fuente: d.source,
        })),
        fuentes: [...response.sources],
      }
    : null

  const regionOptions = regions.map((r) => ({ id: r.id, label: r.name }))

  const showComparativo = comparativoRegiones.length >= 2

  const sidebarItems: { key: Section; icon: React.ComponentType<{ className?: string }>; label: string; hidden?: boolean }[] = [
    { key: "dashboard", icon: LayoutDashboard, label: t("panel.dashboard") },
    { key: "mapa", icon: MapIcon, label: t("panel.map") },
    { key: "consulta", icon: Sparkles, label: t("panel.query") },
    { key: "comparativo", icon: BarChart3, label: t("panel.comparative"), hidden: !showComparativo },
    { key: "mentorias", icon: Users, label: t("panel.mentorship") },
    { key: "empleabilidad", icon: Briefcase, label: t("panel.employability") },
  ]

  if (reportLoading || vulnLoading) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <Spinner size="lg" />
      </div>
    )
  }

  if (reportError && regions.length === 0) {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center gap-3 p-6 text-center">
        <TriangleAlert className="h-8 w-8 text-destructive" />
        <p className="text-sm text-destructive">{reportError}</p>
      </div>
    )
  }

  return (
    <div className="flex h-dvh flex-col overflow-hidden">
      <PanelHeader
        regions={regionOptions}
        selectedRegionId={selectedRegionId ?? regionOptions[0]?.id ?? ""}
        onRegionChange={setSelectedRegion}
        locale={locale}
        onLocaleChange={(l) => setLocale(l as Locale)}
      >
        <div className="flex items-center gap-2">
          <button
            type="button"
            onClick={exportPdf}
            disabled={exporting}
            aria-label={t("export.label")}
            title={t("export.label")}
            className="inline-flex h-9 items-center gap-1.5 rounded-lg border border-border bg-card px-2.5 text-xs font-medium text-foreground transition-colors hover:bg-muted disabled:cursor-not-allowed disabled:opacity-50"
          >
            <FileDown className="h-4 w-4 shrink-0" />
            <span className="hidden sm:inline whitespace-nowrap">{exporting ? t("common.loading") : t("export.label")}</span>
          </button>

          <button
            type="button"
            onClick={() => setAlertHistoryOpen(true)}
            aria-label={t("alert.bellLabel")}
            title={t("alert.history")}
            className="relative inline-flex h-9 w-9 items-center justify-center rounded-lg border border-border bg-card text-muted-foreground transition-colors hover:bg-muted hover:text-foreground"
          >
            <Bell className="h-4 w-4" />
            {unacknowledgedCount > 0 && (
              <span className="absolute -top-1 -right-1 inline-flex h-4 min-w-4 items-center justify-center rounded-full bg-destructive px-1 text-[10px] font-bold leading-4 text-destructive-foreground">
                {unacknowledgedCount > 9 ? "9+" : unacknowledgedCount}
              </span>
            )}
          </button>
        </div>
      </PanelHeader>

      <AlertHistoryPanel open={alertHistoryOpen} onClose={() => setAlertHistoryOpen(false)} />

      <div className="flex flex-wrap items-center gap-1.5 border-b border-border px-4 py-2 md:hidden">
        {sidebarItems
          .filter((item) => !item.hidden)
          .map((item) => (
            <button
              key={item.key}
              onClick={() => setSection(item.key)}
              className={`inline-flex items-center gap-1.5 rounded-lg px-3 py-1.5 text-xs font-medium transition-colors ${
                section === item.key
                  ? "bg-primary/10 text-primary"
                  : "text-muted-foreground hover:bg-muted hover:text-foreground"
              }`}
            >
              <item.icon className="h-3.5 w-3.5" />
              {item.label}
            </button>
          ))}
      </div>

      <div className="mx-auto flex min-h-0 w-full max-w-350 flex-1 px-4 md:px-6">
        <aside className="hidden w-56 shrink-0 overflow-y-auto border-r border-border py-5 pr-4 md:block">
          <nav className="flex flex-col gap-1">
            {sidebarItems
              .filter((item) => !item.hidden)
              .map((item) => (
                <SidebarItem
                  key={item.key}
                  icon={item.icon}
                  label={item.label}
                  active={section === item.key}
                  onClick={() => setSection(item.key)}
                />
              ))}
          </nav>
        </aside>

        <main className="min-w-0 flex-1 overflow-y-auto py-5 pl-4 md:pl-6">
          {section === "dashboard" && (
            <div>
              <div className="mb-3">
                <h1 className="text-lg font-semibold text-foreground">{t("panel.dashboard.title")}</h1>
              </div>
              {report && (
                <div className="mb-4 flex flex-wrap items-center gap-2 text-xs text-muted-foreground">
                  <span className="inline-flex items-center gap-1.5 rounded-full border border-border bg-card px-3 py-1">
                    <span className="h-2 w-2 rounded-full bg-ok" />
                    {t("panel.report.badge", {
                      period: report.reportPeriod,
                      date: formatLocaleDate(report.generatedAt, locale),
                    })}
                  </span>
                </div>
              )}
              <KpiCards items={kpiItems} />
            </div>
          )}

          {section === "mapa" && (
            <div className="mt-4 grid gap-4 lg:grid-cols-[1fr_360px]">
              <Card className="overflow-hidden p-0">
                <div className="flex flex-wrap items-center justify-between gap-2 border-b border-border px-4 py-3">
                  <div className="flex items-center gap-2 text-sm font-medium">
                    <Layers className="h-4 w-4 text-primary" /> {t("panel.visualization")}
                  </div>
                  <Select
                    items={vistas.map((v) => ({ value: v.key, label: v.label }))}
                    value={vista}
                    onValueChange={(v: string | null) => v && setVista(v as Vista)}
                  >
                    <SelectTrigger className="w-50" aria-label={t("panel.indicatorToView")}>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      {vistas.map((v) => (
                        <SelectItem key={v.key} value={v.key}>
                          {v.label}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>

                <div className="flex flex-wrap items-center gap-3 border-b border-border px-4 py-2.5">
                  <div className="flex gap-1.5">
                    {(["dawn", "morning", "afternoon", "night"] as const).map((period) => (
                      <button
                        key={period}
                        type="button"
                        onClick={() => setSelectedPeriod(period)}
                        aria-pressed={selectedPeriod === period}
                        className={`inline-flex h-8 items-center justify-center rounded-lg border px-3 text-xs font-medium transition-colors ${
                          selectedPeriod === period
                            ? "border-accent bg-accent text-accent-foreground"
                            : "border-border bg-card text-muted-foreground hover:bg-muted hover:border-ring"
                        }`}
                      >
                        {t(PERIOD_TRANSLATION_KEY[period] as TranslationKey)}
                      </button>
                    ))}
                  </div>

                  <label className="ml-auto inline-flex items-center gap-2 text-xs font-medium text-foreground">
                    <input
                      type="checkbox"
                      checked={showAntennas}
                      onChange={(e) => setShowAntennas(e.target.checked)}
                      className="h-4 w-4 rounded border-border accent-accent"
                    />
                    {t("map.showAntennas")}
                  </label>

                  <label className="inline-flex items-center gap-2 text-xs font-medium text-foreground">
                    <input
                      type="checkbox"
                      checked={highConcentrationOnly}
                      onChange={(e) => setHighConcentrationOnly(e.target.checked)}
                      className="h-4 w-4 rounded border-border accent-accent"
                    />
                    {t("map.highConcentrationOnly")}
                  </label>
                </div>

                <div className="h-95 w-full md:h-110">
                  <InteractiveMapWidget
                    repository={mobilityDataRepository}
                    regionId={selectedRegionId}
                    period={selectedPeriod}
                    vulnerableOnly={highConcentrationOnly}
                    showAntennas={showAntennas}
                    regions={mapRegions}
                    selectedRegionId={selectedZoneName}
                    onRegionSelect={setSelectedZoneName}
                    popupStrings={{
                      antennaLabel: t("map.legend.antenna"),
                      loadLabel: t("map.population"),
                      concentrationPoint: t("map.legend.highConcentration"),
                      intensityLabel: t("map.vulnerability"),
                    }}
                  />
                </div>
                <div className="flex flex-wrap items-center gap-4 border-t border-border px-4 py-2.5 text-xs text-muted-foreground">
                  <span className="inline-flex items-center gap-1.5"><span className="h-2.5 w-2.5 rounded-full" style={{ background: "#38bdf8" }} />{t("panel.legend.lowConnectivity")}</span>
                  <span className="inline-flex items-center gap-1.5"><span className="h-2.5 w-2.5 rounded-full" style={{ background: "#f5b642" }} />{t("panel.legend.medium")}</span>
                  <span className="inline-flex items-center gap-1.5"><span className="h-2.5 w-2.5 rounded-full" style={{ background: "#ef5a4c" }} />{t("panel.legend.highGap")}</span>
                  <span className="ml-auto">{t("panel.legend.circleSize")}</span>
                </div>
              </Card>

              <div className="flex flex-col gap-4">
                <DetallePanel
                  nombre={selectedMapPoint?.nombre ?? selectedRegion?.name}
                  brecha={selectedMapPoint?.brecha}
                  poblacion={selectedVulnDetail ? formatLocaleNumber(selectedVulnDetail.totalPopulation, locale) : selectedMapPoint?.poblacion ? formatLocaleNumber(selectedMapPoint.poblacion, locale) : undefined}
                  cobertura={selectedMapPoint?.cobertura}
                  antenas={selectedRegionForDetail ? String(selectedRegionForDetail.indicators.antennas) : undefined}
                  indicadores={detailIndicadores}
                />
                <ZonasPanel
                  zonas={zonaItems}
                  seleccionado={selectedZoneName}
                  onSelect={setSelectedZoneName}
                  title={t("panel.vulnerableRegions.title")}
                  description={t("panel.vulnerableRegions.description")}
                />
              </div>
            </div>
          )}

          {section === "consulta" && (
            <div className="mt-4">
              <AiQueryPanel
                consulta={query}
                onConsultaChange={setQuery}
                onSubmit={handleAiSubmit}
                loading={aiLoading}
                error={aiError}
                response={aiPanelResponse}
                pregunta={lastQuestion}
                onClear={clearResponse}
                sugerencias={sugerencias}
              />
            </div>
          )}

          {section === "comparativo" && showComparativo && (
            <div className="mt-4">
              <Comparativo
                regiones={comparativoRegiones}
                chartData={chartData}
                description={t("panel.comparative.description")}
              />
            </div>
          )}

          {section === "mentorias" && (
            <div className="mt-4">
              <div className="mb-3">
                <h1 className="text-lg font-semibold text-foreground">{t("panel.mentorship.title")}</h1>
              </div>
              {mentorshipData.loading ? (
                <div className="flex items-center justify-center py-12">
                  <Spinner size="lg" />
                </div>
              ) : mentorshipData.error && mentorshipData.gaps.length === 0 && mentorshipData.programs.length === 0 ? (
                <div className="flex flex-col items-center justify-center gap-3 py-12 text-center">
                  <TriangleAlert className="h-8 w-8 text-destructive" />
                  <p className="text-sm text-destructive">{mentorshipData.error}</p>
                </div>
              ) : (
                <div className="grid gap-4 lg:grid-cols-2">
                  <MentorshipGapsPanel gaps={mentorshipData.gaps} />
                  <MentorshipProgramsPanel
                    programs={mentorshipData.programs}
                    clusters={mentorshipData.clusters}
                  />
                </div>
              )}
            </div>
          )}

          {section === "empleabilidad" && (
            <div className="mt-4">
              <div className="mb-3">
                <h1 className="text-lg font-semibold text-foreground">{t("panel.employability.title")}</h1>
              </div>
              {employabilityData.loading ? (
                <div className="flex items-center justify-center py-12">
                  <Spinner size="lg" />
                </div>
              ) : employabilityData.error && employabilityData.gaps.length === 0 && employabilityData.odMatrix.length === 0 ? (
                <div className="flex flex-col items-center justify-center gap-3 py-12 text-center">
                  <TriangleAlert className="h-8 w-8 text-destructive" />
                  <p className="text-sm text-destructive">{employabilityData.error}</p>
                </div>
              ) : (
                <div className="grid gap-4 lg:grid-cols-2">
                  <EmployabilityGapsPanel gaps={employabilityData.gaps} />
                  <EmployabilityOdMatrixPanel odMatrix={employabilityData.odMatrix} />
                </div>
              )}
            </div>
          )}
        </main>
      </div>

      <footer className="mx-auto w-full max-w-350 border-t border-border px-4 pt-4 pb-6 text-xs text-muted-foreground md:px-6">
        {t("panel.footer", {
          period: report?.reportPeriod ?? "—",
          population: report ? formatLocaleNumber(report.metadata.totalPopulation, locale) : "—",
        })}
      </footer>
    </div>
  )
}
