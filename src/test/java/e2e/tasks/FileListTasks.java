/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.tasks;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import e2e.LocProperties;
import e2e.model.OCFile;
import e2e.support.log.Log;
import e2e.world.World;

public class FileListTasks {

    private static final String LOCAL = "local";
    private static final String REMOTE = "remote";
    private static final String SERVER = "server";
    private static final String KEEP_BOTH = "keep both";
    private static final String REPLACE = "replace";
    private static final String SKIP = "skip";

    private final World world;

    public FileListTasks(World world) {
        this.world = world;
    }


    public void openHamburgerMenu() {
        Log.log(Level.FINE, "Open hamburger menu");
        world.fileListPage().openHamburgerMenu();
    }

    public void selectFabOption(String operation) {
        Log.log(Level.FINE, "Select FAB option: " + operation);
        switch (operation) {
            case "Upload File" -> world.fileListPage().selectUploadFiles();
            case "Picture from Camera" -> world.fileListPage().selectUploadPicture();
            case "Create Folder" -> world.fileListPage().selectCreateFolder();
            case "Create Shortcut" -> world.fileListPage().selectCreateShortcut();
        }
    }

    public void setItemAsAvailableOffline(String itemName) {
        Log.log(Level.FINE, "Set item as available offline: " + itemName);
        executeOperation("Set as available offline", itemName);
        world.fileListPage().closeSelectionMode();
    }

    public void unsetItemAsAvailableOffline(String itemName) {
        Log.log(Level.FINE, "Unset item as available offline: " + itemName);
        executeOperation("Unset as available offline", itemName);
        world.fileListPage().closeSelectionMode();
    }

    public void selectOperationOnItem(String operation, String itemName) {
        Log.log(Level.FINE, "Select operation " + operation + " on item " + itemName);
        if ("Download".equals(operation) || "open".equals(operation)) {
            downloadAction(itemName);
            return;
        }
        executeOperation(operation, itemName);
    }

    public void selectOperation(String operation) {
        Log.log(Level.FINE, "Select operation: " + operation);
        world.fileListPage().selectOperation(operation);
    }

    public void selectTargetFolder(String targetFolder) {
        Log.log(Level.FINE, "Select target folder: " + targetFolder);
        world.folderPickerPage().selectFolder(targetFolder);
        world.folderPickerPage().accept();
    }

    public void selectSpace(String spaceName) {
        Log.log(Level.FINE, "Select space: " + spaceName);
        if (isOcisBackend()) {
            world.folderPickerPage().selectSpace(spaceName);
        }
    }

    public void createFolderInFolderPicker(String targetFolder) {
        Log.log(Level.FINE, "Create folder in folder picker: " + targetFolder);
        world.folderPickerPage().createFolder();
        world.inputNamePage().setItemName(targetFolder);
    }

    public void refreshList() {
        Log.log(Level.FINE, "Refresh list");
        world.fileListPage().refreshList();
    }

    public void acceptDeletion(String deletionType) {
        Log.log(Level.FINE, "Accept deletion: " + deletionType);
        switch (deletionType) {
            case REMOTE -> world.removeDialogPage().removeAll();
            case LOCAL -> world.removeDialogPage().onlyLocal();
        }
    }

    public void deleteItemRemotely(String fileName) throws IOException {
        Log.log(Level.FINE, "Delete item remotely: " + fileName);
        world.filesAPI().removeItem(fileName, "Alice");
        world.fileListPage().refreshList();
    }

    public void setNewName(String itemName) {
        Log.log(Level.FINE, "Set new name: " + itemName);
        world.inputNamePage().setItemName(itemName);
    }

    public void openLinkShortcut() {
        Log.log(Level.FINE, "Open public link shortcut");
        world.fileListPage().openLinkShortcut();
        world.fileListPage().refreshList();
    }

    public void openAvailableOfflineShortcut() {
        Log.log(Level.FINE, "Open available offline shortcut");
        world.fileListPage().openAvOffShortcut();
        world.fileListPage().refreshList();
    }

    public void browseInto(String path) {
        Log.log(Level.FINE, "Browse into: " + path);
        world.fileListPage().refreshList();
        world.fileListPage().browseToFolder(path);
    }

    public void browseToRootFolder() {
        Log.log(Level.FINE, "Browse to root folder");
        world.fileListPage().browseRoot();
    }

    public void downloadFromThumbnail() {
        Log.log(Level.FINE, "Download from thumbnail");
        world.detailsPage().tapThumbnail();
    }

    public void closePreview() {
        Log.log(Level.FINE, "Close preview");
        world.detailsPage().backListFiles();
    }

    public void fixNameConflict(String conflictFix) {
        Log.log(Level.FINE, "Fix name conflict with: " + conflictFix);
        fixConflict(conflictFix);
    }

    public void fixContentConflict(String conflictFix) {
        Log.log(Level.FINE, "Fix content conflict with: " + conflictFix);
        switch (conflictFix) {
            case LOCAL -> world.conflictPage().selectLocalVersion();
            case SERVER -> world.conflictPage().selectServerVersion();
            case KEEP_BOTH -> world.conflictPage().selectBothVersions();
        }
    }

    public void longPressItem(String itemName) {
        Log.log(Level.FINE, "Long press item: " + itemName);
        world.fileListPage().refreshList();
        world.fileListPage().longPress(itemName);
    }

    public void multiSelectItems(List<List<String>> listItems) {
        for (List<String> row : listItems) {
            String name = row.get(0);
            Log.log(Level.FINE, "Multiselect item: " + name);
            world.fileListPage().selectItem(name);
        }
    }

    public void openPrivateLinkPointingTo(String filePath, String scheme)
            throws IOException, ParserConfigurationException, SAXException {
        Log.log(Level.FINE, "Open private link pointing to: " + filePath
                + " with scheme: " + scheme);
        OCFile item = world.filesAPI().listItems(filePath, "Alice").get(0);
        openPrivateLink(scheme, item.getPrivateLink());
    }

    public void openPrivateLinkPointingToSharedItem(String fileName, String scheme)
            throws IOException, ParserConfigurationException, SAXException {
        Log.log(Level.FINE, "Open private link pointing to shared item: " + fileName
                + " with scheme: " + scheme);
        OCFile item = findSharedItemByName(fileName);
        openPrivateLink(scheme, item.getPrivateLink());
    }

    public void openPrivateLinkPointingToNonExistingItem() {
        Log.log(Level.FINE, "Open private link pointing to non-existing item");
        openFakePrivateLink();
    }

    public void openShareShortcut() {
        Log.log(Level.FINE, "Open share shortcut");
        openFakePrivateLink();
    }

    public void takePicture() {
        Log.log(Level.FINE, "Take picture");
        world.cameraPage().waitUntilCameraIsDisplayed();
        world.cameraPage().tapShutterButton();
        world.cameraPage().confirmPicture();
    }

    public void createWebShortcut(Map<String, String> fields) {
        String name = getRequiredValue(fields, "name");
        String url = getRequiredValue(fields, "url");
        Log.log(Level.FINE, "Create web shortcut. Name: " + name + " - URL: " + url);
        world.shortcutDialogPage().typeURLName(name, url);
        world.shortcutDialogPage().submitShortcut();
    }

    public void openShortcut(String name) {
        Log.log(Level.FINE, "Open shortcut: " + name + ".url");
        downloadAction(name + ".url");
    }

    public void openLink() {
        Log.log(Level.FINE, "Open link");
        world.shortcutDialogPage().openShortcut();
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

    public void executeOperation(String operation, String itemName) {
        Log.log(Level.FINE, "Execute operation: " + operation + " on item: " + itemName);
        world.fileListPage().refreshList();
        world.fileListPage().selectItemList(itemName);
        world.fileListPage().selectOperation(operation);
    }

    public void downloadAction(String itemName) {
        Log.log(Level.FINE, "Download/open item action: " + itemName);
        if (!world.fileListPage().isItemInList(itemName)) {
            Log.log(Level.FINE, "Item not visible. Refreshing list: " + itemName);
            world.fileListPage().refreshList();
        }
        world.fileListPage().selectItem(itemName);
    }

    public void fixConflict(String option) {
        Log.log(Level.FINE, "Fix conflict using option: " + option);
        switch (option) {
            case REPLACE -> world.fileListPage().tapReplaceConflict();
            case KEEP_BOTH -> world.fileListPage().tapKeepBothConflict();
            case SKIP -> world.fileListPage().tapSkipConflict();
        }
    }

    public void openPrivateLink(String scheme, String privateLink) {
        String linkToOpen = buildPrivateLink(scheme, privateLink);
        world.fileListPage().openUrl(linkToOpen);
    }

    public void openFakePrivateLink() {
        final String fakeScheme = "owncloud";
        final String fakeFilePath = "/f/11111111111";
        String server = System.getProperty("server");
        String originalScheme = getScheme(server);
        String fakeUrl = server.replace(originalScheme, fakeScheme) + fakeFilePath;
        Log.log(Level.FINE, "Open fake private link: " + fakeUrl);
        world.fileListPage().openUrl(fakeUrl);
    }

    private String buildPrivateLink(String scheme, String privateLink) {
        final String escapedDollar = "\\$";
        String originalScheme = getScheme(privateLink);
        return privateLink.replace(originalScheme, scheme)
                .replace("$", escapedDollar);
    }

    private String getScheme(String url) {
        final String schemeSeparator = "://";
        return url.split(schemeSeparator)[0];
    }

    public void navigateTargetFolder() {
        Log.log(Level.FINE, "Navigate to target folder");
        world.fileListPage().navigateToTargetFolder();
    }

    private void modifyFileRemotely(String itemName, String text) throws IOException {
        world.filesAPI().pushFile(itemName, text, "Alice");
    }

    private void modifyFileLocally(String itemName) throws IOException {
        String itemPath = buildLocalSyncedFolderPath();
        world.deviceClient().overwriteFile(itemName, itemPath);
    }

    private String buildLocalSyncedFolderPath() throws IOException {
        String folderId = isOcisBackend() ? world.graphAPI().getPersonal().getId() : "";
        String username = LocProperties.getProperties().getProperty("userName1");
        String server = System.getProperty("server")
                .replaceFirst("^https?://", "")
                .replace(":", "%3A");
        return username + "@" + server + "/" + folderId + "/";
    }

    private OCFile findSharedItemByName(String fileName)
            throws IOException, ParserConfigurationException, SAXException {
        ArrayList<OCFile> listShared = world.filesAPI().listShared();
        for (OCFile ocFile : listShared) {
            if (ocFile.getName().equals(fileName)) {
                return ocFile;
            }
        }
        return null;
    }

    private String getRequiredValue(Map<String, String> fields, String key) {
        String value = fields.get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: " + key);
        }
        return value.trim();
    }

    private boolean isOcisBackend() {
        return "oCIS".equals(System.getProperty("backend"));
    }
}
