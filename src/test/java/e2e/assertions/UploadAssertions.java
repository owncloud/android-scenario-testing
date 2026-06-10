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

public class UploadAssertions {

    private final World world;

    public UploadAssertions(World world) {
        this.world = world;
    }

    public void assertFileHasStatusInUploadsView(String fileName, String status) {
        Log.log(Level.FINE, "Checking upload status. File: " + fileName + " - Status: " + status);
        world.fileListPage().openUploadsView();
        try {
            assertUploadStatus(fileName, status);
        } finally {
            world.uploadsPage().clearList();
        }
    }

    private void assertUploadStatus(String fileName, String status) {
        switch (status) {
            case "uploaded" ->
                    assertTrue(world.uploadsPage().isFileUploaded(fileName));
        }
    }
}
