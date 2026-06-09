/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import static e2e.support.log.Log.Log;

import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import e2e.LocProperties;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class DevicePage extends CommonPage {

    private String downloadFolder = "/sdcard/Download";
    String owncloudFolder = downloadFolder + "/owncloud/";

    public DevicePage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void cleanUpDevice() {
        Log.log(Level.FINE, "Starts: Clean up device, owncloud folder");
        // Remove owncloud folder from device
        Map<String, Object> args = new HashMap<>();
        args.put("command", "rm");
        args.put("args", Arrays.asList("-rf", downloadFolder + "/*"));
        driver.executeScript("mobile: shell", args);
    }

    public void cleanUpTemp() {
        Log.log(Level.FINE, "Starts: Clean up device, tmp folder");
        // Remove owncloud folder from device
        Map<String, Object> args = new HashMap<>();
        args.put("command", "rm");
        args.put("args", Arrays.asList("-rf", "/sdcard/tmp/*"));
        driver.executeScript("mobile: shell", args);
    }

    public void pushFile(String itemName, String path) throws IOException {
        Log.log(Level.FINE, "Starts: push file: " + itemName);
        File rootPath = new File(System.getProperty("user.dir"));
        File appDir = new File(rootPath, "src/test/resources");
        File file2push = new File(appDir, "io/cucumber/example-files/" + itemName);
        Log.log(Level.FINE, "File to push: " + downloadFolder + path + itemName);
        driver.pushFile(downloadFolder + path + itemName, file2push);
    }

    public void overwriteFile(String itemName, String path) {
        Log.log(Level.FINE, "Starts: overwriteFile file: " + itemName);
        File rootPath = new File(System.getProperty("user.dir"));
        File appDir = new File(rootPath, "src/test/resources");
        File file2push = new File(appDir, "io/cucumber/example-files/" + itemName);
        try {
            Log.log(Level.FINE, "File to push: " + owncloudFolder + path + itemName);
            driver.pushFile("/sdcard/tmp/" + itemName, file2push);
            Map<String, Object> args = new HashMap<>();
            args.put("command", "cp");
            args.put("args", Arrays.asList("/sdcard/tmp/" + itemName, "'"+owncloudFolder + path + itemName+"'"));
            driver.executeScript("mobile: shell", args);

            Log.log(Level.FINE, "File " + itemName + " pushed");
        } catch (IOException e) {
            Log.log(Level.SEVERE, "IO Exception: " + e.getMessage());
        }
    }

    public String pullList(String folderId) {
        Log.log(Level.FINE, "Starts: pull file from: " + folderId);
        Map<String, Object> args = new HashMap<>();

        String user = LocProperties.getProperties().getProperty("userName1").toLowerCase();
        String server = System.getProperty("server")
                .replaceFirst("^https?://", "")
                .replace(":", "%3A" );
        String target = owncloudFolder + user + "@" + server  + "/" + folderId;
        Log.log(Level.FINE, "Command args to execute: " + target);
        args.put("command", "ls");
        args.put("args", List.of(target));

        String output = (String) driver.executeScript("mobile: shell", args);
        Log.log(Level.FINE, "List of files in given folder: " + output);
        return output;
    }
}
