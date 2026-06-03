# Android Scenario Testing

<!-- OSPO-managed README | Generated: 2026-04-16 | v2 -->

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE) [![ownCloud OSPO](https://img.shields.io/badge/OSPO-ownCloud-blue)](https://kiteworks.com/opensource)

[![Tests last execution](https://github.com/owncloud/android-scenario-testing/actions/workflows/e2e.yml/badge.svg)](https://github.com/owncloud/android-scenario-testing/actions/workflows/e2e.yml)

This repository contains end-to-end scenario tests for the [ownCloud Android app](https://github.com/owncloud/android). Tests are written as Gherkin feature files using Cucumber for step interpretation and Appium for device interaction. The test suite covers core functionality including file operations, sharing, and Spaces, and runs against both oCIS and ownCloud 10 backends.

## Part of Mobile (Android)

This repository provides the automated end-to-end test suite for the [ownCloud Android app](https://github.com/owncloud/android). It is used in CI pipelines to validate app behavior against real server backends.

## Getting Started

Follow the steps below to set up and run the end-to-end test suite.

### Architecture

Tests use [Gherkin Syntax](https://cucumber.io/docs/gherkin/) scenarios interpreted by [Cucumber](https://cucumber.io/), with step implementations in Java and device interaction via [Appium](http://appium.io/).

![](architecture.png)

### Prerequisites

- An [Appium](https://appium.io/) instance running and reachable
- At least one Android device or emulator attached (verify with `adb devices`)
- `$ANDROID_HOME` environment variable set to the Android SDK folder

### Running Tests

#### 1. Build app

First, build the [app](https://github.com/owncloud/android) from the expected branch/commit to get the test object.

The [buildAPK](https://github.com/owncloud/android-scenario-testing/blob/master/buildapk/buildAPK.sh) script will build the app by using the qa variant available in the app. Such variant:

- will disable welcome wizard
- will disable the release notes
- will set basic auth as forced authentication method, required to execute the test suites

Besides of that, the script also:

- builds a release-signed apk with the given keystore path and pass (check script variables)
- moves the final artifact to the correct place (`/src/test/resources` folder in the current structure)

As commented, check the script's variables for the proper setup in your own environment or CI system.

In the current repository there will be always an `owncloud.apk` file located in `/src/test/resources`, as example or fallback.

#### 2. Execute tests

The script `executeTests` will launch the tests. The following environment variables must be set in advance

| Variable | Required | Default | Description |
|---|---|---|---|
| `$OC_SERVER_URL` | Yes | -- | ownCloud server URL to test against |
| `$APPIUM_URL` | No | `localhost:4723` | Appium server URL |
| `$UDID_DEVICE` | No | -- | Device/emulator ID (from `adb devices`) |
| `$BACKEND` | No | `oCIS` | Backend type: `oCIS` or `oC10` |

The script needs some parameters. Check help `executeTests -h`


To execute all tests but the ignored ones (or any other tagged ones):

		export UDID_DEVICE=emulator-5554
		export OC_SERVER_URL=https://my.owncloud.server
		export APPIUM_URL=localhost:4723
		export BACKEND=oCIS
		./executeTests -t "not @ignore"

Example: `./executeTests -t "not @ignore and not @noocis"` runs tests suitable for oCIS.

Since not all tests are suitable for both backends, tests are tagged:

- `@nooc10` -- tests for oCIS only, not suitable for oC10
- `@noocis` -- tests for oC10 only, not suitable for oCIS

The execution will display step by step how the scenario is being executed.

More info in [Cucumber reference](https://cucumber.io/docs/cucumber/api/)

## Documentation

- Feature files are in `src/test/resources/io/cucumber/`
- [Appium documentation](https://appium.io/docs/en/about-appium/getting-started/)
- [Cucumber documentation](https://cucumber.io/docs/gherkin/)

## Version Matrix

| Component | Version |
|---|---|
| Cucumber | 7.31.0 |
| Appium | 3.1.0 |
| Appium UIAutomator2 Driver | 4.2.3 |
| Java Client | 9.4.0 |

## Community & Support

**[Star](https://github.com/owncloud/android-scenario-testing)** this repo and **Watch** for release notifications!

- [ownCloud Website](https://owncloud.com)
- [Community Discussions](https://github.com/orgs/owncloud/discussions)
- [Matrix Chat](https://app.element.io/#/room/#owncloud:matrix.org)
- [Documentation](https://doc.owncloud.com)
- [Enterprise Support](https://owncloud.com/contact-us/)
- [OSPO Home](https://kiteworks.com/opensource)

## Contributing

We welcome contributions! Please read the [Contributing Guidelines](CONTRIBUTING.md)
and our [Code of Conduct](CODE_OF_CONDUCT.md) before getting started.

### Workflow

- **Rebase Early, Rebase Often!** We use a rebase workflow. Always rebase on the target branch before submitting a PR.
- **Dependabot**: Automated dependency updates are managed via Dependabot. Review and merge dependency PRs promptly.
- **Signed Commits**: All commits **must** be PGP/GPG signed. See [GitHub's signing guide](https://docs.github.com/en/authentication/managing-commit-signature-verification).
- **DCO Sign-off**: Every commit must carry a `Signed-off-by` line:
  ```
  git commit -s -S -m "your commit message"
  ```
- **GitHub Actions Policy**: Workflows may only use actions that are (a) owned by `owncloud`, (b) created by GitHub (`actions/*`), or (c) verified in the GitHub Marketplace.

## Security

**Do not open a public GitHub issue for security vulnerabilities.**

Report vulnerabilities at **<https://security.owncloud.com>** -- see [SECURITY.md](SECURITY.md).

Bug bounty: [YesWeHack ownCloud Program](https://yeswehack.com/programs/owncloud-bug-bounty-program)

## License

This project is licensed under the [MIT](LICENSE).

## About the ownCloud OSPO

The [Kiteworks Open Source Program Office](https://kiteworks.com/opensource), operating under
the [ownCloud](https://owncloud.com) brand, launched on May 5, 2026, to steward the open source
ecosystem around ownCloud's products. The OSPO ensures transparent governance, license compliance,
community health, and sustainable collaboration between the open source community and
[Kiteworks](https://www.kiteworks.com), which acquired ownCloud in 2023.

- **OSPO Home**: <https://kiteworks.com/opensource>
- **GitHub**: <https://github.com/owncloud>
- **ownCloud**: <https://owncloud.com>

For questions about the OSPO or licensing, contact ospo@kiteworks.com.

### License Migration to Apache 2.0

The OSPO is driving a strategic relicensing of ownCloud repositories toward the
[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0), following
the [Apache Software Foundation's third-party license policy](https://www.apache.org/legal/resolved.html).

Individual repositories will migrate as their audit is completed. The LICENSE file
in each repo reflects its **current** license status (not the target).

**Current license: MIT** (Category A per Apache policy -- permissive, compatible with Apache-2.0).

Migration prerequisites for this repository:

- **CLA/DCO coverage**: All past contributors must have signed agreements permitting relicensing
- **Header updates**: All source file headers must be updated from MIT to Apache-2.0 notice
- **Dependency audit**: Verify no incompatible transitive dependencies
