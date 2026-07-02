// ---------------------------------------------------------------------------
// DashboardHeader — Title + subtitle for the AI Query Dashboard.
//
// "Consultas de IA" with a descriptive subtitle guiding users to ask
// questions in natural language.  Stacks vertically on all viewports.
// ---------------------------------------------------------------------------

import { useLanguage } from "@/shared/lib/i18n";
import styles from "./DashboardHeader.module.css";

export function DashboardHeader() {
  const { t } = useLanguage();
  return (
    <header className={styles.header}>
      <h1 className={styles.title}>{t("dashboard.title")}</h1>
      <p className={styles.subtitle}>{t("dashboard.subtitle")}</p>
    </header>
  );
}
