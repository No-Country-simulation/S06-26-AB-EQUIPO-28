import { type InputHTMLAttributes, type ReactNode, useId } from "react";
import styles from "./Input.module.css";

interface InputProps extends Omit<InputHTMLAttributes<HTMLInputElement>, "size"> {
  label?: string;
  error?: string;
  icon?: ReactNode;
}

export function Input({
  label,
  error,
  icon,
  disabled,
  className,
  id: externalId,
  ...rest
}: InputProps) {
  const generatedId = useId();
  const inputId = externalId ?? generatedId;
  const errorId = error ? `${inputId}-error` : undefined;

  return (
    <div className={`${styles.wrapper} ${className ?? ""}`}>
      {label && (
        <label htmlFor={inputId} className={styles.label}>
          {label}
        </label>
      )}
      <div
        className={`${styles.inputContainer} ${error ? styles.inputError : ""} ${disabled ? styles.inputDisabled : ""}`}
      >
        {icon && <span className={styles.icon} aria-hidden="true">{icon}</span>}
        <input
          id={inputId}
          className={`${styles.input} ${icon ? styles.inputWithIcon : ""}`}
          disabled={disabled}
          aria-invalid={!!error}
          aria-describedby={errorId}
          {...rest}
        />
      </div>
      {error && (
        <p id={errorId} className={styles.errorMessage} role="alert">
          {error}
        </p>
      )}
    </div>
  );
}
