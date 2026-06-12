package e2e.assertions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.logging.Level;

import e2e.support.log.Log;
import e2e.world.World;

public class DeviceAssertions {

    private final World world;

    public DeviceAssertions(World world) {
        this.world = world;
    }

    public void assertItemStorageInDevice(String itemType, String itemName, String sense)
            throws IOException {
        String folderId = getPersonalFolderIdForCurrentBackend();
        if (!folderId.isEmpty()) {
            Log.log(Level.FINE, "ID from personal space: " + folderId);
        }
        String currentPath = folderId;
        String expectedItemName = itemName;
        if (itemName.contains("/")) {
            String[] parts = itemName.split("/");
            for (int i = 0; i < parts.length - 1; i++) {
                currentPath = currentPath + "/" + parts[i];
                world.deviceClient().pullList(currentPath);
            }
            expectedItemName = itemType.equals("file")
                    ? parts[parts.length - 1]
                    : parts[parts.length - 2];
        }
        String listFiles = world.deviceClient().pullList(currentPath);
        Log.log(Level.FINE, "List of files before assertion: " + listFiles
                + ". ItemName: " + expectedItemName);
        if (isPositiveSense(sense)) {
            assertTrue(listFiles.contains(expectedItemName));
        } else if (isNegativeSense(sense)) {
            assertFalse(listFiles.contains(expectedItemName));
        }
    }

    private String getPersonalFolderIdForCurrentBackend() throws IOException {
        if (!"oCIS".equals(System.getProperty("backend"))) {
            return "";
        }
        return world.graphAPI().getPersonal().getId().replace("$", "\\$");
    }

    private boolean isPositiveSense(String sense) {
        return sense == null || sense.isEmpty();
    }

    private boolean isNegativeSense(String sense) {
        return " not".equals(sense);
    }
}
