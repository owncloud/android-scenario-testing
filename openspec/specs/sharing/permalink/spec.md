## Purpose

Specifies the behavior of the permalink button in the Share header, which allows users to copy or send the private link of a file or folder directly from the sharing screen.

## Requirements

### Requirement: Permalink button is visible in Share header

When a user opens the Share view for any file or folder on an oCIS backend, the sharing screen SHALL display a permalink button in its header area.

#### Scenario: Permalink button shown for a file

- **WHEN** the user opens the Share view for a file
- **THEN** the permalink button is visible in the Share header

#### Scenario: Permalink button shown for a folder

- **WHEN** the user opens the Share view for a folder
- **THEN** the permalink button is visible in the Share header

### Requirement: Tapping the permalink button initiates private-link sharing

When the user taps the permalink button, the app SHALL trigger a system-level action (copy to clipboard or Android share sheet) that carries the item's private link URL.

#### Scenario: Tapping permalink button for a file

- **WHEN** the user taps the permalink button in the Share header for a file
- **THEN** the system share sheet or a clipboard-confirmation toast is displayed, indicating the private link has been copied or is ready to send

#### Scenario: Tapping permalink button for a folder

- **WHEN** the user taps the permalink button in the Share header for a folder
- **THEN** the system share sheet or a clipboard-confirmation toast is displayed, indicating the private link has been copied or is ready to send
