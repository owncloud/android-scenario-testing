## ADDED Requirements

### Requirement: User can remove a public link from a file

On an oCIS backend, when the user taps the trashbin icon on a named public link entry in the PUBLIC LINKS list, the app SHALL display a confirmation dialog. If the user confirms, the link SHALL be deleted and the entry SHALL no longer appear in the PUBLIC LINKS list.

#### Scenario: Public link removed after confirmation

- **GIVEN** a named public link exists on a file
- **WHEN** the user opens the sharing panel for that file
- **AND** the user taps the trashbin icon on the named link entry
- **AND** the user confirms the removal dialog
- **THEN** the link entry SHALL no longer appear in the PUBLIC LINKS list

### Requirement: User can cancel removal of a public link

On an oCIS backend, when the user dismisses the confirmation dialog for public link removal, no deletion SHALL occur and the link entry SHALL remain in the PUBLIC LINKS list unchanged.

#### Scenario: Public link survives after cancellation

- **GIVEN** a named public link exists on a file
- **WHEN** the user opens the sharing panel for that file
- **AND** the user taps the trashbin icon on the named link entry
- **AND** the user dismisses the confirmation dialog
- **THEN** the link entry SHALL still appear in the PUBLIC LINKS list
