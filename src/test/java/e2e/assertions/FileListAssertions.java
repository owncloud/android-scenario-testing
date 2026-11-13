/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.assertions;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(world.fileListPage().isItemInList(visibleItemName))
                .as("'%s' should be visible in file list", visibleItemName)
                .isTrue();
        assertThat(world.filesAPI().itemExist(itemName))
                .as("'%s' should exist on server", itemName)
                .isTrue();
    }

    public void assertItemIsNotVisibleInFileListAnymore(String itemName) throws IOException {
        assertThat(world.fileListPage().isItemInList(itemName))
                .as("'%s' should not be visible in file list", itemName)
                .isFalse();
        assertThat(world.filesAPI().itemExist(itemName))
                .as("'%s' should not exist on server", itemName)
                .isFalse();
    }

    public void assertItemVisibilityInLinksOfflineOrSharesList(String sense, String itemName) {
        if (isPositiveSense(sense)) {
            assertThat(world.fileListPage().isItemInList(itemName))
                    .as("'%s' should be visible in list", itemName)
                    .isTrue();
        } else if (isNegativeSense(sense)) {
            assertThat(world.fileListPage().isItemInList(itemName))
                    .as("'%s' should not be visible in list", itemName)
                    .isFalse();
        }
    }

    public void assertItemIsVisibleInsideFolder(String itemName, String targetFolder)
            throws IOException {
        assertThat(world.fileListPage().isItemInList(itemName))
                .as("'%s' should be visible inside folder '%s'", itemName, targetFolder)
                .isTrue();
        assertThat(world.filesAPI().itemExist(targetFolder + "/" + itemName))
                .as("'%s/%s' should exist on server", targetFolder, itemName)
                .isTrue();
    }

    public void assertItemIsVisibleInsideSpace(String itemName, String spaceName) {
        assertThat(world.fileListPage().isItemInList(itemName))
                .as("'%s' should be visible inside space '%s'", itemName, spaceName)
                .isTrue();
    }

    public void assertItemIsVisibleInFileListAsOriginal(String itemName) throws IOException {
        assertItemIsVisibleInFileList(itemName);
    }

    public void assertDetailedInformation(String itemName, String type, String size) {
        world.detailsPage().closeShareSheet();
        assertThat(world.detailsPage().getName())
                .as("file name should be '%s'", itemName)
                .isEqualTo(itemName);
        assertThat(world.detailsPage().getSize())
                .as("file size should be '%s'", size)
                .isEqualTo(size);
        assertThat(world.detailsPage().getType())
                .as("file type should be '%s'", type)
                .isEqualTo(type);
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
        assertThat(world.detailsPage().isItemPreviewed())
                .as("item should be previewed")
                .isTrue();
        world.detailsPage().backListFiles();
    }

    public void assertFileTypeIsOpenedAndPreviewed(String type) {
        switch (type) {
            case "file" -> assertThat(world.detailsPage().isItemPreviewed())
                    .as("file should be previewed")
                    .isTrue();
            case "audio" -> assertThat(world.detailsPage().isAudioPreviewed())
                    .as("audio should be previewed")
                    .isTrue();
            case "image" -> {
                world.detailsPage().acceptImagePreviewDialogIfDisplayed();
                assertThat(world.detailsPage().isImagePreviewed())
                        .as("image should be previewed")
                        .isTrue();
                world.detailsPage().displayControls();
            }
            case "video" -> assertThat(world.detailsPage().isVideoPreviewed())
                    .as("video should be previewed")
                    .isTrue();
            case "damaged" -> assertThat(world.detailsPage().isDamagedPreviewed())
                    .as("damaged file preview should be shown")
                    .isTrue();
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
        assertThat(world.fileListPage().errorDisplayed(error))
                .as("error message '%s' should be displayed", error)
                .isTrue();
    }

    public void assertFileContainsText(String text) {
        assertThat(world.detailsPage().isTextDisplayedInPreview(text))
                .as("text '%s' should be displayed in preview", text)
                .isTrue();
    }

    public void assertShareSheetIsDisplayed(String itemName) {
        assertThat(world.detailsPage().isShareSheetDisplayed(itemName))
                .as("share sheet should be displayed for '%s'", itemName)
                .isTrue();
    }

    public void assertCannotUnsetAsAvailableOffline(String itemName) {
        world.fileListPage().selectItemList(itemName);
        assertThat(world.fileListPage().isOperationAvailable("Unset as available offline"))
                .as("'Unset as available offline' should not be available")
                .isFalse();
    }

    public void assertConflictDialogWithMessageIsDisplayed(List<List<String>> listItems) {
        String message = listItems.get(0).get(0);
        Log.log(Level.FINE, "Conflict message to check: " + message);
        assertThat(world.fileListPage().isConflictDisplayed())
                .as("conflict dialog should be displayed")
                .isTrue();
        assertThat(world.fileListPage().errorDisplayed(message))
                .as("conflict message '%s' should be displayed", message)
                .isTrue();
    }

    public void assertItemIsOpenedInApp(String itemType, String itemName) {
        assertItemIsOpened(itemType, itemName);
    }

    public void assertBrowserIsDisplayed() {
        assertThat(world.shortcutDialogPage().isBrowserOpen())
                .as("browser should be open")
                .isTrue();
    }

    public void assertErrorPreviewingIsDisplayed() {
        assertThat(world.detailsPage().isDamagedPreviewed())
                .as("damaged file preview should be shown")
                .isTrue();
    }

    public void assertConflictDialogIsDisplayed() {
        assertThat(world.conflictPage().isConflictPageDisplayed())
                .as("conflict page should be displayed")
                .isTrue();
    }

    public void assertFileIsStoredInAccount(String fileName) throws IOException {
        Log.log(Level.FINE, "Checking if file is stored in the account: " + fileName);
        assertThat(world.filesAPI().itemExist(fileName))
                .as("'%s' should exist on server", fileName)
                .isTrue();
        byte[] localFile = Files.readAllBytes(Paths.get(EXAMPLE_FILES_PATH + fileName));
        byte[] serverFile = world.filesAPI().getFile(fileName, "Alice");
        Log.log(Level.FINE, "Comparing local and server files: "
                + localFile.length + " vs " + serverFile.length);
        assertThat(Arrays.equals(localFile, serverFile))
                .as("local and server files should be identical for '%s'", fileName)
                .isTrue();
    }

    public void assertFileIsMarkedAsDownloaded(String path) {
        Log.log(Level.FINE, "Assert file is marked as downloaded: " + path);
        world.fileListPage().selectItemList(path);
        assertThat(world.fileListPage().isDownloadActionVisible())
                .as("download action should not be visible for a downloaded file")
                .isFalse();
        assertThat(world.fileListPage().isSyncActionVisible())
                .as("sync action should be visible for a downloaded file")
                .isTrue();
    }

    public void assertItemIsMarkedAsAvailableOffline(String path) {
        Log.log(Level.FINE, "Assert item is marked as available offline: " + path);
        world.fileListPage().refreshList();
        world.fileListPage().selectItemList(path);
        world.fileListPage().openMenuActions();
        assertThat(world.fileListPage().isAvailableOfflineActionVisible())
                .as("'available offline' action should not be visible for item already marked offline")
                .isFalse();
    }

    public void assertItemIsMarkedAsUnavailableOffline(String path) {
        Log.log(Level.FINE, "Assert item is marked as unavailable offline: " + path);
        world.fileListPage().refreshList();
        world.fileListPage().selectItemList(path);
        world.fileListPage().openMenuActions();
        assertThat(world.fileListPage().isUnavailableOfflineActionVisible())
                .as("'unavailable offline' action should not be visible")
                .isFalse();
    }

    public void assertDisplayedListMatchesServer(String path, ArrayList<OCFile> serverFiles) {
        Log.log(Level.FINE, "Assert displayed list matches server. Path: " + path);
        String userName1 = LocProperties.getProperties().getProperty("userName1");
        boolean allVisibleItemsFound = serverFiles.stream()
            .filter(ocfile -> !ocfile.getName().equalsIgnoreCase(userName1)
                    && ocfile.getName().length() <= 15)
            .allMatch(ocfile -> isServerItemDisplayed(
                    ocfile.getName(),
                    serverFiles.size()
            ));
        assertThat(allVisibleItemsFound)
                .as("all server items should be visible in file list for path '%s'", path)
                .isTrue();
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
        assertThat(world.fileListPage().isFilePreviewOpened(itemName))
                .as("file preview should be open for '%s'", itemName)
                .isTrue();
    }

    private void assertFolderIsOpened(String itemName) {
        assertThat(world.fileListPage().isFolderOpened(itemName))
                .as("folder '%s' should be open", itemName)
                .isTrue();
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
