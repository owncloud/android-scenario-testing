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

public class ShareNGTasks {

    private final World world;

    public ShareNGTasks(World world) {
        this.world = world;
    }

    public void addSharee(String sharee, Map<String, String> fields) {
        Log.log(Level.FINE, "Add sharee: " + sharee);
        world.shareNGPage().addShare();
        world.shareNGCreatePage().selectSharee(sharee);
        applyFields(fields);
        world.shareNGCreatePage().invite();
    }

    private void applyFields(Map<String, String> fields) {
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            applyField(entry.getKey(), entry.getValue());
        }
    }

    private void applyField(String key, String value) {
        switch (key) {
            case "permission" -> world.shareNGCreatePage().setPermission(value);
        }
    }
}
