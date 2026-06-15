/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.steps;

import java.io.IOException;
import java.util.logging.Level;

import e2e.support.log.StepLogger;
import e2e.world.World;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SpacesSteps {

    private World world;

    public SpacesSteps(World world) {
        this.world = world;
    }

    @ParameterType("(?: (enabled|disabled))?")
    public String spaceStatus(String s) {
        return s == null ? "" : s;
    }

    @ParameterType("Can view|Can edit|Can manage")
    public String permissionType(String s) {
        return s == null ? "" : s;
    }

    @Given("the following spaces have been created in {word} account")
    public void spaces_have_been_created(String userName, DataTable table) throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesPreconditions().spacesExistInAccount(userName, table.asMaps(String.class, String.class));
    }

    @Given("the following users are members of the space {word}")
    public void users_members_of_space(String spaceName, DataTable table) throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesPreconditions().usersAreMembersOfSpace(spaceName, table.asMaps(String.class, String.class));
    }

    @Given("the link {word} was created on the space {word} with")
    public void links_created_on_space(String linkName, String spaceName, DataTable table) throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesPreconditions().linkExistsOnSpace(linkName, spaceName, table.asMaps(String.class, String.class));
    }

    @When("Alice selects the spaces view")
    public void user_selects_spaces_view() {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesTasks().openSpacesView();
    }

    @When("the following spaces are disabled in server")
    public void space_disabled_server(DataTable table) throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesTasks().disableSpacesInServer(table.asMaps(String.class, String.class));
    }

    @When("Alice filters the list using {word}")
    public void user_filters_list(String pattern) {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesTasks().filterSpacesList(pattern);
    }

    @When("Alice creates a new space with the following fields")
    public void creates_new_space(DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesTasks().createSpace(table.asMap(String.class, String.class));
    }

    @When("Alice edits the space {word}")
    public void user_edit_space(String spaceName){
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesTasks().openEditSpace(spaceName);
    }

    @When("Alice disables the space {word}")
    public void user_disables_space(String spaceName){
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesTasks().openDisableSpace(spaceName);
    }

    @When("Alice enables the space {word}")
    public void user_enables_space(String spaceName){
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesTasks().openEnableSpace(spaceName);
    }

    @When("Alice deletes the space {word}")
    public void user_deletes_space(String spaceName){
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesTasks().openDeleteSpace(spaceName);
    }

    @When("Alice updates the space with the following fields")
    public void updates_new_space(DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesTasks().updateSpace(table.asMap(String.class, String.class));
    }

    @When("Alice edits the image of the space {word} with the file {word}")
    public void edits_image(String spaceName, String fileName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesTasks().editSpaceImage(spaceName, fileName);
    }

    @When("Alice adds {word} to the space {word} with")
    public void add_member_with_permissions(String userName, String spaceName, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesTasks().addMemberToSpace(userName, spaceName, table.asMap(String.class, String.class));
    }

    @When("Alice creates a new link to the space {word} with")
    public void create_new_link(String spaceName, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesTasks().createLinkToSpace(spaceName, table.asMap(String.class, String.class));
    }

    @When("Alice edits {word} over the space {word} with")
    public void edit_link(String linkName, String spaceName, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesTasks().editLinkOverSpace(linkName, spaceName, table.asMap(String.class, String.class));
    }

    @When("Alice removes {word} from the space {word}")
    public void user_removes_member(String userName, String spaceName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesTasks().removeMemberFromSpace(userName, spaceName);
    }

    @When("Alice edits {word} from the space {word} with the following fields")
    public void edit_member_space(String userName, String spaceName, DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesTasks().editMemberFromSpace(userName, spaceName, table.asMap(String.class, String.class));
    }

    @When("Alice removes {word} over the space {word}")
    public void remove_link_space(String linkName, String spaceName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesTasks().removeLinkOverSpace(linkName, spaceName);
    }

    @Then("Alice should{typePosNeg} see the following{spaceStatus} spaces")
    public void user_should_see_following_spaces(String sense, String status, DataTable table)
            throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesAssertions().assertSpacesVisibility(sense, status, table.asMaps(String.class, String.class));
    }

    @Then("the space should be created/updated in server with the following fields")
    public void spaces_created_in_server(DataTable table) throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesAssertions().assertSpaceCreatedOrUpdatedInServer(table.asMap(String.class, String.class));
    }

    @Then("the space image should be updated in server with file {word}")
    public void space_image_updated(String fileName, DataTable table) throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesAssertions().assertSpaceImageUpdatedInServer(fileName, table.asMaps(String.class, String.class));
    }

    @Then("the quota is correctly displayed")
    public void quota_displayed(DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesAssertions().assertQuotaIsCorrectlyDisplayed(table.asMap(String.class, String.class));
    }

    @Then("{word} should be member of the space {word} with")
    public void is_user_member(String userName, String spaceName, DataTable table) throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesAssertions().assertUserIsMemberOfSpace(userName, spaceName, table.asMap(String.class, String.class));
    }

    @Then("{word} should not be member of the space {word}")
    public void is_user_member(String userName, String spaceName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesAssertions().assertUserIsNotMemberOfSpace(userName);
    }

    @Then("Alice should see the link {word} on {word} with")
    public void is_link_created(String linkName, String spaceName, DataTable table) throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesAssertions().assertLinkIsVisibleOnSpace(linkName, spaceName, table.asMap(String.class, String.class));
    }

    @Then("Alice should not see the link {word} on {word}")
    public void link_not_visible(String linkName, String spaceName) throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        world.spacesAssertions().assertLinkIsNotVisibleOnSpace(linkName, spaceName);
    }
}
