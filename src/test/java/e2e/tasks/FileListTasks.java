/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.tasks;

import org.xml.sax.SAXException;

import e2e.LocProperties;
import e2e.model.OCFile;
import e2e.support.log.Log;
import e2e.world.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

public class FileListTasks {

    private static final String DEFAULT_USER = "Alice";

    private final World world;

    public FileListTasks(World world) {
        this.world = world;
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
        world.fileListPage().executeOperation("Set as available offline", itemName);
        world.fileListPage().closeSelectionMode();
    }

    public void unsetItemAsAvailableOffline(String itemName) {
        Log.log(Level.FINE, "Unset item as available offline: " + itemName);
        world.fileListPage().executeOperation("Unset as available offline", itemName);
        world.fileListPage().closeSelectionMode();
    }

    public void selectOperationOnItem(String operation, String itemName) {
        Log.log(Level.FINE, "Select operation " + operation + " on item " + itemName);
        if ("Download".equals(operation) || "open".equals(operation)) {
            world.fileListPage().downloadAction(itemName);
        } else {
            world.fileListPage().executeOperation(operation, itemName);
        }
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
        if ("remote".equals(deletionType)) {
            world.removeDialogPage().removeAll();
        } else if ("local".equals(deletionType)) {
            world.removeDialogPage().onlyLocal();
        } else {
            throw new IllegalArgumentException("Unsupported deletion type: " + deletionType);
        }
    }

    public void deleteItemRemotely(String fileName) throws IOException {
        Log.log(Level.FINE, "Delete item remotely: " + fileName);
        world.filesAPI().removeItem(fileName, DEFAULT_USER);
        world.fileListPage().refreshList();
    }

    public void setNewName(String itemName) {
        Log.log(Level.FINE, "Set new name: " + itemName);
        world.inputNamePage().setItemName(itemName);
    }

    public void openPublicLinkShortcut() {
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
        world.detailsPage().downloadFromThumbnail();
    }

    public void closePreview() {
        Log.log(Level.FINE, "Close preview");
        world.detailsPage().backListFiles();
    }

    public void fixNameConflict(String conflictFix) {
        Log.log(Level.FINE, "Fix name conflict with: " + conflictFix);
        world.fileListPage().fixConflict(conflictFix);
    }

    public void fixContentConflict(String conflictFix) {
        Log.log(Level.FINE, "Fix content conflict with: " + conflictFix);
        world.conflictPage().fixConflict(conflictFix);
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
        OCFile item = world.filesAPI().listItems(filePath, DEFAULT_USER).get(0);
        String privateLink = world.fileListPage().getPrivateLink(
                scheme,
                item.getPrivateLink()
        );
        world.fileListPage().openPrivateLink(privateLink);
    }

    public void openPrivateLinkPointingToSharedItem(String fileName, String scheme)
            throws IOException, ParserConfigurationException, SAXException {
        Log.log(Level.FINE, "Open private link pointing to shared item: " + fileName
                + " with scheme: " + scheme);
        OCFile item = findSharedItemByName(fileName);
        String privateLink = world.fileListPage().getPrivateLink(scheme, item.getPrivateLink());
        world.fileListPage().openPrivateLink(privateLink);
    }

    public void openPrivateLinkPointingToNonExistingItem() {
        Log.log(Level.FINE, "Open private link pointing to non-existing item");
        world.fileListPage().openFakePrivateLink();
    }

    public void openShareShortcut() {
        Log.log(Level.FINE, "Open share shortcut");
        world.fileListPage().openFakePrivateLink();
    }

    public void takePicture() {
        Log.log(Level.FINE, "Take picture");
        world.cameraPage().takePicture();
    }

    public void createWebShortcut(Map<String, String> fields) {
        String name = fields.get("name");
        String url = fields.get("url");
        Log.log(Level.FINE, "Create web shortcut. Name: " + name + " - URL: " + url);
        world.shortcutDialogPage().typeURLName(name, url);
        world.shortcutDialogPage().submitShortcut();
    }

    public void openShortcut(String name) {
        Log.log(Level.FINE, "Open shortcut: " + name + ".url");
        world.fileListPage().downloadAction(name + ".url");
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
        } else {
            throw new IllegalArgumentException("Unsupported modification type: " + modificationType);
        }
    }

    private void modifyFileRemotely(String itemName, String text) throws IOException {
        world.filesAPI().pushFile(itemName, text, DEFAULT_USER);
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

    private boolean isOcisBackend() {
        return "oCIS".equals(System.getProperty("backend"));
    }
}
