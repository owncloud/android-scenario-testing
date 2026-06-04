/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package android;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.logging.Level;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.log.Log;

public class FolderPickerPage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/folder_picker_btn_choose")
    private WebElement chooseButton;

    @AndroidFindBy(id = "com.owncloud.android:id/folder_picker_btn_cancel")
    private WebElement cancelButton;

    @AndroidFindBy(id = "com.owncloud.android:id/view_type_selector")
    private WebElement createFolderButton;

    public FolderPickerPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void selectSpace(String spaceName) {
        Log.log(Level.FINE, "Start: Select space from picker: " + spaceName);
        findUIAutomatorText(spaceName).click();
    }

    public void selectFolder(String targetFolder) {
        Log.log(Level.FINE, "Start: Select folder from picker: " + targetFolder);
        if (!targetFolder.equals("/")) {
            browseToFolder(targetFolder);
        }
    }

    public void createFolder() {
        Log.log(Level.FINE, "Start: Create folder from picker");
        createFolderButton.click();
    }

    public void accept() {
        Log.log(Level.FINE, "Start: Accept selection picker");
        chooseButton.click();
    }

    public void cancel() {
        Log.log(Level.FINE, "Start: Cancel selection picker");
        cancelButton.click();
    }
}
