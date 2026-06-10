/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.preconditions;

import e2e.support.log.Log;
import e2e.world.World;

import java.io.IOException;
import java.util.logging.Level;

public class LinksPreconditions {

    private static final String PUBLIC_LINK_SHARE_TYPE = "3";
    private static final String DEFAULT_PERMISSION = "1";
    private static final String DEFAULT_SHARE_WITH = "";
    private static final String DEFAULT_LINK_PASSWORD_PREFIX = "aa55AA..";
    private static final int DEFAULT_EXPIRATION_DAYS = 0;

    private final World world;

    public LinksPreconditions(World world) {
        this.world = world;
    }

    public void publicLinkExists(String sharingUser, String itemName) throws IOException {
        Log.log(Level.FINE, "Preparing public link for item: " + itemName
                + " - Sharing user: " + sharingUser);
        world.shareAPI().createShare(sharingUser, itemName, DEFAULT_SHARE_WITH, PUBLIC_LINK_SHARE_TYPE,
                DEFAULT_PERMISSION, itemName, DEFAULT_LINK_PASSWORD_PREFIX + " link",
                DEFAULT_EXPIRATION_DAYS);
    }
}
