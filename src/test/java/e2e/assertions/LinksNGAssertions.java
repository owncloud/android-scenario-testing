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

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import e2e.model.OCSpaceLink;
import e2e.support.date.DateUtils;
import e2e.support.log.Log;
import e2e.world.World;

public class LinksNGAssertions {

    private final World world;

    public LinksNGAssertions(World world) {
        this.world = world;
    }

    public void assertPublicLinkCreated(String itemName, Map<String, String> fields)
            throws IOException, SAXException, ParserConfigurationException {
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
        assertPublicLinkMatchesBackend(itemName, linkName, permission, expirationDays);
    }

    public void assertPublicLinkEdited(String itemName, Map<String, String> fields)
            throws IOException, SAXException, ParserConfigurationException {
        Log.log(Level.FINE, "Assert public link edited via NG on item: " + itemName);
        String linkName = fields.get("name");
        assertThat(world.publicLinkNGPage().isPublicLinkListed(linkName))
                .as("public link '%s' should be listed after edit", linkName)
                .isTrue();
        String permission = fields.get("permission");
        if (permission != null && !permission.isEmpty()) {
            assertThat(world.publicLinkNGPage().isPublicLinkListedWithPermission(linkName, permission))
                    .as("link '%s' should display permission '%s'", linkName, permission)
                    .isTrue();
        }
        String expirationDays = fields.get("expiration days");
        if ("(none)".equals(expirationDays)) {
            assertThat(world.publicLinkNGPage().getPublicLinkExpirationDays(linkName))
                    .as("link '%s' should have no expiration date", linkName)
                    .isNull();
        } else if (expirationDays != null && !expirationDays.isEmpty()) {
            String expectedDate = DateUtils.formatDate(expirationDays, DateUtils.DateFormatType.NUMERIC);
            assertThat(world.publicLinkNGPage().getPublicLinkExpirationDays(linkName))
                    .as("expiration date of link '%s'", linkName)
                    .contains(expectedDate);
        }
        assertPublicLinkMatchesBackend(itemName, linkName, permission, expirationDays);
    }

    private void assertPublicLinkMatchesBackend(String itemName, String linkName, String permission,
            String expirationDays) throws IOException, SAXException, ParserConfigurationException {
        OCSpaceLink backendLink = world.graphAPI().getLinkForItem(itemName, linkName);
        assertThat(backendLink)
                .as("public link '%s' should exist in backend for item '%s'", linkName, itemName)
                .isNotNull();
        Log.log(Level.FINE, "Backend link: name=" + backendLink.getLinkName()
                + " permission=" + backendLink.getPermission()
                + " expirationDate=" + backendLink.getExpirationDate());
        assertThat(backendLink.getLinkName())
                .as("backend link name")
                .isEqualTo(linkName);
        if (permission != null && !permission.isEmpty()) {
            assertThat(backendLink.getPermission())
                    .as("backend link permission for '%s'", linkName)
                    .isEqualTo(getPermissionApiName(permission));
        }
        if ("(none)".equals(expirationDays)) {
            assertThat(backendLink.getExpirationDate())
                    .as("backend expiration date for link '%s' should be absent", linkName)
                    .isNull();
        } else if (expirationDays != null && !expirationDays.isEmpty()) {
            String expectedDatePrefix = DateUtils.dateInDaysWithServerFormat(expirationDays).substring(0, 10);
            assertThat(backendLink.getExpirationDate())
                    .as("backend expiration date for link '%s'", linkName)
                    .startsWith(expectedDatePrefix);
        }
    }

    private String getPermissionApiName(String permission) {
        return switch (permission) {
            case "Can view" -> "view";
            case "Can edit" -> "edit";
            case "Secret file drop" -> "createOnly";
            default -> "";
        };
    }

    public void assertSaveButtonDisabled() {
        Log.log(Level.FINE, "Assert public link save button is disabled");
        assertThat(world.publicLinkNGPage().isSaveEnabled())
                .as("public link save button should be disabled")
                .isFalse();
    }

    public void assertConfirmButtonDisabled() {
        Log.log(Level.FINE, "Assert public link confirm button is disabled");
        assertThat(world.publicLinkNGPage().isConfirmEnabled())
                .as("public link confirm button should be disabled")
                .isFalse();
    }
}
