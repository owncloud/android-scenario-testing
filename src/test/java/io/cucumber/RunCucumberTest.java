/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package io.cucumber;

import android.AndroidManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.util.logging.Level;

import io.appium.java_client.android.AndroidDriver;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import utils.LocProperties;
import utils.log.Log;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"})

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
