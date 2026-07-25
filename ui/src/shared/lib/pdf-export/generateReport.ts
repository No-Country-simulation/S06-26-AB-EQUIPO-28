import { jsPDF } from "jspdf";
import autoTable from "jspdf-autotable";
import type { IndicatorValue } from "@/entities/indicator";
import { getIndicatorMeta } from "@/entities/indicator";
import type { AiResponse } from "@/entities/ai-agent";
import type { Region } from "@/entities/region";
import type { MentalHealthReport, RegionVulnerabilitySummary } from "@/entities/mental-health";
import type { EmployabilityGap } from "@/entities/employability";

interface ReportData {
  region: Region | null;
  indicators: IndicatorValue[];
  aiResponse: AiResponse | null;
  period: string;
  locale: string;
  mentalHealthReport: MentalHealthReport | null;
  vulnerableRegions: readonly RegionVulnerabilitySummary[];
  employabilityGaps: readonly EmployabilityGap[];
  allRegions: Region[];
}

function getRiskLabel(level: string, locale: string): string {
  const isPt = locale.startsWith("pt");
  const labels: Record<string, { es: string; pt: string }> = {
    CRITICAL: { es: "Crítico", pt: "Crítico" },
    HIGH: { es: "Alto", pt: "Alto" },
    MEDIUM: { es: "Medio", pt: "Médio" },
    LOW: { es: "Bajo", pt: "Baixo" },
    NONE: { es: "Ninguno", pt: "Nenhum" },
  };
  return labels[level]?.[isPt ? "pt" : "es"] ?? level;
}

function formatNumber(n: number, locale: string): string {
  try {
    return new Intl.NumberFormat(locale).format(n);
  } catch {
    return String(n);
  }
}

export async function generatePdfReport(data: ReportData): Promise<void> {
  const doc = new jsPDF({ unit: "mm", format: "a4" });
  // Extend doc with autoTable at runtime
  const docWithAutoTable = doc as any;
  docWithAutoTable.autoTable = autoTable;

  const pageWidth = doc.internal.pageSize.getWidth();
  const margin = 20;
  const contentWidth = pageWidth - margin * 2;

  const locale = data.locale === "pt" ? "pt-BR" : "es";
  const isPt = locale.startsWith("pt");

  const regionLabel = isPt ? "Região" : "Región";
  const municipalityLabel = isPt ? "Município" : "Municipio";
  const scoreLabel = isPt ? "Score" : "Score";
  const riskLabel = isPt ? "Risco" : "Riesgo";
  const usersLabel = isPt ? "Usuários" : "Usuarios";
  const networkLabel = isPt ? "Rede %" : "Red %";
  const congestionLabel = isPt ? "Congestão %" : "Congestión %";
  const noCoverageLabel = isPt ? "Sem cobertura" : "Sin cobertura";
  const highLabel = isPt ? "Alto" : "Alto";
  const mediumLabel = isPt ? "Médio" : "Medio";
  const lowLabel = isPt ? "Baixo" : "Bajo";

  let y = margin;

  // ── Build combined regions table data ───────────────────────
  const buildRegionsTableRows = () => {
    const rows: string[][] = [];
    const seen = new Set<string>();

    // From mental health vulnerability regions
    if (data.vulnerableRegions) {
      for (const vr of data.vulnerableRegions) {
        if (seen.has(vr.regionName)) continue;
        seen.add(vr.regionName);

        // Compute risk level from vulnerability score
        let riskLevel: string;
        if (vr.vulnerabilityScore >= 0.66) riskLevel = "CRITICAL";
        else if (vr.vulnerabilityScore >= 0.33) riskLevel = "HIGH";
        else riskLevel = "MEDIUM";
        const risk = getRiskLabel(riskLevel, locale);

        const users = formatNumber(0, locale); // totalPopulation not available in summary
        const network = `${vr.vulnerablePercentage.toFixed(1)}%`;
        const congestion = "—";
        const noCoverage = vr.connectivityLevel === "LOW" ? (isPt ? "Sim" : "Sí") : (isPt ? "Não" : "No");

        rows.push([
          vr.regionName,
          vr.regionName,
          vr.vulnerabilityScore.toFixed(2),
          risk,
          users,
          network,
          congestion,
          noCoverage,
        ]);
      }
    }

    // From employability gaps
    for (const eg of data.employabilityGaps) {
      const key = eg.cluster;
      if (seen.has(key)) continue;
      seen.add(key);

      const risk = getRiskLabel(eg.gapSeverity, locale);
      const users = formatNumber(eg.citizenCount, locale);
      const network = `${Math.round(eg.daytimeAvgUsers / Math.max(1, eg.citizenCount) * 100)}%`;
      const congestion = eg.mobilityIntensity;
      const noCoverage = !eg.hasTelemetryCoverage ? (isPt ? "Sim" : "Sí") : (isPt ? "Não" : "No");

      rows.push([
        eg.cluster,
        eg.municipalities.join(", "),
        eg.gapScore.toFixed(2),
        risk,
        users,
        network,
        congestion,
        noCoverage,
      ]);
    }

    // Fallback: all regions
    if (rows.length === 0) {
      for (const r of data.allRegions) {
        const score = (r.concentration ?? 0).toFixed(2);
        rows.push([
          r.name,
          r.name,
          score,
          "—",
          formatNumber(r.indicators?.averageUsers ?? 0, locale),
          "—",
          "—",
          "—",
        ]);
      }
    }

    return rows;
  };

  // ── 1. HEADER ───────────────────────────────────────────────
  docWithAutoTable.setFontSize(18);
  docWithAutoTable.setFont("helvetica", "bold");
  docWithAutoTable.setTextColor(29, 78, 216);
  docWithAutoTable.text("Panel BiT", margin, y);
  y += 7;

  docWithAutoTable.setFontSize(14);
  docWithAutoTable.setFont("helvetica", "normal");
  docWithAutoTable.setTextColor(55, 65, 81);
  docWithAutoTable.text(isPt ? "Relatório Territorial" : "Reporte Territorial", margin, y);
  y += 10;

  docWithAutoTable.setFontSize(9);
  docWithAutoTable.setFont("helvetica", "normal");
  docWithAutoTable.setTextColor(107, 114, 128);

  const periodLabel = isPt ? "Período" : "Período";
  const regionName = data.region?.name ?? (isPt ? "Todas as regiões" : "Todas las regiones");
  docWithAutoTable.text(`${periodLabel}: ${data.period}`, margin, y);
  y += 5;
  docWithAutoTable.text(`${isPt ? "Região" : "Región"}: ${regionName}`, margin, y);
  y += 5;
  docWithAutoTable.text(`Fuente: Vísent CDRView · Anatel · IBGE`, margin, y);
  y += 10;

  docWithAutoTable.setDrawColor(229, 231, 235);
  docWithAutoTable.line(margin, y, docWithAutoTable.internal.pageSize.getWidth() - margin, y);
  y += 8;

  // ── 2. RISK SUMMARY TABLE ──────────────────────────────────
  const buildRiskSummary = () => {
    const counts = { [highLabel]: 0, [mediumLabel]: 0, [lowLabel]: 0 };
    const rows = buildRegionsTableRows();

    for (const row of rows) {
      const risk = row[3];
      if (risk === highLabel || risk === "Crítico") counts[highLabel]++;
      else if (risk === mediumLabel) counts[mediumLabel]++;
      else if (risk === lowLabel) counts[lowLabel]++;
    }

    const total = counts[highLabel] + counts[mediumLabel] + counts[lowLabel];

    return [
      [{ content: isPt ? "Nível de risco" : "Nivel de riesgo", styles: { fontStyle: "bold" } },
       { content: isPt ? "Quantidade de zonas" : "Cantidad de zonas", styles: { fontStyle: "bold", halign: "right" } }],
      [highLabel, String(counts[highLabel])],
      [mediumLabel, String(counts[mediumLabel])],
      [lowLabel, String(counts[lowLabel])],
      [{ content: isPt ? "Total" : "Total", styles: { fontStyle: "bold" } },
       { content: String(total), styles: { fontStyle: "bold", halign: "right" } }],
    ];
  };

  const riskSummary = buildRiskSummary();
  docWithAutoTable.autoTable({
    startY: y,
    margin: { left: margin, right: margin },
    tableWidth: "wrap",
    theme: "striped",
    headStyles: { fillColor: [29, 78, 216], textColor: 255, fontSize: 9 },
    bodyStyles: { fontSize: 9, cellPadding: 4 },
    columnStyles: { 0: { fontStyle: "bold" }, 1: { halign: "right" } },
    body: riskSummary.slice(1),
    head: [riskSummary[0]],
  });

  y = docWithAutoTable.lastAutoTable.finalY + 8;

  // ── 3. DETAILED REGIONS TABLE ───────────────────────────────
  if (y > docWithAutoTable.internal.pageSize.getHeight() - 60) {
    docWithAutoTable.addPage();
    y = margin;
  }

  docWithAutoTable.setFontSize(13);
  docWithAutoTable.setFont("helvetica", "bold");
  docWithAutoTable.setTextColor(17, 24, 39);
  const detailTitle = isPt ? "Detalhe de zonas" : "Detalle de zonas";
  docWithAutoTable.text(detailTitle, margin, y);
  y += 8;

  const rows = buildRegionsTableRows();
  const displayRows = rows.slice(0, 100);

  docWithAutoTable.autoTable({
    startY: y,
    margin: { left: margin, right: margin },
    tableWidth: "auto",
    theme: "striped",
    headStyles: {
      fillColor: [29, 78, 216],
      textColor: 255,
      fontSize: 7.5,
      cellPadding: 3,
      halign: "center",
    },
    bodyStyles: {
      fontSize: 7,
      cellPadding: 2.5,
      halign: "center",
    },
    columnStyles: {
      0: { cellWidth: 30, halign: "left" },
      1: { cellWidth: 35, halign: "left" },
      2: { cellWidth: 15, halign: "center" },
      3: { cellWidth: 15, halign: "center" },
      4: { cellWidth: 20, halign: "right" },
      5: { cellWidth: 18, halign: "center" },
      6: { cellWidth: 22, halign: "center" },
      7: { cellWidth: 20, halign: "center" },
    },
    head: [[
      regionLabel,
      municipalityLabel,
      scoreLabel,
      riskLabel,
      usersLabel,
      networkLabel,
      congestionLabel,
      noCoverageLabel,
    ]],
    body: displayRows.map((row) => row.map((cell) => {
      const str = String(cell);
      return str.length > 18 ? str.slice(0, 17) + "…" : str;
    })),
    didDrawPage: (data: any) => {
      const pageCount = docWithAutoTable.getNumberOfPages();
      const currentPage = data.pageNumber;
      docWithAutoTable.setFontSize(7);
      docWithAutoTable.setFont("helvetica", "normal");
      docWithAutoTable.setTextColor(156, 163, 175);
      docWithAutoTable.text(
        `Fuente: Vísent CDRView · Anatel · IBGE  |  ${isPt ? "Página" : "Página"} ${currentPage} de ${pageCount}`,
        docWithAutoTable.internal.pageSize.getWidth() / 2,
        docWithAutoTable.internal.pageSize.getHeight() - 8,
        { align: "center" }
      );
    },
  });

  // ── 4. INDICATORS SECTION (if any) ─────────────────────────
  if (data.indicators.length > 0) {
    y = docWithAutoTable.lastAutoTable.finalY + 10;
    if (y > docWithAutoTable.internal.pageSize.getHeight() - 60) {
      docWithAutoTable.addPage();
      y = margin;
    }

    docWithAutoTable.setFontSize(13);
    docWithAutoTable.setFont("helvetica", "bold");
    docWithAutoTable.setTextColor(17, 24, 39);
    docWithAutoTable.text(isPt ? "Indicadores" : "Indicadores", margin, y);
    y += 8;

    const indRows = data.indicators.map((ind) => {
      const meta = getIndicatorMeta(ind.indicatorId);
      const trendMap: Record<string, string> = {
        IMPROVING: isPt ? "Melhorando" : "Mejorando",
        STABLE: isPt ? "Estável" : "Estable",
        DECLINING: isPt ? "Piorando" : "Deteriorando",
      };
      return [
        meta?.label ?? ind.indicatorId,
        String(ind.value),
        ind.trend ? trendMap[ind.trend] ?? ind.trend : "—",
      ];
    });

    docWithAutoTable.autoTable({
      startY: y,
      margin: { left: margin, right: margin },
      theme: "striped",
      headStyles: { fillColor: [29, 78, 216], textColor: 255, fontSize: 9 },
      bodyStyles: { fontSize: 9, cellPadding: 3 },
      head: [[isPt ? "Indicador" : "Indicador", isPt ? "Valor" : "Valor", isPt ? "Tendência" : "Tendencia"]],
      body: indRows,
    });
  }

  // ── 5. AI RESPONSE (if any) ────────────────────────────────
  if (data.aiResponse) {
    y = docWithAutoTable.lastAutoTable?.finalY ?? y;
    y += 10;
    if (y > docWithAutoTable.internal.pageSize.getHeight() - 60) {
      docWithAutoTable.addPage();
      y = margin;
    }

    docWithAutoTable.setFontSize(13);
    docWithAutoTable.setFont("helvetica", "bold");
    docWithAutoTable.setTextColor(17, 24, 39);
    docWithAutoTable.text("Análise IA", margin, y);
    y += 8;

    docWithAutoTable.setFontSize(9);
    docWithAutoTable.setFont("helvetica", "normal");
    docWithAutoTable.setTextColor(55, 65, 81);

    const lines = docWithAutoTable.splitTextToSize(data.aiResponse.summary, contentWidth);
    for (const line of lines) {
      if (y > docWithAutoTable.internal.pageSize.getHeight() - 30) {
        docWithAutoTable.addPage();
        y = margin;
      }
      docWithAutoTable.text(line, margin, y);
      y += 5;
    }
    y += 4;

    // AI data table
    if (data.aiResponse.data.length > 0) {
      const aiRows = data.aiResponse.data.map((d) => [d.region, String(d.value), d.source]);
      docWithAutoTable.autoTable({
        startY: y,
        margin: { left: margin, right: margin },
        theme: "striped",
        headStyles: { fillColor: [139, 92, 246], textColor: 255, fontSize: 9 },
        bodyStyles: { fontSize: 8, cellPadding: 3 },
        head: [[isPt ? "Região" : "Región", isPt ? "Valor" : "Valor", isPt ? "Fonte" : "Fuente"]],
        body: aiRows,
      });
    }

    // Sources
    if (data.aiResponse.sources.length > 0) {
      y = docWithAutoTable.lastAutoTable.finalY + 6;
      docWithAutoTable.setFontSize(10);
      docWithAutoTable.setFont("helvetica", "bold");
      docWithAutoTable.setTextColor(17, 24, 39);
      docWithAutoTable.text(isPt ? "Fontes" : "Fuentes", margin, y);
      y += 6;

      docWithAutoTable.setFontSize(8);
      docWithAutoTable.setFont("helvetica", "normal");
      docWithAutoTable.setTextColor(107, 114, 128);
      for (const src of data.aiResponse.sources) {
        if (y > docWithAutoTable.internal.pageSize.getHeight() - 20) {
          docWithAutoTable.addPage();
          y = margin;
        }
        docWithAutoTable.text(`- ${src}`, margin, y);
        y += 5;
      }
    }
  }

  // ── Final footer on all pages ──────────────────────────────
  const pageCount = docWithAutoTable.getNumberOfPages();
  for (let i = 1; i <= pageCount; i++) {
    docWithAutoTable.setPage(i);
    docWithAutoTable.setFontSize(7);
    docWithAutoTable.setFont("helvetica", "normal");
    docWithAutoTable.setTextColor(156, 163, 175);
    docWithAutoTable.text(
      `Fuente: Vísent CDRView · Anatel · IBGE  |  ${isPt ? "Página" : "Página"} ${i} de ${pageCount}`,
      docWithAutoTable.internal.pageSize.getWidth() / 2,
      docWithAutoTable.internal.pageSize.getHeight() - 8,
      { align: "center" }
    );
  }

  docWithAutoTable.save(`appbit-reporte-territorial-${Date.now()}.pdf`);
}