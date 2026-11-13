## Context

See proposal.md — Why. The existing NG infrastructure (`PublicLinkNGPage`, `LinksNGTasks`, `LinksNGAssertions`, `LinksNGSteps`) covers creation and editing. Deletion adds three new UI interactions: tapping the per-row trashbin button, confirming (or dismissing) the dialog, and asserting the link list state.

**Locators confirmed in `ui_elements_dictionary.md` and existing code:**
- `remove_public_link_button` (ImageButton, `layout/public_link_item.xml`) — per-row trashbin; already used in `SpaceMembersPage.tapRemoveLink()`.
- `android:id/button1` — confirmation dialog's positive button; already used by `SpacesTasks.removeLinkOverSpace()` via `SpaceMembersPage.tapOk()`, and already declared in `PublicLinkNGPage` as `okButton`.
- `android:id/button2` — confirmation dialog's cancel button (standard AlertDialog).

No success snackbar assertion is included — the existing spaces link-removal scenario (`Alice removes {word} over the space {word}` → `Alice should not see the link {word} on {word}`) sets the precedent: assert absence of the link, not a transient message.

## Goals / Non-Goals

**Goals:**
- Minimal extension to the existing NG layer covering removal confirmation and cancellation.
- Full reuse of the `Alice has a public link on {itemtype} {word} via NG with the following fields` precondition from the edit feature.

**Non-Goals:**
- Removal on a project-space root (a separate capability).
- oC10 support — oCIS only.
- Asserting success messages or snackbars.

## Decisions

**Extend `PublicLinkNGPage` following the same per-row child-lookup pattern used by `tapEditPublicLink()`.**
`tapRemoveButton(linkName)` resolves the row via the existing private `findLinkByNameOrNull()`, then finds the child `remove_public_link_button` within that row element. This mirrors the edit button tap exactly.

**Reuse the already-declared `okButton` (`android:id/button1`) for confirmation.**
`PublicLinkNGPage` already declares `okButton` and uses it in `setExpirationDays()`. A `confirmRemoval()` method simply calls `okButton.click()`. The cancel button (`android:id/button2`) needs a new `@AndroidFindBy` field.

**No new precondition step or class.**
`Alice has a public link on {itemtype} {word} via NG with the following fields` already creates named links via `GraphAPI.createPublicLinkForItem()`. The same step and infrastructure are reused with `name`, `permission`, and `password` fields.

## Risks / Trade-offs

- **Timing after deletion**: The list refresh is network-bound. `assertPublicLinkNotListed` must wait for the list to reload. Use `waitForListById` (same mechanism as `findLinkByNameOrNull`) rather than a bare `findElements` to avoid a false-negative race — this is the same stale-element issue fixed in `SpaceMembersPage` (commit 75b2bfe).
