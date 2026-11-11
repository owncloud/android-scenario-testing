/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.preconditions;

import e2e.support.log.Log;
import e2e.world.World;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

public class LinksNGPreconditions {

    private static final String DEFAULT_PERMISSION = "Can view";
    private static final String DEFAULT_PASSWORD = "aa55AA..";

    private final World world;

    public LinksNGPreconditions(World world) {
        this.world = world;
    }

    public void publicLinkExists(String sharingUser, String itemType, String itemName,
                                  Map<String, String> fields)
            throws IOException, SAXException, ParserConfigurationException {
        String linkName = fields.getOrDefault("name", "");
        String permission = fields.getOrDefault("permission", DEFAULT_PERMISSION);
        String password = fields.getOrDefault("password", DEFAULT_PASSWORD);
        String expirationDays = fields.getOrDefault("expiration days", "");
        Log.log(Level.FINE, "Preparing NG public link for item: " + itemName
                + " - Sharing user: " + sharingUser + " - Name: " + linkName
                + " - Permission: " + permission);
        world.graphAPI().createPublicLinkForItem(sharingUser, itemName, linkName,
                permission, password, expirationDays);
    }
}
