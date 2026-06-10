/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import e2e.model.OCSpace;
import e2e.model.OCSpaceLink;
import e2e.model.OCSpaceMember;
import e2e.support.date.DateUtils;
import e2e.support.log.Log;
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
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        List<OCSpace> spaces = world.graphAPI().getMySpaces();
        for (Map<String, String> row : rows) {
            String name = row.get("name").trim();
            String subtitle = row.get("subtitle") != null ? row.get("subtitle").trim() : "";
            Log.log(Level.FINE, "Checking sense: " + sense + " for space: " + name + " " + subtitle);
            if (sense.isEmpty()) { // positive case
                // Local validation
                assertTrue(world.spacesPage().isSpaceDisplayed(name, subtitle, status));
                // Remote validation
                boolean found = false;
                Log.log(Level.FINE, "Checking the remote contains the local");
                for (OCSpace space : spaces) {
                    Log.log(Level.FINE, "Local: " + name + " " + subtitle);
                    Log.log(Level.FINE, "Checking space: " + space.getName() + " " + space.getDescription());
                    if (space.getName().trim().equals(name) && space.getDescription().trim().equals(subtitle)) {
                        Log.log(Level.FINE, "Found!!");
                                found = true;
                        break;
                    }
                }
                assertTrue(found);
            } else if (sense.equals(" not")) { // negative case, status does not matter
                assertFalse(world.spacesPage().isSpaceDisplayed(name, subtitle, ""));
            }
        }
    }

    @Then("the space should be created/updated in server with the following fields")
    public void spaces_created_in_server(DataTable table) throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        // Spaces in scenario definition
        Map<String, String> fields = table.asMap(String.class, String.class);
        boolean matches = true;
        String name = fields.get("name");
        // Description can be null
        String subtitle = fields.get("subtitle") != null ? fields.get("subtitle") : "";
        String quota = fields.get("quota");
        String unit = fields.get("unit");
        Log.log(Level.FINE, "Space from scenario: " + name + " " + subtitle + " " + quota);
        // Spaces in server
        List<OCSpace> spaces = world.graphAPI().getMySpaces();
        for (OCSpace space : spaces) {
            // Check if space in server matches with space in scenario definition
            Log.log(Level.FINE, "Space in server: " + space.getName() + " "
                    + space.getDescription() + " " + space.getQuota(unit));
            if (!(space.getName().equals(name)
                    && space.getDescription().equals(subtitle)
                    && space.getQuota(unit).equals(quota))) {
                matches = false;;
            }
        }
        // Check if all spaces in scenario definition match with spaces in server
        assertTrue(matches);
    }

    @Then("the space image should be updated in server with file {word}")
    public void space_image_updated(String fileName, DataTable table) throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String name = row.get("name");
            String subtitle = row.get("subtitle");
            String id = world.graphAPI().getSpaceIdFromNameAndDescription(name, subtitle);
            assertTrue(world.filesAPI().itemExist(id, "/.space/" + fileName));
        }
    }

    @Then("the quota is correctly displayed")
    public void quota_displayed(DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        Map<String, String> values = table.asMap(String.class, String.class);
        String spaceName = values.get("name");
        String quota = values.get("quota");
        String unit = values.get("unit");
        user_edit_space(spaceName);
        assertTrue(world.spacesPage().isQuotaDisplayed(quota, unit));
    }

    @Then("{word} should be member of the space {word} with")
    public void is_user_member(String userName, String spaceName, DataTable table) throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        // Get member from backend
        OCSpaceMember member = world.graphAPI().getMemberOfSpace(spaceName, userName);
        Log.log(Level.FINE, "Member from backend: " + member.getDisplayName() +
                " " + member.getPermission() +
                " " + member.getExpirationDate());
        Map<String, String> fields = table.asMap(String.class, String.class);
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "permission" -> {
                    // Local validation
                    assertTrue(world.spacesMembersPage().isUserMember(userName, value));
                    // Remote validation
                    assertTrue(member.getPermission().contains(value));
                    }
                case "expirationDate" -> {
                    // Local validation
                    assertTrue(world.spacesMembersPage().isExpirationDateCorrect(value));
                    // Remote validation
                    Log.log(Level.FINE, "Remote date: " + member.getExpirationDate());
                    if (value != null) {
                        String dateRemote = member.getExpirationDate().substring(0, 10) + " 23:59:59";
                        String formattedDate = DateUtils.dateInDaysWithServerFormat(value);
                        Log.log(Level.FINE, "Days: " + value);
                        Log.log(Level.FINE, "Date in server: " + dateRemote);
                        Log.log(Level.FINE, "Date in local: " + formattedDate);
                        assertEquals(formattedDate, dateRemote);
                    } else {
                        assertNull(member.getExpirationDate());
                    }
                }
            }
        }
    }

    @Then("{word} should not be member of the space {word}")
    public void is_user_member(String userName, String spaceName) {
        StepLogger.logCurrentStep(Level.FINE);
        assertFalse(world.spacesMembersPage().isMemberOfSpace(userName, spaceName));
    }

    private void handleSpace(DataTable table, String operation){
        Map<String, String> data = table.asMap(String.class, String.class);
        String name = data.get("name");
        String subtitle = data.get("subtitle")!=null ? data.get("subtitle") : "";
        String quota = data.get("quota");
        if (operation.equals("update")) {
            world.spacesPage().editSpace(name, subtitle, quota);
        } else if (operation.equals("create")) {
            world.spacesPage().createSpace(name, subtitle, quota);
        }
    }

    @Then("Alice should see the link {word} on {word} with")
    public void is_link_created(String linkName, String spaceName, DataTable table) throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        // Get member from backend
        OCSpaceLink linkBackend = world.graphAPI().getLinkOfSpace(spaceName, linkName);
        Log.log(Level.FINE, "Link in backend: name: " + linkBackend.getLinkName() +
                " permission: " + linkBackend.getPermission() +
                " expirationDate: " + linkBackend.getExpirationDate());
        String name = linkName;
        String permission = "";
        String expirationDate = "";
        Map<String, String> fields = table.asMap(String.class, String.class);
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "permission" -> {
                    permission = value;
                }
                case "expirationDate" -> {
                    expirationDate = value;
                }
            }
        }
        Log.log(Level.FINE, "Link from scenario: name: " + name +
                " permission: " + permission +
                " expirationDate: " + expirationDate);
        // Local assertion
        assertTrue(world.spacesMembersPage().isLinkCreated(name, permission, expirationDate));
        // Remote assertion
        assertTrue(linkBackend.getLinkName().equals(name)
                && linkBackend.getPermission().equals(getLinkPermissionDisplayName(permission))
                && ((expirationDate == null || expirationDate.isEmpty())
                ? linkBackend.getExpirationDate() == null
                : linkBackend.getExpirationDate().startsWith(
                DateUtils.dateInDaysWithServerFormat(expirationDate).substring(0, 10)
        )));
    }

    @Then("Alice should not see the link {word} on {word}")
    public void link_not_visible(String linkName, String spaceName) throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        // Local assertion
        assertFalse(world.spacesMembersPage().isLinkCreated(linkName, "", ""));
        // Remote assertion
        OCSpaceLink linkBackend = world.graphAPI().getLinkOfSpace(spaceName, linkName);
        assertNull(linkBackend);
    }

    private String getLinkPermissionDisplayName(String permission) {
        return switch (permission) {
            case "Can view" -> "view";
            case "Can edit" -> "edit";
            case "Secret file drop" -> "createOnly";
            default -> "";
        };
    }
}
