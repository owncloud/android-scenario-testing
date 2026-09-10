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

public class ShareNGTasks {

    private final World world;

    public ShareNGTasks(World world) {
        this.world = world;
    }

    public void addSharee(String shareeType, String sharee, Map<String, String> fields) {
        Log.log(Level.FINE, "Add sharee. Type: " + shareeType + " - Sharee: " + sharee);
        world.shareNGPage().addShare();
        world.shareNGCreatePage().selectSharee(sharee);
        applyFields(fields);
        world.shareNGCreatePage().invite();
    }

    private void applyFields(Map<String, String> fields) {
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            applyField(entry.getKey(), entry.getValue());
        }
    }

    private void applyField(String key, String value) {
        switch (key) {
            case "permission" -> world.shareNGCreatePage().setPermission(value);
            case "expirationDate" -> setExpirationDate(value);
        }
    }

    private void setExpirationDate(String days) {
        Log.log(Level.FINE, "Set expiration date in days: " + days);
        String normalizedDays = normalizeOptional(days);
        boolean switchEnabled = world.shareNGCreatePage().isExpirationDateEnabled();
        boolean hasDays = normalizedDays != null;
        Log.log(Level.FINE, "Expiration switch enabled: " + switchEnabled);
        Log.log(Level.FINE, "Has expiration days: " + hasDays);
        if (!switchEnabled && hasDays) {
            world.shareNGCreatePage().toggleExpirationDate();
            selectExpirationDate(normalizedDays);
        } else if (switchEnabled && hasDays) {
            // emulator needs switch off/on before setting a new date. ugly.
            world.shareNGCreatePage().toggleExpirationDate();
            world.shareNGCreatePage().toggleExpirationDate();
            selectExpirationDate(normalizedDays);
        } else if (switchEnabled) {
            world.shareNGCreatePage().toggleExpirationDate();
        }
    }

    private void selectExpirationDate(String days) {
        String dateToSet = DateUtils.dateInDaysAndroidFormat(days);
        Log.log(Level.FINE, "Date to set: " + dateToSet);
        if (!world.shareNGCreatePage().isCalendarDateVisible(dateToSet)) {
            Log.log(Level.FINE, "Date not found in current calendar page. Moving to next page");
            world.shareNGCreatePage().tapNextCalendarPage();
        }
        world.shareNGCreatePage().selectCalendarDate(dateToSet);
        world.shareNGCreatePage().tapOk();
    }

    private String normalizeOptional(String value) {
        if (value == null) {
            return null;
        }
        String trimmedValue = value.trim();
        return trimmedValue.isEmpty() ? null : trimmedValue;
    }
}
