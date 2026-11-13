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
        assertThat(world.sharePage().isListLinksEmpty())
                .as("links list should be empty")
                .isTrue();
        assertThat(world.shareAPI().getLinksByDefault().isEmpty())
                .as("server links should be empty")
                .isTrue();
        assertThat(world.sharePage().isItemInListLinks(itemName + " link"))
                .as("link for '%s' should not be listed", itemName)
                .isFalse();
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
        assertThat(world.sharePage().isItemInListLinks(linkName))
                .as("link '%s' should be visible in links list", linkName)
                .isTrue();
    }

    private void assertPublicLinkPasswordIsEnabled(String itemName) {
        Log.log(Level.FINE, "Checking public link password is enabled");
        world.sharePage().editLink(itemName);
        try {
            assertThat(world.linksPage().isPasswordEnabled())
                    .as("password should be enabled on the link")
                    .isTrue();
        } finally {
            world.linksPage().close();
        }
    }

    private void assertPublicLinkUserIsVisible(String itemName) {
        Log.log(Level.FINE, "Checking public link user/item: " + itemName);
        assertThat(world.sharePage().isItemInListLinks(itemName))
                .as("'%s' should be visible in links list", itemName)
                .isTrue();
    }

    private boolean areLinksPermissionsCorrect(String permissions) {
        Log.log(Level.FINE, "Starts: Check permissions: " + permissions);
        switch (permissions) {
            case ("1") -> {
                return world.linksPage().isDownloadViewSelected();
            }
            case ("15")-> {
                return world.linksPage().isDownloadViewUploadSelected();
            }
            case ("4") -> {
                return world.linksPage().isUploadOnlySelected();
            }
        }
        return false;
    }

    private void assertPublicLinkExpirationIsCorrect(String itemName, String expectedExpirationDays) {
        Log.log(Level.FINE, "Checking public link expiration days: " + expectedExpirationDays);
        world.sharePage().editLink(itemName);
        try {
            assertThat(assertExpirationCorrect(expectedExpirationDays))
                    .as("expiration date should be correct for %s days", expectedExpirationDays)
                    .isTrue();
        } finally {
            world.linksPage().close();
        }
    }

    private void assertPublicLinkPermissionsAreCorrect(String itemName, String expectedPermissions) {
        Log.log(Level.FINE, "Checking public link permissions: " + expectedPermissions);
        world.sharePage().editLink(itemName);
        try {
            assertThat(areLinksPermissionsCorrect(expectedPermissions))
                    .as("link permissions should match '%s'", expectedPermissions)
                    .isTrue();
        } finally {
            world.linksPage().close();
        }
    }

    private boolean assertExpirationCorrect(String days) {
        Log.log(Level.FINE, "Starts: Check expiration in days: " + days);
        boolean switchEnabled;
        boolean dateCorrect = false;
        int expiration = Integer.parseInt(days);
        String shortDate = DateUtils.formatDate(Integer.toString(expiration), DateUtils.DateFormatType.TEXT);
        Log.log(Level.FINE, "Date to check: " + shortDate + " Expiration: " + expiration);
        switchEnabled = world.linksPage().isExpirationSwitchEnabled();
        Log.log(Level.FINE, "SwitchEnabled -> " + switchEnabled);
        if (switchEnabled) {
            dateCorrect = world.linksPage().isExpirationDateCorrect(shortDate);
        }
        Log.log(Level.FINE, "Date Correct -> " + dateCorrect);
        return switchEnabled && dateCorrect;
    }
}
