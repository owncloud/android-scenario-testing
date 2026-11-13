/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.assertions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import e2e.support.date.DateUtils;
import e2e.support.log.Log;
import e2e.world.World;

public class ShareNGAssertions {

    private final World world;

    public ShareNGAssertions(World world) {
        this.world = world;
    }

    public void assertShareeIsNotVisible(String sharee) {
        Log.log(Level.FINE, "Assert sharee is not visible: " + sharee);
        assertThat(world.shareNGPage().isShareeDisplayed(sharee))
                .as("sharee '%s' should not be visible", sharee)
                .isFalse();
    }

    public void assertShareeIsVisible(String sharee, Map<String, String> fields) {
        Log.log(Level.FINE, "Assert sharee is visible: " + sharee);
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            assertField(sharee, entry.getKey(), entry.getValue());
        }
    }

    private void assertField(String sharee, String key, String value) {
        switch (key) {
            case "permission" -> assertPermission(sharee, value);
            case "expirationDate" -> assertExpirationDate(sharee, value);
        }
    }

    private void assertPermission(String sharee, String expectedPermission) {
        Log.log(Level.FINE, "Assert " + sharee + " has permission: " + expectedPermission);
        assertThat(world.shareNGPage().isShareeDisplayedWithPermission(sharee, expectedPermission))
                .as("sharee '%s' should be displayed with permission '%s'", sharee, expectedPermission)
                .isTrue();
    }

    private void assertExpirationDate(String sharee, String expectedExpirationDays) {
        String expectedLocalDate = (expectedExpirationDays == null || expectedExpirationDays.trim().isEmpty()) ? null
                : DateUtils.formatDate(expectedExpirationDays.trim(), DateUtils.DateFormatType.NUMERIC);
        Log.log(Level.FINE, "Assert " + sharee + " has expiration date: " + expectedLocalDate);
        assertThat(world.shareNGPage().isShareeDisplayedWithExpirationDate(sharee, expectedLocalDate))
                .as("sharee '%s' should have expiration date '%s'", sharee, expectedLocalDate)
                .isTrue();
    }

    public void assertPermalinkButtonDisplayed() {
        Log.log(Level.FINE, "Assert permalink button is displayed");
        assertThat(world.shareNGPage().isPermalinkButtonDisplayed())
                .as("permalink button should be displayed")
                .isTrue();
    }

    public void assertPermalinkActionTriggered() {
        Log.log(Level.FINE, "Assert permalink action (system share sheet) is triggered");
        assertThat(world.shareNGPage().isPermalinkActionTriggered())
                .as("permalink action should be triggered")
                .isTrue();
        // The system Intent Chooser stays open between Cucumber scenarios and causes
        // the next scenario to fail. Dismissed here because close is always paired
        // with this assertion and is not a user-visible step worth surfacing in Gherkin.
        world.shareNGPage().closeShareSheet();
    }

    public void assertUserHasAccess(String userType, String sharee, String itemName) throws IOException {
        Log.log(Level.FINE, "Assert " + sharee + " has access to item: " + itemName);
        assertThat(isAccessible(userType, sharee, itemName))
                .as("'%s' should have access to '%s'", sharee, itemName)
                .isTrue();
    }

    public void assertUserHasNoAccess(String userType, String sharee, String itemName) throws IOException {
        Log.log(Level.FINE, "Assert " + sharee + " has no access to item: " + itemName);
        assertThat(isAccessible(userType, sharee, itemName))
                .as("'%s' should not have access to '%s'", sharee, itemName)
                .isFalse();
    }

    private boolean isAccessible(String userType, String sharee, String itemName) throws IOException {
        if ("user".equalsIgnoreCase(userType)) {
            return world.graphAPI().isSharedWithMe(itemName, sharee);
        } else if ("group".equalsIgnoreCase(userType)) {
            return world.graphAPI().isSharedWithMe(itemName, "Bob");
        }
        return false;
    }
}
