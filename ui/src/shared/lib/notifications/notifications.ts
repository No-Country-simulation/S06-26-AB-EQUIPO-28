// ---------------------------------------------------------------------------
// notifications — Minimal pub/sub notification system (toast-like)
//
// NotificationService: framework-agnostic event emitter for notifications.
// useNotifications: React hook that subscribes to the service.
// ---------------------------------------------------------------------------

import { useState, useEffect, useCallback } from "react";

// ── Types ───────────────────────────────────────────────────────────────────

export type NotificationType = "success" | "error" | "info" | "warning";

export interface Notification {
  readonly id: string;
  readonly type: NotificationType;
  readonly message: string;
  readonly timestamp: number;
}

type Handler = (notification: Notification) => void;

// ── ID generator ────────────────────────────────────────────────────────────

let _counter = 0;
function nextId(): string {
  _counter += 1;
  return `notif-${_counter}-${Date.now()}`;
}

// ── Service ─────────────────────────────────────────────────────────────────

function createNotificationService() {
  const listeners = new Map<NotificationType, Set<Handler>>();

  return {
    on(type: NotificationType, handler: Handler): void {
      if (!listeners.has(type)) {
        listeners.set(type, new Set());
      }
      listeners.get(type)!.add(handler);
    },

    off(type: NotificationType, handler: Handler): void {
      listeners.get(type)?.delete(handler);
    },

    emit(type: NotificationType, message: string): void {
      const notification: Notification = {
        id: nextId(),
        type,
        message,
        timestamp: Date.now(),
      };
      listeners.get(type)?.forEach((handler) => {
        handler(notification);
      });
    },
  };
}

export const notificationService = createNotificationService();

// ── React hook ──────────────────────────────────────────────────────────────

const AUTO_REMOVE_MS = 5_000;

export function useNotifications() {
  const [notifications, setNotifications] = useState<readonly Notification[]>(
    [],
  );

  // Subscribe to all notification types
  useEffect(() => {
    const handlers = new Map<NotificationType, Handler>();

    const types: NotificationType[] = ["success", "error", "info", "warning"];

    for (const type of types) {
      const handler: Handler = (notification) => {
        setNotifications((prev) => [...prev, notification]);
      };
      handlers.set(type, handler);
      notificationService.on(type, handler);
    }

    return () => {
      for (const [type, handler] of handlers) {
        notificationService.off(type, handler);
      }
    };
  }, []);

  const addNotification = useCallback(
    (type: NotificationType, message: string): void => {
      notificationService.emit(type, message);
    },
    [],
  );

  const removeNotification = useCallback((id: string): void => {
    setNotifications((prev) => prev.filter((n) => n.id !== id));
  }, []);

  // Auto-remove after timeout
  useEffect(() => {
    if (notifications.length === 0) return;

    const timers: ReturnType<typeof setTimeout>[] = [];

    for (const notification of notifications) {
      const elapsed = Date.now() - notification.timestamp;
      const delay = Math.max(0, AUTO_REMOVE_MS - elapsed);

      const timer = setTimeout(() => {
        removeNotification(notification.id);
      }, delay);

      timers.push(timer);
    }

    return () => {
      for (const timer of timers) {
        clearTimeout(timer);
      }
    };
  }, [notifications, removeNotification]);

  return {
    notifications,
    addNotification,
    removeNotification,
  } as const;
}
