/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.support.preferences;

import java.io.IOException;
import java.util.logging.Level;

import e2e.support.log.Log;

public class AppPreferences {

    private static final String BASE_SET_BOOLEAN_COMMAND =
            "adb shell content call --uri content://com.owncloud.android.test.preferences "
                    + "--method set_boolean --extra value:b:%s --arg %s";

    private static final String SHOW_HIDDEN_FILES = "show_hidden_files";
    private static final String SHOW_DISABLED_SPACES = "show_disabled_spaces";

    public void disableHiddenFiles() throws IOException, InterruptedException {
        setBoolean(SHOW_HIDDEN_FILES, false);
    }

    public void disableDisabledSpaces() throws IOException, InterruptedException {
        setBoolean(SHOW_DISABLED_SPACES, false);
    }

    private void setBoolean(String key, boolean value)
            throws IOException, InterruptedException {
        String command = String.format(BASE_SET_BOOLEAN_COMMAND, value, key);
        Log.log(Level.FINE, "Set app preference: " + key + "=" + value);
        Runtime.getRuntime().exec(command).waitFor();
    }
}
