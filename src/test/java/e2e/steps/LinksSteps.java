/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.steps;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.logging.Level;

import e2e.model.OCShare;
import e2e.support.log.Log;
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
        world.shareAPI().createShare(sharingUser, itemName, "", "3", "1", itemName, "aa55AA.." + " link", 0);
    }

    @When("Alice creates link on {word} {word} with the following fields")
    public void user_creates_link_with_fields(String type, String itemName, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        Map<String, String> fields = table.asMap(String.class, String.class);
        world.linkTasks().createPublicLink(itemName, fields);
    }

    @When("Alice edits the link on {word} with the following fields")
    public void user_edits_public_link_with_fields(String itemName, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        Map<String, String> fields = table.asMap(String.class, String.class);
        world.linkTasks().editPublicLink(itemName, fields);
    }

    @When("Alice deletes the link on {word}")
    public void user_deletes_link(String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.linkTasks().deletePublicLink();
    }

    @Then("link should be created/edited on {word} with the following fields")
    public void link_should_be_created_with_fields(String itemName, DataTable table)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        //Asserts in UI
        Log.log(Level.FINE, "Checking UI asserts");
        Map<String, String> fields = table.asMap(String.class, String.class);
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "name" -> {
                    Log.log(Level.FINE, "Checking name: " + value);
                    assertTrue(world.sharePage().isItemInListPublicShares(value));
                }
                case "password-auto", "password" -> {
                    world.sharePage().openPublicLink(itemName);
                    assertTrue(world.publicLinksPage().isPasswordEnabled());
                    world.publicLinksPage().close();
                }
                case "user" -> {
                    Log.log(Level.FINE, "checking user: " + itemName);
                    assertTrue(world.sharePage().isItemInListPublicShares(itemName));
                }
                case "permission" -> {
                    Log.log(Level.FINE, "checking permissions: " + value);
                    world.sharePage().openPublicLink(itemName);
                    assertTrue(world.publicLinksPage().arePermissionsCorrect(value));
                    world.publicLinksPage().close();
                }
                case "expiration days" -> {
                    Log.log(Level.FINE, "checking expiration day: " + value);
                    world.sharePage().openPublicLink(itemName);
                    assertTrue(world.publicLinksPage().isExpirationCorrect(value));
                    world.publicLinksPage().close();
                }
            }
        }
        //Asserts in server via API
        Log.log(Level.FINE, "Checking API/server asserts");
        OCShare share = world.shareAPI().getShare(itemName);
        assertTrue(world.sharePage().isShareCorrect(share, fields));
    }

    @Then("link on {word} should not exist anymore")
    public void link_should_not_exist_anymore(String itemName)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        Log.log(Level.FINE, "Checking if item exists: " + itemName);
        assertTrue(world.sharePage().isListPublicLinksEmpty());
        assertTrue(world.shareAPI().getLinksByDefault().isEmpty());
        assertFalse(world.sharePage().isItemInListPublicShares(itemName + " link"));
    }
}
