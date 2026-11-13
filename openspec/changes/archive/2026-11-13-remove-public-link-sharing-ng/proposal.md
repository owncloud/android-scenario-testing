## Why

The oCIS next-gen public-link creation and edit flows are covered, but the deletion path has no E2E coverage. The confirmation dialog is the only guard against accidental deletion — both the happy path (confirmed removal) and the safety path (cancelled removal) need automated verification.

## What Changes

- Adds two Gherkin scenarios to the `sharing/public-links-ng` capability covering removal of a named public link from a file in Alice's personal space.
- **Scenario 1 — Remove public link (confirmed):** Alice opens the sharing panel for a file that already has a named public link (created as a precondition via API), taps the trashbin icon, confirms the dialog, and verifies the link is no longer listed.
- **Scenario 2 — Cancel public link removal:** Alice taps the trashbin icon, dismisses the confirmation dialog, and verifies the link remains listed.

Preconditions reuse the `Alice has a public link on {itemtype} {word} via NG with the following fields` step introduced by the edit feature. No new precondition infrastructure is needed.

## Capabilities

### New Capabilities

_(none — removal is an extension of the existing public-links-ng capability)_

### Modified Capabilities

- `sharing/public-links-ng`: Adds removal requirements — the trashbin action, confirmation dialog, and post-removal / post-cancellation state — to the existing public-link spec.

## Impact

- **Feature file:** New scenarios added under a `@removelinkng` Rule in `src/test/resources/io/cucumber/linksng.feature`.
- **Step definitions:** `LinksNGSteps.java` — 3 new `@When` steps (tap remove, confirm, dismiss) and 2 new `@Then` steps (link listed, link not listed).
- **Page / tasks / assertions:** New methods in `PublicLinkNGPage`, `LinksNGTasks`, and `LinksNGAssertions`; no new top-level classes.
- **Backend:** oCIS only (feature file already carries `@nooc10`).
- **Timing sensitivity:** The step asserting the link is gone must wait for the UI to refresh after the network call; use an explicit wait rather than a simple find to avoid a false-negative race.
