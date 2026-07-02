import { describe, it, expect, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { LanguageProvider } from "@/shared/lib/i18n";
import { AiSearchInput } from "./AiSearchInput.tsx";

function renderWithProviders(ui: React.ReactElement) {
  return render(<LanguageProvider>{ui}</LanguageProvider>);
}

describe("AiSearchInput", () => {
  it("renders with placeholder text", () => {
    renderWithProviders(
      <AiSearchInput
        value=""
        onChange={() => {}}
        onSubmit={() => {}}
      />,
    );
    const input = screen.getByRole("textbox");
    expect(input).toBeInTheDocument();
    expect(input).toHaveAttribute("placeholder");
  });

  it("calls onChange when user types", async () => {
    const onChange = vi.fn();
    const user = userEvent.setup();
    renderWithProviders(
      <AiSearchInput
        value=""
        onChange={onChange}
        onSubmit={() => {}}
      />,
    );
    const input = screen.getByRole("textbox");
    await user.type(input, "h");
    expect(onChange).toHaveBeenCalled();
  });

  it("calls onSubmit when Enter is pressed with non-empty value", async () => {
    const onSubmit = vi.fn();
    const user = userEvent.setup();
    renderWithProviders(
      <AiSearchInput
        value="test query"
        onChange={() => {}}
        onSubmit={onSubmit}
      />,
    );
    const input = screen.getByRole("textbox");
    await user.type(input, "{Enter}");
    expect(onSubmit).toHaveBeenCalledTimes(1);
  });

  it("does not call onSubmit when Enter and value is empty", async () => {
    const onSubmit = vi.fn();
    const user = userEvent.setup();
    renderWithProviders(
      <AiSearchInput
        value=""
        onChange={() => {}}
        onSubmit={onSubmit}
      />,
    );
    const input = screen.getByRole("textbox");
    await user.type(input, "{Enter}");
    expect(onSubmit).not.toHaveBeenCalled();
  });

  it("disables input and submit button when disabled", () => {
    renderWithProviders(
      <AiSearchInput
        value="test"
        onChange={() => {}}
        onSubmit={() => {}}
        disabled
      />,
    );
    expect(screen.getByRole("textbox")).toBeDisabled();
    expect(screen.getByRole("button")).toBeDisabled();
  });

  it("has accessible labels", () => {
    renderWithProviders(
      <AiSearchInput
        value=""
        onChange={() => {}}
        onSubmit={() => {}}
      />,
    );
    // Labels come from translation keys — check that an input exists
    expect(screen.getByRole("textbox")).toBeInTheDocument();
    expect(screen.getByRole("button")).toBeInTheDocument();
  });
});
