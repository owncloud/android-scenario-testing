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
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.entities.OCFile;
import utils.log.Log;

public class FileListSteps {

    private World world;

    public FileListSteps(World world) {
        this.world = world;
    }

    @ParameterType("replace|keep both")
    public String conflictFix(String type) {
        return type;
    }

    @ParameterType("file|audio|image|video|damaged")
    public String fileType(String type) {
        return type;
    }

    @ParameterType("Upload File|Picture from Camera|Create Shortcut|Create Folder")
    public String optionsFab(String type) {
        return type;
    }

    @Given("there is an item called {word} in the folder Downloads of the device")
    public void there_is_an_item_in_folder_downloads(String itemName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.pushFile(itemName);
    }

    @Given("the following items have been created in {word} account")
    public void items_have_been_created_in_account(String userName, DataTable table) throws Throwable {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        List<List<String>> listItems = table.asLists();
        for (List<String> rows : listItems) {
            String type = rows.get(0);
            String name = rows.get(1);
            Log.log(Level.FINE, type + " " + name);
            if (!world.filesAPI.itemExist(name)) {
                switch (type) {
                    case ("folder"):
                    case ("item"): {
                        world.filesAPI.createFolder(name, userName);
                        break;
                    }
                    case ("file"): {
                        world.filesAPI.pushFile(name, userName);
                        break;
                    }
                    case ("image"): {
                        world.filesAPI.pushFileByMime(name, "image/jpg");
                        break;
                    }
                    case ("audio"): {
                        world.filesAPI.pushFileByMime(name, "audio/mpeg3");
                        break;
                    }
                    case ("video"): {
                        world.filesAPI.pushFileByMime(name, "video/mp4");
                        break;
                    }
                    case ("shortcut"): {
                        world.filesAPI.pushFileByMime(name, "text/uri-list");
                        break;
                    }
                    case ("damaged"): { //Our sample damaged file is a .png
                        world.filesAPI.pushFileByMime(name, "image/png");
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }

    @Given("the folder {word} contains {int} files")
    public void folder_contains_files(String folderName, int files) throws Throwable {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        if (!world.filesAPI.itemExist(folderName)) {
            world.filesAPI.createFolder(folderName, "alice");
        }
        for (int i = 0; i < files; i++) {
            world.filesAPI.pushFile(folderName + "/file_" + i + ".txt", "Alice");
        }
    }

    @When("Alice selects to set as av.offline the item {word}")
    public void user_selects_to_set_as_avoffline_item(String itemName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.executeOperation("Set as available offline", itemName);
        world.fileListPage.closeSelectionMode();
    }

    @When("Alice selects to unset as av.offline the item {word}")
    public void user_selects_to_unset_as_unavoffline_item(String itemName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.executeOperation("Unset as available offline", itemName);
        world.fileListPage.closeSelectionMode();
    }

    @When("Alice selects to {word} the {itemtype} {word}")
    public void user_selects_item_to_some_operation(String operation, String type, String itemName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        if (operation.equals("Download") || operation.equals("open")) {
            world.fileListPage.downloadAction(itemName);
        } else {
            world.fileListPage.executeOperation(operation, itemName);
        }
    }

    @When("Alice selects to {word}")
    public void user_selects_item_to_some_operation_in_multi(String operation) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.selectOperation(operation);
    }

    @When("Alice selects {word} as target folder")
    public void user_selects_target_folder(String targetFolder) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.folderPickerPage.selectFolder(targetFolder);
        world.folderPickerPage.accept();
    }

    @When("Alice selects {word} as space")
    public void user_selects_space(String spaceName)
            throws IOException {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        if (world.authAPI.isOidc()) {
            world.folderPickerPage.selectSpace(spaceName);
        }
    }

    @When("Alice creates new folder {word} in the folder picker")
    public void user_creates_folder_picker(String targetFolder) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.folderPickerPage.createFolder();
        world.inputNamePage.setItemName(targetFolder);
    }

    @When("Alice selects the option {optionsFab}")
    public void user_selects_option_upload(String operation) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        switch (operation){
            case "Upload File": {
                world.fileListPage.selectUploadFiles();
                break;
            }
            case "Picture from Camera":{
                world.fileListPage.selectUploadPicture();
                break;
            }
            case "Create Folder":{
                world.fileListPage.selectCreateFolder();
                break;
            }
            case "Create Shortcut":{
                world.fileListPage.selectCreateShortcut();
                break;
            }
            default:
                break;
        }
    }

    @When("Alice refreshes the list")
    public void user_refreshes_list() {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.refreshList();
    }

    @When("Alice accepts the deletion of {word}")
    public void user_accepts_deletion(String type) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.removeDialogPage.removeAll();
    }

    @When("the {word} has been deleted remotely")
    public void item_has_been_deleted_remotely(String fileName)
            throws IOException {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.filesAPI.removeItem(fileName, "Alice");
        world.fileListPage.refreshList();
    }

    @When("Alice sets {word} as new name")
    public void user_sets_new_name(String itemName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.inputNamePage.setItemName(itemName);
    }

    @When("Alice opens the public link shortcut")
    public void user_opens_public_link_shortcut() {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.openLinkShortcut();
        world.fileListPage.refreshList();
    }

    @When("Alice opens the available offline shortcut")
    public void user_opens_av_offline_shortcut() {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.openAvOffhortcut();
        world.fileListPage.refreshList();
    }

    @When("Alice browses into {word}")
    public void user_browses_into_folder(String path) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.refreshList();
        world.fileListPage.browseToFolder(path);
    }

    @When("Alice browses to root folder")
    public void user_browses_root_folder() {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.browseRoot();
    }

    @When("Alice clicks on the thumbnail")
    public void user_clicks_on_the_thumbnail() {
        String stepName = new Object() {
        }.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.detailsPage.downloadFromThumbnail();
    }

    @When("file {word} is modified externally adding {word}")
    public void file_is_modified_externally_adding_text(String itemName, String text)
            throws IOException {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.filesAPI.pushFile(itemName, text, "Alice");
    }

    @When("Alice closes the preview")
    public void user_closes_preview() {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.detailsPage.backListFiles();
    }

    @When("Alice fixes the conflict with {conflictFix}")
    public void user_fixes_conflict(String conflictFix) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.fixConflict(conflictFix);
    }

    @When("Alice long presses over {word}")
    public void user_longpress_over_item(String itemName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.refreshList();
        world.fileListPage.longPress(itemName);
    }

    @When("Alice multiselects the following items")
    public void user_selects_all_items(DataTable table) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        List<List<String>> listItems = table.asLists();
        for (List<String> rows : listItems) {
            String name = rows.get(0);
            world.fileListPage.selectItem(name);
        }
    }

    @When("Alice opens a private link pointing to {word} with scheme {word}")
    public void open_private_link(String filePath, String scheme)
            throws Throwable {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        OCFile item = world.filesAPI.listItems(filePath, "Alice").get(0);
        String privateLink = world.fileListPage.getPrivateLink(scheme, item.getPrivateLink());
        world.fileListPage.openPrivateLink(privateLink);
    }

    @When("Alice opens a private link pointing to shared {word} with scheme {word}")
    public void open_private_link_shared(String fileName, String scheme)
            throws Throwable {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
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
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.openFakePrivateLink();
    }

    @When("Alice opens the share shortcut")
    public void open_share_shortcut() {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.openFakePrivateLink();
    }

    @When("Alice takes a picture")
    public void takes_picture() {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.cameraPage.takePicture();
    }

    @When("Alice creates a web shortcut with the following fields")
    public void creates_shortcut(DataTable table) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
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
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.downloadAction(name+".url");
    }

    @When ("Alice opens the link")
    public void opens_link() {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.shortcutDialogPage.openShortcut();
    }

    @Then("Alice should see {word} in the (file)list")
    public void user_should_see_item_in_filelist(String itemName) throws Throwable {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.refreshList();
        //Get the last token of the item path
        assertTrue(world.fileListPage.isItemInList(itemName.substring(itemName.lastIndexOf('/') + 1)));
        assertTrue(world.filesAPI.itemExist(itemName));
    }

    @Then("Alice should not see {word} in the filelist anymore")
    public void user_should_not_see_item_in_list_anymore(String itemName) throws Throwable {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        assertFalse(world.fileListPage.isItemInList(itemName));
        assertFalse(world.filesAPI.itemExist(itemName));
    }

    @Then("Alice should not see {word} in the links/offline list")
    public void user_should_not_see_item_in_links_list(String itemName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        assertFalse(world.fileListPage.isItemInList(itemName));
    }

    @Then("Alice should see {word} in the shares list")
    public void user_not_see_item_in_shares_list(String itemName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        assertTrue(world.fileListPage.isItemInList(itemName));
    }

    //Variant with word parameter
    @Then("Alice should see {word} inside the folder {word}")
    public void user_should_see_item_inside_folder_word(String itemName, String targetFolder)
            throws Throwable {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.browseInto(targetFolder);
        assertTrue(world.fileListPage.isItemInList(itemName));
        assertTrue(world.filesAPI.itemExist(targetFolder + "/" + itemName));
    }

    //Variant with string parameter, in case the itemName contain blanks
    @Then("Alice should see {string} inside the folder {word}")
    public void user_should_see_item_inside_folder_string(String itemName, String targetFolder)
            throws Throwable {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.browseInto(targetFolder);
        world.fileListPage.isItemInList(itemName);
        assertTrue(world.filesAPI.itemExist(targetFolder + "/" + itemName));
    }

    @Then("Alice should see {word} inside the space {word}")
    public void user_should_see_item_inside_space(String itemName, String spaceName)
            throws Throwable {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.openSpaces();
        world.spacesPage.openSpace(spaceName);
        assertTrue(world.fileListPage.isItemInList(itemName));
    }

    @Then("Alice should see {word} in the filelist as original")
    public void user_should_see_item_in_filelist_as_original_word(String itemName)
            throws Throwable {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        assertTrue(world.fileListPage.isItemInList(itemName.substring(itemName.lastIndexOf('/') + 1)));
        assertTrue(world.filesAPI.itemExist(itemName));
    }

    @Then("Alice should see {string} in the filelist as original")
    public void user_should_see_item_in_filelist_as_original_string(String itemName)
            throws Throwable {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.refreshList();
        assertTrue(world.fileListPage.isItemInList(itemName.substring(itemName.lastIndexOf('/') + 1)));
        assertTrue(world.filesAPI.itemExist(itemName));
    }

    @Then("Alice should see the detailed information: {word}, {word}, and {word}")
    public void user_should_see_defailed_information(String itemName, String type, String size) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.detailsPage.removeShareSheet();
        assertEquals(world.detailsPage.getName(), itemName);
        assertEquals(world.detailsPage.getSize(), size);
        assertEquals(world.detailsPage.getType(), type);
        world.detailsPage.backListFiles();
    }

    @Then("the {fileType} {word} should be marked as downloaded")
    public void item_should_be_marked_as_downloaded(String type, String itemName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        //We always reach this step from a preview... first browsing back
        world.detailsPage.backListFiles();
        assertTrue(world.fileListPage.isFileMarkedAsDownloaded(itemName));
    }

    @Then("Alice should see the {itemtype} {word} as av.offline")
    public void user_should_see_item_marked_as_avOffline(String type, String itemName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        assertTrue(world.fileListPage.isItemMarkedAsAvOffline(itemName));
    }

    @Then("Alice should not see the {itemtype} {word} as av.offline")
    public void user_should_not_see_item_marked_as_avOffline(String type, String itemName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        assertTrue(world.fileListPage.isItemMarkedAsUnAvOffline(itemName));
    }

    @Then("the item {word} should be opened and previewed")
    public void item_should_be_opened_and_previewed(String itemName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        assertTrue(world.detailsPage.isItemPreviewed());
        world.detailsPage.backListFiles();
    }

    @Then("the {fileType} {word} should be opened and previewed")
    public void image_should_be_opened_and_previewed(String type, String itemName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        switch (type) {
            case ("file"): {
                assertTrue(world.detailsPage.isItemPreviewed());
                break;
            }
            case ("audio"): {
                assertTrue(world.detailsPage.isAudioPreviewed());
                break;
            }
            case ("image"): {
                assertTrue(world.detailsPage.isImagePreviewed());
                world.detailsPage.displayControls();
                break;
            }
            case ("video"): {
                assertTrue(world.detailsPage.isVideoPreviewed());
                break;
            }
            case ("damaged"): {
                assertTrue(world.detailsPage.isDamagedPreviewed());
                break;
            }
        }
    }

    @Then("the list of files in {word} folder should match with the server")
    public void list_of_files_in_folder_should_match_server(String path)
            throws Throwable {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.refreshList();
        world.fileListPage.browseToFolder(path);
        ArrayList<OCFile> listServer = world.filesAPI.listItems(path, "Alice");
        assertTrue(world.fileListPage.isDisplayedListCorrect(path, listServer));
    }

    @Then("Alice should see the following error/message/item(s)")
    public void user_should_see_following_error(DataTable table) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        List<List<String>> listItems = table.asLists();
        String error = listItems.get(0).get(0);
        Log.log(Level.FINE, "Error/Message to check: " + error);
        assertTrue(world.fileListPage.errorDisplayed(error));
    }

    @Then("Alice should see the file {word} with {word}")
    public void user_should_see_the_file_with_text(String itemName, String text) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        assertTrue(world.detailsPage.isTextInFile(text));
    }

    @Then("share sheet for the item {word} is displayed")
    public void share_sheet_for_item_displayed(String itemName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        assertTrue(world.detailsPage.isShareSheetDisplayed(itemName));
    }

    @Then("Alice cannot unset as av.offline the item {word}")
    public void user_cannot_unset_avoffline_the_item(String itemName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        world.fileListPage.selectItemList(itemName);
        assertFalse(world.fileListPage.isOperationAvailable("Unset as available offline"));
    }

    @Then("Alice should see the conflict dialog with the following message")
    public void user_see_dialog_conflict(DataTable table) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        List<List<String>> listItems = table.asLists();
        String msg = listItems.get(0).get(0);
        Log.log(Level.FINE, "Message to check: " + msg);
        assertTrue(world.fileListPage.isConflictDisplayed());
        assertTrue(world.fileListPage.errorDisplayed(msg));
    }

    @Then("{itemtype} {word} is opened in the app")
    public void original_is_opened(String itemType, String itemName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        assertTrue(world.fileListPage.isItemOpened(itemType, itemName));
    }

    @Then("Alice should see the browser")
    public void bowser_displayed() {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        assertTrue(world.shortcutDialogPage.isBrowserOpen());
    }

    @Then("Alice should see the error previewing {word}")
    public void error_previewing(String fileName) {
        String stepName = new Object() {}.getClass().getEnclosingMethod().getName().toUpperCase();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        assertTrue(world.detailsPage.isDamagedPreviewed());
    }
}
