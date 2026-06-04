/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package io.cucumber;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.logging.Level;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.entities.OCShare;
import utils.log.Log;
import utils.log.StepLogger;

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
        world.sharePage().addPublicLink();
        Map<String, String> fields = table.asMap(String.class, String.class);
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "name": {
                    world.publicLinksPage().addLinkName(value);
                    break;
                }
                case "password": {
                    world.publicLinksPage().typePassword(itemName, value);
                    break;
                }
                case "password-auto": {
                    world.publicLinksPage().generatePassword();
                    break;
                }
                case "permission": {
                    world.publicLinksPage().setPermission(value);
                    break;
                }
                case "expiration days": {
                    world.publicLinksPage().setExpiration(value);
                    break;
                }
                default:
                    break;
            }
        }
        world.publicLinksPage().submitLink();
    }

    @When("Alice edits the link on {word} with the following fields")
    public void user_edits_public_link_with_fields(String itemName, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        Map<String, String> fields = table.asMap(String.class, String.class);
        world.sharePage().openPublicLink(itemName);
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "name" -> world.publicLinksPage().addLinkName(value);
                case "permissions" -> {
                    switch (value) {
                        case "1" -> {
                            Log.log(Level.FINE, "Select Download / View");
                            world.publicLinksPage().selectDownloadView();
                        }
                        case "15" -> {
                            Log.log(Level.FINE, "Select Download / View / Upload");
                            world.publicLinksPage().selectDownloadViewUpload();
                        }
                        case "4" -> {
                            Log.log(Level.FINE, "Select Upload Only (File Drop)");
                            world.publicLinksPage().selectUploadOnly();
                        }
                    }
                }
                case "password" -> world.publicLinksPage().typePassword(itemName, value);
                case "expiration days" -> world.publicLinksPage().setExpiration(value);
            }

        }
        world.publicLinksPage().submitLink();
    }

    @When("Alice deletes the link on {word}")
    public void user_deletes_link(String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.sharePage().deletePublicShare();
        world.sharePage().acceptDeletion();
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
