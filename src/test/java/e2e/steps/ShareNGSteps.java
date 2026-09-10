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

public class ShareNGSteps {

    private final World world;

    public ShareNGSteps(World world) {
        this.world = world;
    }

    @When("Alice adds {usertype} {word} via Sharing NG with")
    public void add_sharee(String shareeType, String sharee, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        world.shareNGTasks().addSharee(shareeType, sharee, table.asMap(String.class, String.class));
    }

    @Then("{word} should be visible in Sharing NG with")
    public void sharee_should_be_visible(String sharee, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        world.shareNGAssertions().assertShareeIsVisible(sharee, table.asMap(String.class, String.class));
    }
}
