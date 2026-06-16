# Core Skills Library

Reusable skills across all apps. These define the technical foundation for development, testing, and deployment.

---

## Authentication
Role-based authentication (admin, team member).
- Default passwords
- Hashed storage in `/data`

---

## Data Persistence
File-based persistence:
- Save/load data in `/data`
- Sync with Git on startup/shutdown

---

## Local Deploy
Build and test locally before pushing to Git:
- Read config file (`/config/local-deploy.json`) for settings:
  - Port for GUI
  - Context path
  - Database path
  - Logging level
- Build the app locally using Maven/Gradle.
- Deploy on local Tomcat server.
- Auto-reload changes for rapid testing.
- Allow tweaking UI/UX and workflows before committing.
- Validate data persistence and authentication locally.

---

## Hosting Automation
Startup/shutdown scripts:
- Startup: fetch Git, deploy Tomcat
- Shutdown: sync Git, stop instance
Target Oracle Cloud Free VM + free DNS.

---

## UI/UX
Responsive design:
- Mobile-first
- Navigation bar (Scope, Clients, Links, Vendor List)
- Dark/light mode toggle

---

## Dashboard Base
Provide placeholder dashboard sections for customization.

---

## Integration - PDF Export
Export forms as PDF (iText/PDFBox).

---

## Integration - Email
Send forms via email (JavaMail API).
