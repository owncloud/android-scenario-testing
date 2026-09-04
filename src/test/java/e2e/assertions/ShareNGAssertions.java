/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.assertions;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.logging.Level;

import e2e.support.log.Log;
import e2e.world.World;

public class ShareNGAssertions {

    private final World world;

    public ShareNGAssertions(World world) {
        this.world = world;
    }

    public void assertShareeIsVisible(String sharee, Map<String, String> fields) {
        Log.log(Level.FINE, "Assert sharee is visible: " + sharee);
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            assertField(sharee, entry.getKey(), entry.getValue());
        }
    }

    private void assertField(String sharee, String key, String value) {
        switch (key) {
            case "permission" -> assertPermission(sharee, value);
        }
    }

    private void assertPermission(String sharee, String expectedPermission) {
        Log.log(Level.FINE, "Assert " + sharee + " has permission: " + expectedPermission);
        assertTrue(world.shareNGPage().isShareeDisplayedWithPermission(sharee, expectedPermission));
    }
}
