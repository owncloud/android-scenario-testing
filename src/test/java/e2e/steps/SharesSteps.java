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
        world.sharesPreconditions().privateShareExists(sharingUser, shareLevel, itemName, recipientUser, permissions);
    }

    @When("Alice selects {usertype} {word} as sharee")
    public void user_selects_sharee(String type, String sharee)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        world.sharesTasks().selectSharee(type, sharee);
    }

    @When("Alice edits the share on {itemtype} {word} with permissions {word}")
    public void user_edits_share_with_permissions(String type, String itemName, String permissions) {
        StepLogger.logCurrentStep(Level.FINE);
        world.sharesTasks().editPrivateSharePermissions(type, itemName, permissions);
    }

    @When("Alice deletes the share")
    public void user_deletes_share() {
        StepLogger.logCurrentStep(Level.FINE);
        world.sharesTasks().deletePrivateShare();
    }

    @When("Alice closes share view")
    public void user_closes_shares_view() {
        StepLogger.logCurrentStep(Level.FINE);
        world.sharesTasks().closeShareView();
    }

    @Then("share should be created/edited on {word} with the following fields")
    public void share_should_be_created_with_fields(String itemName, DataTable table)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        world.sharesAssertions().assertPrivateShareCreatedOrEdited(itemName, table.asMap(String.class, String.class));
    }

    @Then("{word} should not have access to {word}")
    public void sharee_should_not_have_access_to_item(String userName, String itemName)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        world.sharesAssertions().assertUserDoesNotHaveAccessToItem(userName, itemName);
    }

    @Then("{usertype} {word} should have access to {word}")
    public void sharee_should_not_have_access_the_item(String type, String shareeName, String itemName)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        world.sharesAssertions().assertShareeHasAccessToItem(type, shareeName, itemName);
    }

    @Then("{word} should not be shared anymore with {word}")
    public void item_should_not_be_shared_with(String itemName, String sharee) {
        StepLogger.logCurrentStep(Level.FINE);
        world.sharesAssertions().assertItemIsNotSharedAnymoreWith(itemName, sharee);
    }
}
