import { useState, useEffect, useCallback } from "react";
import { useLanguage } from "@/shared/lib/i18n";
import { ALL_INDICATORS } from "@/entities/indicator";
import type { AlertThreshold } from "@/entities/alert";
import { DEFAULT_THRESHOLDS } from "@/entities/alert";
import { alertRepository } from "@/entities/alert/api/localStorageRepository.ts";
import { Modal, Button } from "@/shared/ui";

interface AlertThresholdsPanelProps {
  open: boolean;
  onClose: () => void;
}

export function AlertThresholdsPanel({ open, onClose }: AlertThresholdsPanelProps) {
  const { t } = useLanguage();
  const [thresholds, setThresholds] = useState<AlertThreshold[]>([]);

  useEffect(() => {
    if (open) {
      setThresholds(alertRepository.getThresholds());
    }
  }, [open]);

  const handleToggle = useCallback((index: number) => {
    setThresholds((prev) =>
      prev.map((t, i) => (i === index ? { ...t, enabled: !t.enabled } : t)),
    );
  }, []);

  const handleValueChange = useCallback((index: number, value: number) => {
    setThresholds((prev) =>
      prev.map((t, i) => (i === index ? { ...t, value } : t)),
    );
  }, []);

  const handleSave = useCallback(() => {
    alertRepository.setThresholds(thresholds);
    onClose();
  }, [thresholds, onClose]);

  const handleReset = useCallback(() => {
    setThresholds([...DEFAULT_THRESHOLDS]);
  }, []);

  return (
    <Modal isOpen={open} onClose={onClose} title={t("alert.thresholdsTitle")}>
      <div className="min-w-[360px]">
        <div className="flex flex-col gap-4">
          {thresholds.map((th, i) => {
            const meta = ALL_INDICATORS.find((m) => m.id === th.indicatorId);
            if (!meta) return null;
            return (
              <div
                key={th.indicatorId}
                className={`flex items-center gap-3 ${th.enabled ? "" : "opacity-50"}`}
              >
                <input
                  type="checkbox"
                  checked={th.enabled}
                  onChange={() => handleToggle(i)}
                  className="size-4 accent-primary"
                  aria-label={`Habilitar alerta para ${meta.label}`}
                />
                <div className="flex-1 min-w-0">
                  <div className="text-sm font-medium text-foreground">
                    {meta.label}
                  </div>
                  <div className="text-[11px] text-muted-foreground truncate">
                    {meta.description}
                  </div>
                </div>
                <div className="flex items-center gap-1">
                  <span className="text-[11px] text-muted-foreground">&lt;</span>
                  <input
                    type="number"
                    value={th.value}
                    onChange={(e) =>
                      handleValueChange(i, Math.max(0, Number(e.target.value)))
                    }
                    disabled={!th.enabled}
                    className="w-16 rounded-md border border-border px-2 py-1 text-sm text-center text-foreground bg-transparent disabled:opacity-50"
                    aria-label={`Valor umbral para ${meta.label}`}
                  />
                </div>
              </div>
            );
          })}
        </div>

        <div className="mt-6 flex items-center justify-between gap-2">
          <Button variant="ghost" onClick={handleReset}>
            {t("alert.resetDefaults")}
          </Button>
          <div className="flex gap-2">
            <Button variant="secondary" onClick={onClose}>
              {t("common.cancel")}
            </Button>
            <Button variant="default" onClick={handleSave}>
              {t("common.submit")}
            </Button>
          </div>
        </div>
      </div>
    </Modal>
  );
}
