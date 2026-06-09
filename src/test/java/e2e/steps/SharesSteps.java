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

public class SharesSteps {

    private World world;

    public SharesSteps(World world) {
        this.world = world;
    }

    @ParameterType("user|group")
    public String usertype(String type){
        return type;
    }

    @ParameterType("shared|reshared")
    public int sharelevel(String type){
        if (type.equals("shared")) {
            return 0; //share, first level
        } else {
            return 1; //reshare
        }
    }

    @Given("{word} has {sharelevel} {itemtype} {word} with {word} with permissions {word}")
    public void user_has_shared_item_with_permissions(String sharingUser, int shareLevel, String type, String itemName,
                                    String recipientUser, String permissions) throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        world.shareAPI().createShare(sharingUser, itemName, recipientUser, "0", permissions, "", "", shareLevel);
        world.shareAPI().acceptAllShares("user", recipientUser);
    }

    @When("Alice selects {usertype} {word} as sharee")
    public void user_selects_sharee(String type, String sharee)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        world.sharePage().addPrivateShare();
        world.searchShareePage().shareWithUser(sharee);
        world.shareAPI().acceptAllShares(type,sharee);
    }

    @When("Alice edits the share on {itemtype} {word} with permissions {word}")
    public void user_edits_share_with_permissions(String type, String itemName, String permissions) {
        StepLogger.logCurrentStep(Level.FINE);
        world.sharesTasks().editPrivateSharePermissions(type, itemName, permissions);
    }

    @When("Alice deletes the share")
    public void user_deletes_share() {
        StepLogger.logCurrentStep(Level.FINE);
        world.sharePage().deletePrivateShare();
        world.sharePage().acceptDeletion();
    }

    @When("Alice closes share view")
    public void user_closes_shares_view() {
        StepLogger.logCurrentStep(Level.FINE);
        world.sharePage().close();
    }

    @Then("share should be created/edited on {word} with the following fields")
    public void share_should_be_created_with_fields(String itemName, DataTable table)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        //Asserts in UI
        Map<String, String> fields = table.asMap(String.class, String.class);
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "sharee" -> assertTrue(world.sharePage().isItemInListPrivateShares(value));
                case "group" -> assertTrue(world.sharePage().isItemInListPrivateShares(value + " (group)"));
                case "permissions" -> {
                    world.sharePage().openPrivateShare(itemName);
                    switch (value) {
                        case "1" -> {
                            Log.log(Level.FINE, "Only read");
                            assertTrue(!world.privateSharePage().isEditEnabled());
                        }
                        case "3" -> {
                            Log.log(Level.FINE, "Edit");
                            Log.log(Level.FINE, Boolean.toString(world.privateSharePage().isEditEnabled()));
                            assertTrue(world.privateSharePage().isEditEnabled());
                        }
                        case "9" -> {
                            Log.log(Level.FINE, "Delete");
                            assertTrue(!world.privateSharePage().isCreateSelected() &&
                                    !world.privateSharePage().isChangeSelected() &&
                                    world.privateSharePage().isDeleteSelected());
                        }
                        case "13" -> {
                            Log.log(Level.FINE, "Delete and Create");
                            assertTrue(world.privateSharePage().isCreateSelected() &&
                                    !world.privateSharePage().isChangeSelected() &&
                                    world.privateSharePage().isDeleteSelected());
                        }
                    }
                }
            }
        }
        //Asserts in server via API
        OCShare share = world.shareAPI().getShare(itemName);
        assertTrue(world.sharePage().isShareCorrect(share, fields));
    }

    @Then("{word} should not have access to {word}")
    public void sharee_should_not_have_access_to_item(String userName, String itemName)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        assertFalse(world.shareAPI().isSharedWithMe(itemName, userName, false));
    }

    @Then("{usertype} {word} should have access to {word}")
    public void sharee_should_not_have_access_the_item(String type, String shareeName, String itemName)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        if (type.equalsIgnoreCase("user")) {
            assertTrue(world.shareAPI().isSharedWithMe(itemName, shareeName, false));
        } else if (type.equalsIgnoreCase("group")) {
            assertTrue(world.shareAPI().isSharedWithMe(itemName, shareeName, true));
        }
    }

    @Then("{word} should not be shared anymore with {word}")
    public void item_should_not_be_shared_with(String itemName, String sharee)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        assertFalse(world.sharePage().isItemInListPrivateShares(sharee));
    }

    @Then("Alice should see {word} as recipient")
    public void user_should_see_recipient(String sharee) {
        StepLogger.logCurrentStep(Level.FINE);
        assertTrue(world.privateSharePage().isSharee(sharee));
    }
}
