## Why

The existing public-link creation flow (`links.feature`) is tagged `@noocis` and does not work on oCIS backends. As the ownCloud Android app's oCIS support matures, users need verified E2E coverage for all public-link creation possibilities on oCIS — including permission types, optional name and expiration date, and the password-policy enforcement that oCIS mandates.

## What Changes

- Add `linksng.feature` under `src/test/resources/io/cucumber/`, following the project naming convention (`links.feature` / `shares.feature` for oC10; `linksng.feature` / `sharingng.feature` for oCIS).
- New `PublicLinkNGPage.java` page object and step definitions covering the oCIS public-link creation dialog.

### Scenarios

- **Scenario Outline — Create a public link**: one outline covers all creation combinations. Examples table varies: `type` (file for Can view/Can edit; folder for all permissions including Secret file drop), `permission` (Can view, Can edit, Secret file drop), `link_name` (empty → "Unnamed Link", custom value), `password`, `expiration_days` (empty or number of days).
- **Scenario — Guard-rail**: confirm button is disabled when the mandatory password field is left empty for a "Can edit" link.

## Capabilities

### New Capabilities

- `sharing/public-links-ng`: E2E behavior specification for creating public links on the oCIS (next-gen sharing) backend, covering all permission types (Can view, Can edit, Secret file drop), optional name and expiration date, and server-side password-policy enforcement.

### Modified Capabilities

_(none — the legacy `sharing` link behavior spec is unchanged)_

## Impact

- **Feature files**: new `linksng.feature` (oCIS-only, no `@noocis` tag).
- **Step definitions**: new steps in `ShareNGSteps.java` or a dedicated `PublicLinksNGSteps.java`; `LinksSteps.java` is untouched.
- **Page objects**: new `PublicLinkNGPage.java` for the oCIS public-link creation dialog.
- **No API changes**: preconditions reuse existing REST helpers; no new dependencies.
