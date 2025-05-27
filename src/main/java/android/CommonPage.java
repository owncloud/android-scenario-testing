/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package android;

import com.google.common.collect.ImmutableList;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.android.connection.ConnectionState;
import io.appium.java_client.android.connection.ConnectionStateBuilder;
import io.appium.java_client.pagefactory.AndroidFindBy;
import utils.log.Log;


public class CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/nav_all_files")
    private WebElement toRoot;

    protected static AndroidDriver driver = AndroidManager.getDriver();
    protected static final int WAIT_TIME = 15;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

    public CommonPage() {
    }

    /* Finders */

    public WebElement findId(String id) {
        return driver.findElement(AppiumBy.id(id));
    }

    public List<WebElement> findListId(String id) {
        return driver.findElements(AppiumBy.id(id));
    }

    public WebElement findXpath(String xpath) {
        return driver.findElement(AppiumBy.xpath(xpath));
    }

    public List<WebElement> findListXpath(String xpath) {
        return driver.findElements(AppiumBy.xpath(xpath));
    }

    public WebElement findUIAutomatorText(String text) {
        return driver.findElement(AppiumBy.androidUIAutomator(
                "new UiSelector().textContains(\"" + text + "\");"));
    }

    public WebElement findUIAutomatorSubText(String text) {
        return driver.findElement(AppiumBy.androidUIAutomator(
                "new UiSelector().textContains(\"" + text + "\");"));
    }

    public WebElement findUIAutomatorDescription(String description) {
        return driver.findElement(AppiumBy.androidUIAutomator(
                "new UiSelector().description(\"" + description + "\");"));
    }

    public List<WebElement> findListUIAutomatorText(String finder) {
        return driver.findElements(AppiumBy.androidUIAutomator(
                "new UiSelector().textContains(\"" + finder + "\");"));
    }

    public WebElement findAccesibility(String id) {
        return driver.findElement(new AppiumBy.ByAccessibilityId(id));
    }

    public List<WebElement> findListAccesibility(String id) {
        return driver.findElements(new AppiumBy.ByAccessibilityId(id));
    }

    /* Waiters by different parameters */

    public static void waitByXpath(int timeToWait, String resourceXpath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeToWait));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath(resourceXpath)));
    }

    public static void waitById(int timeToWait, String resourceId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeToWait));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.id(resourceId)));
    }

    public static void waitById(int timeToWait, WebElement mobileElement) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeToWait));
        wait.until(ExpectedConditions.visibilityOf(mobileElement));
    }

    public static void waitByIdInvisible(int timeToWait, String resourceId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeToWait));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(AppiumBy.id(resourceId)));
    }

    public static void waitByIdInvisible(int timeToWait, WebElement mobileElement) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeToWait));
        wait.until(ExpectedConditions.invisibilityOf(mobileElement));
    }

    public static void waitByTextVisible(int timeToWait, String text) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeToWait));
        WebElement mobileElement = (WebElement)
                driver.findElement(AppiumBy.androidUIAutomator
                        ("new UiSelector().text(\"" + text + "\");"));
        wait.until(ExpectedConditions.textToBePresentInElement(mobileElement, text));
    }

    public void waitByTextInvisible(int timeToWait, String text) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeToWait));
        WebElement mobileElement = (WebElement)
                driver.findElement(AppiumBy.androidUIAutomator
                        ("new UiSelector().text(\"" + text + "\");"));
        wait.until(ExpectedConditions.invisibilityOfElementWithText(AppiumBy.androidUIAutomator(
                "new UiSelector().textContains(\"Download enqueued\");"), "Download enqueued"));
    }

    protected HashMap turnListToHashmap(List<List<String>> dataList) {
        HashMap<String, String> mapFields = new HashMap<String, String>();
        for (List<String> rows : dataList) {
            mapFields.put(rows.get(0), rows.get(1));
        }
        return mapFields;
    }

    /* Finger actions */

    public void swipe(double startx, double starty, double endx, double endy) {
        Dimension size = driver.manage().window().getSize();
        int startY = (int) (size.height * starty);
        int endY = (int) (size.height * endy);
        int startX = (int) (size.width * startx);
        int endX = (int) (size.width * endx);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(1000),
                PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(1000),
                PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(swipe));
    }

    public void longPress(WebElement element) {
        Point location = element.getLocation();
        PointerInput pointerInput = new PointerInput(PointerInput.Kind.TOUCH, "longp");
        Sequence longPress = new Sequence(pointerInput, 0);
        longPress.addAction(pointerInput.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), location.x, location.y));
        longPress.addAction(pointerInput.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPress.addAction(pointerInput.createPointerMove(Duration.ofSeconds(1),
                PointerInput.Origin.viewport(), location.x, location.y));
        longPress.addAction(pointerInput.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(ImmutableList.of(longPress));

    }

    public void tap(int X, int Y) {
        Log.log(Level.FINE, "Starts: tap on X: " + X + ", Y: " + Y);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tapSeq = new Sequence(finger, 1);
        tapSeq.addAction(finger.createPointerMove(Duration.ofSeconds(1), PointerInput.Origin.viewport(), X, Y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(tapSeq));
    }

    /* Browsing methods used in several pages */

    /*
     * Receives: name of the folder in the current list to browse into
     */
    public void browseInto(String folderName) {
        Log.log(Level.FINE, "Starts: browse to " + folderName);
        findUIAutomatorText(folderName).click();
    }

    /*
     * Browses to root folder using the shortcut in the bottom bar
     */
    public void browseRoot() {
        Log.log(Level.FINE, "Starts: browse to root");
        toRoot.click();
    }

    /*
     * Receives: path to a folder. If path does not contain "/", folder is in root.
     * Otherwise browsing to.
     */
    public void browseToFolder(String path) {
        if (path.equals("/")) { //Go to Root
            browseRoot();
        } else if (path.contains("/")) { //browsing to the folder
            int i = 0;
            String[] route = path.split("/");
            for (i = 0; i < route.length; i++) {
                Log.log(Level.FINE, "browsing to " + route[i]);
                browseInto(route[i]);
            }
        } else { //no path to browse, just clicking
            browseInto(path);
        }
    }

    /*
     * Receives: path to a file. If path does not contain "/", file is in the root folder,
     * otherwise browsing to
     * Returns: File name (last chunk of the path), after browsing to reach it.
     */
    public String browseToFile(String path) {
        String[] route = path.split("/");
        int i = 0;
        if (route.length > 0) { //browse
            for (i = 0; i < route.length - 1; i++) {
                Log.log(Level.FINE, "browsing to " + route[i]);
                browseInto(route[i]);
            }
            Log.log(Level.FINE, "Returning: " + route[i]);
            return route[i];
        }
        return path;
    }

    protected boolean parseIntBool(String s) {
        return Boolean.parseBoolean(s);
    }

    public void setConnectionDown(){
        Log.log(Level.FINE, "Starts: Set connection down");
        driver.setConnection(new ConnectionStateBuilder()
                .withWiFiDisabled()
                .withDataDisabled()
                .build());
    }

    public void setConnectionUp(){
        Log.log(Level.FINE, "Starts: Set connection up");
        driver.setConnection(new ConnectionStateBuilder()
                .withWiFiEnabled()
                .withDataEnabled()
                .build());
        // Wait till connection is up
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

    /* Methods to help debugging */

    public static void takeScreenshot(String name) {
        try {
            String sd = sdf.format(new Timestamp(System.currentTimeMillis()).getTime());
            File screenShotFile = (driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenShotFile, new File("screenshots/" + name + "_" + sd + ".png"));
            Log.log(Level.FINE, "Take screenshot " + name + " at: " + sd);
        } catch (IOException e) {
            Log.log(Level.FINE, "Screenshot not taken");
        }
    }

    public static void startRecording() {
        AndroidStartScreenRecordingOptions androidStartScreenRecordingOptions =
                new AndroidStartScreenRecordingOptions();
        androidStartScreenRecordingOptions.withBitRate(2000000);
        androidStartScreenRecordingOptions.withVideoSize("360x640");
        driver.startRecordingScreen(androidStartScreenRecordingOptions);
    }

    public static void stopRecording(String filename) {
        String base64String = driver.stopRecordingScreen();
        byte[] data = Base64.decodeBase64(base64String);
        String destinationPath = "video/" + filename + "_" +
                sdf.format(new Timestamp(System.currentTimeMillis()).getTime()) + ".mp4";
        Path path = Paths.get(destinationPath);
        try {
            Files.write(path, data);
        } catch (IOException e) {
            Log.log(Level.FINE, e.getMessage());
        }
    }
}
