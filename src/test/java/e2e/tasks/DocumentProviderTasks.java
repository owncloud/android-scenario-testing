/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.tasks;

import java.util.logging.Level;

import e2e.support.log.Log;
import e2e.world.World;

public class DocumentProviderTasks {

    private final World world;
    public DocumentProviderTasks(World world) {
        this.world = world;
    }

    public void selectFileToUpload(String fileName) {
        Log.log(Level.FINE, "Selecting file to upload: " + fileName);
        world.documentProviderPage().selectFileToUpload(fileName);
    }
}
