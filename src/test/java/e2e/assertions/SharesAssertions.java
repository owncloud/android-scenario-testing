/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.assertions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import e2e.model.OCShare;
import e2e.support.date.DateUtils;
import e2e.support.log.Log;
import e2e.world.World;

public class SharesAssertions {

    private final World world;

    public SharesAssertions(World world) {
        this.world = world;
    }

    public void assertPrivateShareCreatedOrEdited(String itemName, Map<String, String> fields)
            throws IOException, ParserConfigurationException, SAXException {
        Log.log(Level.FINE, "Checking private share UI assertions");
        assertPrivateShareUiFields(itemName, fields);
        Log.log(Level.FINE, "Checking private share API/server assertions");
        OCShare share = world.shareAPI().getShare(itemName);
        assertShareMatches(share, fields);
    }

    public void assertUserDoesNotHaveAccessToItem(String userName, String itemName)
            throws IOException, ParserConfigurationException, SAXException {
        assertFalse(world.shareAPI().isSharedWithMe(itemName, userName, false));
    }

    public void assertShareeHasAccessToItem(String type, String shareeName, String itemName)
            throws IOException, ParserConfigurationException, SAXException {
        if ("user".equalsIgnoreCase(type)) {
            assertTrue(world.shareAPI().isSharedWithMe(itemName, shareeName, false));
        } else if ("group".equalsIgnoreCase(type)) {
            assertTrue(world.shareAPI().isSharedWithMe(itemName, shareeName, true));
        }
    }

    public void assertItemIsNotSharedAnymoreWith(String itemName, String sharee) {
        assertFalse(world.sharePage().isItemInListPrivateShares(sharee));
    }

    public void assertRecipientIsVisible(String sharee) {
        assertTrue(world.privateSharePage().isSharee(sharee));
    }

    private void assertPrivateShareUiFields(String itemName, Map<String, String> fields) {
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            assertPrivateShareUiField(itemName, entry.getKey(), entry.getValue());
        }
    }

    private void assertPrivateShareUiField(String itemName, String key, String value) {
        switch (key) {
            case "sharee" -> assertPrivateShareeIsVisible(value);
            case "group" -> assertPrivateGroupIsVisible(value);
            case "permissions" -> assertPrivateSharePermissionsAreCorrect(itemName, value);
            default ->
                    Log.log(Level.FINE, "Ignoring unsupported private share UI assertion field: " + key);
        }
    }

    private void assertPrivateShareeIsVisible(String sharee) {
        Log.log(Level.FINE, "Checking sharee: " + sharee);
        assertTrue(world.sharePage().isItemInListPrivateShares(sharee));
    }

    private void assertPrivateGroupIsVisible(String groupName) {
        String expectedGroupText = groupName + " (group)";
        Log.log(Level.FINE, "Checking group: " + expectedGroupText);
        assertTrue(world.sharePage().isItemInListPrivateShares(expectedGroupText));
    }

    private void assertPrivateSharePermissionsAreCorrect(String itemName, String expectedPermissions) {
        Log.log(Level.FINE, "Checking private share permissions: " + expectedPermissions);
        world.sharePage().openPrivateShare(itemName);
        try {
            switch (expectedPermissions) {
                case "1" -> assertOnlyReadPermission();
                case "3" -> assertEditPermission();
                case "9" -> assertDeletePermission();
                case "13" -> assertDeleteAndCreatePermission();
                default -> throw new IllegalArgumentException(
                        "Unsupported private share permissions: " + expectedPermissions
                );
            }
        } finally {
            world.privateSharePage().close();
        }
    }

    private void assertOnlyReadPermission() {
        Log.log(Level.FINE, "Only read");
        assertFalse(world.privateSharePage().isEditEnabled());
    }

    private void assertEditPermission() {
        Log.log(Level.FINE, "Edit");
        assertTrue(world.privateSharePage().isEditEnabled());
    }

    private void assertDeletePermission() {
        Log.log(Level.FINE, "Delete");
        assertFalse(world.privateSharePage().isCreateSelected());
        assertFalse(world.privateSharePage().isChangeSelected());
        assertTrue(world.privateSharePage().isDeleteSelected());
    }

    private void assertDeleteAndCreatePermission() {
        Log.log(Level.FINE, "Delete and Create");
        assertTrue(world.privateSharePage().isCreateSelected());
        assertFalse(world.privateSharePage().isChangeSelected());
        assertTrue(world.privateSharePage().isDeleteSelected());
    }

    public static void assertShareMatches(OCShare remoteShare, Map<String, String> expectedFields) {
        assertNotNull("Remote share should exist", remoteShare);
        for (Map.Entry<String, String> entry : expectedFields.entrySet()) {
            assertShareFieldMatches(remoteShare, entry.getKey(), entry.getValue());
        }
    }

    private static void assertShareFieldMatches(OCShare remoteShare, String key, String expectedValue) {
        switch (key) {
            case "id" -> assertEquals(expectedValue, remoteShare.getId());
            case "sharee", "user" -> {
                if ("0".equals(remoteShare.getType())) {
                    assertEquals(expectedValue.toLowerCase(), remoteShare.getShareeName().toLowerCase());
                }
            }
            case "group" -> {
                if ("1".equals(remoteShare.getType())) {
                    assertEquals(expectedValue.toLowerCase(), remoteShare.getShareeName().toLowerCase());
                }
            }
            case "password", "password-auto" ->
                    assertTrue("3".equals(remoteShare.getType()) && remoteShare.hasPassword());
            case "name" -> assertEquals(expectedValue, remoteShare.getLinkName());
            case "path" -> assertEquals(expectedValue, remoteShare.getItemName());
            case "uid_owner" ->
                    assertEquals(expectedValue.toLowerCase(), remoteShare.getOwner().toLowerCase());
            case "permission", "permissions" ->
                    assertEquals(expectedValue, remoteShare.getPermissions());
            case "expiration days" -> {
                String expectedDate = DateUtils.dateInDaysWithServerFormat(expectedValue);
                assertEquals(expectedDate, remoteShare.getExpiration());
            }
        }
    }
}
