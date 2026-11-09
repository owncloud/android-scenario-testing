## Source Scenario Analysis

Adapted from `source.md` (2 source scenarios) plus additional coverage per design.md Goals. All creation combinations — including Secret file drop (folder-only) — are consolidated into one Scenario Outline. The guard-rail uses individual steps because creation is intentionally not completed.

### Step Reuse Table

| Step (adapted to project conventions) | Reuse | Matching expression | Source file |
|---|---|---|---|
| `user Alice is logged` | ✅ Reused | `user {word} is logged` | `LoginSteps.java` |
| `the following items have been created in Alice account` | ✅ Reused | `the following items have been created in {word} account` | `FileListSteps.java` |
| `Alice selects to share the {itemtype} {word}` | ✅ Reused | `Alice selects to {word} the {itemtype} {word}` | `FileListSteps.java` |
| `Alice creates public link on {itemtype} {word} via NG with the following fields` | ❌ New | mirrors `Alice creates link on {itemtype} {word} with the following fields` | — |
| `public link should be created on {word} via NG with the following fields` | ❌ New | mirrors `link should be created/edited on {word} with the following fields` | — |
| `Alice taps to add a public link` | ❌ New (guard-rail only) | — | — |
| `Alice selects {string} permission for the public link` | ❌ New (guard-rail only) | — | — |
| `the public link confirm button is disabled` | ❌ New (guard-rail only) | — | — |

### Adapted Scenarios

**Scenario Outline 1 — Create a public link (all creation combinations)**

```gherkin
@createpubliclink @smoke
Scenario Outline: Create a public link via NG
  Given the following items have been created in Alice account
    | type   | name   |
    | <type> | <item> |
  When Alice selects to share the <type> <item>
  And Alice creates public link on <type> <item> via NG with the following fields
    | permission      | <permission>      |
    | name            | <link_name>       |
    | password        | <password>        |
    | expiration days | <expiration_days> |
  Then public link should be created on <item> via NG with the following fields
    | permission      | <permission>      |
    | name            | <expected_name>   |
    | expiration days | <expiration_days> |

  Examples:
    | type   | item               | permission       | link_name  | password | expiration_days | expected_name |
    | folder | PublicLinksNG1     | Can view         |            | aa55AA.. |                 | Unnamed Link  |
    | file   | PublicLinksNG2.txt | Can view         |            | aa55AA.. |                 | Unnamed Link  |
    | file   | PublicLinksNG3.txt | Can view         | mylink     | aa55AA.. |                 | mylink        |
    | file   | PublicLinksNG4.txt | Can view         |            | aa55AA.. | 7               | Unnamed Link  |
    | folder | PublicLinksNG5     | Can view         | folderlink | aa55AA.. | 3               | folderlink    |
    | folder | PublicLinksNG6     | Can edit         |            | aa55AA.. |                 | Unnamed Link  |
    | folder | PublicLinksNG7     | Secret file drop |            | aa55AA.. |                 | Unnamed Link  |
```

Note: UNRESOLVED — "Unnamed Link" label must be verified against the running app (source.md reference only). Secret file drop is folder-only; same optional fields apply as for other permissions.

**Scenario 2 — Guard-rail (source.md Scenario 2, adapted)**

```gherkin
@createpubliclink
Scenario: Creation of public link is blocked when mandatory password is not entered
  Given the following items have been created in Alice account
    | type   | name           |
    | folder | PublicLinksNG9 |
  When Alice selects to share the folder PublicLinksNG9
  And Alice taps to add a public link
  And Alice selects "Can edit" permission for the public link
  Then the public link confirm button is disabled
```

---

## 1. Locators (from `ui_elements_dictionary.md`)

- [x] 1.1 Add public link button — `add_public_link_button` (ImageButton, "Add public link") — RESOLVED
- [x] 1.2 Permission selector — per-permission RadioButtons in the creation dialog:
  - `can_view_public_link_radio_button` (RadioButton, label: "Can view")
  - `can_edit_public_link_radio_button` (RadioButton, label: "Can edit")
  - `secret_file_drop_public_link_radio_button` (RadioButton, label: "Secret file drop") — RESOLVED
- [x] 1.3 Password field — `password_value` (TextInputEditText) — RESOLVED
- [x] 1.4 Link name field — `public_link_name_edit_text` (EditText, "Link name") — RESOLVED
- [x] 1.5 Expiration date — `expiration_date_switch` (SwitchCompat) to enable; `expiration_date` (TextView) to read back — RESOLVED
- [x] 1.6 Confirm button — `create_public_link_button` (AppCompatButton, "Create public link") — RESOLVED
- [ ] 1.7 Display label for a nameless link — locator is `public_link_display_name` (TextView); UNRESOLVED: actual string ("Unnamed Link" per source.md) needs runtime verification against the app
- [x] 1.8 Capitalization of "Secret file drop" — dictionary, `spaces.feature` (lines 345, 385, 386), and all artifacts consistently use `"Secret file drop"` (lowercase f/d) — RESOLVED
- [x] 1.9 Copy link button — `copy_public_link_button` (ImageButton) — RESOLVED

## 2. Page Object

**Note:** `SpaceMembersPage.java` already implements the identical public-link creation flow (same locators, same dialog). `PublicLinkNGPage.java` can copy or reuse its implementation directly. Key detail from `SpaceMembersPage`: the password field uses a two-step flow — tap `set_password_button` (opens the password dialog), type into `password_value` (TextInputEditText), then tap `set_password_button` again (confirms). The display-name constant there is `PUBLIC_LINK_DISPLAY_NAME_ID = "com.owncloud.android:id/public_link_display_name"`. Permission is selected via `findUIAutomatorSubText(permission).click()`.

- [x] 2.1 Create `PublicLinkNGPage.java` under `src/test/java/e2e/pages/` extending `CommonPage`; constructor receives `AndroidDriver`
- [x] 2.2 Add `@AndroidFindBy` fields for: add-link button, permission selector, password field, name field, expiration field, confirm button (locators from §1)
- [x] 2.3 Add `tapAddPublicLink()` — taps the "+" button in the PUBLIC LINKS section
- [x] 2.4 Add `selectPermission(String label)` — selects the named permission option (e.g., "Can view", "Can edit", "Secret file drop")
- [x] 2.5 Add `enterPassword(String password)` — two-step: tap `set_password_button`, type into `password_value`, tap `set_password_button` again
- [x] 2.6 Add `enterName(String name)` — types into the link name field
- [x] 2.7 Add `setExpirationDays(int days)` — opens the expiration picker and sets the date N days from today
- [x] 2.8 Add `isConfirmEnabled()` — returns whether the confirm/create button is enabled
- [x] 2.9 Add `confirmCreation()` — taps the confirm/create button
- [x] 2.10 Add `isPublicLinkListed(String label)` — checks whether an entry with the given label is visible in the PUBLIC LINKS list
- [x] 2.11 Add `getPublicLinkPermission(String label)` — returns the permission text shown for the named entry
- [x] 2.12 Add `getPublicLinkExpirationDays(String label)` — returns the expiration info for the named entry

## 3. Step Definitions

Add to `ShareNGSteps.java` or a new `PublicLinksNGSteps.java`; `LinksSteps.java` is untouched.

- [ ] 3.1 Add `@When("Alice creates public link on {itemtype} {word} via NG with the following fields")` (DataTable fields: `permission`, `name`, `password`, `expiration days`) — for each present field calls the corresponding `PublicLinkNGPage` method; absent fields are skipped; always calls `tapAddPublicLink()` first and `confirmCreation()` last; new step, no existing equivalent found
- [ ] 3.2 Add `@Then("public link should be created on {word} via NG with the following fields")` (DataTable fields: `permission`, `name`, `expiration days`) — asserts entry visibility and each present field value via `PublicLinkNGPage`; new step, no existing equivalent found
- [ ] 3.3 Add `@When("Alice taps to add a public link")` — delegates to `PublicLinkNGPage.tapAddPublicLink()`; guard-rail scenario only; new step, no existing equivalent found
- [ ] 3.4 Add `@When("Alice selects {string} permission for the public link")` — delegates to `PublicLinkNGPage.selectPermission()`; guard-rail scenario only; new step, no existing equivalent found
- [ ] 3.5 Add `@Then("the public link confirm button is disabled")` — asserts `!isConfirmEnabled()`; new step, no existing equivalent found
- [ ] 3.6 If `permission` or `expiration days` introduce vocabulary patterns not covered by `openspec/steps_rules.md`, add a task to update that file

## 4. Feature File

- [ ] 4.1 Create `src/test/resources/io/cucumber/linksng.feature` with Feature header and Background (`Given user Alice is logged`)
- [ ] 4.2 Copy Scenario Outline 1 verbatim from "Adapted Scenarios" above; verify all UNRESOLVED items in §1 are resolved before this step (includes Secret file drop row — folder-only)
- [ ] 4.3 Copy Scenario 2 (guard-rail) verbatim from "Adapted Scenarios" above

## 5. Compile Check

- [ ] 5.1 Run `./gradlew clean compileJava compileTestJava` and confirm zero errors; verify against oCIS backend
