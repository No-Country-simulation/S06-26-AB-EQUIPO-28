import { useState, useEffect, useRef } from "react";
import { NavLink, useLocation } from "react-router-dom";
import { useLanguage, LOCALES } from "@/shared/lib/i18n";
import type { Locale } from "@/shared/lib/i18n";
import { Activity } from "lucide-react";

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

  useEffect(() => {
    setMenuOpen(false);
  }, [location.pathname]);

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
        <NavLink to="/panel" className="flex items-center gap-2 text-xl font-bold tracking-tight text-foreground shrink-0">
          <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-primary text-primary-foreground">
            <Activity className="h-4 w-4" />
          </div>
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
        </div>
      </div>
    </nav>
  );
}
