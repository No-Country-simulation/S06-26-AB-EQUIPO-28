/// <reference types="vitest/config" />
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";
import path from "path";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  server: {
    proxy: {
      "/api": {
        target: "https://s06-26-ab-equipo-28.onrender.com",
        changeOrigin: true,
      },
    },
  },
  build: {
    // maplibre-gl bundled inline now that it's installed
  },
  test: {
    globals: true,
    environment: "jsdom",
    setupFiles: ["./src/shared/test/test-setup.ts"],
  },
});
