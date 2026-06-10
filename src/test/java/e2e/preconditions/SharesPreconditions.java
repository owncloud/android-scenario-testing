/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.preconditions;

import org.xml.sax.SAXException;

import e2e.support.log.Log;
import e2e.world.World;

import java.io.IOException;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

public class SharesPreconditions {

    private static final String USER_SHARE_TYPE = "0";
    private static final String EMPTY_SHARE_NAME = "";
    private static final String EMPTY_PASSWORD = "";
    private static final String USER_RECIPIENT_TYPE = "user";

    private final World world;

    public SharesPreconditions(World world) {
        this.world = world;
    }

    public void privateShareExists(String sharingUser, int shareLevel, String itemName,
                   String recipientUser, String permissions) throws IOException, ParserConfigurationException, SAXException {
        Log.log(Level.FINE, "Preparing private share. Sharing user: " + sharingUser
                + " - Item: " + itemName + " - Recipient: " + recipientUser + " - Permissions: " + permissions
                + " - Share level: " + shareLevel);
        world.shareAPI().createShare(sharingUser, itemName, recipientUser, USER_SHARE_TYPE, permissions,
                EMPTY_SHARE_NAME, EMPTY_PASSWORD, shareLevel);
        world.shareAPI().acceptAllShares(USER_RECIPIENT_TYPE, recipientUser);
    }
}
