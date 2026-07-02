import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import { Badge } from "./Badge.tsx";

describe("Badge", () => {
  it("renders with children text", () => {
    render(<Badge>Active</Badge>);
    expect(screen.getByText("Active")).toBeInTheDocument();
  });

  it("renders with success variant", () => {
    render(<Badge variant="success">OK</Badge>);
    expect(screen.getByText("OK")).toBeInTheDocument();
  });

  it("renders with warning variant", () => {
    render(<Badge variant="warning">Warning</Badge>);
    expect(screen.getByText("Warning")).toBeInTheDocument();
  });

  it("renders with error variant", () => {
    render(<Badge variant="error">Error</Badge>);
    expect(screen.getByText("Error")).toBeInTheDocument();
  });

  it("renders with info variant", () => {
    render(<Badge variant="info">Info</Badge>);
    expect(screen.getByText("Info")).toBeInTheDocument();
  });

  it("renders with neutral variant by default", () => {
    render(<Badge>Default</Badge>);
    expect(screen.getByText("Default")).toBeInTheDocument();
  });
});
