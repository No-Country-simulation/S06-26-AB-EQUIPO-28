import { cn } from "@/shared/lib/cn";

interface Column {
  key: string;
  label: string;
  width?: string;
}

interface TableProps {
  columns: Column[];
  data: Record<string, unknown>[];
  className?: string;
}

export function Table({ columns, data, className }: TableProps) {
  return (
    <div className={cn("overflow-x-auto", className)}>
      <table className="w-full border-collapse text-sm text-foreground">
        <thead>
          <tr>
            {columns.map((col) => (
              <th
                key={col.key}
                className="whitespace-nowrap bg-muted px-4 py-3 text-left font-semibold border-b border-border"
                style={col.width ? { width: col.width } : undefined}
              >
                {col.label}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((row, rowIndex) => (
            <tr key={rowIndex} className="hover:bg-muted/50">
              {columns.map((col) => (
                <td key={col.key} className="px-4 py-3 border-b border-border last-of-type:border-b-0">
                  {String(row[col.key] ?? "")}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
