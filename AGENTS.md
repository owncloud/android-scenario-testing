# AI Agent Guidelines for Android Scenario Testing

This file provides context and guidance for AI coding agents (Claude Code, GitHub Copilot, Cursor, etc.) working in this repository.

## What This Project Is

A Gradle/Java end-to-end test harness for the ownCloud Android app. It does **not** contain Android app source code — it runs on a host machine and drives an Android device/emulator via Appium + UIAutomator2, using Cucumber/Gherkin for scenario authoring.

## Repository Overview
- **Product family:** Mobile (Android)
- **Primary language(s):** Java, Gherkin
- **Build system:** Gradle
- **Test framework:** Cucumber, Appium, JUnit
- **CI system:** GitHub Actions

## Build Commands

```bash
# Compile only (what CI checks on every PR)
./gradlew clean compileJava compileTestJava

# Full build + tests
./gradlew build

# Build APK for testing
./buildapk/buildAPK.sh
```

## Running Tests

Tests require a running Appium server, a connected Android device/emulator, and a reachable ownCloud server. The `executeTests` shell script is the standard entry point — it wraps `./gradlew --no-daemon clean test` and forwards coordinates as `-D` system properties.

```bash
# Start Appium server (first time: installs appium + uiautomator2 driver)
./runAppium.sh

# Run all non-ignored tests
./executeTests -t "not @ignore"

# Run a single tagged scenario or subset
./executeTests -t @createfolder
./executeTests -t "not @ignore and not @noocis and not @noci"
```

Environment variables consumed by `executeTests`:
- `OC_SERVER_URL` — ownCloud/oCIS server URL
- `APPIUM_URL` — Appium server (default `localhost:4723`)
- `UDID_DEVICE` — ADB device ID (default `emulator-5554`)
- `BACKEND` — `oCIS` or `oC10`

Local device/server properties can also be set in `local.properties` (not committed to VCS; see `LocProperties.java` for the full list of keys).

### Default tag filter

`src/test/resources/cucumber.properties` applies `not @ignore` and randomises execution order by default.

### Rerun mechanism

The build chains three Gradle tasks: `test` → `cucumberRerun1` → `cucumberRerun2`. Each reruns the failures from the previous run. Only `cucumberRerun2` fails the build. Rerun files live under `target/cucumber-reports/rerun/`.

## Architecture & Key Paths

```
src/test/java/e2e/
├── runner/RunCucumberTest.java   # JUnit 4 + Cucumber entry point
├── hooks/Hooks.java              # @Before/@After: driver setup, video, cleanup
├── world/World.java              # PicoContainer DI root — shared test context
├── pages/                        # Page Object Model — all Appium UI interactions
├── steps/                        # Cucumber step definitions (thin; delegate to tasks/pages)
├── tasks/                        # Multi-step UI task abstractions (e.g. "upload a file")
├── preconditions/                # Test setup helpers (create files/shares via API)
├── assertions/                   # AssertJ-based custom assertions
├── api/                          # REST clients: WebDAV, OCS, Graph API
├── model/                        # Data models: OCFile, OCShare, OCSpace, …
└── support/                      # Utilities: logging, video recording, HTTP helpers, parsers
src/test/resources/io/cucumber/   # Gherkin .feature files + fixture files (images, docs, etc.)
buildapk/                         # Script to build the ownCloud Android app for testing
executeTests                      # Script to launch the test suite
server/                           # Server setup and configuration
build.gradle                      # Gradle build configuration
runAppium.sh                      # Appium server launcher
```

**Key design patterns:**
- **Page Object Model** — every screen/component has a class in `pages/`; steps never call Appium directly.
- **PicoContainer DI** — `World` is injected into every step class and hooks; it holds the Appium driver and all shared state for a scenario.
- **API-first preconditions** — test setup (creating files, users, shares) uses the REST API via `preconditions/` rather than the UI, to keep scenarios fast and isolated.
- **Cucumber tags** control which scenarios run. Standard tags: `@ignore` (skip always), `@noocis` (incompatible with oCIS), `@noci` (skip in CI).

## Dependency Management

All library versions are in `gradle/libs.versions.toml`. Update versions there, not in `build.gradle` directly.

Dependabot is configured for automated dependency updates. Review and merge Dependabot PRs as part of regular maintenance. Do not introduce new dependencies without discussion in an issue first.

## CI

- **Every PR**: `compile.yml` runs `compileJava compileTestJava` on JDK 17.
- **Nightly (Mon–Fri 02:30 UTC)**: `e2e.yml` builds the APK, spins up an oCIS server, runs the emulator suite, and uploads HTML reports + video artifacts.
- Java version: **17 (Temurin)**.

## Development Conventions

- **Branching:** `master` is the default branch
- **Commit messages:** DCO sign-off required (`git commit -s`)
- **Code style:** No specific linter configured; match existing code style
- **PR process:** Open a PR against master. All CI checks must pass.

## Automated proposals from the app repo

Some changes in this repo originate from a cross-repo bridge: the app
repo's SDD process produces a Gherkin scenario proposal on each PR,
which is forwarded here with a pre-assigned `change-id` and placed in
`openspec/changes/<change-id>/source.md` before this proposal flow runs.

Before drafting `proposal.md`, any `specs/**/spec.md` delta, or
`tasks.md` for a change:

1. Check whether `openspec/changes/<change-id>/source.md` exists.
2. If it does, treat it as raw, unreviewed input — not a final spec.
   It contains the originating app PR number, commit SHA, affected
   capability, change type, the proposed Gherkin scenario(s), and any
   detected UI locators. Use it as the basis for the proposal.
3. If it does not exist, this is a manually-authored proposal. Create
   `source.md` following the template in `openspec/steps_rules.md`:
   one 3-line plain-English description per scenario (what it tests,
   which backend, any known constraints) followed by the rough Gherkin.
   Vague phrasing is expected — the proposal step adapts it.

## Step reuse (mandatory before writing any spec delta)

Before including any Given/When/Then step in a spec delta:

1. Read `openspec/steps_rules.md` for vocabulary and structural constraints.
   Use it to adapt input phrasing to the project's conventions before searching.
2. Grep all step definition classes under `src/test/java/.../steps/` for
   `@Given`/`@When`/`@Then` expressions — this is the authoritative list.
   `steps_rules.md` is a vocabulary guide, not a substitute for reading the source.
3. For each step in the proposed scenario, check whether a semantically
   equivalent step already exists — same action or assertion, even if
   phrased differently.
4. If an equivalent exists: use the EXISTING step's exact wording, not
   the proposal's original phrasing. Record in `tasks.md` which step
   was reused and which file it came from.
5. If no equivalent exists: create a new step, flag it explicitly in
   `tasks.md` as "new step, no existing equivalent found", and if it
   introduces a new pattern (actor, verb, parameter type, structural rule),
   add a task to update `openspec/steps_rules.md`.
6. Never create a step that differs from an existing one only in
   superficial ways (word order, synonyms, capitalization).
7. If a step, locator, or error message text cannot be resolved from
   the step files or `ui_elements_dictionary.md`, mark it explicitly as
   "UNRESOLVED" in `tasks.md`. Never fabricate or guess.
   
## UI element lookup

`ui_elements_dictionary.md` at the repo root is the authoritative catalog of UI
element identifiers, kept in sync via CI from the app repo. It contains
tables with columns such as: element ID, element Type, description, and the
layout it belongs to.

Before referencing any UI element in a spec delta, Page Object note, or
`tasks.md` locator entry:

1. Search `ui_elements_dictionary.md` for an entry matching the element described
   in the scenario (match by meaning, not literal string).
2. If found: use the locator value and type exactly as listed there.
   Do not invent or guess a selector.
3. If not found: check `source.md` in this change's folder for a
   `## Locators` section — the element may be new in this PR and not
   yet synced into `ui_elements_dictionary.md` (sync runs after merge to the app
   repo's main branch, so same-PR elements can lag behind).
4. If still not found in either place: mark it explicitly in `tasks.md`
   as `- [ ] UNRESOLVED LOCATOR: <element> — not found in ui_dictionary.md
   or source.md, needs manual resolution before implementation`.
   Never fabricate a resource-id or testTag value.

## OSPO Policy Constraints

### GitHub Actions
- **Only** use actions owned by `owncloud`, created by GitHub (`actions/*`), verified on the GitHub Marketplace, or verified by the ownCloud Maintainers.
- Pin all actions to their full commit SHA (not tags): `uses: actions/checkout@<SHA> # vX.Y.Z`
- Never introduce actions from unverified third parties.

### Licensing
- All code contributions must be compatible with the **MIT** license.
- Do not introduce new **copyleft-licensed dependencies** (GPL, AGPL, LGPL, MPL) without explicit discussion in an issue first.

### Git Workflow
- **Rebase policy**: Always rebase; never create merge commits. Use `git pull --rebase` and `git rebase` before pushing.
- **Signed commits**: All commits **must** be PGP/GPG signed (`git commit -S -s`).
- **DCO sign-off**: Every commit needs a `Signed-off-by` line (`git commit -s`).
- **Conventional Commits & Squash Merge**: Use the [Conventional Commits](https://www.conventionalcommits.org/) format where the repository enforces it. Many repos use squash merge, where the PR title becomes the commit message on the default branch — apply Conventional Commits format to PR titles as well.

## Context for AI Agents
- Match existing code style; do not refactor unrelated code in the same PR
- Write tests for new functionality; keep PRs focused and atomic
- Feature files use Gherkin syntax — follow existing patterns for new scenarios
- Step definitions are in Java — follow existing naming conventions
- Tests require a running Appium server and an Android device/emulator
- Tests require a running ownCloud server (oCIS or OC10)
