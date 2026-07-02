import { describe, it, expect, beforeEach } from "vitest";
import { notificationService } from "./notifications.ts";
import type { Notification } from "./notifications.ts";

describe("NotificationService", () => {
  const captured: Notification[] = [];
  // Keep handler references so we can unregister them
  const activeHandlers: Array<{ type: string; handler: (n: Notification) => void }> = [];

  function track(type: string, handler: (n: Notification) => void) {
    activeHandlers.push({ type, handler });
  }

  function cleanupHandlers() {
    for (const { type, handler } of activeHandlers) {
      notificationService.off(type as any, handler);
    }
    activeHandlers.length = 0;
  }

  beforeEach(() => {
    captured.length = 0;
    cleanupHandlers();
  });

  function capture(n: Notification) {
    captured.push(n);
  }

  it("emits a notification and calls registered handlers", () => {
    notificationService.on("info", capture);
    track("info", capture);
    notificationService.emit("info", "test message");

    expect(captured).toHaveLength(1);
    expect(captured[0].type).toBe("info");
    expect(captured[0].message).toBe("test message");
    expect(captured[0].id).toBeTruthy();
    expect(captured[0].timestamp).toBeGreaterThan(0);
  });

  it("supports multiple handlers for the same type", () => {
    const second: Notification[] = [];
    const h1 = (n: Notification) => captured.push(n);
    const h2 = (n: Notification) => second.push(n);

    notificationService.on("success", h1);
    notificationService.on("success", h2);
    track("success", h1);
    track("success", h2);

    notificationService.emit("success", "multi");
    expect(captured).toHaveLength(1);
    expect(second).toHaveLength(1);
  });

  it("does not call handler after off()", () => {
    notificationService.on("error", capture);
    track("error", capture);
    notificationService.off("error", capture);

    // Remove from tracking so we don't try to off again
    const idx = activeHandlers.findIndex((h) => h.handler === capture);
    if (idx !== -1) activeHandlers.splice(idx, 1);

    notificationService.emit("error", "should not fire");
    expect(captured).toHaveLength(0);
  });

  it("does not call handlers for a different type", () => {
    notificationService.on("warning", capture);
    track("warning", capture);

    notificationService.emit("info", "wrong type");
    expect(captured).toHaveLength(0);
  });

  it("handles emit when no handler registered", () => {
    expect(() => {
      notificationService.emit("warning", "orphan");
    }).not.toThrow();
  });
});
