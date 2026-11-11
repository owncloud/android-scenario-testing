## Adapted Scenarios

> These are the agreed Gherkin scenarios for implementation. The apply phase copies them verbatim into the feature file.
> UNRESOLVED items must be resolved before implementation begins.
>
> Convention used in DataTable columns:
> - Empty string → no change to that field (consistent with the creation step)
> - `(none)` → clear / remove that field (name cleared → "Unnamed Link"; expiration removed → no date)

---

### Scenario Outline: Edit an existing public link

```gherkin
  @editpubliclink
  Scenario Outline: User edits an existing public link
    Given the following items have been created in Alice account
      | type   | name   |
      | <type> | <item> |
    And Alice has a public link on <type> <item> via NG with the following fields
      | name            | <initial_name>  |
      | permission      | <initial_perm>  |
      | password        | aa55AA..        |
      | expiration days | <initial_expiry>|
    When Alice selects to share the <type> <item>
    And Alice edits the public link "<initial_name>" on <type> <item> via NG with the following fields
      | name            | <new_name>     |
      | permission      | <new_perm>     |
      | password        | <new_password> |
      | expiration days | <new_expiry>   |
    Then public link should be edited on <item> via NG with the following fields
      | name            | <expected_name>  |
      | permission      | <expected_perm>  |
      | expiration days | <expected_expiry>|

    Examples:
      | type   | item             | initial_name | initial_perm     | initial_expiry | new_name | new_perm | new_password | new_expiry | expected_name | expected_perm | expected_expiry |
      | file   | EditLinksNG1.txt | FileLink     | Can view         | 3              | NewName  | Can edit | bb66BB..     | 10         | NewName       | Can edit      | 10              |
      | folder | EditLinksNG2     | FolderLink   | Can edit         |                |          | Can view |              | 7          | FolderLink    | Can view      | 7               |
      | folder | EditLinksNG3     | DropLink     | Can view         |                | (none)   | Secret file drop |        |            | Unnamed Link  | Secret file drop |                |
      | file   | EditLinksNG4.txt | ExpiryLink   | Can edit         | 7              |          |          |              | (none)     | ExpiryLink    | Can edit      |                 |
```

---

### Scenario: Remove password blocked by server policy

```gherkin
  @editpassword
  Scenario: Removing the password of a link is blocked when the server mandates passwords
    Given the following items have been created in Alice account
      | type | name             |
      | file | EditLinksNG5.txt |
    And Alice has a public link on file EditLinksNG5.txt via NG with the following fields
      | name       | ProtectedLink |
      | permission | Can view      |
      | password   | aa55AA..      |
    When Alice selects to share the file EditLinksNG5.txt
    And Alice taps to edit the public link "ProtectedLink"
    And Alice removes the password on the public link edit
    Then the public link save button is disabled
```

---

### Step reuse table

| Scenario step | Verdict | Existing step expression | Source file |
|---|---|---|---|
| `user Alice is logged` | REUSE (Background) | `user {word} is logged` | LoginSteps.java |
| `the following items have been created in Alice account` DataTable | REUSE | `the following items have been created in {word} account` | FileListSteps.java |
| `Alice has a public link on {itemtype} {word} via NG with the following fields` | NEW — no existing equivalent found | — | — |
| `Alice selects to share the <type> <item>` | REUSE | `Alice selects to {word} the {itemtype} {word}` | FileListSteps.java |
| `Alice edits the public link {string} on {itemtype} {word} via NG with the following fields` | NEW — no existing equivalent found | — | — |
| `Alice taps to edit the public link {string}` | NEW — no existing equivalent found (used by blocked scenario) | — | — |
| `Alice removes the password on the public link edit` | NEW — no existing equivalent found | — | — |
| `public link should be edited on {word} via NG with the following fields` | NEW — no existing equivalent found | — | — |
| `the public link save button is disabled` | NEW — no existing equivalent found | — | — |

---

## 1. Precondition: API method for NG public links on personal drive items

- [x] 1.1 UNRESOLVED API resolved: OCS `ShareAPI.createShare()` does not support expiration dates. Using Graph API instead. Added `createPublicLinkForItem()` to `GraphAPI.java` (`POST /graph/v1beta1/drives/{driveId}/items/{itemId}/createLink`). Created `LinksNGPreconditions.java` with `publicLinkExists(sharingUser, itemType, itemName, fields)` delegating to the new Graph API method.
- [x] 1.2 Added `@Given("Alice has a public link on {itemtype} {word} via NG with the following fields")` to `LinksNGSteps.java`; added `Given` import.

## 2. Page object: edit dialog interactions

- [x] 2.1 LOCATOR resolved: no separate NG edit dialog in dictionary → edit reuses `add_public_link_fragment.xml`; save button is `create_public_link_button`. USER MUST VERIFY on device: if the edit dialog uses a different layout, update `confirmEdit()` and `isSaveEnabled()` accordingly.
- [x] 2.2 LOCATOR resolved: edit dialog reuses same element IDs as creation (`public_link_name_edit_text`, `set_password_button`/`password_value`, `expiration_date_switch`). USER MUST VERIFY on device.
- [x] 2.3 Added `tapEditPublicLink(String linkName)` to `PublicLinkNGPage.java`.
- [x] 2.4 Added `isSaveEnabled()` to `PublicLinkNGPage.java` (checks `createPublicLinkButton.isEnabled()`).
- [x] 2.5 Added `confirmEdit()` to `PublicLinkNGPage.java` (clicks `createPublicLinkButton`, waits on `addPublicLinkButton`).
- [x] 2.6 Added `setNameOnEdit(String name)` to `PublicLinkNGPage.java` (`(none)` = clear only, otherwise clear+type).
- [x] 2.7 Added `enterPasswordOnEdit(String password)` to `PublicLinkNGPage.java`.
- [x] 2.8 LOCATOR resolved: `remove_password_button` found in `password_layout.xml`. Added `removePasswordOnEdit()` to `PublicLinkNGPage.java`.
- [x] 2.9 Added `setExpirationDaysOnEdit(int days)` to `PublicLinkNGPage.java` (delegates to `setExpirationDays()`).
- [x] 2.10 LOCATOR resolved: toggle `expiration_date_switch` OFF. Added `removeExpirationDateOnEdit()` to `PublicLinkNGPage.java`.

## 3. Tasks layer

- [ ] 3.1 Add `editPublicLink(String linkName, Map<String,String> fields)` to `LinksNGTasks.java`. Calls `tapEditPublicLink`, then iterates over fields (skipping empty values; handling `(none)` sentinel by calling the remove method), then calls `confirmEdit()`. Follow the `applyField` switch pattern from `createPublicLink`.
- [ ] 3.2 Add `tapEditPublicLink(String linkName)` to `LinksNGTasks.java` → `world.publicLinkNGPage().tapEditPublicLink(linkName)`.
- [ ] 3.3 Add `enterPasswordOnEdit(String password)` to `LinksNGTasks.java` → `world.publicLinkNGPage().enterPasswordOnEdit(password)`.
- [ ] 3.4 Add `removePasswordOnEdit()` to `LinksNGTasks.java` → `world.publicLinkNGPage().removePasswordOnEdit()`.

## 4. Assertions layer

- [ ] 4.1 Add `assertPublicLinkEdited(String itemName, Map<String,String> fields)` to `LinksNGAssertions.java`. Reuse `isPublicLinkListed(linkName)`, `isPublicLinkListedWithPermission(linkName, permission)`, and `getPublicLinkExpirationDays(linkName)` page methods. For `expiration days = (none)` assert no expiration date is visible. For `expiration days` = empty string, skip the check. UNRESOLVED: determine whether a server-state check (GET item permissions endpoint) is required; add if confirmed.
- [ ] 4.2 Add `assertSaveButtonDisabled()` to `LinksNGAssertions.java` → `assertThat(world.publicLinkNGPage().isSaveEnabled()).isFalse()`.

## 5. Step definitions

- [ ] 5.1 Add `@Given("Alice has a public link on {itemtype} {word} via NG with the following fields")` to `LinksNGSteps.java`.
- [ ] 5.2 Add `@When("Alice edits the public link {string} on {itemtype} {word} via NG with the following fields")` to `LinksNGSteps.java` → `world.linksNGTasks().editPublicLink(linkName, table)`.
- [ ] 5.3 Add `@When("Alice taps to edit the public link {string}")` to `LinksNGSteps.java` → `world.linksNGTasks().tapEditPublicLink(linkName)`.
- [ ] 5.4 Add `@When("Alice removes the password on the public link edit")` to `LinksNGSteps.java` → `world.linksNGTasks().removePasswordOnEdit()`.
- [ ] 5.5 Add `@Then("public link should be edited on {word} via NG with the following fields")` to `LinksNGSteps.java` → `world.linksNGAssertions().assertPublicLinkEdited(itemName, table)`.
- [ ] 5.6 Add `@Then("the public link save button is disabled")` to `LinksNGSteps.java` → `world.linksNGAssertions().assertSaveButtonDisabled()`.

## 6. World wiring

- [x] 6.1 Added `linksNGPreconditions` field, import, and lazy accessor to `World.java`.

## 7. Feature file

- [ ] 7.1 Create `src/test/resources/io/cucumber/editlinksng.feature` with the Scenario Outline and the one standalone Scenario from the "Adapted Scenarios" section above. Feature-level tags: `@editlinksng @nooc10`. Background: `Given user Alice is logged`.
- [ ] 7.2 Run `./gradlew clean compileJava compileTestJava` and confirm no compilation errors.

## 8. steps_rules.md update

- [ ] 8.1 Add rows to the **Common mismatch traps** table in `openspec/steps_rules.md` for:
  - `Alice edits the public link {string} on {itemtype} {word} via NG with the following fields` — natural phrasing ("Alice opens the edit dialog for link X") does not match; also note the `(none)` sentinel convention for clearing fields.
  - `Alice taps to edit the public link {string}` — used only in disaggregated (blocked-state) scenarios; the atomic edit step is the default for happy paths.
  - `Alice removes the password on the public link edit` — natural phrasing ("Alice deletes/clears the password") does not match.
