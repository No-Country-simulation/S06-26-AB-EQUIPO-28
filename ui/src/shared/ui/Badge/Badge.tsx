import { type ReactNode } from "react";
import styles from "./Badge.module.css";

type BadgeVariant = "success" | "warning" | "error" | "info" | "neutral";

interface BadgeProps {
  variant?: BadgeVariant;
  children: ReactNode;
  className?: string;
}

const variantClasses: Record<BadgeVariant, string> = {
  success: styles.variantSuccess,
  warning: styles.variantWarning,
  error: styles.variantError,
  info: styles.variantInfo,
  neutral: styles.variantNeutral,
};

export function Badge({ variant = "neutral", children, className }: BadgeProps) {
  return (
    <span className={`${styles.badge} ${variantClasses[variant]} ${className ?? ""}`}>
      {children}
    </span>
  );
}
