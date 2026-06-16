# Core Web App Development Workflow

This workflow defines the **reusable steps** for building any Tomcat-based web app.  
App-specific features should be documented separately in each app’s `workflow.md`.

---

## 1. Project Setup
- Scaffold base Tomcat web app project.
- Add responsive layout and mobile-first design.
- Implement navigation bar with tabs (Scope, Clients, Links, Vendor List).
- Include dark/light mode toggle.

---

## 2. Authentication
- Implement role-based access (admin, team member).
- Use default passwords.
- Store credentials hashed in file-based DB (`/data`).

---

## 3. Data Persistence
- Configure file-based DB in `/data` folder.
- Load data automatically on Tomcat startup.
- Sync data back to Git repo on shutdown.

---

## 4. Hosting Automation
- Target Oracle Cloud Free Ampere VM (4 CPU, 24 GB RAM).
- Use temporary free DNS service.
- Create startup script:
  - Fetch data from Git.
  - Deploy on Tomcat.
- Create shutdown script:
  - Sync data back to Git.
  - Stop the instance.

---

## 5. Dashboard Creation (Core)
- Provide a base dashboard structure with placeholder sections.
- Each app will extend/customize these sections in its own workflow file.

---

## Notes
- Keep **core workflow** consistent across all apps in `apps/`.  
- Add **customization workflow** per app inside its folder (`apps/my-appX/workflow.md`).  
- Reuse `skills/skills.md` for modular prompts.  
- Each app folder (`apps/my-appX`) should contain:
  - `data/` → file-based DB
  - `scripts/` → startup/shutdown scripts
  - `.git/` → repo tracking
  - `workflow.md` → app-specific customization steps