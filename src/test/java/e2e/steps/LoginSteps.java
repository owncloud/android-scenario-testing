/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.steps;

import java.util.logging.Level;

import e2e.support.log.StepLogger;
import e2e.world.World;
import io.cucumber.java.en.Given;

public class LoginSteps {

    private World world;

    public LoginSteps(World world) {
        this.world = world;
    }

    @Given("user {word} is logged")
    public void user_is_logged(String user) {
        StepLogger.logCurrentStep(Level.FINE);
        // Server and app MUST work with basic auth mode.
        world.loginPreconditions().userIsLogged(user);
    }
}
