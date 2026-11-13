## Adapted Scenarios

The following Gherkin is the agreed implementation contract. The apply phase copies it verbatim into `src/test/resources/io/cucumber/linksng.feature` under a new `@removelinkng` Rule.

The file's existing Background (`Given user Alice is logged`) and feature-level `@nooc10` tag apply to both scenarios.

```gherkin
@removelinkng
Rule: Remove a public link

  @removelinkng
  Scenario: User removes a public link from a file
    Given the following items have been created in Alice account
      | type | name               |
      | file | RemoveLinksNG1.txt |
    And Alice has a public link on file RemoveLinksNG1.txt via NG with the following fields
      | name       | Quarterly report |
      | permission | Can view         |
      | password   | aa55AA..         |
    When Alice selects to share the file RemoveLinksNG1.txt
    And Alice removes the public link "Quarterly report" on file RemoveLinksNG1.txt
    Then the public link "Quarterly report" should not be listed in the PUBLIC LINKS section

  @removelinkng
  Scenario: User cancels the removal of a public link
    Given the following items have been created in Alice account
      | type | name               |
      | file | RemoveLinksNG2.txt |
    And Alice has a public link on file RemoveLinksNG2.txt via NG with the following fields
      | name       | Quarterly report |
      | permission | Can view         |
      | password   | aa55AA..         |
    When Alice selects to share the file RemoveLinksNG2.txt
    And Alice taps the remove button on the public link "Quarterly report"
    And Alice dismisses the public link removal dialog
    Then the public link "Quarterly report" should be listed in the PUBLIC LINKS section
```

---

## Step Reuse Table

| Step expression | Action | Source |
|---|---|---|
| `user Alice is logged` | REUSE | `LoginSteps.java:23` |
| `the following items have been created in {word} account` | REUSE | `FileListSteps.java:54` |
| `Alice has a public link on {itemtype} {word} via NG with the following fields` | REUSE | `LinksNGSteps.java:31` — fields: `name`, `permission`, `password` |
| `Alice selects to {word} the {itemtype} {word}` (`share`, `file`, item) | REUSE | `FileListSteps.java:90` |
| `Alice removes the public link {string} on {itemtype} {word}` | NEW — composite (tap + confirm); mirrors `Alice removes {word} over the space {word}` (SpacesSteps.java:146) and `Alice edits the public link "X" on {itemtype} {word} via NG with the following fields` (LinksNGSteps.java:56) |
| `Alice taps the remove button on the public link {string}` | NEW — isolated tap for disaggregated scenarios; mirrors `Alice taps to edit the public link {string}` (LinksNGSteps.java:62) |
| `Alice dismisses the public link removal dialog` | NEW — no existing equivalent |
| `the public link {string} should not be listed in the PUBLIC LINKS section` | NEW — no existing equivalent |

**All locators resolved (no UNRESOLVED items):**
- `remove_public_link_button` → `com.owncloud.android:id/remove_public_link_button` — confirmed in `ui_elements_dictionary.md` and `SpaceMembersPage.java:80`
- Confirm button → `android:id/button1` — confirmed from `SpacesTasks.removeLinkOverSpace()` + already declared in `PublicLinkNGPage` as `okButton`
- Cancel button → `android:id/button2` — standard AlertDialog cancel button

---

## 1. Page object — Removal interaction

- [x] 1.1 Add `@AndroidFindBy(id = "android:id/button2") private WebElement cancelButton` field to `src/test/java/e2e/pages/PublicLinkNGPage.java`
- [x] 1.2 Add `tapRemoveButton(String linkName)` method — resolves the row via `findLinkByNameOrNull(linkName)`, finds the child with `AppiumBy.id("com.owncloud.android:id/remove_public_link_button")`, and taps it
- [x] 1.3 Add `confirmRemoval()` method — calls `okButton.click()` (already declared)
- [x] 1.4 Add `dismissRemovalDialog()` method — calls `cancelButton.click()`
- [x] 1.5 Add `isPublicLinkNotListed(String linkName)` method — waits for the list to reload via `waitForListById(WAIT_TIME, PUBLIC_LINK_ITEM_LAYOUT_ID)`, then returns `findLinkByNameOrNull(linkName) == null` (avoids the stale-element race fixed in SpaceMembersPage commit 75b2bfe)

## 2. Tasks layer

- [x] 2.1 Add `removePublicLink(String linkName)` to `src/test/java/e2e/tasks/LinksNGTasks.java` — composite: calls `tapRemovePublicLink(linkName)` then `confirmPublicLinkRemoval()`; mirrors `removeLinkOverSpace` in SpacesTasks
- [x] 2.2 Add `tapRemovePublicLink(String linkName)` — isolated tap only: `world.publicLinkNGPage().tapRemoveButton(linkName)`; mirrors `tapEditPublicLink` in LinksNGTasks
- [x] 2.3 Add `confirmPublicLinkRemoval()` → `world.publicLinkNGPage().confirmRemoval()` (symmetric with dismissPublicLinkRemovalDialog)
- [x] 2.4 Add `dismissPublicLinkRemovalDialog()` → `world.publicLinkNGPage().dismissRemovalDialog()`

## 3. Assertions layer

- [x] 3.1 Add `assertPublicLinkListed(String linkName)` to `src/test/java/e2e/assertions/LinksNGAssertions.java` → asserts `world.publicLinkNGPage().isPublicLinkListed(linkName)` is true (used by scenario 2)
- [x] 3.2 Add `assertPublicLinkNotListed(String linkName)` → asserts `world.publicLinkNGPage().isPublicLinkNotListed(linkName)` is true (used by scenario 1)

## 4. Step definitions

- [ ] 4.1 Add the following to `src/test/java/e2e/steps/LinksNGSteps.java` (keep @When before @Then, in order after existing entries):
  - `@When("Alice removes the public link {string} on {itemtype} {word}")` → `world.linksNGTasks().removePublicLink(linkName)` — composite happy-path step
  - `@When("Alice taps the remove button on the public link {string}")` → `world.linksNGTasks().tapRemovePublicLink(linkName)` — isolated tap for disaggregated scenarios
  - `@When("Alice dismisses the public link removal dialog")` → `world.linksNGTasks().dismissPublicLinkRemovalDialog()`
  - `@Then("the public link {string} should be listed in the PUBLIC LINKS section")` → `world.linksNGAssertions().assertPublicLinkListed(linkName)`
  - `@Then("the public link {string} should not be listed in the PUBLIC LINKS section")` → `world.linksNGAssertions().assertPublicLinkNotListed(linkName)`

## 5. Feature file

- [ ] 5.1 Add the adapted scenarios (from the **Adapted Scenarios** section above) to `src/test/resources/io/cucumber/linksng.feature` under a new `@removelinkng` Rule block

## 6. Compile check

- [ ] 6.1 Run `./gradlew clean compileJava compileTestJava` — must pass with no errors

## 7. Steps rules update

- [ ] 7.1 Add the following rows to the **Common mismatch traps** table in `openspec/steps_rules.md`:
  - `"user Alice has logged in"` → `"user Alice is logged"` (login prefix convention)
  - `"Alice opens the sharing panel for file X"` → `"Alice selects to share the file X"` (context menu verb)
  - `"Alice taps the trash/delete button on link X"` → use `Alice removes the public link "X" on {itemtype} Y` for happy paths (composite); use `Alice taps the remove button on the public link "X"` only for disaggregated cancel scenarios — mirrors the edit/taps-to-edit split
