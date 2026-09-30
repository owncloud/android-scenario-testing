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
    private static final String GROUP_SHARE_TYPE = "1";
    private static final String EMPTY_SHARE_NAME = "";
    private static final String EMPTY_PASSWORD = "";
    private static final String USER_RECIPIENT_TYPE = "user";

    private final World world;

    public SharesPreconditions(World world) {
        this.world = world;
    }

    public void shareNGShareExists(String sharingUser, String itemName, String recipientUser, String permission)
            throws IOException, ParserConfigurationException, SAXException {
        shareNGShareExists(sharingUser, itemName, recipientUser, permission, null, USER_RECIPIENT_TYPE);
    }

    public void shareNGShareExists(String sharingUser, String itemName, String recipientUser,
            String permission, String expirationDate, String shareeType)
            throws IOException, ParserConfigurationException, SAXException {
        Log.log(Level.FINE, "Preparing ShareNG share. Sharing user: " + sharingUser
                + " - Item: " + itemName + " - Recipient: " + recipientUser + " - Permission: " + permission);
        String shareType = "group".equalsIgnoreCase(shareeType) ? GROUP_SHARE_TYPE : USER_SHARE_TYPE;
        world.shareAPI().createShare(sharingUser, itemName, recipientUser, shareType,
                mapPermissionToOcs(permission), EMPTY_SHARE_NAME, EMPTY_PASSWORD, 0);
        world.shareAPI().acceptAllShares(shareeType, recipientUser);
    }

    private String mapPermissionToOcs(String permission) {
        return switch (permission) {
            case "Can edit" -> "3";
            default -> "1";
        };
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
