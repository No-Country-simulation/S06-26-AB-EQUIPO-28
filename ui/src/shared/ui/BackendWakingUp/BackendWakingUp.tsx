import { Spinner } from "../Spinner";
import { cn } from "@/shared/lib/cn";
import { RefreshCw } from "lucide-react";

interface BackendWakingUpProps {
  message?: string;
  subMessage?: string;
  onRetry?: () => void;
  retryLabel?: string;
  className?: string;
}

export function BackendWakingUp({
  message = "Conectando con el servidor...",
  subMessage = "El servicio puede tardar unos segundos en iniciar",
  onRetry,
  retryLabel = "Reintentar",
  className,
}: BackendWakingUpProps) {
  return (
    <div
      className={cn(
        "flex min-h-[60vh] flex-col items-center justify-center gap-4 p-6 text-center",
        className,
      )}
    >
      <Spinner size="lg" label={message} />
      <p className="text-sm font-medium text-foreground">{message}</p>
      <p className="text-xs text-muted-foreground">{subMessage}</p>
      {onRetry && (
        <button
          type="button"
          onClick={onRetry}
          className="mt-2 inline-flex items-center gap-2 rounded-lg border border-border bg-card px-4 py-2 text-xs font-medium text-foreground transition-colors hover:bg-muted"
        >
          <RefreshCw className="h-3.5 w-3.5" />
          {retryLabel}
        </button>
      )}
    </div>
  );
}
