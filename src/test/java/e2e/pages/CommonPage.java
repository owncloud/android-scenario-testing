/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import static e2e.support.log.Log.Log;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebElement;
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
import java.util.Map;
import java.util.logging.Level;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.android.connection.ConnectionState;
import io.appium.java_client.android.connection.ConnectionStateBuilder;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/nav_all_files")
    private WebElement toRoot;

    protected static final int WAIT_TIME = 7;
    private static final String UI_AUTOMATOR_TEXT_TEMPLATE = "new UiSelector().text(\"%s\")";
    private static final String UI_AUTOMATOR_TEXT_CONTAINS_TEMPLATE = "new UiSelector().textContains(\"%s\")";
    private static final String UI_AUTOMATOR_DESCRIPTION_TEMPLATE = "new UiSelector().description(\"%s\")";
    private static final String ROOT_PATH = "/";
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd_HHmmss");
    protected static AndroidDriver driver;
    private static boolean recordingStarted = false;

    public CommonPage(AndroidDriver driver) {
        this.driver = driver;
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
                String.format(UI_AUTOMATOR_TEXT_TEMPLATE, text)));
    }

    public WebElement findUIAutomatorSubText(String text) {
        return driver.findElement(AppiumBy.androidUIAutomator(
                String.format(UI_AUTOMATOR_TEXT_CONTAINS_TEMPLATE, text)));
    }

    public WebElement findUIAutomatorDescription(String description) {
        return driver.findElement(AppiumBy.androidUIAutomator(
                String.format(UI_AUTOMATOR_DESCRIPTION_TEMPLATE, description)));
    }

    public List<WebElement> findListUIAutomatorText(String finder) {
        return driver.findElements(AppiumBy.androidUIAutomator(
                String.format(UI_AUTOMATOR_TEXT_CONTAINS_TEMPLATE, finder)));
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

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.xpath(resourceXpath)));
    }

    public static void waitById(int timeToWait, String resourceId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeToWait));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.id(resourceId)));
    }

    public static void waitById(int timeToWait, WebElement mobileElement) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeToWait));

        wait.until(ExpectedConditions.visibilityOf(mobileElement));
    }

    public static void waitByIdInvisible(int timeToWait, String resourceId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeToWait));

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                AppiumBy.id(resourceId)));
    }

    public static void waitByIdInvisible(int timeToWait, WebElement mobileElement) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeToWait));

        wait.until(ExpectedConditions.invisibilityOf(mobileElement));
    }

    public static void waitByTextVisible(int timeToWait, String text) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeToWait));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.androidUIAutomator(
                        String.format(UI_AUTOMATOR_TEXT_CONTAINS_TEMPLATE, text))));
    }

    public void waitByTextInvisible(int timeToWait, String text) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeToWait));

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                AppiumBy.androidUIAutomator(
                        String.format(UI_AUTOMATOR_TEXT_CONTAINS_TEMPLATE, text))));
    }

    public void waitUntilTextIsNotEmpty(int timeToWait, String resourceId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeToWait));
        ExpectedCondition<Boolean> textNotEmpty = driver -> {
            WebElement element = driver.findElement(AppiumBy.id(resourceId));
            String text = element.getText();
            return text != null && !text.trim().isEmpty();
        };
        wait.until(textNotEmpty);
    }

    protected HashMap turnListToHashmap(List<List<String>> dataList) {
        HashMap<String, String> mapFields = new HashMap<>();
        for (List<String> rows : dataList) {
            mapFields.put(rows.get(0), rows.get(1));
        }
        return mapFields;
    }

    /* Finger actions */

    public void swipe(double startx, double starty, double endx, double endy) {
        final String fingerName = "finger";
        final int pointerMoveDurationMs = 1000;
        Dimension size = driver.manage().window().getSize();
        int startY = (int) (size.height * starty);
        int endY = (int) (size.height * endy);
        int startX = (int) (size.width * startx);
        int endX = (int) (size.width * endx);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, fingerName);
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(
                Duration.ofMillis(pointerMoveDurationMs),
                PointerInput.Origin.viewport(),
                startX,
                startY));

        swipe.addAction(finger.createPointerDown(
                PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(
                Duration.ofMillis(pointerMoveDurationMs),
                PointerInput.Origin.viewport(),
                endX,
                endY));
        swipe.addAction(finger.createPointerUp(
                PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(swipe));
    }

    public void longPress(String text) {
        final String fileNameId = "com.owncloud.android:id/Filename";
        final String textViewXpath =
                "//android.widget.TextView[@resource-id=\"%s\" and @text=\"%s\"]";
        final String longClickGesture = "mobile: longClickGesture";
        final int durationMs = 2000;
        Log.log(Level.FINE, "Starting long press on element with text: " + text);
        WebElement element = driver.findElement(AppiumBy.xpath(
                String.format(textViewXpath, fileNameId, text)));
        driver.executeScript(longClickGesture, Map.of("elementId", ((RemoteWebElement) element).getId(),
                        "duration", durationMs));
    }

    public void tap(int X, int Y) {
        final String fingerName = "finger";
        final int pointerMoveDurationSeconds = 1;
        Log.log(Level.FINE, "Starts: tap on X: " + X + ", Y: " + Y);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, fingerName);
        Sequence tapSeq = new Sequence(finger, 1);
        tapSeq.addAction(finger.createPointerMove(Duration.ofSeconds(pointerMoveDurationSeconds),
                PointerInput.Origin.viewport(), X, Y)).addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(tapSeq));
    }

    /* Browsing methods used in several pages */

    public void browseInto(String folderName) {
        Log.log(Level.FINE, "Starts: browse to " + folderName);
        findUIAutomatorSubText(folderName).click();
    }

    public void browseRoot() {
        Log.log(Level.FINE, "Starts: browse to root");
        toRoot.click();
    }

    public void browseToFolder(String path) {
        if (ROOT_PATH.equals(path)) {
            browseRoot();
        } else if (path.contains(ROOT_PATH)) {
            String[] route = path.split(ROOT_PATH);
            for (String folder : route) {
                Log.log(Level.FINE, "browsing to " + folder);
                browseInto(folder);
            }
        } else {
            browseInto(path);
        }
    }

    public String browseToFile(String path) {
        String[] route = path.split(ROOT_PATH);
        if (route.length > 0) {
            int i;
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

    /* Methods to help debugging */

    public static void takeScreenshot(String name) throws IOException {
        final String screenshotsFolder = "screenshots";
        final String screenshotExtension = ".png";
        String timestamp = SDF.format(new Timestamp(System.currentTimeMillis()).getTime());
        File screenShotFile = driver.getScreenshotAs(OutputType.FILE);
        File destinationFile = new File(screenshotsFolder + "/" + name + "_"
                        + timestamp + screenshotExtension);
        FileUtils.copyFile(screenShotFile, destinationFile);
        Log.log(Level.FINE, "Take screenshot " + name + " at: " + timestamp);
    }

    public static void startRecording() {
        final int bitRate = 2_000_000;
        final String videoSize = "360x640";
        try {
            AndroidStartScreenRecordingOptions androidStartScreenRecordingOptions =
                    new AndroidStartScreenRecordingOptions();
            androidStartScreenRecordingOptions.withBitRate(bitRate);
            androidStartScreenRecordingOptions.withVideoSize(videoSize);
            driver.startRecordingScreen(androidStartScreenRecordingOptions);
            recordingStarted = true;
        } catch (Exception e) {
            recordingStarted = false;
            Log.log(Level.FINE, "Screen recording not initiated. Error:  "
                    + e.getMessage());
        }
    }

    public static void stopRecording(String filename, String featureName, boolean failed) {
        final String videoFolder = "video";
        final String videoExtension = ".mp4";
        if (!recordingStarted) {
            return;
        }
        try {
            String base64String = driver.stopRecordingScreen();
            byte[] data = Base64.decodeBase64(base64String);
            if (failed) {
                createFeatureFolder(featureName);
                String timestamp = SDF.format(new Timestamp(System.currentTimeMillis()).getTime());
                String destinationPath = videoFolder + "/" + featureName + "/" + filename + "_"
                        + timestamp + videoExtension;
                Path path = Paths.get(destinationPath);
                try {
                    Files.write(path, data);
                } catch (IOException e) {
                    Log.log(Level.FINE, e.getMessage());
                }
            }
        } catch (WebDriverException wde) {
            Log.log(Level.FINE, "Error when stopping screen recording: " + wde.getMessage());
            recordingStarted = false;
        } catch (Exception e) {
            Log.log(Level.FINE, "Error saving video: " + e.getMessage());
            recordingStarted = false;
        }
    }

    private static void createFeatureFolder(String featureName) {
        final String videoFolder = "video";
        File folder = new File(videoFolder + "/" + featureName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
