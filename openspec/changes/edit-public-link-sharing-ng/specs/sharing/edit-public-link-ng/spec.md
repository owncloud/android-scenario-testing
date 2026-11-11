## Purpose

Specifies the behavior of editing an existing public link in the oCIS (next-gen) sharing flow, covering all four editable attributes: permission, name, password, and expiration date, including the server-enforced constraint on password removal.

## ADDED Requirements

### Requirement: User can change the permission type of an existing public link on oCIS

On an oCIS backend, the app SHALL allow a user to edit the permission of an existing named public link on files and folders. The updated permission SHALL be submitted to the item permissions endpoint. After a successful save, the PUBLIC LINKS list SHALL refresh and the link entry SHALL display the updated permission.

The test environment runs with a server that mandates passwords for all public links; precondition links always carry a password. Dynamic server-configuration scenarios (e.g., toggling the mandatory-password policy mid-scenario) are out of scope.

#### Scenario: Permission changed from "Can view" to "Can edit" on a file link

- **WHEN** the user opens the sharing panel for a file that has an existing "Can view" public link
- **AND** the user taps the edit (pencil) icon on the link entry
- **AND** the user changes the permission to "Can edit"
- **AND** the user taps SAVE
- **THEN** the PUBLIC LINKS list SHALL refresh and the link entry SHALL show "Can edit"

#### Scenario: Permission changed from "Can view" to "Can edit" on a folder link

- **WHEN** the user opens the sharing panel for a folder that has an existing "Can view" public link
- **AND** the user taps the edit (pencil) icon on the link entry
- **AND** the user changes the permission to "Can edit"
- **AND** the user taps SAVE
- **THEN** the PUBLIC LINKS list SHALL refresh and the link entry SHALL show "Can edit"

#### Scenario: Permission changed from "Can view" to "Secret file drop" on a folder link

- **WHEN** the user opens the sharing panel for a folder that has an existing "Can view" public link
- **AND** the user taps the edit (pencil) icon on the link entry
- **AND** the user changes the permission to "Secret file drop"
- **AND** the user taps SAVE
- **THEN** the PUBLIC LINKS list SHALL refresh and the link entry SHALL show "Secret file drop"

### Requirement: User can change the name of an existing public link on oCIS

On an oCIS backend, the app SHALL allow a user to rename an existing public link. The updated name SHALL be reflected in the PUBLIC LINKS list after a successful save. If the name field is cleared, the server SHALL assign the default name "Unnamed Link".

#### Scenario: Link renamed to a new non-empty value

- **WHEN** the user taps the edit icon on a named public link and sets a new non-empty name
- **AND** the user taps SAVE
- **THEN** the PUBLIC LINKS list SHALL show the link with the new name

#### Scenario: Link name cleared, reverts to "Unnamed Link"

- **WHEN** the user taps the edit icon on a named public link and clears the name field
- **AND** the user taps SAVE
- **THEN** the PUBLIC LINKS list SHALL show the link labelled "Unnamed Link"

### Requirement: User can change the password of an existing public link on oCIS

On an oCIS backend, the app SHALL allow a user to replace the password of an existing public link with a new password that meets the server's complexity policy. The link SHALL be saved successfully with the new password.

#### Scenario: Password changed to a new valid value

- **WHEN** the user taps the edit icon on a public link and enters a new password that meets the complexity policy
- **AND** the user taps SAVE
- **THEN** the link SHALL be saved and SHALL remain listed in the PUBLIC LINKS list

### Requirement: Removing the password of a public link is blocked when the server mandates passwords

On an oCIS backend that mandates passwords for all public links, the app SHALL prevent saving an edit when the password field is cleared. The SAVE action SHALL remain disabled until a valid password is present.

#### Scenario: SAVE disabled when password is removed on a mandatory-password server

- **WHEN** the user taps the edit icon on a public link and removes the password
- **AND** the server mandates passwords for all public links
- **THEN** the SAVE action SHALL be disabled

### Requirement: User can change the expiration date of an existing public link on oCIS

On an oCIS backend, the app SHALL allow a user to add, change, or remove the expiration date of an existing public link. The PUBLIC LINKS list SHALL reflect the updated expiration state after a successful save.

#### Scenario: Expiration date added to a link that had none

- **WHEN** the user taps the edit icon on a public link that has no expiration date and sets an expiration date
- **AND** the user taps SAVE
- **THEN** the PUBLIC LINKS list SHALL show the link with the configured expiration date

#### Scenario: Existing expiration date changed to a new date

- **WHEN** the user taps the edit icon on a public link that has an expiration date and changes it to a different date
- **AND** the user taps SAVE
- **THEN** the PUBLIC LINKS list SHALL show the link with the new expiration date

#### Scenario: Existing expiration date removed

- **WHEN** the user taps the edit icon on a public link that has an expiration date and removes it
- **AND** the user taps SAVE
- **THEN** the PUBLIC LINKS list SHALL show the link without an expiration date
