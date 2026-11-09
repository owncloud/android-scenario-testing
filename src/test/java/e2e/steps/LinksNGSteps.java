/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.steps;

import java.util.logging.Level;

import e2e.support.log.StepLogger;
import e2e.world.World;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LinksNGSteps {

    private final World world;

    public LinksNGSteps(World world) {
        this.world = world;
    }

    @When("Alice creates public link on {itemtype} {word} via NG with the following fields")
    public void alice_creates_public_link_ng(String type, String itemName, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGTasks().createPublicLink(itemName, table.asMap(String.class, String.class));
    }

    @When("Alice taps to add a public link")
    public void alice_taps_to_add_public_link() {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGTasks().tapAddPublicLink();
    }

    @When("Alice selects {string} permission for the public link")
    public void alice_selects_permission_for_public_link(String permission) {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGTasks().selectPermission(permission);
    }

    @Then("public link should be created on {word} via NG with the following fields")
    public void public_link_should_be_created_ng(String itemName, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGAssertions().assertPublicLinkCreated(itemName, table.asMap(String.class, String.class));
    }

    @Then("the public link confirm button is disabled")
    public void public_link_confirm_button_is_disabled() {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGAssertions().assertConfirmButtonDisabled();
    }
}
