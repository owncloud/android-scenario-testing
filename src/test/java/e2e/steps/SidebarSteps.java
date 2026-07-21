/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.steps;

import java.util.logging.Level;

import e2e.support.log.StepLogger;
import e2e.world.World;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SidebarSteps {

    private World world;

    public SidebarSteps(World world) {
        this.world = world;
    }

    @ParameterType("Help|Privacy Policy")
    public String webType(String type) {
        return type;
    }

    @When("Alice opens the {webType} web")
    public void user_opens_help_web(String webType) {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListTasks().openHamburgerMenu();
        switch (webType) {
            case "Help":
                world.sidebarTasks().openHelpWeb();
                break;
            case "Privacy Policy":
                world.sidebarTasks().openPrivacyPolicyWeb();
                break;
            default:
                throw new IllegalArgumentException("Invalid web type: " + webType);
        }
    }

    @Then("Alice should see the {webType} web")
    public void user_should_see_help_web(String webType) {
        StepLogger.logCurrentStep(Level.FINE);
        switch (webType) {
            case "Help":
                world.sidebarAssertions().assertHelpWebIsDisplayed();
                break;
            case "Privacy Policy":
                world.sidebarAssertions().assertPrivacyPolicyWebIsDisplayed();
                break;
            default:
                throw new IllegalArgumentException("Invalid web type: " + webType);
        }
    }
}
