/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.tasks;

import java.util.logging.Level;

import e2e.support.log.Log;
import e2e.world.World;

public class SidebarTasks {

    private final World world;

    public SidebarTasks(World world) {
        this.world = world;
    }

    public void openHelpWeb() {
        Log.log(Level.FINE, "Starts: Open Help web");
        world.sidebarPage().openHelp();
    }

    public void openPrivacyPolicyWeb() {
        Log.log(Level.FINE, "Starts: Open Privacy Policy web");
        world.sidebarPage().openPrivacyPolicy();
    }
}
