# Gherkin Entry Rules

## How to use this file

This is a **vocabulary guide** — it encodes actor conventions, verb constraints, parameter types,
and common mismatch traps derived from the existing step definitions.

It is **not** a complete step catalog. Always grep `src/test/java/e2e/steps/` for the full current
list of `@Given`/`@When`/`@Then` expressions when matching a step. Use this file to guide the
phrasing *before* you search, not as a substitute for searching.

## source.md template (manual proposals)

For manually-authored changes (no cross-repo `source.md`), create the file with this structure
per scenario:

```
## Scenario: <short name>
<3-line plain-English description: what it tests, which backend (oCIS / oC10 / both),
any known constraints or timing concerns>

Given ...
When ...
Then ...
```

Vague phrasing in the Gherkin is expected and fine — the proposal step adapts it using these rules.
The description is the authoritative statement of intent; the Gherkin is the shape.

## Maintenance

When a new step definition introduces a pattern where a reasonable agent reading a rough
`source.md` description would use a different (wrong) phrasing on first attempt, add a row
to the **Common mismatch traps** table below. Additions are required when:

- A natural English phrasing does not grep-match to the correct step (e.g., "shares" vs "selects to share")
- A token has a constrained spelling that differs from ordinary language (e.g., `av.offline`, `(file)list`)
- Two steps look similar but apply to different contexts (e.g., `filelist` vs `(file)list`)

Do not copy full step expressions here — the step source files are the authoritative catalog.

---

Rules for writing input steps that match existing step definitions reliably.

## Actor

- Primary actor: always `Alice`
- Secondary actors: exact username (`Bob`, `Charles`)
- Never: `I`, `the user`, `a user`, `the system`

## Login

```
Given user Alice is logged   # "user" prefix is mandatory
```

## Navigation verbs

| Intent | Step |
|---|---|
| Into a folder | `Alice browses into {word}` |
| Back to root | `Alice browses to root folder` |
| Open a link URL | `Alice opens a private link pointing to {word} with scheme {word}` |
| Open a shortcut | `Alice opens the link shortcut` / `Alice opens the share shortcut` |

`opens` is for links and shortcuts only — never for folder navigation.

## Context menu actions

```
Alice selects to {word} the {itemtype} {word}   # action on a named item
Alice selects to {word}                          # action without a named item
```

Valid `{word}` verbs: `share`, `delete`, `rename`, `move`, `copy`, `download`.

FAB actions use a different step — `Alice selects the option [Upload File|Picture from Camera|Create Shortcut|Create Folder]`.

## Rename flow

Rename is a two-step action — context menu opens the dialog, then the name is set:
```
Alice selects to rename the {itemtype} {word}
Alice sets {word} as new name
```

## Item type constraints

- `{itemtype}` → `file` | `folder` | `item`
- `{fileType}` (download/preview steps) → `file` | `audio` | `image` | `video` | `damaged` | `folder`

## Assertions: UI vs server state

UI (Alice subject):
```
Alice should see "name" in the (file)list          # {string} → quotes required; (file)list with parentheses
Alice should see "name" in the filelist as original # filelist without parentheses — conflict-specific variant
Alice should not see "name" in the filelist anymore # filelist without parentheses — removal assertion
Alice should see "name" inside the folder {word}
Alice should see {word} inside the space {word}    # space items use {word}, not {string}
Alice should see {word} as {word} in the uploads view
Alice should{typePosNeg} see {word} in the links/offline/shares list  # exact token "links/offline/shares"
```

Server state (no Alice subject):
```
share should be created/edited on {word} with the following fields
link should be created/edited on {word} with the following fields
public link should be created on {word} via NG with the following fields
the space should be created/updated in server with the following fields
```

## Public links: oC10 vs NG

```
Alice creates link on {itemtype} {word} with the following fields           # oC10
Alice creates public link on {itemtype} {word} via NG with the following fields  # NG — no "a", "via NG" suffix
```

## Sharing NG vs legacy

| | Step pattern |
|---|---|
| Add collaborator (NG) | `Alice adds {usertype} {word} via Sharing NG with` |
| Check access (NG) | `{usertype} {word} should have access via NG to {word}` |
| Deny access (NG) | `{usertype} {word} should not have access via NG to {word}` |
| Visible in share panel (NG) | `{word} should be visible in Sharing NG with` |
| Not visible in share panel (NG) | `{word} should not be visible in Sharing NG` |
| Edit permissions (legacy) | `Alice edits the share on {itemtype} {word} with permissions {word}` |
| Shared by link (precondition) | `{word} has shared the {itemtype} {word} by link` |

## Spaces

```
Alice adds {word} to the space {word} with             # "to the space" — "the" is required
{word} should be member of the space {word} with       # no "a" before "member"
{word} should not be member of the space {word}
Alice creates a new link to the space {word} with
Alice edits {word} from the space {word} with the following fields
Alice removes {word} from the space {word}
```

## Other constrained tokens

- Offline: `av.offline` (not "available offline")
- Share sheet: `share sheet for the item {word} is displayed`
- Conflict fix values: `server` | `keep both` | `local` | `replace`
- Search/filter: `Alice filters the list using {word}` (not "searches for")

## Common mismatch traps

| Wrong | Right |
|---|---|
| `Alice is logged` | `user Alice is logged` |
| `Alice opens the folder Documents` | `Alice browses into Documents` |
| `Alice navigates to root` | `Alice browses to root folder` |
| `Alice shares the file report.pdf` | `Alice selects to share the file report.pdf` |
| `Alice renames the file X to Y` | `Alice selects to rename the file X` + `Alice sets Y as new name` |
| `Alice searches for X` | `Alice filters the list using X` |
| `Alice clicks Upload File` / `Alice creates a folder` | `Alice selects the option Upload File` / `Alice selects the option Create Folder` |
| `available offline` | `av.offline` |
| `Alice should see report.pdf in the filelist` | `Alice should see "report.pdf" in the (file)list` |
| `Alice should see X in the file list` | `Alice should see "X" in the (file)list` — quotes + `(file)list` |
| `Alice should see X inside space Y` | `Alice should see X inside the space Y` — `{word}` not `{string}`, `the space` required |
| `Alice creates a public link on file X via NG` | `Alice creates public link on file X via NG with the following fields` — no "a" |
| `Alice creates a link on file X` (oC10) | `Alice creates link on file X with the following fields` — no "a" |
| `Bob should have access via NG` | `{usertype} Bob should have access via NG to {word}` — `{usertype}` not `{word}`, item name required |
| `Alice adds Bob to space X` | `Alice adds Bob to the space X with` — `the` required, `with` closes a DataTable step |
| `Bob should be a member of space X` | `Bob should be member of the space X with` — no "a", `the` required |
| `share created for X` | `share should be created/edited on X with the following fields` — server-state, no Alice |
| `link created for X` | `link should be created/edited on X with the following fields` — server-state, no Alice |
| `the {fileType} is downloaded` | `the {fileType} {word} should be marked as downloaded` |
| `Alice opens the edit dialog for link X` / `Alice edits link X via NG` | `Alice edits the public link "X" on {itemtype} {word} via NG with the following fields` — atomic DataTable step; empty cell = no change, `(none)` = clear field |
| `Alice taps to edit the public link "X"` (happy path) | use `Alice edits the public link "X" on {itemtype} {word} via NG with the following fields` for happy paths — `taps to edit` is disaggregated form for blocked-state scenarios only |
| `Alice deletes the password` / `Alice clears the password on the edit` | `Alice removes the password on the public link edit` |
