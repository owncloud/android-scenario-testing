# create-public-link-sharing-ng

## Scenario 1: User creates a view-only link by supplying the required password

Validates the happy path for creating a read-only public link on a server that mandates passwords. The user fills in the required password field and confirms creation. The expected outcome is a named link entry in the PUBLIC LINKS list with a shareable URL.

```gherkin
Given a signed-in user on an oCIS server with permission to create links on "report.pdf"
When the user opens the sharing panel for "report.pdf"
And taps the "+" button in the PUBLIC LINKS section
And selects the "Can view" permission
And enters a valid password
And confirms creation without a name or expiration date
Then a new public link appears in the PUBLIC LINKS list labelled "Unnamed Link"
And tapping its chain icon opens the Android share sheet with the link URL
```

## Scenario 2: Creation is blocked when the mandatory password field is left empty

Verifies that the server-side password policy blocks creation of writeable links until a valid password is supplied. The expected outcome is that the confirmation action stays disabled until a non-empty password is entered.

```gherkin
Given a signed-in user with permission to create links on the folder "Team"
When the user opens the sharing panel for "Team"
And taps the "+" button and selects the "Can edit" permission
And attempts to confirm creation without entering a password
Then creation is blocked until a valid password is entered
```