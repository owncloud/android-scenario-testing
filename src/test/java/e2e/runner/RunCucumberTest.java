/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.util.logging.Level;

import e2e.LocProperties;
import e2e.pages.AndroidManager;
import e2e.support.log.Log;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        glue = "e2e",
        features = "src/test/resources/features"
)

public class RunCucumberTest {

    @BeforeClass
    public static void beforeclass() {
        Log.init();
        Log.log(Level.FINE, "START EXECUTION\n");
    }

    @AfterClass
    public static void afterClass() {
        try {
            if (AndroidManager.hasDriver()) {
                AndroidDriver driver = AndroidManager.getDriver();
                String appPackage = LocProperties.getProperties().getProperty("appPackage");
                if (appPackage != null && !appPackage.isBlank()) {
                    driver.removeApp(appPackage);
                }
                driver.removeApp("io.appium.settings");
            }
        } finally {
            AndroidManager.quitDriver();
            Log.log(Level.FINE, "END EXECUTION\n");
        }
    }
}
