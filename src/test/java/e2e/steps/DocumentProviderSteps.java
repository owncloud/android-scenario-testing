/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.steps;

import java.io.IOException;
import java.util.logging.Level;

import e2e.support.log.StepLogger;
import e2e.world.World;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class DocumentProviderSteps {

    private World world;

    public DocumentProviderSteps(World world) {
        this.world = world;
    }

    @Given("a file {word} exists in the device")
    public void a_file_exists_in_device(String fileName) throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        world.devicePreconditions().pushFileDevice(fileName, "/");
    }

    @When("Alice selects {word} to upload")
    public void user_selects_file_to_upload(String fileName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.documentProviderTasks().selectFileToUpload(fileName);
    }
}
