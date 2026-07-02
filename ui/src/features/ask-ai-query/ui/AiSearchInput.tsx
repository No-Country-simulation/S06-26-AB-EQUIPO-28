// ---------------------------------------------------------------------------
// AiSearchInput — Large search input with magnifying glass icon and submit.
//
// Styled consistently with shared/ui/Input but at a larger scale (56 px
// height, 12 px border-radius).  The submit button is embedded on the
// right-hand side inside the same container.
// ---------------------------------------------------------------------------

import {
  type KeyboardEvent,
  type ChangeEvent,
  useId,
} from "react";
import { useLanguage } from "@/shared/lib/i18n";
import styles from "./AiSearchInput.module.css";

interface AiSearchInputProps {
  value: string;
  onChange: (value: string) => void;
  onSubmit: () => void;
  disabled?: boolean;
  placeholder?: string;
}

export function AiSearchInput({
  value,
  onChange,
  onSubmit,
  disabled = false,
  placeholder,
}: AiSearchInputProps) {
  const { t } = useLanguage();
  const resolvedPlaceholder = placeholder ?? t("dashboard.searchPlaceholder");
  const inputId = useId();

  const handleKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter" && !disabled && value.trim()) {
      onSubmit();
    }
  };

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    onChange(e.target.value);
  };

  return (
    <div className={styles.wrapper}>
      <div
        className={`${styles.container} ${disabled ? styles.disabled : ""}`}
      >
        <span className={styles.icon} aria-hidden="true">
          <svg
            width="20"
            height="20"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
          >
            <circle cx="11" cy="11" r="8" />
            <path d="M21 21l-4.35-4.35" />
          </svg>
        </span>

        <input
          id={inputId}
          type="text"
          className={styles.input}
          value={value}
          onChange={handleChange}
          onKeyDown={handleKeyDown}
          disabled={disabled}
          placeholder={resolvedPlaceholder}
          aria-label={t("common.search")}
          autoComplete="off"
        />

        <button
          type="button"
          className={styles.submitButton}
          onClick={onSubmit}
          disabled={disabled || !value.trim()}
          aria-label={t("common.submit")}
        >
          <svg
            width="20"
            height="20"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
          >
            <path d="M22 2L11 13" />
            <path d="M22 2l-7 20-4-9-9-4 20-7z" />
          </svg>
        </button>
      </div>
    </div>
  );
}
