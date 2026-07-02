// ---------------------------------------------------------------------------
// MarkdownRenderer — renders a markdown string as styled HTML via
// react-markdown + remark-gfm (GitHub Flavored Markdown, including tables).
// Provides sensible typography defaults (Inter font, proper heading / list /
// code spacing) that align with the project's design system.
// ---------------------------------------------------------------------------

import Markdown from "react-markdown";
import type { Components } from "react-markdown";
import remarkGfm from "remark-gfm";
import styles from "./MarkdownRenderer.module.css";

interface MarkdownRendererProps {
  content: string;
}

const components: Components = {
  h1: ({ children }) => <h1 className={styles.h1}>{children}</h1>,
  h2: ({ children }) => <h2 className={styles.h2}>{children}</h2>,
  h3: ({ children }) => <h3 className={styles.h3}>{children}</h3>,
  h4: ({ children }) => <h4 className={styles.h4}>{children}</h4>,
  p: ({ children }) => <p className={styles.paragraph}>{children}</p>,
  ul: ({ children }) => <ul className={styles.unorderedList}>{children}</ul>,
  ol: ({ children }) => <ol className={styles.orderedList}>{children}</ol>,
  li: ({ children }) => <li className={styles.listItem}>{children}</li>,
  strong: ({ children }) => (
    <strong className={styles.strong}>{children}</strong>
  ),
  em: ({ children }) => <em className={styles.em}>{children}</em>,
  code: ({ children, ...rest }) => {
    const isInline = rest.node?.position
      ? rest.node.position.start.line === rest.node.position.end.line &&
        typeof children === "string" &&
        !children.includes("\n")
      : true;
    return isInline ? (
      <code className={styles.inlineCode}>{children}</code>
    ) : (
      <code className={styles.blockCode}>{children}</code>
    );
  },
  pre: ({ children }) => <pre className={styles.pre}>{children}</pre>,
  blockquote: ({ children }) => (
    <blockquote className={styles.blockquote}>{children}</blockquote>
  ),
  hr: () => <hr className={styles.hr} />,
  table: ({ children }) => (
    <div className={styles.tableWrapper}>
      <table className={styles.table}>{children}</table>
    </div>
  ),
  thead: ({ children }) => <thead className={styles.thead}>{children}</thead>,
  tbody: ({ children }) => <tbody className={styles.tbody}>{children}</tbody>,
  tr: ({ children }) => <tr className={styles.tr}>{children}</tr>,
  th: ({ children }) => <th className={styles.th}>{children}</th>,
  td: ({ children }) => <td className={styles.td}>{children}</td>,
};

export function MarkdownRenderer({ content }: MarkdownRendererProps) {
  return (
    <div className={styles.wrapper}>
      <Markdown remarkPlugins={[remarkGfm]} components={components}>
        {content}
      </Markdown>
    </div>
  );
}
