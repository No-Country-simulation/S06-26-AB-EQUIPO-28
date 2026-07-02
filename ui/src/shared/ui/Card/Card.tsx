import { type ReactNode } from "react";
import styles from "./Card.module.css";

type CardPadding = "sm" | "md" | "lg";

interface CardProps {
  className?: string;
  children: ReactNode;
  padding?: CardPadding;
}

const paddingClasses: Record<CardPadding, string> = {
  sm: styles.paddingSm,
  md: styles.paddingMd,
  lg: styles.paddingLg,
};

export function Card({ className, children, padding = "md" }: CardProps) {
  return (
    <div className={`${styles.card} ${paddingClasses[padding]} ${className ?? ""}`}>
      {children}
    </div>
  );
}
