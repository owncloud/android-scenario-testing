## Why

The ownCloud Android app exposes a "permalink" button in the Share header that lets users copy or send an item's private link, but this UI path has no automated test coverage. Adding coverage ensures regressions in the share-to-permalink flow are caught by CI.

## What Changes

- New Gherkin scenario(s) in a dedicated feature file that open the Share view for an item, assert the permalink button is visible, and tap it to verify the private-link copy/send action.
- New step definitions for `the permalink button is shown` and `tapping the permalink button copies or sends the item's private link`.
- New or extended page-object support for the permalink button element in the Share/ShareNG screen.

## Capabilities

### New Capabilities

- `sharing/permalink`: Verifying that the permalink button is visible in the Share header and that tapping it triggers the copy/send of the item's private link.

### Modified Capabilities

_(none — existing private-link navigation scenarios in `privatelink.feature` are not affected)_

## Impact

- New `.feature` file (e.g. `permalink.feature`) under `src/test/resources/io/cucumber/`.
- New step definitions class or additions to an existing share-related steps class.
- Possible new `@AndroidFindBy` element(s) in `SharePage` or `ShareNGPage` for the permalink button.
- Applies to oCIS backend only (`@nooc10` tag required); the permalink/private-link concept is not available on oC10.
- The tap-to-copy action may invoke a system share sheet or a clipboard write — both are timing-sensitive Appium interactions.
