## Step reuse analysis

Steps proposed by source.md vs existing step definitions:

| Proposed step | Existing equivalent | File | Decision |
|---|---|---|---|
| Given user Alice is logged | `{word} is logged` | `LoginSteps.java` | REUSE |
| Given the following items have been created in Alice account | `the following items have been created in {word} account` | `FileListSteps.java` | REUSE |
| When Alice selects to share the \<type\> \<item\> | `Alice selects to {word} the {itemtype} {word}` | `FileListSteps.java` | REUSE |
| Then the permalink button is shown | none | — | NEW step, no existing equivalent found |
| When Alice taps the permalink button | none | — | NEW step, no existing equivalent found |
| Then the permalink action is triggered | none | — | NEW step, no existing equivalent found |

---

## 1. Resolved UI element IDs

Locators confirmed from `ui_elements_dictionary.md` and user-provided inspection:

- **Permalink button**: `com.owncloud.android:id/getPrivateLinkButton` (ImageButton, `layout/share_file_layout.xml`, description "Get private link")
- **Post-tap confirmation**: `com.android.intentresolver:id/chooser_headline_row_container` (system Intent Chooser share sheet — valid on **stock Android 12+ / API 31+** only; use `waitByTextVisible` with the chooser headline as a fallback for OEM devices or older API levels)

## 2. Page object — ShareNGPage

- [ ] 2.1 Add `@AndroidFindBy(id = "com.owncloud.android:id/getPrivateLinkButton")` field in `ShareNGPage`.
- [ ] 2.2 Add `isPermalinkButtonDisplayed()` method to `ShareNGPage` — returns `true` if the element is present and visible.
- [ ] 2.3 Add `tapPermalinkButton()` method to `ShareNGPage` — clicks the permalink button element.
- [ ] 2.4 Add `isPermalinkActionTriggered()` method to `ShareNGPage` — returns `true` if the system Intent Chooser share sheet is displayed. Primary strategy: `waitByTextVisible` with a stable text visible in the chooser (e.g. the chooser headline). Secondary/documented: `waitById(WAIT_TIME, "com.android.intentresolver:id/chooser_headline_row_container")` — valid on stock Android 12+ (API 31+) only.

## 3. Tasks — ShareNGTasks

- [ ] 3.1 Add `tapPermalinkButton()` method to `ShareNGTasks` — delegates to `world.shareNGPage().tapPermalinkButton()`.

## 4. Assertions — ShareNGAssertions

- [ ] 4.1 Add `assertPermalinkButtonDisplayed()` method to `ShareNGAssertions` — calls `world.shareNGPage().isPermalinkButtonDisplayed()` and asserts true.
- [ ] 4.2 Add `assertPermalinkActionTriggered()` method to `ShareNGAssertions` — calls `world.shareNGPage().isPermalinkActionTriggered()` and asserts true.

## 5. Step definitions — ShareNGSteps

- [ ] 5.1 Add `@Then("the permalink button is shown")` step in `ShareNGSteps` — delegates to `world.shareNGAssertions().assertPermalinkButtonDisplayed()`. (NEW step)
- [ ] 5.2 Add `@When("Alice taps the permalink button")` step in `ShareNGSteps` — delegates to `world.shareNGTasks().tapPermalinkButton()`. (NEW step)
- [ ] 5.3 Add `@Then("the private link action is triggered")` step in `ShareNGSteps` — delegates to `world.shareNGAssertions().assertPermalinkActionTriggered()`. (NEW step)

## 6. Feature file

- [ ] 6.1 Create `src/test/resources/io/cucumber/permalink.feature` with:
  - Feature tag `@permalink @nooc10`
  - Background: `Given user Alice is logged`
  - Scenario Outline "Permalink button is visible in Share header" covering file and folder items, using the reused steps from `FileListSteps` and the new `Then the permalink button is shown` step.
  - Scenario Outline "Tapping permalink button copies or sends the private link" covering file and folder items, using the new `When Alice taps the permalink button` and `Then the private link action is triggered` steps.

## 7. Verify

- [ ] 7.1 Run `./gradlew clean compileJava compileTestJava` and confirm zero compilation errors.
- [ ] 7.2 (Optional/local only) Run `./executeTests -t @permalink` against a running oCIS server and verify both scenarios pass. If flaky on CI, tag with `@noci`.
