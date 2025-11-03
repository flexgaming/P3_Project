import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
        port: 5173, // React default port
        proxy: {
            "/api": {
                target: "http://localhost:8080", // Backend server
                changeOrigin: true,
                secure: false,
                rewrite: (path) => path.replace(/^\/api/, ""), // Remove /api prefix when forwarding
            },
        },
    },
});
