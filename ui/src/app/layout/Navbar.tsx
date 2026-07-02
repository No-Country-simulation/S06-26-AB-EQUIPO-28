// ---------------------------------------------------------------------------
// Navbar — Application navigation bar.
//
// Contains the "App BiT" brand on the left, navigation links on the right,
// and a CSS-only hamburger toggle for mobile (< 768 px).  Active link is
// highlighted with a bottom border.
//
// The map-centric design uses a single primary view (/mapa) with
// methodology as the only secondary page.
// ---------------------------------------------------------------------------

import { useState, useEffect } from "react";
import { NavLink, useLocation } from "react-router-dom";
import { useLanguage } from "@/shared/lib/i18n";
import styles from "./Navbar.module.css";

function navLinkClass({ isActive }: { isActive: boolean }): string {
  return `${styles.navLink}${isActive ? ` ${styles.active}` : ""}`;
}

export function Navbar() {
  const { t } = useLanguage();
  const [menuOpen, setMenuOpen] = useState(false);
  const location = useLocation();

  // Close menu on route change (mobile only).
  useEffect(() => {
    setMenuOpen(false);
  }, [location.pathname]);

  return (
    <nav className={styles.navbar} aria-label={t("nav.menuLabel")}>
      <div className={styles.inner}>
        {/* ── Brand ─────────────────────────────────────────────── */}
        <NavLink to="/mapa" className={styles.brand}>
          {t("nav.brand")}
        </NavLink>

        {/* ── Hamburger toggle (CSS-only with hidden checkbox) ──── */}
        <label className={styles.hamburgerLabel} htmlFor="nav-toggle">
          <input
            id="nav-toggle"
            type="checkbox"
            className={styles.hamburgerInput}
            checked={menuOpen}
            onChange={(e) => setMenuOpen(e.target.checked)}
            aria-label={t("nav.menuLabel")}
          />
          <span className={styles.hamburgerIcon} aria-hidden="true">
            <span className={styles.bar} />
            <span className={styles.bar} />
            <span className={styles.bar} />
          </span>
        </label>

        {/* ── Nav links ─────────────────────────────────────────── */}
        <div
          className={`${styles.navLinks}${menuOpen ? ` ${styles.navLinksOpen}` : ""}`}
        >
          <NavLink to="/mapa" className={navLinkClass}>
            {t("nav.map")}
          </NavLink>
          <NavLink to="/metodologia" className={navLinkClass}>
            {t("nav.methodology")}
          </NavLink>
        </div>
      </div>
    </nav>
  );
}
