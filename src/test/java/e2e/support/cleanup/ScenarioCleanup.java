/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.support.cleanup;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import e2e.model.OCFile;
import e2e.support.log.Log;
import e2e.world.World;

public class ScenarioCleanup {

    private final World world;

    public ScenarioCleanup(World world) {
        this.world = world;
    }

    public void cleanUp() throws IOException, ParserConfigurationException, SAXException {
        Log.log(Level.FINE, "-------------------------------");
        Log.log(Level.FINE, "STARTS: CLEAN UP AFTER SCENARIO");
        Log.log(Level.FINE, "-------------------------------");
        cleanUsersRootFolders();
        cleanSpacesIfNeeded();
        cleanDevice();
    }

    private void cleanUsersRootFolders()
            throws IOException, ParserConfigurationException, SAXException {
        ArrayList<String> userNames = new ArrayList<>(Arrays.asList("Alice", "Bob"));
        for (String userToClean : userNames) {
            cleanRootFolder(userToClean);
            world.trashbinAPI().emptyTrashbin(userToClean);
        }
    }

    private void cleanRootFolder(String userToClean)
            throws IOException, ParserConfigurationException, SAXException {
        ArrayList<OCFile> filesRoot = world.filesAPI().listItems("", userToClean);
        for (OCFile file : filesRoot) {
            world.filesAPI().removeItem(file.getName(), userToClean);
        }
    }

    private void cleanSpacesIfNeeded() throws IOException {
        if ("oCIS".equals(System.getProperty("backend"))) {
            world.graphAPI().removeSpacesOfUser();
        }
    }

    private void cleanDevice() {
        world.deviceClient().cleanUpDevice();
        world.deviceClient().cleanUpTemp();
    }
}
