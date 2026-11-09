## Purpose

Specifies the behavior of public-link creation in the oCIS (next-gen) sharing flow, including permission selection, mandatory password enforcement, and the visibility of the resulting link entry in the PUBLIC LINKS list.

## Requirements

### Requirement: User can create a view-only public link with a valid password on oCIS

On an oCIS backend that mandates passwords for public links, the app SHALL allow a user to create a view-only ("Can view") public link by providing a valid password. After confirmation, a named link entry SHALL appear in the PUBLIC LINKS list and the copy-link button SHALL be accessible.

#### Scenario: View-only link created successfully with a valid password

- **WHEN** the user opens the sharing panel for a file on an oCIS server
- **AND** the user taps the "+" button in the PUBLIC LINKS section
- **AND** the user selects "Can view" permission
- **AND** the user enters a valid password
- **AND** the user confirms creation without a custom name or expiration date
- **THEN** a new public link entry labelled "Unnamed Link" SHALL appear in the PUBLIC LINKS list
- **AND** the copy-link button on the entry SHALL be visible and tappable

### Requirement: User can create a public link with a custom name on oCIS

On an oCIS backend, the app SHALL allow a user to provide a custom name when creating a public link. After confirmation, the link entry SHALL appear in the PUBLIC LINKS list labelled with the user-provided name.

#### Scenario: Link created with a custom name

- **WHEN** the user opens the sharing panel for a file on an oCIS server
- **AND** the user taps the "+" button in the PUBLIC LINKS section
- **AND** the user enters a custom name and a valid password
- **AND** the user confirms creation
- **THEN** a public link entry labelled with the custom name SHALL appear in the PUBLIC LINKS list

### Requirement: User can create a public link with an expiration date on oCIS

On an oCIS backend, the app SHALL allow a user to set an expiration date when creating a public link. After confirmation, the link entry SHALL reflect the configured expiration date.

#### Scenario: Link created with an expiration date

- **WHEN** the user opens the sharing panel for a file on an oCIS server
- **AND** the user taps the "+" button in the PUBLIC LINKS section
- **AND** the user enters a valid password and sets an expiration date
- **AND** the user confirms creation
- **THEN** a public link entry SHALL appear in the PUBLIC LINKS list with the configured expiration date visible

### Requirement: User can create a "Can edit" public link with a valid password on oCIS

On an oCIS backend, the app SHALL allow a user to create a writeable ("Can edit") public link on a folder by providing a valid password. After confirmation, the link entry SHALL reflect the "Can edit" permission.

#### Scenario: Editable link created successfully with a valid password on a folder

- **WHEN** the user opens the sharing panel for a folder on an oCIS server
- **AND** the user taps the "+" button in the PUBLIC LINKS section
- **AND** the user selects "Can edit" permission and enters a valid password
- **AND** the user confirms creation
- **THEN** a public link entry with the "Can edit" permission SHALL appear in the PUBLIC LINKS list

### Requirement: User can create a "Secret file drop" public link for a folder on oCIS

On an oCIS backend, the app SHALL allow a user to create an upload-only ("Secret file drop") public link for a folder. "Secret file drop" is a folder-only permission; the same optional fields (name, password, expiration date) apply as for other permission types. After confirmation, the link entry SHALL appear in the PUBLIC LINKS list reflecting the "Secret file drop" permission.

#### Scenario: Secret file drop link created for a folder

- **WHEN** the user opens the sharing panel for a folder on an oCIS server
- **AND** the user taps the "+" button in the PUBLIC LINKS section
- **AND** the user selects "Secret file drop" permission and provides the required fields
- **AND** the user confirms creation
- **THEN** a public link entry with the "Secret file drop" permission SHALL appear in the PUBLIC LINKS list

### Requirement: Link creation is blocked when the mandatory password field is empty

On an oCIS backend, the app SHALL prevent confirmation of a public link with writeable ("Can edit") permission when the password field is left empty. The confirm action SHALL remain disabled until a non-empty password is entered.

#### Scenario: Creation blocked with empty password on a writeable link

- **WHEN** the user opens the sharing panel for a folder on an oCIS server
- **AND** the user taps the "+" button in the PUBLIC LINKS section
- **AND** the user selects "Can edit" permission
- **AND** the user attempts to confirm creation without entering a password
- **THEN** the confirm action SHALL be disabled
- **AND** no public link entry SHALL be added to the PUBLIC LINKS list
