## Context

The existing public-link E2E tests live in `links.feature` / `LinksSteps.java` / `LinksPage.java` and are all tagged `@noocis`. They drive the legacy OC10 link-creation bottom sheet (resource IDs from `add_public_link_fragment.xml`). The oCIS sharing flow uses a separate screen path entered from `ShareNGPage`, which currently has no public-link creation support.

The oCIS backend enforces mandatory passwords server-side; the UI must reflect whether the confirm action is enabled or disabled based on password presence.

See `proposal.md – Why` for motivation.

## Goals / Non-Goals

**Goals:**
- Cover all public-link creation combinations on oCIS via a single Scenario Outline: type (file for Can view/Can edit; folder for all permissions), permission (Can view, Can edit, Secret file drop — folder-only), optional link name, optional expiration date, password.
- Verify that the confirm action is blocked when the mandatory password field is empty on a writeable link (guard-rail scenario).
- Reuse existing NG-flow infrastructure (`ShareNGPage`, `ShareNGSteps`) where possible.

**Non-Goals:**
- Modifying or fixing the legacy `@noocis` link tests.
- Covering link deletion or link editing in this change (deferred to follow-on).
- Testing on the OC10 backend.

## Decisions

### Decision 1: New page object for the oCIS public-link creation dialog

The oCIS public-link creation dialog is a different screen from the OC10 bottom sheet (`LinksPage`). Reusing `LinksPage` would couple oCIS locators to OC10 locators and pollute a `@noocis`-only class. A new `PublicLinkNGPage.java` keeps the two flows independent and follows the established `*NGPage` naming pattern.

**Reuse opportunity:** `SpaceMembersPage.java` already implements the identical dialog flow for spaces public links (same locators: `add_public_link_button`, `public_link_name_edit_text`, `expiration_date_switch`, `create_public_link_button`; same permission-selection via `findUIAutomatorSubText`; same two-step password flow via `set_password_button` → `password_value` → `set_password_button`). `PublicLinkNGPage.java` should copy or adapt this implementation rather than starting from scratch.

**Alternative considered:** Extend `LinksPage` with oCIS-specific methods. Rejected because `LinksPage` uses OC10 resource IDs exclusively, and mixing backends in one page object increases fragility.

### Decision 2: DataTable step definitions mirroring the OC10 `LinksSteps` pattern

All creation-path scenarios use two DataTable steps — `Alice creates public link on {itemtype} {word} via NG with the following fields` and `public link should be created on {word} via NG with the following fields` — mirroring the existing `Alice creates link on {itemtype} {word} with the following fields` / `link should be created/edited on {word} with the following fields` pattern in `LinksSteps.java`. This keeps the NG feature file compact (one Scenario Outline) and the step implementation flexible (fields absent from the table are skipped). The guard-rail scenario uses three individual steps because it intentionally does not complete creation.

Whether the new steps live in `ShareNGSteps.java` or a dedicated `PublicLinksNGSteps.java` is an implementation choice; co-location in `ShareNGSteps.java` is preferred for discoverability.

**Alternative considered:** Individual step per action (tap "+", select permission, enter password separately). Rejected — produces verbose scenario bodies and a large step count for no additional test value; the DataTable approach is already established in this project.

**Alternative considered:** Put new steps in `LinksSteps.java`. Rejected — that file is tightly coupled to the OC10 flow.

### Decision 3: Feature file is `linksng.feature` (oCIS-only, no `@noocis` tag needed)

New scenarios are written in `linksng.feature`, following the project convention: `links.feature` and `shares.feature` cover oC10; `linksng.feature` and `sharingng.feature` cover oCIS. No `@noocis` tag is needed since the file is oCIS-only by design. Adding an explicit `@ocis` tag is optional but improves filter legibility; the project currently uses `@noocis` for exclusion rather than inclusion tags, so no new tag is strictly required.

## Risks / Trade-offs

- **UNRESOLVED locators** — the oCIS public-link creation dialog locators (permission label, password field, confirm button, link entry label) are not yet in `ui_elements_dictionary.md`. They will be marked UNRESOLVED in `tasks.md` and require manual resolution before implementation.
- **Password-policy enforcement varies** — whether the confirm button is disabled client-side or the error surfaces after tapping is app-version-dependent. The scenario should be adjusted once the actual app behavior is confirmed.
- **Unnamed Link label** — the "Unnamed Link" label is taken from `source.md`; the actual string may differ once the dialog is inspected. Mark as UNRESOLVED in tasks until verified against the app.
