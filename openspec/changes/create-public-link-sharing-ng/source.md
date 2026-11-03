# create-public-link-sharing-ng

Feature: Create a public link for a file or folder

  Scenario: User creates a password-less view link for a file
    Given a signed-in user on an oCIS server with permission to create links on "report.pdf"
    And the server does not enforce a password for public links
    When the user opens the sharing panel for "report.pdf"
    And taps the "+" button in the PUBLIC LINKS section
    And selects the "Can view" permission
    And confirms creation without a name, password, or expiration date
    Then a new public link appears in the PUBLIC LINKS list labelled "Unnamed Link"
    And tapping its chain icon opens the Android share sheet with the link URL

  Scenario: Server enforces a password for an edit link on a folder
    Given a signed-in user with permission to create links on the folder "Team"
    And the server enforces a password for writeable public links
    When the user opens the sharing panel for "Team"
    And taps the "+" button and selects the "Can edit" permission
    And attempts to confirm creation without entering a password
    Then creation is blocked until a valid password is entered
