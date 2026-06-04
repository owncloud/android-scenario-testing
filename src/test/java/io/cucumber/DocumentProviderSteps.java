package io.cucumber;

import java.io.IOException;
import java.util.logging.Level;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import utils.log.StepLogger;

public class DocumentProviderSteps {

    private World world;

    public DocumentProviderSteps(World world) {
        this.world = world;
    }

    @Given("a file {word} exists in the device")
    public void a_file_exists_in_device(String fileName) throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        world.devicePage().pushFile(fileName, "/");
    }

    @When("Alice selects {word} to upload")
    public void user_selects_file_to_upload(String fileName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.documentProviderPage().selectFileToUpload(fileName);
    }
}
