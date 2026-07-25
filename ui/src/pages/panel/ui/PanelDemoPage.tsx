"use client"

import { useState, useCallback, useMemo, useEffect, useRef, startTransition } from "react"
import {
  useLanguage,
  formatLocaleNumber,
  type Locale,
  type TranslationKey,
} from "@/shared/lib/i18n"
import { useAppContext } from "@/app/providers/app-context"
import { useRegionFilter } from "@/features/filter-by-region"
import { useAskAi } from "@/features/ask-ai-query"
import { useAlertMonitor, AlertHistoryPanel } from "@/features/alert-monitor"
import { useExportPdf } from "@/shared/lib/pdf-export"
import { Comparativo, AiQueryPanel, MentorshipGapsPanel, MentorshipProgramsPanel, EmployabilityGapsPanel, EmployabilityOdMatrixPanel, FloatingAntennaCard } from "@/widgets/panel"
import { InteractiveMapWidget } from "@/widgets/interactive-map"
import type { RegionPoint } from "@/widgets/interactive-map"
import { Card, Spinner, BackendWakingUp, Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/shared/ui"
import { MapIcon, BarChart3, Layers, TriangleAlert, Sparkles, Bell, FileDown, Users, Briefcase, Check, Search, X, Activity, Globe, Languages, ChevronDown } from "lucide-react"
import { type Period, PERIOD_TRANSLATION_KEY } from "../model/filter-bar-types.ts"
import { usePanelData } from "../model/usePanelData.ts"
import { useMentorshipData } from "../model/useMentorshipData.ts"
import { useEmployabilityData } from "../model/useEmployabilityData.ts"

const LANGUAGE_OPTIONS = [
  { value: "es", label: "Español" },
  { value: "pt", label: "Português" },
  { value: "en", label: "English" },
]

type Section =  "mapa" | "consulta" | "comparativo" | "mentorias" | "empleabilidad"
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

function isColdStartError(error: string | null): boolean {
  if (!error) return false;
  const lower = error.toLowerCase();
  return (
    lower.includes("502") ||
    lower.includes("503") ||
    lower.includes("failed to fetch") ||
    lower.includes("network error") ||
    lower.includes("could not reach the server")
  );
}

export function PanelDemoPage() {
  const { locale, setLocale, t } = useLanguage()
  const { regionRepository, aiAgentRepository, indicatorRepository, mentalHealthRepository, mobilityDataRepository, mentorshipRepository, employabilityRepository } = useAppContext()

  const { regions, selectedRegionId, setSelectedRegion, selectedRegion } = useRegionFilter(regionRepository)

  const [retryKey, setRetryKey] = useState(0)

  const { report, reportError, vulnerableRegions, indicators } = usePanelData(
    mentalHealthRepository,
    indicatorRepository,
    selectedRegionId,
    t("panel.loadReportError"),
    retryKey,
  )

  const mentorshipData = useMentorshipData(mentorshipRepository, t("panel.loadReportError"), retryKey)
  const employabilityData = useEmployabilityData(employabilityRepository, t("panel.loadReportError"), retryKey)

  const [selectedZoneName, setSelectedZoneName] = useState<string | null>(null)
  const [vista, setVista] = useState<Vista>("vulnerabilidad")
  const [section, setSection] = useState<Section>("mapa")

  const [selectedPeriod, setSelectedPeriod] = useState<Period>("morning")
  const [showAntennas, setShowAntennas] = useState(true)
  const [highConcentrationOnly, setHighConcentrationOnly] = useState(false)

  // When "Solo regiones vulnerables (≥66)" is checked, hide antennas automatically
  useEffect(() => {
    if (highConcentrationOnly) {
      setShowAntennas(false)
    }
  }, [highConcentrationOnly])

  // Initialize comparative selection with all regions when report loads
  useEffect(() => {
    if (report?.regionSummaries) {
      const allNames = report.regionSummaries.map((rs) => rs.regionName)
      setSelectedComparativeRegions((prev) => {
        if (prev.length === 0) return allNames
        return prev
      })
    }
  }, [report?.regionSummaries])
  const [alertHistoryOpen, setAlertHistoryOpen] = useState(false)
  const [selectedComparativeRegions, setSelectedComparativeRegions] = useState<string[]>([])
  const [compareSearch, setCompareSearch] = useState("")
  const [compareLoadingState, setCompareLoadingState] = useState(false)
  const [compareErrorState, setCompareErrorState] = useState<string | null>(null)
  const [compareResponseState, setCompareResponseState] = useState<ReturnType<typeof useAskAi>["response"] | null>(null)
  const [showZonesList, setShowZonesList] = useState(true)
  const [loadingMessage, setLoadingMessage] = useState("")
  const loadingTimerRef = useRef<ReturnType<typeof setInterval> | null>(null)
  const { query, setQuery, submit, cancel: cancelAiQuery, response, lastQuestion, isLoading: aiLoading, error: aiError, clearResponse } =
    useAskAi(aiAgentRepository, { region: selectedRegionId, language: locale, errorFallback: t("dashboard.error") })

  // Cycling loading messages while AI processes the query
  const [aiLoadingMessage, setAiLoadingMessage] = useState("")
  useEffect(() => {
    if (!aiLoading) {
      setAiLoadingMessage("")
      return
    }
    const messages = [
      t("panel.queryLoading1"),
      t("panel.queryLoading2"),
      t("panel.queryLoading3"),
      t("panel.queryLoading4"),
      t("panel.queryLoading5"),
      t("panel.queryLoading6"),
    ]
    let idx = 0
    setAiLoadingMessage(messages[0])
    const timer = setInterval(() => {
      idx = (idx + 1) % messages.length
      setAiLoadingMessage(messages[idx])
    }, 4000)
    return () => clearInterval(timer)
  }, [aiLoading, t])

  const { unacknowledgedCount } = useAlertMonitor(indicators, selectedRegionId, selectedRegion?.name)

  const { exportPdf, exporting } = useExportPdf({
    region: selectedRegion,
    indicators,
    aiResponse: response,
    period: selectedPeriod,
    locale,
    mentalHealthReport: report,
    vulnerableRegions: vulnerableRegions,
    employabilityGaps: employabilityData.gaps,
    allRegions: regions,
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

  const allComparativoRegiones = (report?.regionSummaries ?? []).map((rs) => ({
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

  const comparativoRegiones = allComparativoRegiones.filter((r) => selectedComparativeRegions.includes(r.id))

  const vulnerabilityScoreLabel = t("panel.metric.vulnerabilityScore")
  const vulnerablePctLabel = t("panel.metric.vulnerablePct")
  const priorityLabel = t("panel.metric.priority")

  const allChartData = (report?.regionSummaries ?? []).map((rs) => ({
    indicador: rs.regionName,
    [vulnerabilityScoreLabel]: rs.vulnerabilityScore,
    [vulnerablePctLabel]: rs.vulnerablePercentage,
    [priorityLabel]: rs.isPriorityForIntervention ? 100 : 0,
  }))

  const chartData = allChartData.filter((d) => selectedComparativeRegions.includes(d.indicador as string))

  const toggleComparativeRegion = useCallback((regionName: string) => {
    setSelectedComparativeRegions((prev) =>
      prev.includes(regionName) ? prev.filter((r) => r !== regionName) : [...prev, regionName]
    )
  }, [])

  const allRegionsSelected = selectedComparativeRegions.length === allComparativoRegiones.length && allComparativoRegiones.length > 0

  const handleSelectAllRegions = useCallback(() => {
    if (allRegionsSelected) {
      setSelectedComparativeRegions([])
    } else {
      setSelectedComparativeRegions(allComparativoRegiones.map((r) => r.id))
    }
  }, [allRegionsSelected, allComparativoRegiones])

  const filteredComparativeRegions = useMemo(() => {
    if (!compareSearch.trim()) return allComparativoRegiones
    const term = compareSearch.toLowerCase()
    return allComparativoRegiones.filter((r) => r.label.toLowerCase().includes(term))
  }, [allComparativoRegiones, compareSearch])

  // Cycling loading messages while AI compares
  useEffect(() => {
    if (!compareLoadingState) {
      if (loadingTimerRef.current) {
        clearInterval(loadingTimerRef.current)
        loadingTimerRef.current = null
      }
      return
    }
    const count = comparativoRegiones.length
    const messages = [
      `${t("panel.comparative.loading1", { count })}`,
      t("panel.comparative.loading2"),
      t("panel.comparative.loading3"),
      t("panel.comparative.loading4"),
      t("panel.comparative.loading5"),
      t("panel.comparative.loading6"),
    ]
    let idx = 0
    setLoadingMessage(messages[0])
    loadingTimerRef.current = setInterval(() => {
      idx = (idx + 1) % messages.length
      setLoadingMessage(messages[idx])
    }, 4000)
    return () => {
      if (loadingTimerRef.current) {
        clearInterval(loadingTimerRef.current)
        loadingTimerRef.current = null
      }
    }
  }, [compareLoadingState, comparativoRegiones.length, t])

  const handleAiCompare = useCallback(async () => {
    if (comparativoRegiones.length < 2) return
    const regionNames = comparativoRegiones.map((r) => r.label).join(", ")
    const prompt = t("panel.comparative.aiPrompt", { regions: regionNames })

    setCompareLoadingState(true)
    setCompareErrorState(null)

    try {
      const result = await aiAgentRepository.askQuery({
        question: prompt,
        language: locale,
      })
      setCompareResponseState(result)
    } catch (err) {
      const message = err instanceof Error ? err.message : t("dashboard.error")
      setCompareErrorState(message)
    } finally {
      setCompareLoadingState(false)
    }
  }, [comparativoRegiones, t, aiAgentRepository, locale])

  const clearCompareResponse = useCallback(() => {
    setCompareResponseState(null)
    setCompareErrorState(null)
  }, [])

  const comparePanelResponse = compareResponseState
    ? {
        respuesta_ia: compareResponseState.summary,
        titulo: t("panel.comparative.aiCompare"),
        metrica: t("panel.comparative.aiCompare"),
        datos: comparativoRegiones.map((r) => ({
          region: r.label,
          valor: r.metricas[0]?.value ?? "—",
          fuente: compareResponseState.sources[0] ?? "—",
        })),
        fuentes: [...compareResponseState.sources],
      }
    : null

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

  const showComparativo = allComparativoRegiones.length >= 2

  const sidebarItems: { key: Section; icon: React.ComponentType<{ className?: string }>; label: string; hidden?: boolean }[] = [
    { key: "mapa", icon: MapIcon, label: t("panel.map") },
    { key: "consulta", icon: Sparkles, label: t("panel.query") },
    { key: "comparativo", icon: BarChart3, label: t("panel.comparative"), hidden: !showComparativo },
    { key: "mentorias", icon: Users, label: t("panel.mentorship") },
    { key: "empleabilidad", icon: Briefcase, label: t("panel.employability") },
  ]

  if (reportError && regions.length === 0) {
    if (isColdStartError(reportError)) {
      return (
        <BackendWakingUp
          message={t("panel.backendWakingUp")}
          subMessage={t("panel.backendWakingUpSub")}
          retryLabel={t("panel.retry")}
          onRetry={() => startTransition(() => setRetryKey((k) => k + 1))}
        />
      );
    }
    return (
      <div className="flex min-h-screen flex-col items-center justify-center gap-3 p-6 text-center">
        <TriangleAlert className="h-8 w-8 text-destructive" />
        <p className="text-sm text-destructive">{reportError}</p>
      </div>
    );
  }

  return (
    <div className="relative h-dvh w-full overflow-hidden bg-background">
      {/* ── Full-screen map (always rendered) ── */}
      <div className="absolute inset-0 z-0">
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

      {/* ── Dim overlay for non-map sections ── */}
      {section !== "mapa" && (
        <div className="absolute inset-0 z-10 bg-background/80 backdrop-blur-sm" />
      )}

      {/* ── Floating top bar ── */}
      <div className="absolute inset-x-0 top-0 z-30 border-b border-border/50 bg-background/80 backdrop-blur-md">
        <div className="mx-auto flex max-w-screen-2xl flex-wrap items-center gap-3 px-4 py-2.5 md:px-6">
          <div className="flex items-center gap-2.5">
            <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-primary text-primary-foreground">
              <Activity className="h-4 w-4" />
            </div>
            <div className="min-w-0">
              <h1 className="truncate text-sm font-semibold leading-tight">{t("panel.title")}</h1>
              <p className="hidden truncate text-[10px] text-muted-foreground sm:block">{t("panel.subtitle")}</p>
            </div>
          </div>

          <div className="ml-auto flex flex-wrap items-center gap-2">
            <Select
              items={regionOptions.map((r) => ({ value: r.id, label: r.label }))}
              value={selectedRegionId ?? regionOptions[0]?.id ?? ""}
              onValueChange={(v: string | null) => v && setSelectedRegion(v)}
            >
              <SelectTrigger className="w-37.5 sm:w-52.5" aria-label={t("panel.region")}>
                <Globe className="h-4 w-4 shrink-0 text-primary" />
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {regionOptions.map((r) => (
                  <SelectItem key={r.id} value={r.id}>
                    {r.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>

            <Select
              items={LANGUAGE_OPTIONS}
              value={locale}
              onValueChange={(v: string | null) => v && setLocale(v as Locale)}
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

            <button
              type="button"
              onClick={exportPdf}
              disabled={exporting}
              aria-label={t("export.label")}
              title={t("export.label")}
              className="inline-flex h-8 items-center gap-1.5 rounded-lg border border-border bg-card px-2.5 text-xs font-medium text-foreground transition-colors hover:bg-muted disabled:cursor-not-allowed disabled:opacity-50"
            >
              <FileDown className="h-3.5 w-3.5 shrink-0" />
              <span className="hidden sm:inline whitespace-nowrap">{exporting ? t("common.loading") : t("export.label")}</span>
            </button>

            <button
              type="button"
              onClick={() => setAlertHistoryOpen(true)}
              aria-label={t("alert.bellLabel")}
              title={t("alert.history")}
              className="relative inline-flex h-8 w-8 items-center justify-center rounded-lg border border-border bg-card text-muted-foreground transition-colors hover:bg-muted hover:text-foreground"
            >
              <Bell className="h-3.5 w-3.5" />
              {unacknowledgedCount > 0 && (
                <span className="absolute -top-1 -right-1 inline-flex h-4 min-w-4 items-center justify-center rounded-full bg-destructive px-1 text-[10px] font-bold leading-4 text-destructive-foreground">
                  {unacknowledgedCount > 9 ? "9+" : unacknowledgedCount}
                </span>
              )}
            </button>
          </div>
        </div>
      </div>

      <AlertHistoryPanel open={alertHistoryOpen} onClose={() => setAlertHistoryOpen(false)} />

      {/* ── Floating tab bar ── */}
      <div className="absolute inset-x-0 top-14.25 z-30 border-b border-border/50 bg-background/80 backdrop-blur-md">
        <div className="mx-auto flex max-w-screen-2xl items-center gap-1 overflow-x-auto px-4 py-1.5 md:px-6">
          {sidebarItems
            .filter((item) => !item.hidden)
            .map((item) => (
              <button
                key={item.key}
                onClick={() => setSection(item.key)}
                className={`inline-flex shrink-0 items-center gap-1.5 rounded-lg px-3 py-1.5 text-xs font-medium transition-colors ${
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
      </div>

      {/* ── Floating map controls (period + filters) ── */}
      {section === "mapa" && (
        <div className="absolute left-4 top-27 z-20 flex flex-col gap-2 md:left-6">
          {/* Indicator selector */}
          <div className="rounded-xl border border-border/50 bg-background/80 p-2.5 backdrop-blur-md shadow-lg">
            <div className="mb-2 flex items-center gap-1.5 text-xs font-medium text-muted-foreground">
              <Layers className="h-3.5 w-3.5 text-primary" /> {t("panel.visualization")}
            </div>
            <Select
              items={vistas.map((v) => ({ value: v.key, label: v.label }))}
              value={vista}
              onValueChange={(v: string | null) => v && setVista(v as Vista)}
            >
              <SelectTrigger className="w-full" aria-label={t("panel.indicatorToView")}>
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

          {/* Period buttons */}
          <div className="rounded-xl border border-border/50 bg-background/80 p-2 backdrop-blur-md shadow-lg">
            <div className="mb-1.5 text-[10px] font-medium text-muted-foreground uppercase tracking-wide">
              {t("panel.period")}
            </div>
            <div className="flex gap-1">
              {(["dawn", "morning", "afternoon", "night"] as const).map((period) => (
                <button
                  key={period}
                  type="button"
                  onClick={() => setSelectedPeriod(period)}
                  aria-pressed={selectedPeriod === period}
                  className={`inline-flex h-8 flex-1 items-center justify-center rounded-lg border px-2 text-[11px] font-medium transition-colors ${
                    selectedPeriod === period
                      ? "border-accent bg-accent text-accent-foreground"
                      : "border-border bg-card text-muted-foreground hover:bg-muted hover:border-ring"
                  }`}
                >
                  {t(PERIOD_TRANSLATION_KEY[period] as TranslationKey)}
                </button>
              ))}
            </div>
          </div>

          {/* Filter checkboxes */}
          <div className="rounded-xl border border-border/50 bg-background/80 p-2.5 backdrop-blur-md shadow-lg space-y-2">
            <label className="flex items-center gap-2 text-xs font-medium text-foreground cursor-pointer">
              <input
                type="checkbox"
                checked={showAntennas}
                onChange={(e) => setShowAntennas(e.target.checked)}
                className="h-3.5 w-3.5 rounded border-border accent-accent"
              />
              {t("map.showAntennas")}
            </label>
            <label className="flex items-center gap-2 text-xs font-medium text-foreground cursor-pointer">
              <input
                type="checkbox"
                checked={highConcentrationOnly}
                onChange={(e) => setHighConcentrationOnly(e.target.checked)}
                className="h-3.5 w-3.5 rounded border-border accent-accent"
              />
              {t("map.highConcentrationOnly")}
            </label>
          </div>
        </div>
      )}

      {/* ── Floating antenna detail card (right side) ── */}
      {section === "mapa" && selectedMapPoint && (
        <div className="absolute right-4 top-27 z-20 md:right-6">
          <FloatingAntennaCard
            nombre={selectedMapPoint.nombre}
            municipio={selectedMapPoint.nombre !== selectedRegion?.name ? selectedRegion?.name : undefined}
            poblacion={selectedVulnDetail ? formatLocaleNumber(selectedVulnDetail.totalPopulation, locale) : selectedMapPoint?.poblacion ? formatLocaleNumber(selectedMapPoint.poblacion, locale) : undefined}
            cobertura={selectedMapPoint.cobertura}
            brecha={selectedMapPoint.brecha}
            antenas={selectedRegionForDetail ? String(selectedRegionForDetail.indicators.antennas) : undefined}
            indicadores={detailIndicadores}
            onClose={() => setSelectedZoneName(null)}
          />
        </div>
      )}

      {/* ── Floating zones list (bottom right) ── */}
      {section === "mapa" && (
        <div className="absolute bottom-16 right-4 z-20 hidden max-h-[40vh] w-64 overflow-hidden md:right-6 lg:block">
          <div className="rounded-xl border border-border/50 bg-background/80 backdrop-blur-md shadow-lg">
            <button
              type="button"
              onClick={() => setShowZonesList((v) => !v)}
              className="flex w-full items-center justify-between gap-2 border-b border-border/50 px-3 py-2 text-left"
            >
              <div className="flex items-center gap-2">
                <TriangleAlert className="h-3.5 w-3.5 text-alert" />
                <h3 className="text-xs font-semibold">{t("panel.vulnerableRegions.title")}</h3>
              </div>
              <ChevronDown className={`h-3.5 w-3.5 text-muted-foreground transition-transform ${showZonesList ? "" : "-rotate-90"}`} />
            </button>
            {showZonesList && (
              <div className="max-h-[calc(40vh-40px)] overflow-y-auto p-1.5">
                {zonaItems.map((z) => (
                  <button
                    key={z.id}
                    onClick={() => setSelectedZoneName(z.id)}
                    className={`flex w-full items-center justify-between rounded-lg px-2.5 py-1.5 text-left text-xs transition-colors ${
                      selectedZoneName === z.id
                        ? "bg-primary/10 text-primary"
                        : "text-muted-foreground hover:bg-muted"
                    }`}
                  >
                    <span className="truncate">{z.nombre}</span>
                    <span className="ml-2 shrink-0 font-mono text-[10px]">{z.brecha}</span>
                  </button>
                ))}
              </div>
            )}
          </div>
        </div>
      )}

      {/* ── Non-map section overlays ── */}
      {section !== "mapa" && (
        <div className="absolute inset-x-0 bottom-0 top-23.75 z-20 overflow-y-auto">
          <div className="mx-auto max-w-3xl px-4 py-6 md:px-6">


            {section === "consulta" && (
              <AiQueryPanel
                consulta={query}
                onConsultaChange={setQuery}
                onSubmit={handleAiSubmit}
                loading={aiLoading}
                loadingMessage={aiLoadingMessage}
                onCancel={cancelAiQuery}
                error={aiError}
                response={aiPanelResponse}
                pregunta={lastQuestion}
                onClear={clearResponse}
                sugerencias={sugerencias}
              />
            )}

            {section === "comparativo" && showComparativo && (
              <div className="flex flex-col gap-4">
                <div>
                  <h1 className="text-lg font-semibold text-foreground">{t("panel.comparative.title")}</h1>
                  <p className="text-xs text-muted-foreground">{t("panel.comparative.description")}</p>
                </div>

                <Card className="p-4">
                  <div className="mb-3 flex flex-wrap items-center gap-2">
                    <div className="relative flex-1 min-w-50 max-w-xs">
                      <Search className="pointer-events-none absolute left-2.5 top-1/2 h-3.5 w-3.5 -translate-y-1/2 text-muted-foreground" />
                      <input
                        type="text"
                        placeholder={t("panel.comparative.searchPlaceholder")}
                        value={compareSearch}
                        onChange={(e) => setCompareSearch(e.target.value)}
                        className="w-full rounded-lg border border-border bg-background py-1.5 pl-8 pr-8 text-xs text-foreground outline-none placeholder:text-muted-foreground focus:border-primary focus:ring-1 focus:ring-primary/30"
                      />
                      {compareSearch && (
                        <button
                          type="button"
                          onClick={() => setCompareSearch("")}
                          className="absolute right-2 top-1/2 -translate-y-1/2 rounded-full p-0.5 text-muted-foreground hover:text-foreground"
                        >
                          <X className="h-3 w-3" />
                        </button>
                      )}
                    </div>
                    <button
                      type="button"
                      onClick={handleSelectAllRegions}
                      className="inline-flex items-center gap-1 rounded-full border border-border bg-card px-2.5 py-1 text-xs font-medium text-muted-foreground transition-colors hover:bg-muted hover:text-foreground"
                    >
                      {allRegionsSelected ? t("panel.comparative.clearAll") : t("panel.comparative.selectAll")}
                    </button>
                  </div>
                  <div className="flex flex-wrap gap-2">
                    {filteredComparativeRegions.map((r) => {
                      const selected = selectedComparativeRegions.includes(r.id)
                      return (
                        <button
                          key={r.id}
                          type="button"
                          onClick={() => toggleComparativeRegion(r.id)}
                          className={`inline-flex items-center gap-1.5 rounded-full border px-3 py-1 text-xs font-medium transition-colors ${
                            selected
                              ? "border-primary bg-primary/10 text-primary"
                              : "border-border bg-card text-muted-foreground hover:bg-muted"
                          }`}
                        >
                          {selected && <Check className="h-3 w-3" />}
                          {r.label}
                        </button>
                      )
                    })}
                  </div>
                  {selectedComparativeRegions.length > 0 && selectedComparativeRegions.length < 2 && (
                    <p className="mt-2 text-[10px] text-muted-foreground">{t("panel.comparative.minRegions")}</p>
                  )}
                </Card>

                <div className="flex flex-wrap items-center justify-between gap-2 rounded-xl border border-border bg-muted/30 px-4 py-3">
                  <div className="flex items-center gap-2 text-sm">
                    <span className="font-semibold text-foreground">
                      {selectedComparativeRegions.length > 0
                        ? `${selectedComparativeRegions.length} ${selectedComparativeRegions.length === 1 ? t("panel.comparative.regionSelected") : t("panel.comparative.regionsSelected")}`
                        : t("panel.comparative.noneSelected")}
                    </span>
                    {selectedComparativeRegions.length > 0 && selectedComparativeRegions.length <= 3 && (
                      <>
                        <span className="text-muted-foreground">·</span>
                        <span className="text-muted-foreground text-xs">
                          {selectedComparativeRegions.map((id) => allComparativoRegiones.find((r) => r.id === id)?.label).filter(Boolean).join(" · ")}
                        </span>
                      </>
                    )}
                    {selectedComparativeRegions.length > 3 && (
                      <>
                        <span className="text-muted-foreground">·</span>
                        <span className="text-muted-foreground text-xs">
                          {selectedComparativeRegions.slice(0, 3).map((id) => allComparativoRegiones.find((r) => r.id === id)?.label).filter(Boolean).join(" · ")} · +{selectedComparativeRegions.length - 3}
                        </span>
                      </>
                    )}
                  </div>
                  <button
                    type="button"
                    onClick={handleAiCompare}
                    disabled={selectedComparativeRegions.length < 2 || compareLoadingState}
                    className="inline-flex items-center gap-1.5 rounded-lg bg-primary px-4 py-2 text-xs font-medium text-primary-foreground transition-colors hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-50"
                  >
                    {compareLoadingState ? (
                      <svg className="h-3.5 w-3.5 animate-spin" viewBox="0 0 24 24" fill="none">
                        <circle cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="3" opacity="0.25" />
                        <path d="M12 2a10 10 0 0 1 10 10" stroke="currentColor" strokeWidth="3" strokeLinecap="round" />
                      </svg>
                    ) : (
                      <Sparkles className="h-3.5 w-3.5" />
                    )}
                    {compareLoadingState ? loadingMessage : t("panel.comparative.aiCompare")}
                  </button>
                </div>

                {comparativoRegiones.length >= 2 && (
                  <Comparativo regiones={comparativoRegiones} chartData={chartData} />
                )}

                {comparativoRegiones.length < 2 && selectedComparativeRegions.length >= 2 && (
                  <div className="flex flex-col items-center justify-center py-8 text-center">
                    <p className="text-sm text-muted-foreground">{t("panel.comparative.minRegions")}</p>
                  </div>
                )}

                {compareErrorState && (
                  <div className="rounded-xl border border-destructive/30 bg-destructive/5 p-4">
                    <p className="text-sm text-destructive">{compareErrorState}</p>
                  </div>
                )}

                {comparePanelResponse && (
                  <div className="rounded-xl border border-border bg-card p-5">
                    <div className="mb-4 flex items-center gap-2">
                      <div className="flex h-7 w-7 items-center justify-center rounded-full bg-primary/15">
                        <Sparkles className="h-3.5 w-3.5 text-primary" />
                      </div>
                      <div>
                        <h3 className="text-sm font-semibold">{t("panel.comparative.aiCompare")}</h3>
                        <p className="text-[10px] text-muted-foreground">{t("panel.comparative.description")}</p>
                      </div>
                      <button type="button" onClick={clearCompareResponse} className="ml-auto text-muted-foreground hover:text-foreground transition-colors">
                        <X className="h-4 w-4" />
                      </button>
                    </div>

                    {comparePanelResponse.respuesta_ia && (
                      <div className="mb-4 rounded-lg bg-primary/5 p-4 text-sm leading-relaxed text-foreground">
                        {comparePanelResponse.respuesta_ia}
                      </div>
                    )}

                    {comparePanelResponse.datos && comparePanelResponse.datos.length > 0 && (
                      <div className="overflow-hidden rounded-lg border border-border">
                        <table className="w-full text-sm">
                          <thead className="bg-muted/50 text-xs text-muted-foreground">
                            <tr>
                              <th className="px-3 py-2 text-left font-medium">{t("panel.queryTable.region")}</th>
                              <th className="px-3 py-2 text-right font-medium">{t("panel.queryTable.value")}</th>
                              <th className="px-3 py-2 text-left font-medium">{t("panel.queryTable.source")}</th>
                            </tr>
                          </thead>
                          <tbody>
                            {comparePanelResponse.datos.map((d, i) => (
                              <tr key={d.region} className={i % 2 ? "bg-background/40" : ""}>
                                <td className="px-3 py-2 font-medium">{d.region}</td>
                                <td className="px-3 py-2 text-right font-mono">{d.valor}</td>
                                <td className="px-3 py-2 text-xs text-muted-foreground">{d.fuente}</td>
                              </tr>
                            ))}
                          </tbody>
                        </table>
                      </div>
                    )}

                    {comparePanelResponse.fuentes && comparePanelResponse.fuentes.length > 0 && (
                      <div className="mt-3 flex flex-wrap items-center gap-1.5">
                        <span className="text-xs text-muted-foreground">{t("panel.querySources")}</span>
                        {comparePanelResponse.fuentes.map((f) => (
                          <span key={f} className="inline-flex items-center rounded-full border border-border bg-card px-2 py-0.5 text-[10px] font-medium text-muted-foreground">
                            {f}
                          </span>
                        ))}
                      </div>
                    )}
                  </div>
                )}
              </div>
            )}

            {section === "mentorias" && (
              <div>
                <div className="mb-3">
                  <h1 className="text-lg font-semibold text-foreground">{t("panel.mentorship.title")}</h1>
                </div>
                {mentorshipData.loading ? (
                  <div className="flex items-center justify-center py-12">
                    <Spinner size="lg" />
                  </div>
                ) : mentorshipData.error && mentorshipData.gaps.length === 0 && mentorshipData.programs.length === 0 ? (
                  isColdStartError(mentorshipData.error) ? (
                    <BackendWakingUp
                      message={t("panel.backendWakingUp")}
                      subMessage={t("panel.backendWakingUpSub")}
                      retryLabel={t("panel.retry")}
                      onRetry={() => startTransition(() => setRetryKey((k) => k + 1))}
                    />
                  ) : (
                    <div className="flex flex-col items-center justify-center gap-3 py-12 text-center">
                      <TriangleAlert className="h-8 w-8 text-destructive" />
                      <p className="text-sm text-destructive">{mentorshipData.error}</p>
                    </div>
                  )
                ) : (
                  <div className="grid gap-4 lg:grid-cols-2">
                    <MentorshipGapsPanel gaps={mentorshipData.gaps} />
                    <MentorshipProgramsPanel programs={mentorshipData.programs} clusters={mentorshipData.clusters} />
                  </div>
                )}
              </div>
            )}

            {section === "empleabilidad" && (
              <div>
                <div className="mb-3">
                  <h1 className="text-lg font-semibold text-foreground">{t("panel.employability.title")}</h1>
                </div>
                {employabilityData.loading ? (
                  <div className="flex items-center justify-center py-12">
                    <Spinner size="lg" />
                  </div>
                ) : employabilityData.error && employabilityData.gaps.length === 0 && employabilityData.odMatrix.length === 0 ? (
                  isColdStartError(employabilityData.error) ? (
                    <BackendWakingUp
                      message={t("panel.backendWakingUp")}
                      subMessage={t("panel.backendWakingUpSub")}
                      retryLabel={t("panel.retry")}
                      onRetry={() => startTransition(() => setRetryKey((k) => k + 1))}
                    />
                  ) : (
                    <div className="flex flex-col items-center justify-center gap-3 py-12 text-center">
                      <TriangleAlert className="h-8 w-8 text-destructive" />
                      <p className="text-sm text-destructive">{employabilityData.error}</p>
                    </div>
                  )
                ) : (
                  <div className="grid gap-4 lg:grid-cols-2">
                    <EmployabilityGapsPanel gaps={employabilityData.gaps} />
                    <EmployabilityOdMatrixPanel odMatrix={employabilityData.odMatrix} />
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      )}

      {/* ── Footer ── */}
      <div className="absolute inset-x-0 bottom-0 z-20 border-t border-border/50 bg-background/80 backdrop-blur-md">
        <div className="mx-auto max-w-screen-2xl px-4 py-2 text-[10px] text-muted-foreground md:px-6">
          {t("panel.footer", {
            period: report?.reportPeriod ?? "—",
            population: report ? formatLocaleNumber(report.metadata.totalPopulation, locale) : "—",
          })}
        </div>
      </div>
    </div>
  )
}
