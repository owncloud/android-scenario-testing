/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package io.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.LocProperties;
import utils.entities.OCFile;
import utils.log.Log;
import utils.log.StepLogger;

public class FileListSteps {

    private World world;

    public FileListSteps(World world) {
        this.world = world;
    }

    @ParameterType("server|keep both|local|replace")
    public String conflictFix(String type) {
        return type;
    }

    @ParameterType("file|audio|image|video|damaged|folder")
    public String fileType(String type) {
        return type;
    }

    @ParameterType("(?: not)?")
    public String typePosNeg(String type) {
        return type == null ? "" : type;
    }

    @ParameterType("Upload File|Picture from Camera|Create Shortcut|Create Folder")
    public String optionsFab(String type) {
        return type;
    }

    @ParameterType("remotely|locally")
    public String modificationType(String type) {
        return type;
    }


    @Given("the following items have been created in {word} account")
    public void items_have_been_created_in_account(String userName, DataTable table) throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        List<List<String>> listItems = table.asLists();
        for (List<String> rows : listItems) {
            String type = rows.get(0);
            String name = rows.get(1);
            Log.log(Level.FINE, type + " " + name);
            if (!world.filesAPI.itemExist(name)) {
                switch (type) {
                    case "folder", "item" -> world.filesAPI.createFolder(name, userName);
                    case "file" -> world.filesAPI.pushFile(name, userName);
                    case "image" -> world.filesAPI.pushFileByMime(name, "image/jpg");
                    case "audio" -> world.filesAPI.pushFileByMime(name, "audio/mpeg3");
                    case "video" -> world.filesAPI.pushFileByMime(name, "video/mp4");
                    case "shortcut" -> world.filesAPI.pushFileByMime(name, "text/uri-list");
                    case "damaged" -> world.filesAPI.pushFileByMime(name, "image/png");
                }
            }
        }
    }

    @Given("the folder {word} contains {int} files")
    public void folder_contains_files(String folderName, int files) throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        if (!world.filesAPI.itemExist(folderName)) {
            world.filesAPI.createFolder(folderName, "alice");
        }
        for (int i = 0; i < files; i++) {
            world.filesAPI.pushFile(folderName + "/file_" + i + ".txt", "Alice");
        }
    }

    @Given("the device has no connection")
    public void theDeviceHasNoConnection() {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.setConnectionDown();
    }

    @When("Alice selects to set as av.offline the item {word}")
    public void user_selects_to_set_as_avoffline_item(String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.executeOperation("Set as available offline", itemName);
        world.fileListPage.closeSelectionMode();
    }

    @When("Alice selects to unset as av.offline the item {word}")
    public void user_selects_to_unset_as_unavoffline_item(String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.executeOperation("Unset as available offline", itemName);
        world.fileListPage.closeSelectionMode();
    }

    @When("Alice selects to {word} the {itemtype} {word}")
    public void user_selects_item_to_some_operation(String operation, String type, String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        if (operation.equals("Download") || operation.equals("open")) {
            world.fileListPage.downloadAction(itemName);
        } else {
            world.fileListPage.executeOperation(operation, itemName);
        }
    }

    @When("Alice selects to {word}")
    public void user_selects_item_to_some_operation_in_multi(String operation) {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.selectOperation(operation);
    }

    @When("Alice selects {word} as target folder")
    public void user_selects_target_folder(String targetFolder) {
        StepLogger.logCurrentStep(Level.FINE);
        world.folderPickerPage.selectFolder(targetFolder);
        world.folderPickerPage.accept();
    }

    @When("Alice selects {word} as space")
    public void user_selects_space(String spaceName) {
        StepLogger.logCurrentStep(Level.FINE);
        if (System.getProperty("backend").equals("oCIS")) {
            world.folderPickerPage.selectSpace(spaceName);
        }
    }

    @When("Alice creates new folder {word} in the folder picker")
    public void user_creates_folder_picker(String targetFolder) {
        StepLogger.logCurrentStep(Level.FINE);
        world.folderPickerPage.createFolder();
        world.inputNamePage.setItemName(targetFolder);
    }

    @When("Alice selects the option {optionsFab}")
    public void user_selects_option_upload(String operation) {
        StepLogger.logCurrentStep(Level.FINE);
        switch (operation) {
            case "Upload File" -> world.fileListPage.selectUploadFiles();
            case "Picture from Camera" -> world.fileListPage.selectUploadPicture();
            case "Create Folder" -> world.fileListPage.selectCreateFolder();
            case "Create Shortcut" -> world.fileListPage.selectCreateShortcut();
        }
    }

    @When("Alice refreshes the list")
    public void user_refreshes_list() {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.refreshList();
    }

    @When("Alice accepts the {word} deletion of {word}")
    public void user_accepts_deletion(String deletionType, String type) {
        StepLogger.logCurrentStep(Level.FINE);
        if ("remote".equals(deletionType)) {
            world.removeDialogPage.removeAll();
        } else if ("local".equals(deletionType)) {
            world.removeDialogPage.onlyLocal();
        }
    }

    @When("the {word} has been deleted remotely")
    public void item_has_been_deleted_remotely(String fileName)
            throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        world.filesAPI.removeItem(fileName, "Alice");
        world.fileListPage.refreshList();
    }

    @When("Alice sets {word} as new name")
    public void user_sets_new_name(String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.inputNamePage.setItemName(itemName);
    }

    @When("Alice opens the public link shortcut")
    public void user_opens_public_link_shortcut() {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.openLinkShortcut();
        world.fileListPage.refreshList();
    }

    @When("Alice opens the available offline shortcut")
    public void user_opens_av_offline_shortcut() {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.openAvOffhortcut();
        world.fileListPage.refreshList();
    }

    @When("Alice browses into {word}")
    public void user_browses_into_folder(String path) {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.refreshList();
        world.fileListPage.browseToFolder(path);
    }

    @When("Alice browses to root folder")
    public void user_browses_root_folder() {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.browseRoot();
    }

    @When("Alice clicks on the thumbnail")
    public void user_clicks_on_the_thumbnail() {
        StepLogger.logCurrentStep(Level.FINE);
        world.detailsPage.downloadFromThumbnail();
    }

    @When("file {word} is modified {modificationType} adding {word}")
    public void file_is_modified_adding_text(String itemName, String modificationType, String text)
            throws IOException {
        StepLogger.logCurrentStep(Level.FINE);
        Log.log(Level.FINE, "Modified file: " + itemName + " " + modificationType);
        if (modificationType.equals("remotely")) {
            world.filesAPI.pushFile(itemName, text, "Alice");
        } else if (modificationType.equals("locally")) {
            String folderId = System.getProperty("backend").equals("oCIS")
                    ? world.graphAPI.getPersonal().getId()
                    : "";
            String username = LocProperties.getProperties().getProperty("userName1");
            String server = System.getProperty("server")
                    .replaceFirst("^https?://", "")
                    .replace(":", "%3A" );
            String itemPath = username + "@" + server + "/" + folderId + "/";
            world.devicePage.overwriteFile(itemName, itemPath);
        }
    }

    @When("Alice closes the preview")
    public void user_closes_preview() {
        StepLogger.logCurrentStep(Level.FINE);
        world.detailsPage.backListFiles();
    }

    @When("Alice fixes the conflict with {conflictFix}")
    public void user_fixes_name_conflict(String conflictFix) {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.fixConflict(conflictFix);
    }

    @When("Alice fixes the conflict with {conflictFix} version")
    public void user_fixes_content_conflict(String conflictFix) {
        StepLogger.logCurrentStep(Level.FINE);
        world.conflictPage.fixConflict(conflictFix);
    }

    @When("Alice long presses over {word}")
    public void user_longpress_over_item(String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.refreshList();
        world.fileListPage.longPress(itemName);
    }

    @When("Alice multiselects the following items")
    public void user_selects_all_items(DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        List<List<String>> listItems = table.asLists();
        for (List<String> rows : listItems) {
            String name = rows.get(0);
            world.fileListPage.selectItem(name);
        }
    }

    @When("Alice opens a private link pointing to {word} with scheme {word}")
    public void open_private_link(String filePath, String scheme)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        OCFile item = world.filesAPI.listItems(filePath, "Alice").get(0);
        String privateLink = world.fileListPage.getPrivateLink(scheme, item.getPrivateLink());
        world.fileListPage.openPrivateLink(privateLink);
    }

    @When("Alice opens a private link pointing to shared {word} with scheme {word}")
    public void open_private_link_shared(String fileName, String scheme)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        ArrayList<OCFile> listShared = world.filesAPI.listShared();
        OCFile item = null;
        for (OCFile ocFile : listShared) {
            if (ocFile.getName().equals(fileName)) {
                item = ocFile;
            }
        }
        String privateLink = world.fileListPage.getPrivateLink(scheme, item.getPrivateLink());
        world.fileListPage.openPrivateLink(privateLink);
    }

    @When("Alice opens a private link pointing to non-existing item")
    public void open_fake_private_link() {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.openFakePrivateLink();
    }

    @When("Alice opens the share shortcut")
    public void open_share_shortcut() {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.openFakePrivateLink();
    }

    @When("Alice takes a picture")
    public void takes_picture() {
        StepLogger.logCurrentStep(Level.FINE);
        world.cameraPage.takePicture();
    }

    @When("Alice creates a web shortcut with the following fields")
    public void creates_shortcut(DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        List<List<String>> listItems = table.asLists();
        for (List<String> rows : listItems) {
            String name = rows.get(0);
            String url = rows.get(1);
            world.shortcutDialogPage.typeURLName(name, url);
            world.shortcutDialogPage.submitShortcut();
        }
    }

    @When ("Alice opens the shortcut {word}.url")
    public void opens_shortcut(String name) {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.downloadAction(name+".url");
    }

    @When ("Alice opens the link")
    public void opens_link() {
        StepLogger.logCurrentStep(Level.FINE);
        world.shortcutDialogPage.openShortcut();
    }

    @Then("Alice should see {string} in the (file)list")
    public void user_should_see_item_in_filelist(String itemName) throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.refreshList();
        //Get the last token of the item path
        assertTrue(world.fileListPage.isItemInList(itemName.substring(itemName.lastIndexOf('/') + 1)));
        assertTrue(world.filesAPI.itemExist(itemName));
    }

    @Then("Alice should not see {string} in the filelist anymore")
    public void user_should_not_see_item_in_list_anymore(String itemName) throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        assertFalse(world.fileListPage.isItemInList(itemName));
        assertFalse(world.filesAPI.itemExist(itemName));
    }

    @Then("Alice should{typePosNeg} see {word} in the links/offline/shares list")
    public void user_should_not_see_item_in_links_list(String sense, String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        if (sense.isEmpty()) {
            assertTrue(world.fileListPage.isItemInList(itemName));
        } else if (sense.equals(" not")) {
            assertFalse(world.fileListPage.isItemInList(itemName));
        }
    }

    @Then("Alice should see {string} inside the folder {word}")
    public void user_should_see_item_inside_folder_string(String itemName, String targetFolder)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.browseInto(targetFolder);
        world.fileListPage.isItemInList(itemName);
        assertTrue(world.filesAPI.itemExist(targetFolder + "/" + itemName));
    }

    @Then("Alice should see {word} inside the space {word}")
    public void user_should_see_item_inside_space(String itemName, String spaceName)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.openSpaces();
        world.spacesPage.openSpace(spaceName);
        assertTrue(world.fileListPage.isItemInList(itemName));
    }

    @Then("Alice should see {string} in the filelist as original")
    public void user_should_see_item_in_filelist_as_original_string(String itemName)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.refreshList();
        assertTrue(world.fileListPage.isItemInList(itemName.substring(itemName.lastIndexOf('/') + 1)));
        assertTrue(world.filesAPI.itemExist(itemName));
    }

    @Then("Alice should see the detailed information: {word}, {word}, and {word}")
    public void user_should_see_defailed_information(String itemName, String type, String size) {
        StepLogger.logCurrentStep(Level.FINE);
        world.detailsPage.removeShareSheet();
        assertEquals(world.detailsPage.getName(), itemName);
        assertEquals(world.detailsPage.getSize(), size);
        assertEquals(world.detailsPage.getType(), type);
        world.detailsPage.backListFiles();
    }

    @Then("the {fileType} {word} should be marked as downloaded")
    public void item_should_be_marked_as_downloaded(String type, String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        //We always reach this step from a preview... first browsing back
        world.detailsPage.backListFiles();
        assertTrue(world.fileListPage.isFileMarkedAsDownloaded(itemName));
    }

    @Then("Alice should{typePosNeg} see the {itemtype} {word} as av.offline")
    public void user_should_see_item_marked_as_avOffline(String sense, String type, String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        if (sense.isEmpty()) {
            assertTrue(world.fileListPage.isItemMarkedAsAvOffline(itemName));
        } else if (sense.equals(" not")) {
            assertTrue(world.fileListPage.isItemMarkedAsUnAvOffline(itemName));
        }
    }

    @Then("the item {word} should be opened and previewed")
    public void item_should_be_opened_and_previewed(String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        assertTrue(world.detailsPage.isItemPreviewed());
        world.detailsPage.backListFiles();
    }

    @Then("the {fileType} {word} should be opened and previewed")
    public void image_should_be_opened_and_previewed(String type, String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        switch (type) {
            case "file" -> assertTrue(world.detailsPage.isItemPreviewed());
            case "audio" -> assertTrue(world.detailsPage.isAudioPreviewed());
            case "image" -> {
                assertTrue(world.detailsPage.isImagePreviewed());
                world.detailsPage.displayControls();
            }
            case "video" -> assertTrue(world.detailsPage.isVideoPreviewed());
            case "damaged" -> assertTrue(world.detailsPage.isDamagedPreviewed());
        }
    }

    @Then("the list of files in {word} folder should match with the server")
    public void list_of_files_in_folder_should_match_server(String path)
            throws Throwable {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.refreshList();
        world.fileListPage.browseToFolder(path);
        ArrayList<OCFile> listServer = world.filesAPI.listItems(path, "Alice");
        assertTrue(world.fileListPage.isDisplayedListCorrect(path, listServer));
    }

    @Then("Alice should see the following error/message/item(s)")
    public void user_should_see_following_error(DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        List<List<String>> listItems = table.asLists();
        String error = listItems.get(0).get(0);
        Log.log(Level.FINE, "Error/Message to check: " + error);
        assertTrue(world.fileListPage.errorDisplayed(error));
    }

    @Then("Alice should see the file {word} with {word}")
    public void user_should_see_the_file_with_text(String itemName, String text) {
        StepLogger.logCurrentStep(Level.FINE);
        assertTrue(world.detailsPage.isTextInFile(text));
    }

    @Then("share sheet for the item {word} is displayed")
    public void share_sheet_for_item_displayed(String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        assertTrue(world.detailsPage.isShareSheetDisplayed(itemName));
    }

    @Then("Alice cannot unset as av.offline the item {word}")
    public void user_cannot_unset_avoffline_the_item(String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        world.fileListPage.selectItemList(itemName);
        assertFalse(world.fileListPage.isOperationAvailable("Unset as available offline"));
    }

    @Then("Alice should see the conflict dialog with the following message")
    public void user_see_dialog_conflict(DataTable table) {
        StepLogger.logCurrentStep(Level.FINE);
        List<List<String>> listItems = table.asLists();
        String msg = listItems.get(0).get(0);
        Log.log(Level.FINE, "Message to check: " + msg);
        assertTrue(world.fileListPage.isConflictDisplayed());
        assertTrue(world.fileListPage.errorDisplayed(msg));
    }

    @Then("{itemtype} {word} is opened in the app")
    public void original_is_opened(String itemType, String itemName) {
        StepLogger.logCurrentStep(Level.FINE);
        assertTrue(world.fileListPage.isItemOpened(itemType, itemName));
    }

    @Then("Alice should see the browser")
    public void bowser_displayed() {
        StepLogger.logCurrentStep(Level.FINE);
        assertTrue(world.shortcutDialogPage.isBrowserOpen());
    }

    @Then("Alice should see the error previewing {word}")
    public void error_previewing(String fileName) {
        StepLogger.logCurrentStep(Level.FINE);
        assertTrue(world.detailsPage.isDamagedPreviewed());
    }

    @Then("{fileType} {word} should{typePosNeg} be stored in device")
    public void file_downloaded_in_device(String itemType, String itemName, String sense)
            throws IOException {
        StepLogger.logCurrentStep(Level.FINE);

        String folderId = System.getProperty("backend").equals("oCIS")
                ? world.graphAPI.getPersonal().getId().replace("$", "\\$")
                : "";

        if (!folderId.isEmpty()) {
            Log.log(Level.FINE, "ID from personal space: " + folderId);
        }

        String currentPath = folderId;
        if (itemName.contains("/")) {
            String[] parts = itemName.split("/");
            //Building parcial path
            for (int i = 0; i < parts.length - 1; i++) {
                currentPath = currentPath + "/" + parts[i];
                world.devicePage.pullList(currentPath);
            }
            itemName = itemType.equals("file")
                    ? parts[parts.length - 1]
                    : parts[parts.length - 2];
        }

        String listFiles = world.devicePage.pullList(currentPath);
        Log.log(Level.FINE, "List of files before assertion: " + listFiles +
                ". ItemName: " + itemName);
        if (sense.isEmpty()) {
            assertTrue(listFiles.contains(itemName));
        } else if (sense.equals(" not")) {
            assertFalse(listFiles.contains(itemName));
        }
    }

    @Then("Alice should see the conflict dialog")
    public void conflict_dialog_displayed() {
        StepLogger.logCurrentStep(Level.FINE);
        assertTrue(world.conflictPage.isConflictPageDisplayed());
    }
}
