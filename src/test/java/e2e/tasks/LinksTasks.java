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

public class LinksTasks {

    private final World world;

    public LinksTasks(World world) {
        this.world = world;
    }

    public void createPublicLink(String itemName, Map<String, String> fields) {
        Log.log(Level.FINE, "Create public link on item: " + itemName);
        world.sharePage().addPublicLink();
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            applyPublicLinkCreationField(itemName, entry.getKey(), entry.getValue());
        }
        world.publicLinksPage().submitLink();
    }

    public void editPublicLink(String itemName, Map<String, String> fields) {
        Log.log(Level.FINE, "Edit public link on item: " + itemName);
        world.sharePage().openPublicLink(itemName);
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            applyPublicLinkEditionField(itemName, entry.getKey(), entry.getValue());
        }
        world.publicLinksPage().submitLink();
    }

    public void deletePublicLink() {
        Log.log(Level.FINE, "Delete public link");
        world.sharePage().deletePublicShare();
        world.sharePage().acceptDeletion();
    }

    private void applyPublicLinkCreationField(String itemName, String key, String value) {
        switch (key) {
            case "name" -> world.publicLinksPage().addLinkName(value);
            case "password" -> world.publicLinksPage().typePassword(itemName, value);
            case "password-auto" -> world.publicLinksPage().generatePassword();
            case "permission", "permissions" -> world.publicLinksPage().setPermission(value);
            case "expiration days" -> world.publicLinksPage().setExpiration(value);
            default -> Log.log(Level.FINE, "Ignoring unsupported public link creation field: " + key);
        }
    }

    private void applyPublicLinkEditionField(String itemName, String key, String value) {
        switch (key) {
            case "name" -> world.publicLinksPage().addLinkName(value);
            case "password" -> world.publicLinksPage().typePassword(itemName, value);
            case "expiration days" -> world.publicLinksPage().setExpiration(value);
            case "permission", "permissions" -> selectPermissions(value);
            default -> Log.log(Level.FINE, "Ignoring unsupported public link edition field: " + key);
        }
    }

    private void selectPermissions(String value) {
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
            default -> throw new IllegalArgumentException("Unsupported public link permissions: " + value);
        }
    }
}
