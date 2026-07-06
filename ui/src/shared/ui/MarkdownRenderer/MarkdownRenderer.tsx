import Markdown from "react-markdown";
import type { Components } from "react-markdown";
import remarkGfm from "remark-gfm";
interface MarkdownRendererProps {
  content: string;
}

const components: Components = {
  h1: ({ children }) => <h1 className="m-0 mb-3 text-xl font-bold leading-tight text-foreground">{children}</h1>,
  h2: ({ children }) => <h2 className="mt-5 mb-2 text-lg font-semibold leading-snug text-foreground">{children}</h2>,
  h3: ({ children }) => <h3 className="mt-4 mb-1.5 text-base font-semibold leading-snug text-foreground">{children}</h3>,
  h4: ({ children }) => <h4 className="mt-3 mb-1 text-sm font-semibold leading-snug text-foreground">{children}</h4>,
  p: ({ children }) => <p className="mb-2.5 last:mb-0">{children}</p>,
  ul: ({ children }) => <ul className="mb-2.5 list-disc pl-5.5">{children}</ul>,
  ol: ({ children }) => <ol className="mb-2.5 list-decimal pl-5.5">{children}</ol>,
  li: ({ children }) => <li className="mb-1 last:mb-0 leading-relaxed">{children}</li>,
  strong: ({ children }) => <strong className="font-semibold text-foreground">{children}</strong>,
  em: ({ children }) => <em className="italic">{children}</em>,
  code: ({ children, ...rest }) => {
    const isInline = rest.node?.position
      ? rest.node.position.start.line === rest.node.position.end.line &&
        typeof children === "string" &&
        !children.includes("\n")
      : true;
    return isInline ? (
      <code className="rounded bg-muted px-1.5 py-0.5 text-xs font-mono text-[#BE123C]">{children}</code>
    ) : (
      <code className="font-mono text-xs leading-relaxed">{children}</code>
    );
  },
  pre: ({ children }) => <pre className="my-3 overflow-x-auto rounded-lg bg-[#1F2937] p-3.5 text-xs leading-relaxed text-[#F3F4F6]">{children}</pre>,
  blockquote: ({ children }) => (
    <blockquote className="my-3 border-l-3 border-accent bg-muted/50 py-2 pl-3.5 pr-3.5 text-muted-foreground italic rounded-r-md">{children}</blockquote>
  ),
  hr: () => <hr className="my-4 border-t border-border" />,
  table: ({ children }) => (
    <div className="my-3 overflow-x-auto rounded-lg border border-border">
      <table className="w-full border-collapse text-xs">{children}</table>
    </div>
  ),
  thead: ({ children }) => <thead className="bg-muted">{children}</thead>,
  tbody: ({ children }) => <tbody>{children}</tbody>,
  tr: ({ children }) => <tr className="border-b border-border last:border-b-0">{children}</tr>,
  th: ({ children }) => <th className="whitespace-nowrap px-3 py-2 text-left font-semibold text-muted-foreground">{children}</th>,
  td: ({ children }) => <td className="px-3 py-2 text-foreground">{children}</td>,
};

export function MarkdownRenderer({ content }: MarkdownRendererProps) {
  return (
    <div className="font-sans text-sm leading-relaxed text-foreground">
      <Markdown remarkPlugins={[remarkGfm]} components={components}>
        {content}
      </Markdown>
    </div>
  );
}
