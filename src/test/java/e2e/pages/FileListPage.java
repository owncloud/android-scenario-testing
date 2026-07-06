/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import static e2e.support.log.Log.Log;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.logging.Level;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class FileListPage extends CommonPage {

    @AndroidFindBy(uiAutomator = CLOSE_SELECTION_MODE_UI_SELECTOR)
    private WebElement closeSelectionModeButton;

    @AndroidFindBy(id = FAB_EXPAND_MENU_BUTTON_ID)
    private WebElement fabButton;

    @AndroidFindBy(id = ROOT_TOOLBAR_LEFT_ICON_ID)
    private List<WebElement> hamburgerButton;

    @AndroidFindBy(id = CREATE_FOLDER_FAB_ID)
    private WebElement createFolderButton;

    @AndroidFindBy(id = UPLOAD_FAB_ID)
    private WebElement uploadButton;

    @AndroidFindBy(id = UPLOAD_FROM_FILES_ID)
    private WebElement uploadFilesButton;

    @AndroidFindBy(id = UPLOAD_FROM_CAMERA_ID)
    private WebElement uploadPictureButton;

    @AndroidFindBy(id = CREATE_SHORTCUT_FAB_ID)
    private WebElement createShortcutButton;

    @AndroidFindBy(id = ROOT_TOOLBAR_ID)
    private List<WebElement> toolbar;

    @AndroidFindBy(id = BOTTOM_NAV_VIEW_ID)
    private WebElement bottomBar;

    @AndroidFindBy(id = FILE_CELL_ID)
    private WebElement fileCell;

    @AndroidFindBy(id = FILE_NAME_ID)
    private WebElement fileName;

    @AndroidFindBy(id = CONFLICT_KEEP_BOTH_ID)
    private WebElement keepBothButton;

    @AndroidFindBy(id = CONFLICT_REPLACE_ID)
    private WebElement replaceButton;

    @AndroidFindBy(id = CONFLICT_SKIP_ID)
    private WebElement skipButton;

    @AndroidFindBy(id = LINK_SHORTCUT_ID)
    private WebElement linksShortcut;

    @AndroidFindBy(id = AVAILABLE_OFFLINE_SHORTCUT_ID)
    private WebElement availableOfflineShortcut;

    @AndroidFindBy(id = SPACES_TAB_ID)
    private WebElement spacesTab;

    @AndroidFindBy(id = UPLOADS_TAB_ID)
    private WebElement uploadsTab;

    @AndroidFindBy(id = EMPTY_MESSAGE_ID)
    private WebElement emptyMessage;

    @AndroidFindBy(id = CONFLICT_TITLE_ID)
    private WebElement conflictTitle;

    @AndroidFindBy(id = CHOOSER_HEADER_ID)
    private List<WebElement> chooserHeader;

    @AndroidFindBy(id = SNACKBAR_ACTIONS)
    private WebElement navigateToFolder;

    private static final String CLOSE_SELECTION_MODE_UI_SELECTOR = "new UiSelector().resourceId(\"com.owncloud.android:id/action_mode_close_button\")";
    private static final String FAB_EXPAND_MENU_BUTTON_ID = "com.owncloud.android:id/fab_expand_menu_button";
    private static final String ROOT_TOOLBAR_LEFT_ICON_ID = "com.owncloud.android:id/root_toolbar_left_icon";
    private static final String CREATE_FOLDER_FAB_ID = "com.owncloud.android:id/fab_mkdir";
    private static final String UPLOAD_FAB_ID = "com.owncloud.android:id/fab_upload";
    private static final String UPLOAD_FROM_FILES_ID = "com.owncloud.android:id/upload_from_files_item_view";
    private static final String UPLOAD_FROM_CAMERA_ID = "com.owncloud.android:id/upload_from_camera_item_view";
    private static final String CREATE_SHORTCUT_FAB_ID = "com.owncloud.android:id/fab_newshortcut";
    private static final String ROOT_TOOLBAR_ID = "com.owncloud.android:id/root_toolbar";
    private static final String BOTTOM_NAV_VIEW_ID = "com.owncloud.android:id/bottom_nav_view";
    private static final String FILE_CELL_ID = "com.owncloud.android:id/file_list_constraint_layout";
    private static final String FILE_NAME_ID = "com.owncloud.android:id/Filename";
    private static final String CONFLICT_KEEP_BOTH_ID = "com.owncloud.android:id/dialog_file_already_exists_keep_both";
    private static final String CONFLICT_REPLACE_ID = "com.owncloud.android:id/dialog_file_already_exists_replace";
    private static final String CONFLICT_SKIP_ID = "com.owncloud.android:id/dialog_file_already_exists_skip";
    private static final String LINK_SHORTCUT_ID = "com.owncloud.android:id/nav_shared_by_link_files";
    private static final String AVAILABLE_OFFLINE_SHORTCUT_ID = "com.owncloud.android:id/nav_available_offline_files";
    private static final String SPACES_TAB_ID = "nav_spaces";
    private static final String UPLOADS_TAB_ID = "nav_uploads";
    private static final String EMPTY_MESSAGE_ID = "com.owncloud.android:id/list_empty_dataset_title";
    private static final String CONFLICT_TITLE_ID = "com.owncloud.android:id/dialog_file_already_exists_title";
    private static final String CHOOSER_HEADER_ID = "android:id/chooser_header";
    private static final String SHARE_OPTION_ID = "com.owncloud.android:id/action_share_file";
    private static final String AVAILABLE_OFFLINE_OPTION_ID = "com.owncloud.android:id/action_set_available_offline";
    private static final String UNAVAILABLE_OFFLINE_OPTION_ID = "com.owncloud.android:id/action_set_unavailable_offline";
    private static final String DOWNLOAD_OPTION_ID = "com.owncloud.android:id/action_download_file";
    private static final String SYNC_OPTION_ID = "com.owncloud.android:id/action_sync_file";
    private static final String DELETE_OPTION_ID = "com.owncloud.android:id/action_remove_file";
    private static final String FILE_DETAIL_IMAGE_ID = "com.owncloud.android:id/fdImageDetailFile";
    private static final String MORE_OPTIONS_DESCRIPTION = "More options";
    private static final String SNACKBAR_ACTIONS = "com.owncloud.android:id/snackbar_action";

    public FileListPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void refreshList() {
        final double startX = 0.50;
        final double startY = 0.30;
        final double endX = 0.50;
        final double endY = 0.80;
        Log.log(Level.FINE, "Refresh file list");
        waitById(WAIT_TIME, bottomBar);
        swipe(startX, startY, endX, endY);
    }

    public void selectCreateFolder() {
        Log.log(Level.FINE, "Select create folder");
        openFabMenu();
        createFolderButton.click();
    }

    public void selectUploadFiles() {
        Log.log(Level.FINE, "Select upload files");
        openFabMenu();
        uploadButton.click();
        uploadFilesButton.click();
    }

    public void selectUploadPicture() {
        Log.log(Level.FINE, "Select upload picture");
        openFabMenu();
        uploadButton.click();
        uploadPictureButton.click();
    }

    public void selectCreateShortcut() {
        Log.log(Level.FINE, "Select create shortcut");
        openFabMenu();
        createShortcutButton.click();
    }

    public boolean isItemInList(String itemName) {
        Log.log(Level.FINE, "Check if item is in list: " + itemName);
        return !findListUIAutomatorText(itemName).isEmpty();
    }

    public boolean isFileListVisible() {
        Log.log(Level.FINE, "Check if file list is visible");
        return !toolbar.isEmpty();
    }

    public boolean errorDisplayed(String error) {
        Log.log(Level.FINE, "Check error displayed: " + error);
        return findUIAutomatorSubText(error).isDisplayed();
    }

    public void selectItemList(String path) {
        Log.log(Level.FINE, "Select item from list: " + path);
        String itemName = path.contains("/") ? browseToFile(path) : path;
        Log.log(Level.FINE, "Item name to select: " + itemName);
        refreshList();
        waitByTextVisible(WAIT_TIME, itemName);
        longPress(itemName);
    }

    public void selectOperation(String operationName) {
        if ("share".equals(operationName)) {
            tapShareAction();
            return;
        }
        if ("Delete".equals(operationName)) {
            tapDeleteAction();
            return;
        }
        Log.log(Level.FINE, "Operation placed in menu: " + operationName);
        selectOperationMenu(operationName);
    }

    public void selectItem(String itemName) {
        Log.log(Level.FINE, "Select item: " + itemName);
        findUIAutomatorText(itemName).click();
    }

    public void openLinkShortcut() {
        Log.log(Level.FINE, "Open link shortcut");
        linksShortcut.click();
    }

    public void openAvOffShortcut() {
        final String downloadEnqueuedText = "Download enqueued";
        Log.log(Level.FINE, "Open available offline shortcut");
        waitByTextInvisible(WAIT_TIME, downloadEnqueuedText);
        availableOfflineShortcut.click();
    }

    public void openSpaces() {
        Log.log(Level.FINE, "Open spaces view");
        waitById(WAIT_TIME, spacesTab);
        spacesTab.click();
    }

    public void openUploadsView() {
        Log.log(Level.FINE, "Open uploads view");
        uploadsTab.click();
    }

    public void closeSelectionMode() {
        Log.log(Level.FINE, "Close selection mode");
        closeSelectionModeButton.click();
    }

    public void openMenuActions() {
        Log.log(Level.FINE, "Open item actions menu");
        findUIAutomatorDescription(MORE_OPTIONS_DESCRIPTION).click();
    }

    public boolean isOperationAvailable(String operationName) {
        Log.log(Level.FINE, "Check if operation is available: " + operationName);
        openMenuActions();
        return !findListUIAutomatorText(operationName).isEmpty();
    }

    public boolean isConflictDisplayed() {
        Log.log(Level.FINE, "Check if conflict is displayed");
        return conflictTitle.isDisplayed();
    }

    public void tapReplaceConflict() {
        Log.log(Level.FINE, "Tap replace conflict option");
        replaceButton.click();
    }

    public void tapKeepBothConflict() {
        Log.log(Level.FINE, "Tap keep both conflict option");
        keepBothButton.click();
    }

    public void tapSkipConflict() {
        Log.log(Level.FINE, "Tap skip conflict option");
        skipButton.click();
    }

    public boolean isDownloadActionVisible() {
        return !findListId(DOWNLOAD_OPTION_ID).isEmpty();
    }

    public boolean isSyncActionVisible() {
        return !findListId(SYNC_OPTION_ID).isEmpty();
    }

    public boolean isAvailableOfflineActionVisible() {
        return !findListId(AVAILABLE_OFFLINE_OPTION_ID).isEmpty();
    }

    public boolean isUnavailableOfflineActionVisible() {
        return !findListId(UNAVAILABLE_OFFLINE_OPTION_ID).isEmpty();
    }

    public boolean isEndOfListDisplayed(int numberItems) {
        String endListText = Integer.toString(numberItems - 1) + " files";
        return !findListUIAutomatorText(endListText).isEmpty();
    }

    public void openUrl(String url) {
        Log.log(Level.FINE, "Open URL: " + url);
        waitUntilFileListIsReady();
        driver.get(url);
    }

    public void waitUntilFileListIsReady() {
        Log.log(Level.FINE, "Wait until file list is ready");
        waitById(WAIT_TIME, FAB_EXPAND_MENU_BUTTON_ID);
    }

    public void dismissChooserIfDisplayed() {
        final int chooserDismissX = 200;
        final int chooserDismissY = 300;
        if (!chooserHeader.isEmpty()) {
            tap(chooserDismissX, chooserDismissY);
        }
    }

    public boolean isFilePreviewOpened(String itemName) {
        boolean fileNameVisible = findUIAutomatorText(itemName).isDisplayed();
        boolean fileTypeIconVisible = findId(FILE_DETAIL_IMAGE_ID).isDisplayed();

        return fileNameVisible && fileTypeIconVisible;
    }

    public boolean isFolderOpened(String itemName) {
        boolean folderNameVisible = findUIAutomatorText(itemName).isDisplayed();
        boolean hamburgerButtonVisible = !hamburgerButton.isEmpty();
        return folderNameVisible && !hamburgerButtonVisible;
    }

    public void navigateToTargetFolder() {
        Log.log(Level.FINE, "Starts: navigate to target folder");
        navigateToFolder.click();
    }

    private void openFabMenu() {
        fabButton.click();
    }

    private void tapShareAction() {
        findId(SHARE_OPTION_ID).click();
    }

    private void tapDeleteAction() {
        findId(DELETE_OPTION_ID).click();
    }

    private void selectOperationMenu(String operationName) {
        Log.log(Level.FINE, "Select operation from menu: " + operationName);
        openMenuActions();
        findUIAutomatorText(operationName).click();
    }
}
