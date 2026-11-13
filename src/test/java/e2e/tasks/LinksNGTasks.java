/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.tasks;

import java.util.Map;
import java.util.logging.Level;

import e2e.support.log.Log;
import e2e.world.World;

public class LinksNGTasks {

    private final World world;

    public LinksNGTasks(World world) {
        this.world = world;
    }

    public void createPublicLink(String itemName, Map<String, String> fields) {
        Log.log(Level.FINE, "Create public link via NG on item: " + itemName);
        world.publicLinkNGPage().tapAddPublicLink();
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            applyField(entry.getKey(), entry.getValue());
        }
        world.publicLinkNGPage().confirmCreation();
    }

    public void editPublicLink(String linkName, Map<String, String> fields) {
        Log.log(Level.FINE, "Edit public link via NG: " + linkName);
        tapEditPublicLink(linkName);
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            applyEditField(entry.getKey(), entry.getValue());
        }
        world.publicLinkNGPage().confirmEdit();
    }

    public void tapEditPublicLink(String linkName) {
        world.publicLinkNGPage().tapEditPublicLink(linkName);
    }

    public void removePublicLink(String linkName) {
        Log.log(Level.FINE, "Remove public link: " + linkName);
        tapRemovePublicLink(linkName);
        confirmPublicLinkRemoval();
    }

    public void tapRemovePublicLink(String linkName) {
        world.publicLinkNGPage().tapRemoveButton(linkName);
    }

    public void confirmPublicLinkRemoval() {
        world.publicLinkNGPage().confirmRemoval();
    }

    public void dismissPublicLinkRemovalDialog() {
        world.publicLinkNGPage().dismissRemovalDialog();
    }

    public void enterPasswordOnEdit(String password) {
        world.publicLinkNGPage().enterPasswordOnEdit(password);
    }

    public void removePasswordOnEdit() {
        world.publicLinkNGPage().removePasswordOnEdit();
    }

    public void tapAddPublicLink() {
        world.publicLinkNGPage().tapAddPublicLink();
    }

    public void selectPermission(String permission) {
        world.publicLinkNGPage().selectPermission(permission);
    }

    private void applyEditField(String key, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        switch (key) {
            case "permission" -> world.publicLinkNGPage().selectPermission(value);
            case "name" -> world.publicLinkNGPage().setNameOnEdit(value);
            case "password" -> enterPasswordOnEdit(value);
            case "expiration days" -> {
                if ("(none)".equals(value)) {
                    world.publicLinkNGPage().removeExpirationDateOnEdit();
                } else {
                    world.publicLinkNGPage().setExpirationDaysOnEdit(Integer.parseInt(value));
                }
            }
            default -> Log.log(Level.FINE, "Ignoring unsupported public link NG field: " + key);
        }
    }

    private void applyField(String key, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        switch (key) {
            case "permission" -> world.publicLinkNGPage().selectPermission(value);
            case "name" -> world.publicLinkNGPage().enterName(value);
            case "password" -> world.publicLinkNGPage().enterPassword(value);
            case "expiration days" -> world.publicLinkNGPage().setExpirationDays(Integer.parseInt(value));
            default -> Log.log(Level.FINE, "Ignoring unsupported public link NG field: " + key);
        }
    }
}
