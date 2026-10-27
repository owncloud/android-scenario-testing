## Context

See `proposal.md - Why` for motivation. Relevant existing code:
- `ShareNGPage` — the Share screen used on oCIS. Currently has no permalink element.
- `SharePage` — the Share screen used on oC10. Not in scope (feature is oCIS-only).
- `FileListSteps`: the step `Alice selects to {word} the {itemtype} {word}` opens the Share view when the operation word is `share`.
- `ShareNGSteps` — existing steps for the ShareNG screen.
- `privatelink.feature` — existing coverage for *opening* private links received from outside; this change covers *generating* them from inside the app.

## Goals / Non-Goals

**Goals:**
- Add a `@AndroidFindBy` element for the permalink button to the appropriate page object.
- Add a task method that taps the permalink button.
- Add an assertion that verifies the post-tap outcome (share sheet or toast).
- Add two step definitions (one assertion, one action) and a new feature file.

**Non-Goals:**
- Testing that the private link URL is correct — that is an app-level concern; E2E only verifies the UI action completes.
- Coverage on oC10 — the feature does not exist there.
- Reading the clipboard content via ADB — too brittle across Android API levels.

## Decisions

**Where does the permalink element live?**
Add it to `ShareNGPage`, since the Share header is part of the ShareNG screen (oCIS-only). Creating a separate page class for a single button adds unnecessary indirection. The button's resource id is `permanent_link_button` (confirmed in `ui_elements_dictionary.md`, layout `members_activity.xml`, description "Get permanent link").

**Which step class gets the new steps?**
Add to `ShareNGSteps` — keeps share-related steps cohesive. If the steps grow significantly, a dedicated `PermalinkSteps` can be extracted later.

**How to assert the tap outcome?**
Tapping the permalink button opens the Android system Intent Chooser (share sheet), confirmed via XML inspection of the live device. The chooser headline text is `"Sharing link"` (found in `com.android.intentresolver:id/headline`). Primary strategy: `waitByTextVisible("Sharing link")`. Secondary/documented option: `waitById(WAIT_TIME, "com.android.intentresolver:id/chooser_headline_row_container")` — valid on stock Android 12+ (API 31+) only; the `com.android.intentresolver` package did not exist before API 31.

**Dismissal after assertion:** The Intent Chooser remains on screen after the assertion and causes subsequent scenarios to fail if not dismissed. Use `pressBack()` to close the system share sheet cleanly. Add an explicit `And Alice closes the share sheet` step to the tapping scenario so teardown is visible and traceable, rather than hiding it in `Hooks.java`'s `@After`.

**Feature file placement**
New file `permalink.feature` under `src/test/resources/io/cucumber/`, tagged `@permalink @nooc10`. This follows the pattern established by `privatelink.feature` and `sharingng.feature`.

## Risks / Trade-offs

- **Share sheet detection** → The system share sheet renders outside the app's view hierarchy; UIAutomator2 can still find it. The headline text `"Sharing link"` is confirmed stable on the test device; use `waitByTextVisible("Sharing link")` as the primary strategy. The resource-id `com.android.intentresolver:id/chooser_headline_row_container` is valid on stock Android 12+ (API 31+) but has no public stability contract across OEM skins.
- **Test isolation — share sheet stays open** → The Intent Chooser does not auto-dismiss between Cucumber scenarios. Without an explicit `pressBack()`, subsequent scenarios start with the share sheet still visible and fail. Mitigation: explicit `And Alice closes the share sheet` step after the assertion in the tapping scenario.
- **Timing sensitivity** → The permalink button tap triggers an async system action. The 7-second `WAIT_TIME` constant should be sufficient; flag this scenario `@noci` if it proves unstable in CI.

## Open Questions

- The headline text `"Sharing link"` is confirmed on the test device but may vary across locales or OEM skins. If the nightly emulator image changes locale or skin, fall back to `waitById(WAIT_TIME, "com.android.intentresolver:id/chooser_headline_row_container")`.
