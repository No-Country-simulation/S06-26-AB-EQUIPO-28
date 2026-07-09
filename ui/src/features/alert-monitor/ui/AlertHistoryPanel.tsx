import { useState, useEffect, useCallback } from "react";
import { useLanguage } from "@/shared/lib/i18n";
import { getIndicatorMeta } from "@/entities/indicator";
import type { AlertEvent } from "@/entities/alert";
import { alertRepository } from "@/entities/alert/api/localStorageRepository.ts";
import { Modal, Button } from "@/shared/ui";
import { AlertThresholdsPanel } from "./AlertThresholdsPanel.tsx";

interface AlertHistoryPanelProps {
  open: boolean;
  onClose: () => void;
}

function formatTime(ts: number): string {
  const d = new Date(ts);
  return d.toLocaleString("es-BR", {
    day: "2-digit",
    month: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
}

export function AlertHistoryPanel({ open, onClose }: AlertHistoryPanelProps) {
  const { t } = useLanguage();
  const [history, setHistory] = useState<AlertEvent[]>([]);
  const [thresholdsOpen, setThresholdsOpen] = useState(false);

  useEffect(() => {
    if (open) {
      setHistory(alertRepository.getHistory());
    }
  }, [open]);

  const handleAcknowledge = useCallback(
    (id: string) => {
      alertRepository.acknowledgeEvent(id);
      setHistory((prev) =>
        prev.map((e) => (e.id === id ? { ...e, acknowledged: true } : e)),
      );
    },
    [],
  );

  const handleClear = useCallback(() => {
    alertRepository.clearHistory();
    setHistory([]);
  }, []);

  return (
    <>
    <Modal isOpen={open} onClose={onClose} title={t("alert.historyTitle")}>
      <div className="min-w-[400px] max-h-[70vh] flex flex-col">
        <div className="flex justify-end items-center mb-4 gap-2">
          <div className="flex gap-2">
            <Button variant="ghost" onClick={() => setThresholdsOpen(true)}>
              {t("alert.configure")}
            </Button>
            {history.length > 0 && (
              <Button variant="ghost" onClick={handleClear}>
                {t("alert.clearHistory")}
              </Button>
            )}
          </div>
        </div>

        <div className="flex-1 overflow-y-auto flex flex-col gap-2">
          {history.length === 0 && (
            <p className="text-sm text-muted-foreground text-center py-8">
              {t("alert.noHistory")}
            </p>
          )}

          {history.map((event) => {
            const meta = getIndicatorMeta(event.threshold.indicatorId);
            return (
              <div
                key={event.id}
                className={`flex items-start gap-3 p-3 rounded-lg ${
                  event.acknowledged
                    ? "bg-muted/50 border border-border"
                    : "bg-amber-50 border border-amber-200"
                }`}
              >
                <div className="flex-1 min-w-0">
                  <div className="text-xs font-semibold text-foreground">
                    {meta?.label ?? event.threshold.indicatorId}
                  </div>
                  <div className="text-[11px] text-muted-foreground mt-0.5">
                    {event.currentRegionName}
                  </div>
                  <div className="text-[11px] text-destructive mt-1">
                    {event.currentValue} &lt; {event.threshold.value}
                  </div>
                  <div className="text-[10px] text-muted-foreground mt-1">
                    {formatTime(event.triggeredAt)}
                  </div>
                </div>
                {!event.acknowledged && (
                  <button
                    type="button"
                    onClick={() => handleAcknowledge(event.id)}
                    className="shrink-0 rounded-md border border-accent px-3 py-1 text-[11px] font-medium text-accent bg-transparent cursor-pointer whitespace-nowrap hover:bg-accent/10 transition-colors"
                  >
                    {t("alert.acknowledge")}
                  </button>
                )}
              </div>
            );
          })}
        </div>

        <div className="mt-4 text-right">
          <Button variant="secondary" onClick={onClose}>
            {t("common.close")}
          </Button>
        </div>
      </div>
    </Modal>

    <AlertThresholdsPanel open={thresholdsOpen} onClose={() => setThresholdsOpen(false)} />
    </>
  );
}
