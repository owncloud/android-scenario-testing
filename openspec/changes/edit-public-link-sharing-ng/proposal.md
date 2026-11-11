## Why

The current E2E suite covers public-link creation on oCIS but has no coverage for editing an existing link. All four editable link attributes — permission, name, password, and expiration date — are unverified in the edit flow.

## What Changes

- Add E2E coverage for editing each of the four editable attributes of an existing oCIS (next-gen) public link. All test scenarios run against a server with mandatory password always enabled.

### Scenarios

- **Scenario Outline – Edit a link attribute (happy paths)**: One atomic outline (4 Examples rows) covering all four editable attributes. Starting permissions are varied (not always "Can view") since the Graph API creates precondition links in any state. Row 1 (file, Can view→Can edit): also covers rename, password change, and expiration change. Row 2 (folder, Can edit→Can view downgrade): also covers adding an expiration. Row 3 (folder, Can view→Secret file drop): also covers clearing the name (reverts to "Unnamed Link"). Row 4 (file, Can edit, no permission change): covers expiration removal. Uses an atomic `Alice edits the public link ... via NG with the following fields` DataTable step (mirrors the creation step). Empty column = no change; `(none)` = clear/remove the field.
- **Scenario – Remove password blocked**: Alice clears the password in the edit dialog; SAVE stays disabled because the server mandates passwords.
## Capabilities

### New Capabilities

- `sharing/edit-public-link-ng`: Behavior of editing an existing public link in the oCIS next-gen sharing flow — permission, name, password, and expiration date changes, including server-enforced password constraints.

### Modified Capabilities

(none — creation behavior in `sharing/public-links-ng` is unchanged)

## Impact

- New Gherkin feature file with one Scenario Outline (4 Examples rows) and one standalone Scenario (oCIS only).
- New or extended step definitions, tasks, page objects, preconditions, and assertions as needed to drive and verify all edit paths.
- No changes to existing creation scenarios or the `sharing/public-links-ng` spec.
