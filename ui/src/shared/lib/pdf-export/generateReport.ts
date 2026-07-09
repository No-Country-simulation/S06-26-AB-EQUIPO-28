import { jsPDF } from "jspdf";
import type { IndicatorValue } from "@/entities/indicator";
import { getIndicatorMeta } from "@/entities/indicator";
import type { AiResponse } from "@/entities/ai-agent";
import type { Region } from "@/entities/region";

interface ReportData {
  region: Region | null;
  indicators: IndicatorValue[];
  aiResponse: AiResponse | null;
  period: string;
  locale: string;
}

function getLocaleDate(locale: string): string {
  try {
    return new Intl.DateTimeFormat(locale, {
      dateStyle: "long",
      timeStyle: "short",
    }).format(new Date());
  } catch {
    return new Date().toLocaleString();
  }
}

export async function generatePdfReport(data: ReportData): Promise<void> {
  const doc = new jsPDF({ unit: "mm", format: "a4" });
  const pageWidth = doc.internal.pageSize.getWidth();
  const margin = 20;
  const contentWidth = pageWidth - margin * 2;
  let y = margin;

  const locale = data.locale === "pt" ? "pt-BR" : "es";
  const col1 = margin;
  const col2 = margin + 90;
  const rowH = 7;

  // ── Header ──────────────────────────────────────────────
  doc.setFontSize(22);
  doc.setFont("helvetica", "bold");
  doc.setTextColor("#1D4ED8");
  doc.text("App BiT", margin, y);
  y += 8;

  doc.setFontSize(10);
  doc.setFont("helvetica", "normal");
  doc.setTextColor("#6B7280");
  doc.text(getLocaleDate(locale), margin, y);
  y += 12;

  // ── Separator ──────────────────────────────────────────
  doc.setDrawColor("#E5E7EB");
  doc.line(margin, y, pageWidth - margin, y);
  y += 8;

  // ── Region section ─────────────────────────────────────
  doc.setFontSize(14);
  doc.setFont("helvetica", "bold");
  doc.setTextColor("#111");
  const regionLabel = locale === "pt-BR" ? "Região" : "Región";
  doc.text(`${regionLabel}: ${data.region?.name ?? "Todas"}`, margin, y);
  y += 8;

  if (data.region) {
    doc.setFontSize(10);
    doc.setFont("helvetica", "normal");
    doc.setTextColor("#374151");

    const popLabel = locale === "pt-BR" ? "População" : "Población";
    const connLabel = locale === "pt-BR" ? "Conectividade" : "Conectividad";
    const concLabel = locale === "pt-BR" ? "Concentração" : "Concentración";

    doc.text(`${popLabel}: ${data.region.indicators.averageUsers}`, margin, y);
    y += 6;
    doc.text(`${connLabel}: ${data.region.connectivity}/100`, margin, y);
    y += 6;
    doc.text(`${concLabel}: ${data.region.concentration}/100`, margin, y);
    y += 6;
    y += 4;
  }

  // ── Period ─────────────────────────────────────────────
  doc.setFontSize(10);
  doc.setFont("helvetica", "normal");
  doc.setTextColor("#6B7280");
  const periodLabel = locale === "pt-BR" ? "Período" : "Período";
  doc.text(`${periodLabel}: ${data.period}`, margin, y);
  y += 10;

  // ── Indicators section ────────────────────────────────
  if (data.indicators.length > 0) {
    const indTitle = locale === "pt-BR" ? "Indicadores" : "Indicadores";
    doc.setFontSize(13);
    doc.setFont("helvetica", "bold");
    doc.setTextColor("#111");
    doc.text(indTitle, margin, y);
    y += 8;

    // Table header
    const col1 = margin;
    const col2 = margin + 90;
    const col3 = margin + 140;
    const rowH = 7;

    doc.setFontSize(9);
    doc.setFont("helvetica", "bold");
    doc.setTextColor("#6B7280");

    const hLabel = locale === "pt-BR" ? "Indicador" : "Indicador";
    const vLabel = locale === "pt-BR" ? "Valor" : "Valor";
    const tLabel = locale === "pt-BR" ? "Tendência" : "Tendencia";

    // Header background
    doc.setFillColor("#F9FAFB");
    doc.rect(margin, y - 4, contentWidth, rowH, "F");

    doc.text(hLabel, col1, y);
    doc.text(vLabel, col2, y);
    doc.text(tLabel, col3, y);
    y += rowH;

    // Table rows
    doc.setFont("helvetica", "normal");
    doc.setTextColor("#374151");
    doc.setFontSize(9);

    for (const ind of data.indicators) {
      const meta = getIndicatorMeta(ind.indicatorId);
      const trendMap: Record<string, string> = {
        IMPROVING: locale === "pt-BR" ? "Melhorando" : "Mejorando",
        STABLE: locale === "pt-BR" ? "Estável" : "Estable",
        DECLINING: locale === "pt-BR" ? "Piorando" : "Deteriorando",
      };

      doc.text(meta?.label ?? ind.indicatorId, col1, y);
      doc.text(String(ind.value), col2, y);
      doc.text(ind.trend ? trendMap[ind.trend] ?? ind.trend : "-", col3, y);
      y += rowH;

      // Check page break
      if (y > 270) {
        doc.addPage();
        y = margin;
      }
    }
    y += 6;
  }

  // ── AI Response ───────────────────────────────────────
  if (data.aiResponse) {
    if (y > 240) {
      doc.addPage();
      y = margin;
    }

    const aiTitle = "Análise IA";
    doc.setFontSize(13);
    doc.setFont("helvetica", "bold");
    doc.setTextColor("#111");
    doc.text(aiTitle, margin, y);
    y += 8;

    doc.setFontSize(9);
    doc.setFont("helvetica", "normal");
    doc.setTextColor("#374151");

    const lines = doc.splitTextToSize(data.aiResponse.summary, contentWidth);
    for (const line of lines) {
      if (y > 275) {
        doc.addPage();
        y = margin;
      }
      doc.text(line, margin, y);
      y += 5;
    }
    y += 4;

    // AI data table
    if (data.aiResponse.data.length > 0) {
      const rLabel = locale === "pt-BR" ? "Região" : "Región";
      const vLabel = locale === "pt-BR" ? "Valor" : "Valor";

      doc.setFontSize(9);
      doc.setFont("helvetica", "bold");
      doc.setTextColor("#6B7280");

      doc.setFillColor("#F9FAFB");
      doc.rect(margin, y - 4, contentWidth, rowH, "F");

      doc.text(rLabel, col1, y);
      doc.text(vLabel, col2, y);
      y += rowH;

      doc.setFont("helvetica", "normal");
      doc.setTextColor("#374151");

      for (const item of data.aiResponse.data) {
        if (y > 275) {
          doc.addPage();
          y = margin;
        }
        doc.text(item.region, col1, y);
        doc.text(String(item.value), col2, y);
        y += rowH;
      }
      y += 4;
    }

    // Sources
    if (data.aiResponse.sources.length > 0) {
      if (y > 260) {
        doc.addPage();
        y = margin;
      }
      const srcLabel = locale === "pt-BR" ? "Fontes" : "Fuentes";
      doc.setFontSize(10);
      doc.setFont("helvetica", "bold");
      doc.setTextColor("#111");
      doc.text(srcLabel, margin, y);
      y += 6;

      doc.setFontSize(8);
      doc.setFont("helvetica", "normal");
      doc.setTextColor("#6B7280");
      for (const src of data.aiResponse.sources) {
        if (y > 280) {
          doc.addPage();
          y = margin;
        }
        doc.text(`- ${src}`, margin, y);
        y += 5;
      }
    }
  }

  // ── Footer ────────────────────────────────────────────
  const pageCount = doc.getNumberOfPages();
  for (let i = 1; i <= pageCount; i++) {
    doc.setPage(i);
    doc.setFontSize(8);
    doc.setFont("helvetica", "normal");
    doc.setTextColor("#9CA3AF");
    doc.text(
      `App BiT — ${getLocaleDate(locale)} — Página ${i} de ${pageCount}`,
      margin,
      290,
    );
  }

  doc.save(`appbit-reporte-${Date.now()}.pdf`);
}
