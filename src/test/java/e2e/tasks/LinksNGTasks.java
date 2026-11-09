/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.tasks;

import java.util.Map;
import java.util.logging.Level;

import e2e.support.log.Log;
import e2e.world.World;

public class LinksNGTasks {

    private final World world;

    public LinksNGTasks(World world) {
        this.world = world;
    }

    public void createPublicLink(String itemName, Map<String, String> fields) {
        Log.log(Level.FINE, "Create public link via NG on item: " + itemName);
        world.publicLinkNGPage().tapAddPublicLink();
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            applyField(entry.getKey(), entry.getValue());
        }
        world.publicLinkNGPage().confirmCreation();
    }

    public void tapAddPublicLink() {
        world.publicLinkNGPage().tapAddPublicLink();
    }

    public void selectPermission(String permission) {
        world.publicLinkNGPage().selectPermission(permission);
    }

    private void applyField(String key, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        switch (key) {
            case "permission" -> world.publicLinkNGPage().selectPermission(value);
            case "name" -> world.publicLinkNGPage().enterName(value);
            case "password" -> world.publicLinkNGPage().enterPassword(value);
            case "expiration days" -> world.publicLinkNGPage().setExpirationDays(Integer.parseInt(value));
            default -> Log.log(Level.FINE, "Ignoring unsupported public link NG field: " + key);
        }
    }
}
