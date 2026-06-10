/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.tasks;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import e2e.support.log.Log;
import e2e.world.World;

public class SpacesTasks {

    private static final String DEFAULT_SUBTITLE = "";
    private static final String DEFAULT_QUOTA = "No restriction";

    private final World world;

    public SpacesTasks(World world) {
        this.world = world;
    }

    public void openSpacesView() {
        Log.log(Level.FINE, "Open spaces view");
        world.fileListPage().openSpaces();
    }

    public void disableSpacesInServer(List<Map<String, String>> rows) throws IOException {
        for (Map<String, String> row : rows) {
            String name = row.get("name");
            String subtitle = getOptionalTrimmedValue(row, "subtitle", DEFAULT_SUBTITLE);
            Log.log(Level.FINE, "Disable space in server. Name: " + name
                    + " - Subtitle: " + subtitle);
            world.graphAPI().disableSpace(name, subtitle);
        }
    }

    public void filterSpacesList(String pattern) {
        Log.log(Level.FINE, "Filter spaces list using: " + pattern);
        world.spacesPage().typeSearch(pattern);
    }

    public void createSpace(Map<String, String> fields) {
        String name = getRequiredValue(fields, "name");
        String subtitle = getOptionalTrimmedValue(fields, "subtitle", DEFAULT_SUBTITLE);
        String quota = getOptionalTrimmedValue(fields, "quota", DEFAULT_QUOTA);
        Log.log(Level.FINE, "Create space. Name: " + name
                + " - Subtitle: " + subtitle
                + " - Quota: " + quota);
        world.spacesPage().createSpace(name, subtitle, quota);
    }

    public void openEditSpace(String spaceName) {
        Log.log(Level.FINE, "Open edit space: " + spaceName);
        world.spacesPage().openEditSpace(spaceName);
    }

    public void openDisableSpace(String spaceName) {
        Log.log(Level.FINE, "Disable space: " + spaceName);
        world.spacesPage().openDisableSpace(spaceName);
    }

    public void openEnableSpace(String spaceName) {
        Log.log(Level.FINE, "Enable space: " + spaceName);
        world.spacesPage().openEnableSpace(spaceName);
    }

    public void openDeleteSpace(String spaceName) {
        Log.log(Level.FINE, "Delete space: " + spaceName);
        world.spacesPage().openDeleteSpace(spaceName);
    }

    public void updateSpace(Map<String, String> fields) {
        String name = getRequiredValue(fields, "name");
        String subtitle = getOptionalTrimmedValue(fields, "subtitle", DEFAULT_SUBTITLE);
        String quota = getOptionalTrimmedValue(fields, "quota", DEFAULT_QUOTA);
        Log.log(Level.FINE, "Update space. Name: " + name
                + " - Subtitle: " + subtitle
                + " - Quota: " + quota);
        world.spacesPage().editSpace(name, subtitle, quota);
    }

    public void editSpaceImage(String spaceName, String fileName) {
        Log.log(Level.FINE, "Edit image of space " + spaceName + " with file " + fileName);
        world.spacesPage().openEditSpaceImage(spaceName);
        world.documentProviderPage().selectImageToUpload(fileName);
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

    private String getRequiredValue(Map<String, String> fields, String key) {
        String value = fields.get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Required space field is missing: " + key);
        }
        return value.trim();
    }

    private String getOptionalTrimmedValue(Map<String, String> fields, String key, String defaultValue) {
        String value = fields.get(key);
        return value == null ? defaultValue : value.trim();
    }
}
