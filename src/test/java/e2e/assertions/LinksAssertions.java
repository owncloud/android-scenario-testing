/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.assertions;

import static org.junit.Assert.assertFalse;
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

public class LinksAssertions {

    private final World world;

    public LinksAssertions(World world) {
        this.world = world;
    }

    public void assertPublicLinkCreatedOrEdited(String itemName, Map<String, String> fields)
            throws IOException, ParserConfigurationException, SAXException {
        Log.log(Level.FINE, "Checking public link UI assertions");
        assertPublicLinkUiFields(itemName, fields);
        Log.log(Level.FINE, "Checking public link API/server assertions");
        OCShare share = world.shareAPI().getShare(itemName);
        SharesAssertions.assertShareMatches(share, fields);
    }

    public void assertPublicLinkDoesNotExistAnymore(String itemName)
            throws IOException, ParserConfigurationException, SAXException {
        Log.log(Level.FINE, "Checking public link does not exist anymore: " + itemName);
        assertTrue(world.sharePage().isListLinksEmpty());
        assertTrue(world.shareAPI().getLinksByDefault().isEmpty());
        assertFalse(world.sharePage().isItemInListLinks(itemName + " link"));
    }

    private void assertPublicLinkUiFields(String itemName, Map<String, String> fields) {
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            assertPublicLinkUiField(itemName, entry.getKey(), entry.getValue());
        }
    }

    private void assertPublicLinkUiField(String itemName, String key, String value) {
        switch (key) {
            case "name" -> assertPublicLinkNameIsVisible(value);
            case "password-auto", "password" -> assertPublicLinkPasswordIsEnabled(itemName);
            case "user" -> assertPublicLinkUserIsVisible(itemName);
            case "permission", "permissions" -> assertPublicLinkPermissionsAreCorrect(itemName, value);
            case "expiration days" -> assertPublicLinkExpirationIsCorrect(itemName, value);
            default -> Log.log(Level.FINE, "Ignoring unsupported public link UI assertion field: " + key);
        }
    }

    private void assertPublicLinkNameIsVisible(String linkName) {
        Log.log(Level.FINE, "Checking link name: " + linkName);
        assertTrue(world.sharePage().isItemInListLinks(linkName));
    }

    private void assertPublicLinkPasswordIsEnabled(String itemName) {
        Log.log(Level.FINE, "Checking public link password is enabled");
        world.sharePage().editLink(itemName);
        try {
            assertTrue(world.publicLinksPage().isPasswordEnabled());
        } finally {
            world.publicLinksPage().close();
        }
    }

    private void assertPublicLinkUserIsVisible(String itemName) {
        Log.log(Level.FINE, "Checking public link user/item: " + itemName);
        assertTrue(world.sharePage().isItemInListLinks(itemName));
    }

    private boolean areLinksPermissionsCorrect(String permissions) {
        Log.Log.log(Level.FINE, "Starts: Check permissions: " + permissions);
        switch (permissions) {
            case ("1") -> {
                return world.publicLinksPage().isDownloadViewSelected();
            }
            case ("15")-> {
                return world.publicLinksPage().isDownloadViewUploadSelected();
            }
            case ("4") -> {
                return world.publicLinksPage().isUploadOnlySelected();
            }
        }
        return false;
    }

    private void assertPublicLinkExpirationIsCorrect(String itemName, String expectedExpirationDays) {
        Log.log(Level.FINE, "Checking public link expiration days: " + expectedExpirationDays);
        world.sharePage().editLink(itemName);
        try {
            assertTrue(assertExpirationCorrect(expectedExpirationDays));
        } finally {
            world.publicLinksPage().close();
        }
    }

    private void assertPublicLinkPermissionsAreCorrect(String itemName, String expectedPermissions) {
        Log.log(Level.FINE, "Checking public link permissions: " + expectedPermissions);
        world.sharePage().editLink(itemName);
        try {
            assertTrue(areLinksPermissionsCorrect(expectedPermissions));
        } finally {
            world.publicLinksPage().close();
        }
    }

    private boolean assertExpirationCorrect(String days) {
        Log.Log.log(Level.FINE, "Starts: Check expiration in days: " + days);
        boolean switchEnabled;
        boolean dateCorrect = false;
        int expiration = Integer.parseInt(days);
        String shortDate = DateUtils.formatDate(Integer.toString(expiration), DateUtils.DateFormatType.TEXT);
        Log.Log.log(Level.FINE, "Date to check: " + shortDate + " Expiration: " + expiration);
        //switchEnabled = parseIntBool(expirationSwitch.getAttribute("checked"));
        switchEnabled = world.publicLinksPage().isExpirationSwitchEnabled();
        Log.Log.log(Level.FINE, "SwitchEnabled -> " + switchEnabled);
        if (switchEnabled) {
            dateCorrect = world.publicLinksPage().isExpirationDateCorrect(shortDate);
        }
        Log.Log.log(Level.FINE, "Date Correct -> " + dateCorrect);
        return switchEnabled && dateCorrect;
    }
}
