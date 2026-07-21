/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.assertions;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;

import e2e.support.log.Log;
import e2e.world.World;

public class SidebarAssertions {

    private final World world;

    public SidebarAssertions(World world) {
        this.world = world;
    }

    public void assertHelpWebIsDisplayed() {
        Log.log(Level.FINE, "Starts: Assert Help web is displayed");
        assertTrue(world.sidebarPage().isHelpWebDisplayed());
    }

    public void assertPrivacyPolicyWebIsDisplayed() {
        Log.log(Level.FINE, "Starts: Assert Help web is displayed");
        assertTrue(world.sidebarPage().isPrivacyPolicyWebDisplayed());
    }
}
