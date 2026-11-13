# remove-public-link-sharing-ng

## Scenario 1: User removes a public link from a file inside a space

Confirms the happy path for the item-scoped deletion, which is the case the sharing panel is actually used for. The distinguishing precondition is that the link belongs to a file inside a space rather than to the space root, so the request must address the item path. The link is expected to vanish from the list right away.

```gherkin
Given user "Alice" has logged in to the Android client
And user "Alice" has a file "report.pdf" inside the project space "Marketing"
And user "Alice" has created a public link named "Quarterly report" for "report.pdf"
And user "Alice" holds the "libre.graph/driveItem/permissions/delete" permission on "report.pdf"
When user "Alice" opens the sharing panel for "report.pdf"
Then the public link "Quarterly report" should be listed in the "PUBLIC LINKS" section
And a trashbin icon should be displayed on the public link "Quarterly report"
When user "Alice" taps the trashbin icon on the public link "Quarterly report"
Then a confirmation dialog mentioning "Quarterly report" should be displayed
When user "Alice" confirms the removal
Then user "Alice" should see the message "Public link removed correctly"
And the public link "Quarterly report" should not be listed in the "PUBLIC LINKS" section
```

## Scenario 2: User cancels the removal of a public link

Guards the confirmation step so an accidental tap cannot destroy a link that cannot be recreated. What sets this apart is that the user reaches the dialog and then backs out, so no request may reach the server at all. The link must survive completely untouched.

```gherkin
Given user "Alice" has logged in to the Android client
And user "Alice" has a file "report.pdf" inside the project space "Marketing"
And user "Alice" has created a public link named "Quarterly report" for "report.pdf"
And user "Alice" holds the "libre.graph/driveItem/permissions/delete" permission on "report.pdf"
When user "Alice" opens the sharing panel for "report.pdf"
And user "Alice" taps the trashbin icon on the public link "Quarterly report"
Then a confirmation dialog mentioning "Quarterly report" should be displayed
When user "Alice" dismisses the confirmation dialog
Then the public link "Quarterly report" should still be listed in the "PUBLIC LINKS" section
```
