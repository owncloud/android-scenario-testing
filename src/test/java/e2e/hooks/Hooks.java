/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.hooks;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import e2e.LocProperties;
import e2e.pages.AndroidManager;
import e2e.pages.CommonPage;
import e2e.support.log.Log;
import e2e.world.World;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    private final World world;

    public Hooks(World world) {
        this.world = world;
    }

    @Before
    public void setup(Scenario scenario) {
        Log.log(Level.FINE, "START SCENARIO EXECUTION: " + scenario.getName());
        AndroidManager.getDriver().activateApp(
                LocProperties.getProperties().getProperty("appPackage"));
        world.fileListPage().setConnectionUp();
        CommonPage.startRecording();
    }

    @After
    public void tearDown(Scenario scenario)
            throws IOException, ParserConfigurationException, SAXException, InterruptedException {
        AndroidManager.getDriver().terminateApp(
                LocProperties.getProperties().getProperty("appPackage"));
        world.scenarioCleanup().cleanUp();
        String featurePath = scenario.getUri().toString();
        String featureName = Paths.get(featurePath).getFileName().toString()
                .replace(".feature", "");
        boolean saveVideo = scenario.isFailed();
        CommonPage.stopRecording(scenario.getName(), featureName, saveVideo);
        resetSettingsIfNeeded(scenario);
        Log.log(Level.FINE, "END SCENARIO EXECUTION: " + scenario.getName() + "\n\n");
    }

    private void resetSettingsIfNeeded(Scenario scenario)
            throws IOException, InterruptedException {
        if (scenario.getSourceTagNames().contains("@hidden")
                || scenario.getSourceTagNames().contains("@spaces")) {
            world.appPreferences().disableHiddenFiles();
            world.appPreferences().disableDisabledSpaces();
        }
    }
}
