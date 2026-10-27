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

Updated manually on PR when a new step introduces a vocabulary pattern not already covered here
(new actor convention, new verb, new constrained parameter type, new structural rule).
Step expressions themselves are always read from the source — do not copy the full step list here.

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

## Item type constraints

- `{itemtype}` → `file` | `folder` | `item`
- `{fileType}` (download/preview steps) → `file` | `audio` | `image` | `video` | `damaged` | `folder`

## Assertions: UI vs server state

UI (Alice subject):
```
Alice should see "name" in the (file)list          # {string} → quotes required
Alice should see "name" inside the folder {word}
Alice should see {word} inside the space {word}
```

Server state (no Alice subject):
```
share should be created/edited on {word} with the following fields
link should be created/edited on {word} with the following fields
the space should be created/updated in server with the following fields
```

## Sharing NG vs legacy

| | Step pattern |
|---|---|
| Add collaborator (NG) | `Alice adds {usertype} {word} via Sharing NG with` |
| Check access (NG) | `{usertype} {word} should have access via NG to {word}` |
| Edit permissions (legacy) | `Alice edits the share on {itemtype} {word} with permissions {word}` |

## Other constrained tokens

- Offline: `av.offline` (not "available offline")
- Share sheet: `share sheet for the item {word} is displayed`
- Conflict fix values: `server` | `keep both` | `local` | `replace`

## Common mismatch traps

| Wrong | Right |
|---|---|
| `Alice is logged` | `user Alice is logged` |
| `Alice opens the folder Documents` | `Alice browses into Documents` |
| `Alice shares the file report.pdf` | `Alice selects to share the file report.pdf` |
| `available offline` | `av.offline` |
| `Alice should see report.pdf in the filelist` | `Alice should see "report.pdf" in the (file)list` |
