/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.support.preferences;

import java.util.List;

import e2e.support.device.DeviceClient;

public class AppPreferences {

    private static final String PREFERENCES_URI = "content://com.owncloud.android.test.preferences";
    private final DeviceClient deviceClient;

    public AppPreferences(DeviceClient deviceClient) {
        this.deviceClient = deviceClient;
    }

    public void disableHiddenFiles() {
        setBoolean("show_hidden_files", false);
    }

    public void disableDisabledSpaces() {
        setBoolean("show_disabled_spaces", false);
    }

    private void setBoolean(String key, boolean value) {
        deviceClient.executeShellCommand("content", List.of("call", "--uri", PREFERENCES_URI,
                "--method", "set_boolean", "--extra", "value:b:" + value, "--arg", key));
    }
}
