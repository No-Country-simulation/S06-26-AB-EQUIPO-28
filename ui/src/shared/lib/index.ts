// ---------------------------------------------------------------------------
// Public API — src/shared/lib
//
// Generic, business-agnostic utility library. Consumers should import
// from this barrel (e.g. `import { clsx, formatNumber } from "@/shared/lib"`).
// ---------------------------------------------------------------------------

export { clsx } from "./clsx/index.ts";
export type { ClassValue } from "./clsx/index.ts";

export {
  formatNumber,
  formatPercentage,
  formatDate,
  formatRegionName,
  truncate,
} from "./formatters/index.ts";

export { isNonEmpty, isValidRegionId } from "./validators/index.ts";

export { notificationService, useNotifications } from "./notifications/index.ts";
export type { Notification, NotificationType } from "./notifications/index.ts";
