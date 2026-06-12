/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.preconditions;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import e2e.support.log.Log;
import e2e.world.World;

public class DevicePreconditions {

    private static final String APP_PACKAGE = "com.owncloud.android";
    private static final String TEST_PREFERENCES_URI =
            "content://com.owncloud.android.test.preferences";

    private final World world;

    public DevicePreconditions(World world) {
        this.world = world;
    }

    public void pushFileDevice(String fileName, String path) throws IOException {
        Log.log(Level.FINE, "Pushing file to device: " + fileName + " in path: " + path);
        world.deviceClient().pushFile(fileName, path);
    }

    public void deviceHasNoConnection() {
        Log.log(Level.FINE, "Setting device connection down");
        world.fileListPage().setConnectionDown();
    }

    public void settingsHaveBeenSet(List<Map<String, String>> rows)
            throws IOException, InterruptedException {
        for (Map<String, String> row : rows) {
            String setting = row.get("setting");
            String value = row.get("value");
            Log.log(Level.FINE, "Setting preference. " + setting + " = " + value);
            setBooleanPreference(setting, value);
            reopenApp();
        }
    }

    private void setBooleanPreference(String setting, String value)
            throws IOException, InterruptedException {
        String command = "adb shell content call --uri " + TEST_PREFERENCES_URI
                + " --method set_boolean"
                + " --arg " + setting
                + " --extra value:b:" + value;
        executeAdbCommand(command);
    }

    private void reopenApp() throws IOException, InterruptedException {
        executeAdbCommand("adb shell am force-stop " + APP_PACKAGE);
        executeAdbCommand("adb shell monkey -p " + APP_PACKAGE + " 1");
    }

    private void executeAdbCommand(String command)
            throws IOException, InterruptedException {
        Log.log(Level.FINE, "Command: " + command);
        int exitCode = Runtime.getRuntime().exec(command).waitFor();
        Log.log(Level.FINE, "Exit code of adb command: " + exitCode);
        if (exitCode != 0) {
            throw new IllegalStateException("ADB command failed with exit code "
                    + exitCode + ": " + command);
        }
    }
}
