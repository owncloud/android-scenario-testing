/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package io.cucumber;

import android.AndroidManager;
import android.CommonPage;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.LocProperties;
import utils.entities.OCFile;
import utils.log.Log;

public class Hooks {

    private World world;

    public Hooks(World world) {
        this.world = world;
    }

    private String base = "adb shell content call --uri content://com.owncloud.android.test.preferences " +
            "--method set_boolean --extra value:b:false --arg ";

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
        cleanUp();
        String featurePath = scenario.getUri().toString();
        String featureName = Paths.get(featurePath).getFileName().toString()
                .replace(".feature", "");
        boolean saveVideo = scenario.isFailed();
        CommonPage.stopRecording(scenario.getName(), featureName, saveVideo);
        //Disable settings
        if (scenario.getSourceTagNames().contains("@hidden") || scenario.getSourceTagNames().contains("@spaces")) {
            Runtime.getRuntime().exec(base + "show_hidden_files").waitFor();
            Runtime.getRuntime().exec(base + "show_disabled_spaces").waitFor();
        }
        Log.log(Level.FINE, "END SCENARIO EXECUTION: " + scenario.getName() + "\n\n");
    }

    private void cleanUp()
            throws IOException, ParserConfigurationException, SAXException {
        Log.log(Level.FINE, "-------------------------------");
        Log.log(Level.FINE, "STARTS: CLEAN UP AFTER SCENARIO");
        Log.log(Level.FINE, "-------------------------------");
        //First, remove leftovers in root folder for every user
        ArrayList<String> userNames = new ArrayList<>(Arrays.asList("Alice", "Bob"));
        for (String userToClean: userNames) {
            ArrayList<OCFile> filesRoot = world.filesAPI().listItems("", userToClean);
            for (OCFile iterator : filesRoot) {
                world.filesAPI().removeItem(iterator.getName(), userToClean);
            }
            //Empty trashbins
            world.trashbinAPI().emptyTrashbin(userToClean);
        }
        //Remove spaces
        if (System.getProperty("backend").equals("oCIS")) {
            world.graphAPI().removeSpacesOfUser();
        }
        //Remove owncloud folder from device
        world.devicePage().cleanUpDevice();
        //Remove tmp folder from device
        world.devicePage().cleanUpTemp();
    }
}
