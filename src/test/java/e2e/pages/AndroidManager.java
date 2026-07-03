/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.logging.Level;

import e2e.LocProperties;
import e2e.support.log.Log;
import io.appium.java_client.android.AndroidDriver;

public class AndroidManager {

    private static final int IMPLICIT_WAIT_SECONDS = 7;
    private static final String APP_ACTIVITY = "com.owncloud.android.ui.activity.SplashActivity";

    private static AndroidDriver driver = null;

    private AndroidManager() {
    }

    public static synchronized AndroidDriver getDriver() {
        if (driver == null) {
            init();
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public static boolean hasDriver() {
        return driver != null;
    }

    private static void init() {
        File app = resolveAppFile();
        DesiredCapabilities capabilities = createCapabilities(app);
        driver = createDriver(capabilities);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_SECONDS));
        logDeviceInfo();
    }

    private static File resolveAppFile() {
        File rootPath = new File(System.getProperty("user.dir"));
        File appDir = new File(rootPath, "src/test/resources");
        return new File(appDir, LocProperties.getProperties().getProperty("apkName"));
    }

    private static AndroidDriver createDriver(DesiredCapabilities capabilities) {
        String appiumUrl = getAppiumUrl();
        try {
            Log.log(Level.FINE, "Appium driver located in: " + appiumUrl);
            return new AndroidDriver(new URL(appiumUrl), capabilities);
        } catch (MalformedURLException e) {
            Log.log(Level.SEVERE, "Driver could not be created: " + e.getMessage());
            throw new IllegalStateException("Invalid Appium URL: " + appiumUrl, e);
        }
    }

    private static DesiredCapabilities createCapabilities(File app) {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("appium:deviceName", "test");
        capabilities.setCapability("appium:app", app.getAbsolutePath());
        capabilities.setCapability("appium:platformName", "Android");
        capabilities.setCapability("appium:automationName", "UIAutomator2");
        capabilities.setCapability("appium:appPackage", getAppPackage());
        capabilities.setCapability("appium:appActivity", APP_ACTIVITY);
        capabilities.setCapability("appium:appWaitPackage", getAppPackage());
        capabilities.setCapability("appium:appWaitForLaunch", "true");
        capabilities.setCapability("appium:autoGrantPermissions", true);
        capabilities.setCapability("appium:unicodeKeyboard", true);
        capabilities.setCapability("appium:resetKeyboard", true);
        capabilities.setCapability("appium:disableWindowAnimation", true);
        capabilities.setCapability("appium:noReset", true);
        capabilities.setCapability("appium:newCommandTimeout", 60);
        capabilities.setCapability("appium:uiautomator2ServerLaunchTimeout", 60000);
        capabilities.setCapability("appium:adbExecTimeout", 60000);
        capabilities.setCapability("appium:androidInstallTimeout", 90000);
        String device = getDevice();
        if (device != null) {
            capabilities.setCapability("appium:udid", device);
        }
        return capabilities;
    }

    private static String getAppiumUrl() {
        String appiumUrl = System.getProperty("appium");
        if (appiumUrl != null && !appiumUrl.isEmpty()) {
            return appiumUrl;
        }
        return LocProperties.getProperties().getProperty("appiumURL");
    }

    private static String getAppPackage() {
        return LocProperties.getProperties().getProperty("appPackage");
    }

    private static String getDevice() {
        return System.getProperty("device");
    }

    private static void logDeviceInfo() {
        Log.log(Level.FINE, "Device: "
                + driver.getCapabilities().getCapability("deviceManufacturer")
                + " "
                + driver.getCapabilities().getCapability("deviceModel"));
        Log.log(Level.FINE, "Platform: "
                + driver.getCapabilities().getCapability("platformName")
                + " "
                + driver.getCapabilities().getCapability("platformVersion"));
        Log.log(Level.FINE, "API Level: "
                + driver.getCapabilities().getCapability("deviceApiLevel")
                + "\n");
        Log.log(Level.FINE, "Device UDID: " + getDevice());
    }
}
