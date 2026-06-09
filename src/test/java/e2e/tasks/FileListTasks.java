/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.tasks;

import java.io.IOException;
import java.util.logging.Level;

import e2e.LocProperties;
import e2e.support.log.Log;
import e2e.world.World;

public class FileListTasks {

    private final World world;
    public FileListTasks(World world) {
        this.world = world;
    }

    public void selectFabOption(String operation) {
        switch (operation) {
            case "Upload File" -> world.fileListPage().selectUploadFiles();
            case "Picture from Camera" -> world.fileListPage().selectUploadPicture();
            case "Create Folder" -> world.fileListPage().selectCreateFolder();
            case "Create Shortcut" -> world.fileListPage().selectCreateShortcut();
        }
    }

    public void modifyFileAddingText(String itemName, String modificationType, String text)
            throws IOException {
        Log.log(Level.FINE, "Modify file: " + itemName + " " + modificationType);
        if ("remotely".equalsIgnoreCase(modificationType)) {
            modifyFileRemotely(itemName, text);
        } else if ("locally".equalsIgnoreCase(modificationType)) {
            modifyFileLocally(itemName);
        }
    }

    private void modifyFileRemotely(String itemName, String text) throws IOException {
        world.filesAPI().pushFile(itemName, text, "Alice");
    }

    private void modifyFileLocally(String itemName) throws IOException {
        String itemPath = buildLocalSyncedFolderPath();
        world.devicePage().overwriteFile(itemName, itemPath);
    }

    private String buildLocalSyncedFolderPath() throws IOException {
        String folderId = isOcisBackend() ? world.graphAPI().getPersonal().getId() : "";
        String username = LocProperties.getProperties().getProperty("userName1");
        String server = System.getProperty("server")
                .replaceFirst("^https?://", "")
                .replace(":", "%3A");
        return username + "@" + server + "/" + folderId + "/";
    }

    private boolean isOcisBackend() {
        return "oCIS".equals(System.getProperty("backend"));
    }
}
