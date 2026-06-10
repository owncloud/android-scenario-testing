/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.preconditions;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import e2e.support.log.Log;
import e2e.world.World;

public class FileListPreconditions {

    private final World world;

    public FileListPreconditions(World world) {
        this.world = world;
    }

    public void itemsExistInAccount(String userName, List<Map<String, String>> rows)
            throws IOException {
        for (Map<String, String> row : rows) {
            String type = row.get("type");
            String name = row.get("name");
            Log.log(Level.FINE, "Preparing item in account. Type: " + type + " - Name: " + name);
            if (!world.filesAPI().itemExist(name)) {
                createItem(type, name, userName);
            }
        }
    }

    public void folderContainsFiles(String folderName, int numberOfFiles)
            throws IOException {
        if (!world.filesAPI().itemExist(folderName)) {
            world.filesAPI().createFolder(folderName, "alice");
        }

        for (int i = 0; i < numberOfFiles; i++) {
            String fileName = folderName + "/file_" + i + ".txt";

            Log.log(Level.FINE, "Creating file in folder precondition: " + fileName);

            world.filesAPI().pushFile(fileName, "alice");
        }
    }

    private void createItem(String type, String name, String userName)
            throws IOException {
        switch (type) {
            case "folder", "item" -> world.filesAPI().createFolder(name, userName);
            case "file" -> world.filesAPI().pushFile(name, userName);
            case "image" -> world.filesAPI().pushFileByMime(name, "image/jpg");
            case "audio" -> world.filesAPI().pushFileByMime(name, "audio/mpeg3");
            case "video" -> world.filesAPI().pushFileByMime(name, "video/mp4");
            case "shortcut" -> world.filesAPI().pushFileByMime(name, "text/uri-list");
            case "damaged" -> world.filesAPI().pushFileByMime(name, "image/png");
        }
    }
}
