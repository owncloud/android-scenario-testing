/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.preconditions;

import e2e.support.log.Log;
import e2e.world.World;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class SpacesPreconditions {

    private final World world;

    public SpacesPreconditions(World world) {
        this.world = world;
    }

    public void spacesExistInAccount(String userName, List<Map<String, String>> rows) throws IOException {
        for (Map<String, String> row : rows) {
            String name = row.get("name");
            String subtitle = getOptionalTrimmedValue(row, "subtitle");
            Log.log(Level.FINE, "Preparing space. User: " + userName + " - Name: " + name + " - Subtitle: " + subtitle);
            world.graphAPI().createSpace(name, subtitle, userName);
        }
    }

    public void usersAreMembersOfSpace(String spaceName, List<Map<String, String>> rows) throws IOException {
        for (Map<String, String> row : rows) {
            String userName = row.get("user");
            String permission = row.get("permission");
            String expirationDate = getOptionalTrimmedValue(row, "expirationDate");
            Log.log(Level.FINE, "Preparing space member. Space: " + spaceName
                    + " - User: " + userName + " - Permission: " + permission
                    + " - Expiration date: " + expirationDate);
            world.graphAPI().addMemberToSpace(spaceName, userName, permission, expirationDate);
        }
    }

    public void linkExistsOnSpace(String linkName, String spaceName, List<Map<String, String>> rows) throws IOException {
        for (Map<String, String> row : rows) {
            String permission = row.get("permission");
            String expirationDate = getOptionalTrimmedValue(row, "expirationDate");
            Log.log(Level.FINE, "Preparing space link. Space: " + spaceName + " - Link: " + linkName
                    + " - Permission: " + permission + " - Expiration date: " + expirationDate);
            world.graphAPI().addLinkToSpace(spaceName, linkName, permission, expirationDate);
        }
    }

    private String getOptionalTrimmedValue(Map<String, String> row, String key) {
        String value = row.get(key);
        return value == null ? "" : value.trim();
    }
}
