/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import static e2e.support.log.Log.Log;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import e2e.LocProperties;
import e2e.model.OCFile;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class FileListPage extends CommonPage {

    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"com.owncloud.android:id/action_mode_close_button\")")
    private WebElement closeSelectionMode;

    @AndroidFindBy(id = "com.owncloud.android:id/fab_expand_menu_button")
    private WebElement fabButton;

    @AndroidFindBy(id = "com.owncloud.android:id/root_toolbar_left_icon")
    private List<WebElement> hamburgerButton;

    @AndroidFindBy(id = "com.owncloud.android:id/fab_mkdir")
    private WebElement createFolder;

    @AndroidFindBy(id = "com.owncloud.android:id/fab_upload")
    private WebElement uploadOption;

    @AndroidFindBy(id = "com.owncloud.android:id/upload_from_files_item_view")
    private WebElement uploadFiles;

    @AndroidFindBy(id = "com.owncloud.android:id/upload_from_camera_item_view")
    private WebElement uploadPic;

    @AndroidFindBy(id = "com.owncloud.android:id/fab_newshortcut")
    private WebElement createShortcut;

    @AndroidFindBy(id = "com.owncloud.android:id/root_toolbar")
    private List<WebElement> toolbar;

    @AndroidFindBy(id = "com.owncloud.android:id/bottom_nav_view")
    private WebElement bottomBar;

    @AndroidFindBy(id = "com.owncloud.android:id/file_list_constraint_layout")
    private WebElement fileCell;

    @AndroidFindBy(id = "com.owncloud.android:id/Filename")
    private WebElement fileName;

    @AndroidFindBy(id = "com.owncloud.android:id/dialog_file_already_exists_keep_both")
    private WebElement keepBoth;

    @AndroidFindBy(id = "com.owncloud.android:id/dialog_file_already_exists_replace")
    private WebElement replace;

    @AndroidFindBy(id = "com.owncloud.android:id/dialog_file_already_exists_skip")
    private WebElement skip;

    @AndroidFindBy(id = "com.owncloud.android:id/nav_shared_by_link_files")
    private WebElement linksShortcut;

    @AndroidFindBy(id = "com.owncloud.android:id/nav_available_offline_files")
    private WebElement avOffShortcut;

    @AndroidFindBy(id = "nav_spaces")
    private WebElement spacesTab;

    @AndroidFindBy(id = "nav_uploads")
    private WebElement uploads;

    @AndroidFindBy(id = "com.owncloud.android:id/list_empty_dataset_title")
    private WebElement emptyMessage;

    @AndroidFindBy(id = "com.owncloud.android:id/dialog_file_already_exists_title")
    private WebElement conflictTitle;

    @AndroidFindBy(id = "android:id/chooser_header")
    private List<WebElement> chooserHeader;

    private static final String SHARE_OPTION_ID = "com.owncloud.android:id/action_share_file";
    private static final String AVAILABLE_OFFLINE_OPTION_ID = "com.owncloud.android:id/action_set_available_offline";
    private static final String UNAVAILABLE_OFFLINE_OPTION_ID = "com.owncloud.android:id/action_set_unavailable_offline";
    private static final String DOWNLOAD_OPTION_ID = "com.owncloud.android:id/action_download_file";
    private static final String SYNC_OPTION_ID = "com.owncloud.android:id/action_sync_file";
    private static final String DELETE_OPTION_ID = "com.owncloud.android:id/action_remove_file";
    private static final String MORE_OPTIONS_DESCRIPTION = "More options";

    public FileListPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void refreshList() {
        final double startX = 0.50;
        final double startY = 0.30;
        final double endX = 0.50;
        final double endY = 0.80;
        Log.log(Level.FINE, "Refresh list");
        waitById(WAIT_TIME, bottomBar);
        swipe(startX, startY, endX, endY);
    }

    public void selectCreateFolder() {
        Log.log(Level.FINE, "Starts: create folder");
        fabButton.click();
        createFolder.click();
    }

    public void selectUploadFiles() {
        Log.log(Level.FINE, "Starts: upload file");
        fabButton.click();
        uploadOption.click();
        uploadFiles.click();
    }

    public void selectUploadPicture() {
        Log.log(Level.FINE, "Starts: upload picture");
        fabButton.click();
        uploadOption.click();
        uploadPic.click();
    }

    public void selectCreateShortcut() {
        Log.log(Level.FINE, "Starts: create shortcut");
        fabButton.click();
        createShortcut.click();
    }

    public void executeOperation(String operation, String itemName) {
        Log.log(Level.FINE, "Starts: execute operation: " + operation + " " + itemName);
        refreshList();
        selectItemList(itemName);
        selectOperation(operation);
    }

    public void downloadAction(String itemName) {
        Log.log(Level.FINE, "Starts: download action: " + itemName);
        if (!isItemInList(itemName)) {
            Log.log(Level.FINE, "Searching item... swiping: " + itemName);
            refreshList();
        }
        findUIAutomatorText(itemName).click();
    }

    public boolean isItemInList(String itemName) {
        Log.log(Level.FINE, "Starts: Check if item is in list: " + itemName);
        return !findListUIAutomatorText(itemName).isEmpty();
    }

    public boolean isFileListVisible() {
        Log.log(Level.FINE, "Starts: is File list Visible");
        return !toolbar.isEmpty();
    }

    public boolean errorDisplayed(String error) {
        Log.log(Level.FINE, "Starts: Error displayed: " + error);
        return findUIAutomatorSubText(error).isDisplayed();
    }

    public void selectItemList(String path) {
        Log.log(Level.FINE, "Starts: select item from list: " + path);
        String itemName = path.contains("/") ? browseToFile(path) : path;
        Log.log(Level.FINE, "Item name to select: " + itemName);
        refreshList();
        waitByTextVisible(WAIT_TIME, itemName);
        longPress(itemName);
    }

    public void selectOperation(String operationName) {
        if ("share".equals(operationName)) {
            findId(SHARE_OPTION_ID).click();
            return;
        }
        if ("Delete".equals(operationName)) {
            findId(DELETE_OPTION_ID).click();
            return;
        }
        Log.log(Level.FINE, "Operation: " + operationName + " placed in menu");
        selectOperationMenu(operationName);
    }

    public void selectItem(String itemName) {
        Log.log(Level.FINE, "Starts: select item: " + itemName);
        findUIAutomatorText(itemName).click();
    }

    public void openLinkShortcut() {
        Log.log(Level.FINE, "Starts: open link shortcut");
        linksShortcut.click();
    }

    public void openAvOffShortcut() {
        final String downloadEnqueuedText = "Download enqueued";
        Log.log(Level.FINE, "Starts: open av offline shortcut");
        waitByTextInvisible(WAIT_TIME, downloadEnqueuedText);
        avOffShortcut.click();
    }

    public void openSpaces() {
        waitById(WAIT_TIME, spacesTab);
        spacesTab.click();
    }

    public void openUploadsView() {
        uploads.click();
    }

    public void closeSelectionMode() {
        Log.log(Level.FINE, "Starts: close selection mode");
        closeSelectionMode.click();
    }

    public boolean isFileMarkedAsDownloaded(String path) {
        Log.log(Level.FINE, "Starts: Check if file is downloaded: " + path);
        selectItemList(path);
        List<WebElement> downloadOptions = findListId(DOWNLOAD_OPTION_ID);
        List<WebElement> syncOptions = findListId(SYNC_OPTION_ID);
        return downloadOptions.isEmpty() && !syncOptions.isEmpty();
    }

    public boolean isItemMarkedAsAvOffline(String path) {
        Log.log(Level.FINE, "Starts: Check is file is av. offline: " + path);
        refreshList();
        selectItemList(path);
        openMenuActions();
        return findListId(AVAILABLE_OFFLINE_OPTION_ID).isEmpty();
    }

    public boolean isItemMarkedAsUnAvOffline(String path) {
        Log.log(Level.FINE, "Starts: Check is file is unav. offline: " + path);
        refreshList();
        selectItemList(path);
        openMenuActions();
        return findListId(UNAVAILABLE_OFFLINE_OPTION_ID).isEmpty();
    }

    private void openMenuActions() {
        findUIAutomatorDescription(MORE_OPTIONS_DESCRIPTION).click();
    }

    private void selectOperationMenu(String operationName) {
        Log.log(Level.FINE, "Starts: Select operation from the menu: " + operationName);
        openMenuActions();
        findUIAutomatorText(operationName).click();
    }

    public boolean isOperationAvailable(String operationName) {
        Log.log(Level.FINE, "Starts: Check if operation is available: " + operationName);
        openMenuActions();
        return !findListUIAutomatorText(operationName).isEmpty();
    }

    public boolean isConflictDisplayed() {
        Log.log(Level.FINE, "Starts: Conflict displayed");
        return conflictTitle.isDisplayed();
    }

    public void fixConflict(String option) {
        final String replaceOption = "replace";
        Log.log(Level.FINE, "Starts: Fix Conflict: " + option);
        if (replaceOption.equals(option)) {
            replace.click();
        } else {
            keepBoth.click();
        }
    }

    public boolean isDisplayedListCorrect(String path, ArrayList<OCFile> listServer) {
        final int maxVisibleNameLength = 15;
        browseToFolder(path);
        String userName1 = LocProperties.getProperties().getProperty("userName1");
        return listServer.stream()
            .filter(ocfile -> !ocfile.getName().equalsIgnoreCase(userName1)
                    && ocfile.getName().length() <= maxVisibleNameLength)
            .allMatch(ocfile -> {
                while (!isItemInList(ocfile.getName()) && !endList(listServer.size())) {
                    refreshList();
                }
                return isItemInList(ocfile.getName());
            });
    }

    private boolean endList(int numberItems) {
        String endListText = Integer.toString(numberItems - 1) + " files";
        return !findListUIAutomatorText(endListText).isEmpty();
    }

    private WebElement getElementFromFileList(String itemName) {
        Log.log(Level.FINE, "Starts: searching item in list: " + itemName);
        if (isItemInList(itemName)) {
            Log.log(Level.FINE, "Item found: " + itemName);
            return findUIAutomatorText(itemName);
        }
        Log.log(Level.FINE, "Item not found: " + itemName);
        return null;
    }

    public String getPrivateLink(String scheme, String privateLink) {
        final String escapedDollar = "\\$";
        Log.log(Level.FINE, "Starts: Create private link: " + scheme + " " + privateLink);
        String originalScheme = getScheme(privateLink);
        String linkToOpen = privateLink.replace(originalScheme, scheme)
                .replace("$", escapedDollar);
        Log.log(Level.FINE, "Link to open: " + linkToOpen);
        return linkToOpen;
    }

    private String getScheme(String originalURL) {
        final String schemeSeparator = "://";
        return originalURL.split(schemeSeparator)[0];
    }

    public void openPrivateLink(String privateLink) {
        final String fabExpandMenuButtonId = "com.owncloud.android:id/fab_expand_menu_button";
        Log.log(Level.FINE, "Starts: Open private link: " + privateLink);
        waitById(WAIT_TIME, fabExpandMenuButtonId);
        driver.get(privateLink);
    }

    public void openFakePrivateLink() {
        final String fakeScheme = "owncloud";
        final String fakeFilePath = "/f/11111111111";
        Log.log(Level.FINE, "Starts: Open fake private link");
        String server = System.getProperty("server");
        String originalScheme = getScheme(server);
        String fakeURL = server.replace(originalScheme, fakeScheme) + fakeFilePath;
        Log.log(Level.FINE, "Fake URL: " + fakeURL);
        driver.get(fakeURL);
    }

    public boolean isItemOpened(String itemType, String itemName) {
        final String fileType = "file";
        final String folderType = "folder";
        final String fileDetailImageId = "com.owncloud.android:id/fdImageDetailFile";
        final int chooserDismissX = 200;
        final int chooserDismissY = 300;
        Log.log(Level.FINE, "Starts: checking if item is opened: " + itemType + " " + itemName);
        if (fileType.equals(itemType)) {
            Log.log(Level.FINE, "Opening file");
            if (!chooserHeader.isEmpty()) {
                tap(chooserDismissX, chooserDismissY);
            }
            boolean fileNameVisible = findUIAutomatorText(itemName).isDisplayed();
            boolean fileTypeIconVisible = findId(fileDetailImageId).isDisplayed();
            return fileNameVisible && fileTypeIconVisible;
        }
        if (folderType.equals(itemType)) {
            Log.log(Level.FINE, "Opening folder");
            boolean folderNameVisible = findUIAutomatorText(itemName).isDisplayed();
            boolean hamburgerButtonVisible = !hamburgerButton.isEmpty();
            return folderNameVisible && !hamburgerButtonVisible;
        }
        return false;
    }
}
