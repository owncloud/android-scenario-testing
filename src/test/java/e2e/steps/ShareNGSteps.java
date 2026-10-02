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
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ShareNGSteps {

    private final World world;

    public ShareNGSteps(World world) {
        this.world = world;
    }

    @Given("{word} has shared {itemtype} {word} with {word} with")
    public void user_has_shared_item_via_shareng(String actor, String itemType, String itemName,
            String sharee, DataTable table) throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        var fields = table.asMap(String.class, String.class);
        world.sharesPreconditions().shareNGShareExists(actor, itemName, sharee,
                fields.get("permission"), fields.get("expirationDate"),
                fields.getOrDefault("shareeType", "user"));
    }

    @When("Alice adds {usertype} {word} via Sharing NG with")
    public void add_sharee(String shareeType, String sharee, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        world.shareNGTasks().addSharee(shareeType, sharee, table.asMap(String.class, String.class));
    }

    @When("{word} edits the share on {itemtype} {word} for user {word} with")
    public void edit_share(String actor, String itemType, String itemName, String sharee, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        world.shareNGTasks().editShare(sharee, table.asMap(String.class, String.class));
    }

    @When("{word} removes the share on {itemtype} {word} for user {word}")
    public void remove_share(String actor, String itemType, String itemName, String sharee) {
        StepLogger.logCurrentStep(Level.FINE);
        world.shareNGTasks().removeShare(sharee);
    }

    @Then("{usertype} {word} should have access via NG to {word}")
    public void user_should_have_access(String userType, String userName, String itemName) throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        world.shareNGAssertions().assertUserHasAccess(userType, userName, itemName);
    }

    @Then("{usertype} {word} should not have access via NG to {word}")
    public void user_should_not_have_access(String userType, String userName, String itemName) throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        world.shareNGAssertions().assertUserHasNoAccess(userType, userName, itemName);
    }

    @Then("{word} should be visible in Sharing NG with")
    public void sharee_should_be_visible(String sharee, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        world.shareNGAssertions().assertShareeIsVisible(sharee, table.asMap(String.class, String.class));
    }

    @Then("{word} should not be visible in Sharing NG")
    public void sharee_should_not_be_visible(String sharee) {
        StepLogger.logCurrentStep(Level.FINE);
        world.shareNGAssertions().assertShareeIsNotVisible(sharee);
    }
}
