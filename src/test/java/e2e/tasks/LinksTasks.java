/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.tasks;

import java.util.Map;
import java.util.logging.Level;

import e2e.support.date.DateUtils;
import e2e.support.log.Log;
import e2e.world.World;

public class LinksTasks {

    private final World world;

    public LinksTasks(World world) {
        this.world = world;
    }

    public void createPublicLink(String itemName, Map<String, String> fields) {
        Log.log(Level.FINE, "Create public link on item: " + itemName);
        world.sharePage().addLink();
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            applyPublicLinkCreationField(entry.getKey(), entry.getValue());
        }
        world.publicLinksPage().clickSave();
    }

    public void editPublicLink(String itemName, Map<String, String> fields) {
        Log.log(Level.FINE, "Edit public link on item: " + itemName);
        world.sharePage().editLink(itemName);
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            applyPublicLinkEditionField(entry.getKey(), entry.getValue());
        }
        world.publicLinksPage().clickSave();
    }

    public void deletePublicLink(String itemName) {
        Log.log(Level.FINE, "Delete public link: " + itemName);
        world.sharePage().deleteLink(itemName);
        world.sharePage().acceptDeletion();
    }

    private void applyPublicLinkCreationField(String key, String value) {
        switch (key) {
            case "name" -> world.publicLinksPage().addLinkName(value);
            case "password" -> world.publicLinksPage().typePassword(value);
            case "password-auto" -> world.publicLinksPage().generatePassword();
            case "permission", "permissions" -> selectPermissions(value);
            case "expiration days" -> setExpiration(value);
            default -> Log.log(Level.FINE, "Ignoring unsupported public link creation field: " + key);
        }
    }

    private void applyPublicLinkEditionField(String key, String value) {
        switch (key) {
            case "name" -> world.publicLinksPage().addLinkName(value);
            case "password" -> world.publicLinksPage().typePassword(value);
            case "expiration days" -> setExpiration(value);
            case "permission", "permissions" -> selectPermissions(value);
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
        }
    }

    public void setExpiration(String days) {
        Log.log(Level.FINE, "Starts: Set Expiration date in days: " + days);
        world.publicLinksPage().clickExpirationSwitch();
        String dateToSet = DateUtils.dateInDaysAndroidFormat(days);
        Log.log(Level.FINE, "Days: " + days + " Date to set: " + dateToSet);
        if (world.publicLinksPage().findListAccesibility(dateToSet).isEmpty()) {
            Log.log(Level.FINE, "Date not found, next page");
            world.publicLinksPage().clickNextButton();
        }
        world.publicLinksPage().findAccesibility(dateToSet).click();
        world.publicLinksPage().clickOkButton();
    }
}
