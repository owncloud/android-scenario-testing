/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.steps;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import e2e.support.log.StepLogger;
import e2e.world.World;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LinksNGSteps {

    private final World world;

    public LinksNGSteps(World world) {
        this.world = world;
    }

    @Given("Alice has a public link on {itemtype} {word} via NG with the following fields")
    public void alice_has_public_link_ng(String itemType, String itemName, DataTable table) throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGPreconditions().publicLinkExists("Alice", itemType, itemName,
                table.asMap(String.class, String.class));
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

    @When("Alice edits the public link {string} on {itemtype} {word} via NG with the following fields")
    public void alice_edits_public_link_ng(String linkName, String type, String itemName, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGTasks().editPublicLink(linkName, table.asMap(String.class, String.class));
    }

    @When("Alice taps to edit the public link {string}")
    public void alice_taps_to_edit_public_link(String linkName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGTasks().tapEditPublicLink(linkName);
    }

    @When("Alice removes the password on the public link edit")
    public void alice_removes_password_on_public_link_edit() {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGTasks().removePasswordOnEdit();
    }

    @When("Alice removes the public link {string} on {itemtype} {word}")
    public void alice_removes_public_link_ng(String linkName, String type, String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGTasks().removePublicLink(linkName);
    }

    @When("Alice taps the remove button on the public link {string}")
    public void alice_taps_remove_button_on_public_link(String linkName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGTasks().tapRemovePublicLink(linkName);
    }

    @When("Alice dismisses the public link removal dialog")
    public void alice_dismisses_public_link_removal_dialog() {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGTasks().dismissPublicLinkRemovalDialog();
    }

    @Then("public link should be created on {word} via NG with the following fields")
    public void public_link_should_be_created_ng(String itemName, DataTable table)
            throws IOException, ParserConfigurationException, SAXException {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGAssertions().assertPublicLinkCreated(itemName, table.asMap(String.class, String.class));
    }

    @Then("public link should be edited on {word} via NG with the following fields")
    public void public_link_should_be_edited_ng(String itemName, DataTable table)
            throws IOException, ParserConfigurationException, SAXException {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGAssertions().assertPublicLinkEdited(itemName, table.asMap(String.class, String.class));
    }

    @Then("the public link save button is disabled")
    public void public_link_save_button_is_disabled() {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGAssertions().assertSaveButtonDisabled();
    }

    @Then("the public link confirm button is disabled")
    public void public_link_confirm_button_is_disabled() {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGAssertions().assertConfirmButtonDisabled();
    }

    @Then("the public link {string} should be listed in the PUBLIC LINKS section")
    public void public_link_should_be_listed(String linkName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGAssertions().assertPublicLinkListed(linkName);
    }

    @Then("the public link {string} should not be listed in the PUBLIC LINKS section")
    public void public_link_should_not_be_listed(String linkName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.linksNGAssertions().assertPublicLinkNotListed(linkName);
    }
}
