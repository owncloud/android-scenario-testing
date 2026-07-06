/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.support.device;

import static e2e.support.log.Log.Log;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import e2e.LocProperties;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.connection.ConnectionState;
import io.appium.java_client.android.connection.ConnectionStateBuilder;

public class DeviceClient {

    private static final long WAIT_TIME = 7;
    private final String downloadFolder = "/sdcard/Download";
    private final String owncloudFolder = downloadFolder + "/owncloud/";
    private final AndroidDriver driver;

    public DeviceClient(AndroidDriver driver) {
        this.driver = driver;
    }

    public void cleanUpDevice() {
        Log.log(Level.FINE, "Starts: Clean up device, owncloud folder");
        // Remove owncloud folder from device
        executeShellCommand("rm", Arrays.asList("-rf", downloadFolder + "/*"));
    }

    public void cleanUpTemp() {
        Log.log(Level.FINE, "Starts: Clean up device, tmp folder");
        executeShellCommand("rm", Arrays.asList("-rf", "/sdcard/tmp/*"));
    }

    public void pushFile(String itemName, String path) throws IOException {
        Log.log(Level.FINE, "Starts: push file: " + itemName);
        File rootPath = new File(System.getProperty("user.dir"));
        File appDir = new File(rootPath, "src/test/resources");
        File file2push = new File(appDir, "io/cucumber/example-files/" + itemName);
        Log.log(Level.FINE, "File to push: " + downloadFolder + path + itemName);
        driver.pushFile(downloadFolder + path + itemName, file2push);
    }

    public void overwriteFile(String itemName, String path) throws IOException {
        Log.log(Level.FINE, "Starts: overwriteFile file: " + itemName);
        File rootPath = new File(System.getProperty("user.dir"));
        File appDir = new File(rootPath, "src/test/resources");
        File file2push = new File(appDir, "io/cucumber/example-files/" + itemName);
        Log.log(Level.FINE, "File to push: " + owncloudFolder + path + itemName);
        driver.pushFile("/sdcard/tmp/" + itemName, file2push);
        executeShellCommand("cp", Arrays.asList("/sdcard/tmp/" + itemName, "'"
                + owncloudFolder + path + itemName+"'"));
        Log.log(Level.FINE, "File " + itemName + " pushed");
    }

    public String pullList(String folderId) {
        Log.log(Level.FINE, "Starts: pull file from: " + folderId);
        String user = LocProperties.getProperties().getProperty("userName1").toLowerCase();
        String server = System.getProperty("server")
                .replaceFirst("^https?://", "")
                .replace(":", "%3A" );
        String target = owncloudFolder + user + "@" + server  + "/" + folderId;
        Log.log(Level.FINE, "Command args to execute: " + target);
        String output = executeShellCommand("ls", List.of(target));
        Log.log(Level.FINE, "List of files in given folder: " + output);
        return output;
    }

    public String executeShellCommand(String command, List<String> commandArgs) {
        Map<String, Object> args = new HashMap<>();
        args.put("command", command);
        args.put("args", commandArgs);
        return (String) driver.executeScript("mobile: shell", args);
    }

    public void setConnectionDown() {
        Log.log(Level.FINE, "Starts: Set connection down");
        driver.setConnection(new ConnectionStateBuilder().withWiFiDisabled().withDataDisabled().build());
    }

    public void setConnectionUp() {
        Log.log(Level.FINE, "Starts: Set connection up");
        driver.setConnection(new ConnectionStateBuilder().withWiFiEnabled().withDataEnabled().build());
        WebDriverWait wait = new WebDriverWait(driver, Duration.of(WAIT_TIME, ChronoUnit.SECONDS));
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                AndroidDriver d = (AndroidDriver) input;
                ConnectionState state = d.getConnection();
                return state.isWiFiEnabled() || state.isDataEnabled();
            }
        });
    }
}
