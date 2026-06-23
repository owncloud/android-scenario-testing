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

import e2e.LocProperties;
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

    public void assertItemIsVisibleInsideFolder(String itemName, String targetFolder)
            throws IOException {
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
        world.detailsPage().closeShareSheet();
        assertEquals(itemName, world.detailsPage().getName());
        assertEquals(size, world.detailsPage().getSize());
        assertEquals(type, world.detailsPage().getType());
        world.detailsPage().backListFiles();
    }

    public void assertItemIsMarkedAsDownloaded(String itemName) {
        world.detailsPage().backListFiles();
        assertFileIsMarkedAsDownloaded(itemName);
    }

    public void assertItemAvailabilityOfflineStatus(String sense, String itemName) {
        if (isPositiveSense(sense)) {
            assertItemIsMarkedAsAvailableOffline(itemName);
        } else if (isNegativeSense(sense)) {
            assertItemIsMarkedAsUnavailableOffline(itemName);
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
                world.detailsPage().acceptImagePreviewDialogIfDisplayed();
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
        ArrayList<OCFile> serverFiles = world.filesAPI().listItems(path, "Alice");
        assertDisplayedListMatchesServer(path, serverFiles);
    }

    public void assertErrorMessageDisplayed(List<List<String>> listItems) {
        String error = listItems.get(0).get(0);
        Log.log(Level.FINE, "Error/Message to check: " + error);
        assertTrue(world.fileListPage().errorDisplayed(error));
    }

    public void assertFileContainsText(String text) {
        assertTrue(world.detailsPage().isTextDisplayedInPreview(text));
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
        Log.log(Level.FINE, "Conflict message to check: " + message);
        assertTrue(world.fileListPage().isConflictDisplayed());
        assertTrue(world.fileListPage().errorDisplayed(message));
    }

    public void assertItemIsOpenedInApp(String itemType, String itemName) {
        assertItemIsOpened(itemType, itemName);
    }

    public void assertBrowserIsDisplayed() {
        assertTrue(world.shortcutDialogPage().isBrowserOpen());
    }

    public void assertErrorPreviewingIsDisplayed() {
        assertTrue(world.detailsPage().isDamagedPreviewed());
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

    public void assertFileIsMarkedAsDownloaded(String path) {
        Log.log(Level.FINE, "Assert file is marked as downloaded: " + path);
        world.fileListPage().selectItemList(path);
        assertFalse(world.fileListPage().isDownloadActionVisible());
        assertTrue(world.fileListPage().isSyncActionVisible());
    }

    public void assertItemIsMarkedAsAvailableOffline(String path) {
        Log.log(Level.FINE, "Assert item is marked as available offline: " + path);
        world.fileListPage().refreshList();
        world.fileListPage().selectItemList(path);
        world.fileListPage().openMenuActions();
        assertFalse(world.fileListPage().isAvailableOfflineActionVisible());
    }

    public void assertItemIsMarkedAsUnavailableOffline(String path) {
        Log.log(Level.FINE, "Assert item is marked as unavailable offline: " + path);
        world.fileListPage().refreshList();
        world.fileListPage().selectItemList(path);
        world.fileListPage().openMenuActions();
        assertFalse(world.fileListPage().isUnavailableOfflineActionVisible());
    }

    public void assertDisplayedListMatchesServer(String path, ArrayList<OCFile> serverFiles) {
        Log.log(Level.FINE, "Assert displayed list matches server. Path: " + path);
        world.fileListPage().browseToFolder(path);
        String userName1 = LocProperties.getProperties().getProperty("userName1");
        boolean allVisibleItemsFound = serverFiles.stream()
            .filter(ocfile -> !ocfile.getName().equalsIgnoreCase(userName1)
                    && ocfile.getName().length() <= 15)
            .allMatch(ocfile -> isServerItemDisplayed(
                    ocfile.getName(),
                    serverFiles.size()
            ));
        assertTrue(allVisibleItemsFound);
    }

    public void assertItemIsOpened(String itemType, String itemName) {
        Log.log(Level.FINE, "Assert item is opened. Type: " + itemType
                + " - Name: " + itemName);
        switch (itemType) {
            case "file" -> assertFileIsOpened(itemName);
            case "folder" -> assertFolderIsOpened(itemName);
            default -> throw new IllegalArgumentException("Unsupported item type: " + itemType);
        }
    }

    private boolean isServerItemDisplayed(String itemName, int numberOfItems) {
        while (!world.fileListPage().isItemInList(itemName) && !world.fileListPage().isEndOfListDisplayed(numberOfItems)) {
            world.fileListPage().refreshList();
        }
        return world.fileListPage().isItemInList(itemName);
    }

    private void assertFileIsOpened(String itemName) {
        world.fileListPage().dismissChooserIfDisplayed();
        assertTrue(world.fileListPage().isFilePreviewOpened(itemName));
    }

    private void assertFolderIsOpened(String itemName) {
        assertTrue(world.fileListPage().isFolderOpened(itemName));
    }

    private String getLastPathToken(String itemName) {
        return itemName.substring(itemName.lastIndexOf('/') + 1);
    }

    private boolean isPositiveSense(String sense) {
        return sense == null || sense.isEmpty();
    }

    private boolean isNegativeSense(String sense) {
        return " not".equals(sense);
    }
}
