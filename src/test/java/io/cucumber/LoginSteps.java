/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package io.cucumber;

import java.util.logging.Level;

import io.cucumber.java.en.Given;
import utils.LocProperties;
import utils.log.StepLogger;

public class LoginSteps {

    private World world;

    public LoginSteps(World world) {
        this.world = world;
    }

    @Given("user {word} is logged")
    public void user_is_logged(String user) {
        StepLogger.logCurrentStep(Level.FINE);
        // Server and app MUST work with basic auth mode.
        if(!world.fileListPage().isFileListVisible()) {
            String server = System.getProperty("server");
            String password = LocProperties.getProperties().getProperty("passw1");
            world.loginPage().typeURL(server);
            world.loginPage().typeCredentials(user, password);
            world.loginPage().submitLogin();
        }
    }
}
