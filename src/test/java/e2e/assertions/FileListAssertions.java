/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.assertions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import e2e.model.OCFile;
import e2e.support.log.Log;
import e2e.world.World;

public class FileListAssertions {

    private static final String EXAMPLE_FILES_PATH = "src/test/resources/io/cucumber/example-files/";

    private final World world;

    public FileListAssertions(World world) {
        this.world = world;
    }

    public void assertItemIsVisibleInFileList(String itemName) throws IOException {
        world.fileListPage().refreshList();
        String visibleItemName = getLastPathToken(itemName);
        assertTrue(world.fileListPage().isItemInList(visibleItemName));
        assertTrue(world.filesAPI().itemExist(itemName));
    }

    public void assertItemIsNotVisibleInFileListAnymore(String itemName) throws IOException {
        assertFalse(world.fileListPage().isItemInList(itemName));
        assertFalse(world.filesAPI().itemExist(itemName));
    }

    public void assertItemVisibilityInLinksOfflineOrSharesList(String sense, String itemName) {
        if (isPositiveSense(sense)) {
            assertTrue(world.fileListPage().isItemInList(itemName));
        } else if (isNegativeSense(sense)) {
            assertFalse(world.fileListPage().isItemInList(itemName));
        }
    }

    public void assertItemIsVisibleInsideFolder(String itemName, String targetFolder) throws IOException {
        world.fileListPage().browseInto(targetFolder);
        assertTrue(world.fileListPage().isItemInList(itemName));
        assertTrue(world.filesAPI().itemExist(targetFolder + "/" + itemName));
    }

    public void assertItemIsVisibleInsideSpace(String itemName, String spaceName) {
        world.fileListPage().openSpaces();
        world.spacesPage().openSpace(spaceName);
        assertTrue(world.fileListPage().isItemInList(itemName));
    }

    public void assertItemIsVisibleInFileListAsOriginal(String itemName) throws IOException {
        assertItemIsVisibleInFileList(itemName);
    }

    public void assertDetailedInformation(String itemName, String type, String size) {
        world.detailsPage().removeShareSheet();
        assertEquals(itemName, world.detailsPage().getName());
        assertEquals(size, world.detailsPage().getSize());
        assertEquals(type, world.detailsPage().getType());
        world.detailsPage().backListFiles();
    }

    public void assertItemIsMarkedAsDownloaded(String itemName) {
        world.detailsPage().backListFiles();
        assertTrue(world.fileListPage().isFileMarkedAsDownloaded(itemName));
    }

    public void assertItemAvailabilityOfflineStatus(String sense, String itemName) {
        if (isPositiveSense(sense)) {
            assertTrue(world.fileListPage().isItemMarkedAsAvOffline(itemName));
        } else if (isNegativeSense(sense)) {
            assertTrue(world.fileListPage().isItemMarkedAsUnAvOffline(itemName));
        }
    }

    public void assertItemIsOpenedAndPreviewed() {
        assertTrue(world.detailsPage().isItemPreviewed());
        world.detailsPage().backListFiles();
    }

    public void assertFileTypeIsOpenedAndPreviewed(String type) {
        switch (type) {
            case "file" -> assertTrue(world.detailsPage().isItemPreviewed());
            case "audio" -> assertTrue(world.detailsPage().isAudioPreviewed());
            case "image" -> {
                assertTrue(world.detailsPage().isImagePreviewed());
                world.detailsPage().displayControls();
            }
            case "video" -> assertTrue(world.detailsPage().isVideoPreviewed());
            case "damaged" -> assertTrue(world.detailsPage().isDamagedPreviewed());
        }
    }

    public void assertFolderFileListMatchesServer(String path)
            throws IOException, ParserConfigurationException, SAXException {
        world.fileListPage().refreshList();
        world.fileListPage().browseToFolder(path);
        ArrayList<OCFile> listServer = world.filesAPI().listItems(path, "Alice");
        assertTrue(world.fileListPage().isDisplayedListCorrect(path, listServer));
    }

    public void assertErrorMessageOrItemIsDisplayed(List<List<String>> listItems) {
        String error = listItems.get(0).get(0);
        Log.log(Level.FINE, "Error/Message to check: " + error);
        assertTrue(world.fileListPage().errorDisplayed(error));
    }

    public void assertFileContainsText(String text) {
        assertTrue(world.detailsPage().isTextInFile(text));
    }

    public void assertShareSheetIsDisplayed(String itemName) {
        assertTrue(world.detailsPage().isShareSheetDisplayed(itemName));
    }

    public void assertCannotUnsetAsAvailableOffline(String itemName) {
        world.fileListPage().selectItemList(itemName);
        assertFalse(world.fileListPage().isOperationAvailable("Unset as available offline"));
    }

    public void assertConflictDialogWithMessageIsDisplayed(List<List<String>> listItems) {
        String message = listItems.get(0).get(0);
        Log.log(Level.FINE, "Message to check: " + message);
        assertTrue(world.fileListPage().isConflictDisplayed());
        assertTrue(world.fileListPage().errorDisplayed(message));
    }

    public void assertItemIsOpenedInApp(String itemType, String itemName) {
        assertTrue(world.fileListPage().isItemOpened(itemType, itemName));
    }

    public void assertBrowserIsDisplayed() {
        assertTrue(world.shortcutDialogPage().isBrowserOpen());
    }

    public void assertErrorPreviewingIsDisplayed() {
        assertTrue(world.detailsPage().isDamagedPreviewed());
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
                world.devicePage().pullList(currentPath);
            }
            expectedItemName = itemType.equals("file")
                    ? parts[parts.length - 1]
                    : parts[parts.length - 2];
        }
        String listFiles = world.devicePage().pullList(currentPath);
        Log.log(Level.FINE, "List of files before assertion: " + listFiles
                + ". ItemName: " + expectedItemName);
        if (isPositiveSense(sense)) {
            assertTrue(listFiles.contains(expectedItemName));
        } else if (isNegativeSense(sense)) {
            assertFalse(listFiles.contains(expectedItemName));
        }
    }

    public void assertConflictDialogIsDisplayed() {
        assertTrue(world.conflictPage().isConflictPageDisplayed());
    }

    public void assertFileIsStoredInAccount(String fileName) throws IOException {
        Log.log(Level.FINE, "Checking if file is stored in the account: " + fileName);
        assertTrue(world.filesAPI().itemExist(fileName));
        byte[] localFile = Files.readAllBytes(Paths.get(EXAMPLE_FILES_PATH + fileName));
        byte[] serverFile = world.filesAPI().getFile(fileName, "Alice");
        Log.log(Level.FINE, "Comparing local and server files: "
                + localFile.length + " vs " + serverFile.length);
        assertTrue(Arrays.equals(localFile, serverFile));
    }

    private String getLastPathToken(String itemName) {
        return itemName.substring(itemName.lastIndexOf('/') + 1);
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
