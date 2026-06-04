package io.cucumber;

import java.util.logging.Level;

import io.cucumber.java.en.Then;
import utils.log.StepLogger;

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
