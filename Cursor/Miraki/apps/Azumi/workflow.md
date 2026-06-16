# Custom Workflow: AzumiDesigns

This workflow extends the **Core Web App Development Workflow** with app-specific features for AzumiDesigns.  
Templates stored in `/templates/` are used as design references for each form.  
Additional references (images, costing sheets, renders, materials, style guides) are stored in `/references/`.

---

## 1. Dashboard - Client Discovery
- Use `templates/01-client-discovery-brief.docx` as reference.
- Form to capture client requirements, preferences, and design vision.
- Persist data in `/data` folder.

---

## 2. Dashboard - Room Scope
- Use `templates/02-room-scope-template.docx` as reference.
- Editable table for each room’s scope (dimensions, functions, requirements).
- Persist changes in `/data` folder.

---

## 3. Dashboard - Inclusions/Exclusions
- Use `templates/03-inclusions-exclusions-clauses.docx` as reference.
- Section to define what is included/excluded in the project scope.
- Persist in `/data` folder.

---

## 4. Dashboard - Budget Calibration
- Use `templates/05-budget-calibration-worksheet.docx` as reference.
- Form to align client expectations with budget ranges.
- Persist in `/data` folder.

---

## 5. Dashboard - First Meeting Checklist
- Use `templates/06-first-meeting-checklist.docx` as reference.
- Checklist for initial client meetings (documents, preferences, site details).
- Persist in `/data` folder.

---

## 6. Dashboard - Vendor List
- Add tab for vendor list management.
- Include vendor name, contact, category.
- Persist vendor data in `/data` folder.

---

## 7. Dashboard - Project Details & Progress
- Add section to create a confirmed project view.
- Editable details: rooms, tasks for each room.
- Option to mark tasks as completed or in-progress.
- Generate progress report (percentage completion).
- Store project details in `/data` folder.

---

## 8. Integration
- Enable forms to export as PDF (using iText or Apache PDFBox).
- Enable forms to send via email (using JavaMail API).
- Provide option to save locally on device.

---

## 9. References (App-Specific)
- **Costing**: Store costing sheets in `/references/costing/` (Excel/CSV).
- **Referral Images**: Upload design inspiration in `/references/images/`.
- **Renders**: Save 3D renders or visualizations in `/references/renders/`.
- **Materials Library**: Maintain catalogs in `/references/materials/`:
  - Tiles
  - Paint schemes
  - Wood finishes
  - Fabrics
  - Lighting options
- **Style Guides**: Store mood boards, palettes, and theme references in `/references/styles/`.
- **Client-Specific Assets**: Each client folder under `/references/clients/` can hold site photos, sketches, and custom design notes.

---

## Notes
- All customization builds on the **core workflow** defined in `workflows/workflows.md`.
- Prompts for each feature can be reused from `skills/skills.md`.
- Keep app-specific scripts in `apps/Azumi/scripts/`.
- Ensure Git sync on startup/shutdown includes all dashboard data, templates, and references.
- Templates in `/templates/` serve as the blueprint for structured forms.
- References in `/references/` provide creative and costing assets for design visualization and client presentations.
