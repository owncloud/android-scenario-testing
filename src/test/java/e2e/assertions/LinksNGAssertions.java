/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.assertions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.logging.Level;

import e2e.support.date.DateUtils;
import e2e.support.log.Log;
import e2e.world.World;

public class LinksNGAssertions {

    private final World world;

    public LinksNGAssertions(World world) {
        this.world = world;
    }

    public void assertPublicLinkCreated(String itemName, Map<String, String> fields) {
        Log.log(Level.FINE, "Assert public link created via NG on item: " + itemName);
        String linkName = fields.get("name");
        assertThat(world.publicLinkNGPage().isPublicLinkListed(linkName))
                .as("public link '%s' should be listed", linkName)
                .isTrue();
        String permission = fields.get("permission");
        if (permission != null && !permission.isEmpty()) {
            assertThat(world.publicLinkNGPage().isPublicLinkListedWithPermission(linkName, permission))
                    .as("link '%s' should display permission '%s'", linkName, permission)
                    .isTrue();
        }
        String expirationDays = fields.get("expiration days");
        if (expirationDays != null && !expirationDays.isEmpty()) {
            String expectedDate = DateUtils.formatDate(expirationDays, DateUtils.DateFormatType.NUMERIC);
            assertThat(world.publicLinkNGPage().getPublicLinkExpirationDays(linkName))
                    .as("expiration date of link '%s'", linkName)
                    .contains(expectedDate);
        }
    }

    public void assertConfirmButtonDisabled() {
        Log.log(Level.FINE, "Assert public link confirm button is disabled");
        assertThat(world.publicLinkNGPage().isConfirmEnabled())
                .as("public link confirm button should be disabled")
                .isFalse();
    }
}
