/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.steps;

import java.util.logging.Level;

import e2e.support.log.StepLogger;
import e2e.world.World;
import io.cucumber.java.en.Then;

public class UploadSteps {

    private World world;

    public UploadSteps(World world) {
        this.world = world;
    }

    @Then("Alice should see {word} as {word} in the uploads view")
    public void file_in_uploads(String fileName, String status) {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage().openUploadsView();
        if (status.equals("uploaded")) {
            world.uploadsPage().isFileUploaded(fileName);
        }
        world.uploadsPage().clearList();
    }
}
