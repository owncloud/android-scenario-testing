# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What This Project Is

A Gradle/Java end-to-end test harness for the ownCloud Android app. It does **not** contain Android app source code — it runs on a host machine and drives an Android device/emulator via Appium + UIAutomator2, using Cucumber/Gherkin for scenario authoring.

## Build Commands

```bash
# Compile only (what CI checks on every PR)
./gradlew clean compileJava compileTestJava

# Full build + tests
./gradlew build
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

## Architecture

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
```

**Key design patterns:**
- **Page Object Model** — every screen/component has a class in `pages/`; steps never call Appium directly.
- **PicoContainer DI** — `World` is injected into every step class and hooks; it holds the Appium driver and all shared state for a scenario.
- **API-first preconditions** — test setup (creating files, users, shares) uses the REST API via `preconditions/` rather than the UI, to keep scenarios fast and isolated.
- **Cucumber tags** control which scenarios run. Standard tags: `@ignore` (skip always), `@noocis` (incompatible with oCIS), `@noci` (skip in CI).

## Dependency Management

All library versions are in `gradle/libs.versions.toml`. Update versions there, not in `build.gradle` directly.

## CI

- **Every PR**: `compile.yml` runs `compileJava compileTestJava` on JDK 17.
- **Nightly (Mon–Fri 02:30 UTC)**: `e2e.yml` builds the APK, spins up an oCIS server, runs the emulator suite, and uploads HTML reports + video artifacts.
- Java version: **17 (Temurin)**.
