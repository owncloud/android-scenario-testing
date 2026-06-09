/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.tasks;

import e2e.support.log.Log;
import e2e.world.World;

import java.util.Map;
import java.util.logging.Level;

public class SpacesTasks {

    private final World world;

    public SpacesTasks(World world) {
        this.world = world;
    }

    public void addMemberToSpace(String userName, String spaceName, Map<String, String> fields) {
        Log.log(Level.FINE, "Add member " + userName + " to space " + spaceName);
        openMembersView(spaceName);
        world.spacesMembersPage().addMember(userName);
        applyMemberFields(fields);
        world.spacesMembersPage().inviteMember();
    }

    public void editMemberFromSpace(String userName, String spaceName, Map<String, String> fields) {
        Log.log(Level.FINE, "Edit member " + userName + " from space " + spaceName);
        openMembersView(spaceName);
        world.spacesMembersPage().openEditMember(userName);
        applyMemberFields(fields);
        world.spacesMembersPage().inviteMember();
    }

    public void removeMemberFromSpace(String userName, String spaceName) {
        Log.log(Level.FINE, "Remove member " + userName + " from space " + spaceName);
        openMembersView(spaceName);
        world.spacesMembersPage().removeMember(userName);
    }

    public void createLinkToSpace(String spaceName, Map<String, String> fields) {
        Log.log(Level.FINE, "Create new link to space " + spaceName);
        openMembersView(spaceName);
        world.spacesMembersPage().addLink();
        applyCreateLinkFields(fields);
        world.spacesMembersPage().createLink();
    }

    public void editLinkOverSpace(String linkName, String spaceName, Map<String, String> fields) {
        Log.log(Level.FINE, "Edit link " + linkName + " over space " + spaceName);
        openMembersView(spaceName);
        world.spacesMembersPage().editLink(linkName);
        applyEditLinkFields(fields);
        world.spacesMembersPage().createLink();
    }

    public void removeLinkOverSpace(String linkName, String spaceName) {
        Log.log(Level.FINE, "Remove link " + linkName + " over space " + spaceName);
        openMembersView(spaceName);
        world.spacesMembersPage().removeLink(linkName);
    }

    private void openMembersView(String spaceName) {
        world.spacesPage().openMembers(spaceName);
    }

    private void applyMemberFields(Map<String, String> fields) {
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            applyMemberField(entry.getKey(), entry.getValue());
        }
    }

    private void applyMemberField(String key, String value) {
        switch (key) {
            case "permission" -> world.spacesMembersPage().setPermission(value);
            case "expirationDate" -> world.spacesMembersPage().setExpirationDate(value);
            default -> Log.log(Level.FINE, "Ignoring unsupported space member field: " + key);
        }
    }

    private void applyCreateLinkFields(Map<String, String> fields) {
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            applyCreateLinkField(entry.getKey(), entry.getValue());
        }
    }

    private void applyCreateLinkField(String key, String value) {
        switch (key) {
            case "name" -> world.spacesMembersPage().setName(value);
            case "permission" -> world.spacesMembersPage().setPermission(value);
            case "password" -> world.spacesMembersPage().setPassword();
            case "expirationDate" -> world.spacesMembersPage().setExpirationDate(value);
            default -> Log.log(Level.FINE, "Ignoring unsupported space link creation field: " + key);
        }
    }

    private void applyEditLinkFields(Map<String, String> fields) {
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            applyEditLinkField(entry.getKey(), entry.getValue());
        }
    }

    private void applyEditLinkField(String key, String value) {
        switch (key) {
            case "name" -> world.spacesMembersPage().setName(value);
            case "permission" -> world.spacesMembersPage().setPermission(value);
            case "password" -> world.spacesMembersPage().editPassword();
            case "expirationDate" -> world.spacesMembersPage().setExpirationDate(value);
            default -> Log.log(Level.FINE, "Ignoring unsupported space link edition field: " + key);
        }
    }
}
