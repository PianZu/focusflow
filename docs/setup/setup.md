# FocusFlow - Technologie-Stack & Entwicklungsorganisation

## 1. Technologie-Stack: Wahl der Basis-Technologien

Für die Implementierung der **FocusFlow**-Anwendung setzen wir auf **Spring Boot** für das Backend und **Next.js mit TypeScript** für das Frontend. Diese Technologien wurden gezielt ausgewählt, um eine moderne, skalierbare und gut wartbare Anwendung zu ermöglichen.

### **Backend: Spring Boot (Java/Kotlin)**
- **Bewährte Stabilität und Skalierbarkeit**: Spring Boot ist eine ausgereifte Technologie, die sich in vielen Enterprise-Anwendungen bewährt hat.  
- **Effiziente Entwicklung durch Automatisierung**: Dank **Spring Data JPA** wird der Datenbankzugriff stark vereinfacht, ohne dass viele SQL-Abfragen manuell geschrieben werden müssen.  
- **Integrierte Sicherheitslösungen**: **Spring Security** bietet eine solide Grundlage für Authentifizierung und Autorisierung.  
- **Modulare Architektur**: Die Trennung von **Controller, Service und Repository** ermöglicht eine klare Code-Struktur und einfache Wartung.  
- **Automatische API-Dokumentation**: Mit **SpringDoc** (Swagger) erhalten wir eine direkt nutzbare API-Dokumentation, die die Entwicklung beschleunigt.

### **Frontend: Next.js mit TypeScript**
- **Moderne, schnelle Webentwicklung**: Next.js bietet Server-Side Rendering (SSR) und Static Site Generation (SSG), was die Performance optimiert.  
- **TypeScript für Typsicherheit**: Dies reduziert Fehler, verbessert die Code-Wartbarkeit und erleichtert die Zusammenarbeit im Team.  
- **Einfache Anbindung an das Backend**: Durch REST-APIs oder GraphQL kann das Next.js-Frontend problemlos mit dem Spring Boot-Backend kommunizieren.  
- **Performance-Optimierung**: Next.js sorgt automatisch für effizientes Code-Splitting und optimierte Ladezeiten.  
- **Gute Entwickler-Experience**: Features wie Hot-Reloading und eine intuitive Routing-Strategie erleichtern die Entwicklung.

---

## 2. Organisation der Entwicklungsaufgaben

### **Kanban-Board zur Aufgabenverwaltung**
Für die Organisation der Entwicklung setzen wir auf ein **Kanban-Board**. Dies ermöglicht eine flexible und transparente Aufgabenverteilung und hilft dem Team, stets den Überblick über den Fortschritt des Projekts zu behalten.  
- **Spaltenstruktur**: „To Do“ → „In Progress“ → „Review“ → „Done“  
- **Klare Verantwortlichkeiten**: Jeder Entwickler übernimmt spezifische Aufgaben, die sichtbar im Board verwaltet werden.  
- **Wöchentliche Planung**: Regelmäßige Updates stellen sicher, dass alle Teammitglieder synchron bleiben.

### **GitHub für Versionierung und Zusammenarbeit**
- **GitHub als zentrale Codebasis**: Der gesamte Code wird in einem GitHub-Repository verwaltet, wodurch alle Änderungen versioniert und nachverfolgt werden können.  
- **Branching-Strategie**:
  - `main`: Enthält die stabile Version des Codes.  
  - `develop`: Aktuelle Entwicklungsbasis für neue Features.  
  - Feature-Branches: Für spezifische neue Funktionen oder Bugfixes, die per Pull Request gemerged werden.  
- **Code-Reviews über Pull Requests**: Jedes neue Feature wird durch eine Code-Review geprüft, um Qualität und Wartbarkeit sicherzustellen.  
- **Automatisiertes Testing und CI/CD**: GitHub Actions ermöglicht automatische Tests und Deployment-Prozesse.

---

## 3. Fazit
Mit **Spring Boot** im Backend und **Next.js mit TypeScript** im Frontend setzen wir auf moderne, performante und gut wartbare Technologien. Durch die Kombination aus **Kanban für die Aufgabenverwaltung** und **GitHub für Versionierung und Zusammenarbeit** stellen wir eine effiziente und transparente Entwicklung sicher. Dies ermöglicht eine strukturierte und produktive Arbeit während des gesamten Semesters.
