/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.assertions;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(world.shareAPI().isSharedWithMe(itemName, userName, false))
                .as("'%s' should not have access to '%s'", userName, itemName)
                .isFalse();
    }

    public void assertShareeHasAccessToItem(String type, String shareeName, String itemName)
            throws IOException, ParserConfigurationException, SAXException {
        if ("user".equalsIgnoreCase(type)) {
            assertThat(world.shareAPI().isSharedWithMe(itemName, shareeName, false))
                    .as("'%s' should have access to '%s'", shareeName, itemName)
                    .isTrue();
        } else if ("group".equalsIgnoreCase(type)) {
            assertThat(world.shareAPI().isSharedWithMe(itemName, shareeName, true))
                    .as("group '%s' should have access to '%s'", shareeName, itemName)
                    .isTrue();
        }
    }

    public void assertItemIsNotSharedAnymoreWith(String itemName, String sharee) {
        assertThat(world.sharePage().isItemInListPrivateShares(sharee))
                .as("'%s' should not be listed in private shares", sharee)
                .isFalse();
    }

    public void assertRecipientIsVisible(String sharee) {
        assertThat(world.privateSharePage().isSharee(sharee))
                .as("'%s' should be visible as sharee", sharee)
                .isTrue();
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
        assertThat(world.sharePage().isItemInListPrivateShares(sharee))
                .as("'%s' should be listed in private shares", sharee)
                .isTrue();
    }

    private void assertPrivateGroupIsVisible(String groupName) {
        String expectedGroupText = groupName + " (group)";
        Log.log(Level.FINE, "Checking group: " + expectedGroupText);
        assertThat(world.sharePage().isItemInListPrivateShares(expectedGroupText))
                .as("group '%s' should be listed in private shares", expectedGroupText)
                .isTrue();
    }

    private void assertPrivateSharePermissionsAreCorrect(String itemName, String expectedPermissions) {
        Log.log(Level.FINE, "Checking private share permissions: " + expectedPermissions);
        world.sharePage().editPrivateShare(itemName);
        try {
            switch (expectedPermissions) {
                case "17" -> assertOnlyReadPermission();
                case "19" -> assertEditPermission();
                case "25" -> assertDeletePermission();
                case "29" -> assertDeleteAndCreatePermission();
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
        assertThat(world.privateSharePage().isEditEnabled())
                .as("edit permission should not be enabled for read-only share")
                .isFalse();
    }

    private void assertEditPermission() {
        Log.log(Level.FINE, "Edit");
        assertThat(world.privateSharePage().isEditEnabled())
                .as("edit permission should be enabled")
                .isTrue();
    }

    private void assertDeletePermission() {
        Log.log(Level.FINE, "Delete");
        assertThat(world.privateSharePage().isCreateSelected())
                .as("create permission should not be selected")
                .isFalse();
        assertThat(world.privateSharePage().isChangeSelected())
                .as("change permission should not be selected")
                .isFalse();
        assertThat(world.privateSharePage().isDeleteSelected())
                .as("delete permission should be selected")
                .isTrue();
    }

    private void assertDeleteAndCreatePermission() {
        Log.log(Level.FINE, "Delete and Create");
        assertThat(world.privateSharePage().isCreateSelected())
                .as("create permission should be selected")
                .isTrue();
        assertThat(world.privateSharePage().isChangeSelected())
                .as("change permission should not be selected")
                .isFalse();
        assertThat(world.privateSharePage().isDeleteSelected())
                .as("delete permission should be selected")
                .isTrue();
    }

    public static void assertShareMatches(OCShare remoteShare, Map<String, String> expectedFields) {
        assertThat(remoteShare)
                .as("remote share should exist")
                .isNotNull();
        for (Map.Entry<String, String> entry : expectedFields.entrySet()) {
            assertShareFieldMatches(remoteShare, entry.getKey(), entry.getValue());
        }
    }

    private static void assertShareFieldMatches(OCShare remoteShare, String key, String expectedValue) {
        Log.log(Level.FINE, "Checking share field: " + key + " with expected value: " + expectedValue);
        switch (key) {
            case "id" -> assertThat(remoteShare.getId())
                    .as("share id should be '%s'", expectedValue)
                    .isEqualTo(expectedValue);
            case "sharee", "user" -> {
                if ("0".equals(remoteShare.getType())) {
                    assertThat(remoteShare.getShareeName().toLowerCase())
                            .as("sharee name should be '%s'", expectedValue)
                            .isEqualTo(expectedValue.toLowerCase());
                }
            }
            case "group" -> {
                if ("1".equals(remoteShare.getType())) {
                    assertThat(remoteShare.getShareeName().toLowerCase())
                            .as("group name should be '%s'", expectedValue)
                            .isEqualTo(expectedValue.toLowerCase());
                }
            }
            case "password", "password-auto" ->
                    assertThat("3".equals(remoteShare.getType()) && remoteShare.hasPassword())
                            .as("share should be a public link with password")
                            .isTrue();
            case "name" -> assertThat(remoteShare.getLinkName())
                    .as("link name should be '%s'", expectedValue)
                    .isEqualTo(expectedValue);
            case "path" -> assertThat(remoteShare.getItemName())
                    .as("item path should be '%s'", expectedValue)
                    .isEqualTo(expectedValue);
            case "uid_owner" ->
                    assertThat(remoteShare.getOwner().toLowerCase())
                            .as("share owner should be '%s'", expectedValue)
                            .isEqualTo(expectedValue.toLowerCase());
            case "permission", "permissions" -> {
                    Log.log(Level.FINE, "Checking share permissions: " + expectedValue + " against remote share permissions: " + remoteShare.getPermissions());
                    assertThat(remoteShare.getPermissions())
                            .as("share permissions should be '%s'", expectedValue)
                            .isEqualTo(expectedValue);
            }
            case "expiration days" -> {
                String expectedDate = DateUtils.dateInDaysWithServerFormat(expectedValue);
                assertThat(remoteShare.getExpiration())
                        .as("share expiration should be '%s'", expectedDate)
                        .isEqualTo(expectedDate);
            }
        }
    }
}
