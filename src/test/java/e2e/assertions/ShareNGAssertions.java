/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.assertions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        assertFalse(world.shareNGPage().isShareeDisplayed(sharee));
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
        assertTrue(world.shareNGPage().isShareeDisplayedWithPermission(sharee, expectedPermission));
    }

    private void assertExpirationDate(String sharee, String expectedExpirationDays) {
        String expectedLocalDate = (expectedExpirationDays == null || expectedExpirationDays.trim().isEmpty()) ? null
                : DateUtils.formatDate(expectedExpirationDays.trim(), DateUtils.DateFormatType.NUMERIC);
        Log.log(Level.FINE, "Assert " + sharee + " has expiration date: " + expectedLocalDate);
        assertTrue(world.shareNGPage().isShareeDisplayedWithExpirationDate(sharee, expectedLocalDate));
    }

    public void assertUserHasAccess(String userType, String sharee, String itemName) throws IOException {
        Log.log(Level.FINE, "Assert " + sharee + " has access to item: " + itemName);
        assertTrue(isAccessible(userType, sharee, itemName));
    }

    public void assertUserHasNoAccess(String userType, String sharee, String itemName) throws IOException {
        Log.log(Level.FINE, "Assert " + sharee + " has no access to item: " + itemName);
        assertFalse(isAccessible(userType, sharee, itemName));
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
