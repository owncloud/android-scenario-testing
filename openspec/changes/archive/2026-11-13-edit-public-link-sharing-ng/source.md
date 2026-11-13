# edit-public-link-sharing-ng

## Scenario 1: User changes the permission type of an existing link

This verifies the edit PATCH targets the item permissions endpoint: when a user edits the permission level of a link that already has a name and expiration, the link's attributes are sent to the item permissions endpoint and the permission is updated on the server after the edit succeeds.

```gherkin
Given a signed-in user with update permission on "report.pdf" that has a "Can view" public link named "Report"
When the user opens the sharing panel for "report.pdf"
And taps the pencil icon on the "Report" link
And changes the permission to "Can edit"
And taps "SAVE"
Then the PUBLIC LINKS list refreshes and the "Report" link shows "Can edit"
And the edit is sent to the item permissions endpoint
```

## Scenario 2: Server enforces a password when upgrading a folder link to edit

This verifies the password policy is re-evaluated on permission change during an edit: a password-less view link on a folder, edited to a writeable type on a server that mandates passwords for writeable shares, must block SAVE until a compliant password is entered and then be sent via a separate setPassword request.

```gherkin
Given a signed-in user with update permission on the folder "Team" holding a password-less "Can view" link
And the server enforces a password for writeable public links
When the user opens the sharing panel for "Team"
And taps the pencil icon on the link and selects "Can edit"
Then SAVE is disabled until a valid password is entered
And on saving, the new password is sent through a separate setPassword request
```
