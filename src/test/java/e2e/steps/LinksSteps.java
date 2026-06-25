/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.steps;

import java.util.Map;
import java.util.logging.Level;

import e2e.support.log.StepLogger;
import e2e.world.World;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LinksSteps {

    private World world;

    public LinksSteps(World world) {
        this.world = world;
    }

    @ParameterType("item|file|folder")
    public String itemtype(String type) {
        return type;
    }

    @Given("{word} has shared the {itemtype} {word} by link")
    public void user_has_shared_the_item_by_link(String sharingUser, String type, String itemName)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksPreconditions().publicLinkExists(sharingUser, itemName);
    }

    @When("Alice creates link on {itemtype} {word} with the following fields")
    public void user_creates_link_with_fields(String type, String itemName, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        Map<String, String> fields = table.asMap(String.class, String.class);
        world.linksTasks().createPublicLink(itemName, fields);
    }

    @When("Alice edits the link on {word} with the following fields")
    public void user_edits_public_link_with_fields(String itemName, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        Map<String, String> fields = table.asMap(String.class, String.class);
        world.linksTasks().editPublicLink(itemName, fields);
    }

    @When("Alice deletes the link on {word}")
    public void user_deletes_link(String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksTasks().deletePublicLink(itemName);
    }

    @Then("link should be created/edited on {word} with the following fields")
    public void link_should_be_created_with_fields(String itemName, DataTable table)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        Map<String, String> fields = table.asMap(String.class, String.class);
        world.linksAssertions().assertPublicLinkCreatedOrEdited(itemName, fields);
    }

    @Then("link on {word} should not exist anymore")
    public void link_should_not_exist_anymore(String itemName)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksAssertions().assertPublicLinkDoesNotExistAnymore(itemName);
    }
}
