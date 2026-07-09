import { useState, useEffect, useRef } from "react";
import { NavLink, useLocation } from "react-router-dom";
import { useLanguage, LOCALES } from "@/shared/lib/i18n";
import type { Locale } from "@/shared/lib/i18n";
import { AlertHistoryPanel } from "@/features/alert-monitor";
import type { AlertEvent } from "@/entities/alert";
import { alertRepository } from "@/entities/alert/api/localStorageRepository.ts";

function navLinkClass({ isActive }: { isActive: boolean }): string {
  return `inline-flex items-center h-16 px-4 text-sm font-medium whitespace-nowrap border-b-2 transition-colors ${
    isActive
      ? "text-foreground border-accent"
      : "text-muted-foreground border-transparent hover:text-foreground"
  }`;
}

export function Navbar() {
  const { t, locale, setLocale } = useLanguage();
  const [menuOpen, setMenuOpen] = useState(false);
  const location = useLocation();
  const menuRef = useRef<HTMLDivElement>(null);
  const [historyOpen, setHistoryOpen] = useState(false);
  const [unacknowledged, setUnacknowledged] = useState(() => {
    return alertRepository.getHistory().filter((e: AlertEvent) => !e.acknowledged).length;
  });

  useEffect(() => {
    setMenuOpen(false);
  }, [location.pathname]);

  useEffect(() => {
    const interval = setInterval(() => {
      const count = alertRepository.getHistory().filter((e: AlertEvent) => !e.acknowledged).length;
      setUnacknowledged(count);
    }, 3000);
    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    function handleClickOutside(e: MouseEvent) {
      if (menuRef.current && !menuRef.current.contains(e.target as Node)) {
        setMenuOpen(false);
      }
    }
    if (menuOpen) {
      document.addEventListener("mousedown", handleClickOutside);
    }
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [menuOpen]);

  if (location.pathname.startsWith("/panel")) return null;

  return (
    <nav
      className="fixed top-0 left-0 right-0 z-100 h-16 bg-card border-b border-border"
      aria-label={t("nav.menuLabel")}
    >
      <div className="mx-auto flex h-full max-w-7xl items-center justify-between px-20 max-md:px-8">
        <NavLink to="/panel" className="text-xl font-bold tracking-tight text-foreground shrink-0">
          {t("nav.brand")}
        </NavLink>

        {/* Hamburger — mobile */}
        <button
          type="button"
          className="hidden max-md:block p-2 cursor-pointer"
          onClick={() => setMenuOpen((v) => !v)}
          aria-label={t("nav.menuLabel")}
          aria-expanded={menuOpen}
        >
          <div className="flex flex-col gap-1.25 w-6">
            <span className={`block h-0.5 bg-foreground rounded transition-transform duration-200 ${menuOpen ? "translate-y-1.75 rotate-45" : ""}`} />
            <span className={`block h-0.5 bg-foreground rounded transition-opacity duration-200 ${menuOpen ? "opacity-0" : ""}`} />
            <span className={`block h-0.5 bg-foreground rounded transition-transform duration-200 ${menuOpen ? "-translate-y-1.75 -rotate-45" : ""}`} />
          </div>
        </button>

        {/* Nav links — desktop always visible, mobile toggle */}
        <div
          ref={menuRef}
          className={`flex items-center gap-2 max-md:fixed max-md:top-16 max-md:left-0 max-md:right-0 max-md:flex-col max-md:bg-card max-md:border-b max-md:border-border max-md:p-2 max-md:gap-0 max-md:transition-transform max-md:duration-200 max-md:ease ${
            menuOpen
              ? "max-md:translate-y-0 max-md:opacity-100 max-md:pointer-events-auto"
              : "max-md:-translate-y-full max-md:opacity-0 max-md:pointer-events-none"
          }`}
        >
          <NavLink to="/metodologia" className={navLinkClass}>
            {t("nav.methodology")}
          </NavLink>
          <NavLink to="/panel" className={navLinkClass}>
            {t("nav.panel")}
          </NavLink>

          <div className="flex items-center gap-0.5 mx-2 p-0.5 bg-muted rounded-lg max-md:mx-6 max-md:mt-2 max-md:self-start">
            {(Object.keys(LOCALES) as Locale[]).map((l) => (
              <button
                key={l}
                type="button"
                className={`inline-flex items-center justify-center min-w-7 h-6 px-1.5 text-xs font-semibold rounded-md border-none cursor-pointer transition-colors ${
                  l === locale
                    ? "bg-card text-foreground shadow-sm"
                    : "bg-transparent text-muted-foreground hover:text-foreground"
                }`}
                onClick={() => setLocale(l)}
                aria-label={LOCALES[l]}
              >
                {l.toUpperCase()}
              </button>
            ))}
          </div>

          <div className="flex items-center gap-1 ml-2 pl-2 border-l border-border max-md:ml-0 max-md:pl-0 max-md:border-l-0 max-md:px-6 max-md:py-2 max-md:w-full max-md:justify-start">
            <button
              type="button"
              className="relative inline-flex items-center justify-center w-9 h-9 border-none bg-transparent text-muted-foreground rounded-lg cursor-pointer hover:bg-muted hover:text-foreground transition-colors"
              onClick={() => setHistoryOpen(true)}
              aria-label={t("alert.bellLabel")}
              title={t("alert.history")}
            >
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9" />
                <path d="M13.73 21a2 2 0 0 1-3.46 0" />
              </svg>
              {unacknowledged > 0 && (
                <span className="absolute top-0.5 right-0.5 min-w-4 h-4 px-1 bg-destructive text-destructive-foreground text-[10px] font-bold leading-4 text-center rounded-full pointer-events-none">
                  {unacknowledged > 9 ? "9+" : unacknowledged}
                </span>
              )}
            </button>
          </div>
        </div>
      </div>

      <AlertHistoryPanel open={historyOpen} onClose={() => setHistoryOpen(false)} />
    </nav>
  );
}
