import { defineConfig } from "cypress";

export default defineConfig({
  // Globales Deaktivieren der Videoaufzeichnung:
  video: false,

  e2e: {
    // Hier übergibst du Event‐Handler & Plugins
    setupNodeEvents(on, config) {
      // Beispiel: Einfache Log‐Task registrieren
      on("task", {
        log(message) {
          console.log(message);
          return null;
        },
      });
      return config;
    },
  },
});

