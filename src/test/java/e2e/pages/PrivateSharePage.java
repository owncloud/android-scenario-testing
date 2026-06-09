/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import static e2e.support.log.Log.Log;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.logging.Level;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class PrivateSharePage extends CommonPage {

    private final String createboxId = "com.owncloud.android:id/canEditCreateCheckBox";
    private final String changeboxId = "com.owncloud.android:id/canEditChangeCheckBox";
    private final String deleteboxId = "com.owncloud.android:id/canEditDeleteCheckBox";
    private final String editboxId = "com.owncloud.android:id/canEditSwitch";
    private final String shareboxId = "com.owncloud.android:id/canShareSwitch";

    @AndroidFindBy(id = createboxId)
    private WebElement create;

    @AndroidFindBy(id = changeboxId)
    private WebElement change;

    @AndroidFindBy(id = deleteboxId)
    private WebElement delete;

    @AndroidFindBy(id = editboxId)
    private WebElement edit;

    @AndroidFindBy(id = shareboxId)
    private WebElement share;

    @AndroidFindBy(id = "com.owncloud.android:id/closeButton")
    private WebElement close;

    public PrivateSharePage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public boolean isSharee(String user) {
        Log.log(Level.FINE, "Starts: Sharee in list: " + user);
        return findUIAutomatorText(user).isDisplayed();
    }

    public void switchCreate() {
        Log.log(Level.FINE, "Starts: Click create checkbox");
        create.click();
    }

    public void switchChange() {
        Log.log(Level.FINE, "Starts: Click change checkbox");
        change.click();
    }

    public void switchDelete() {
        Log.log(Level.FINE, "Starts: Click delete checkbox:");
        delete.click();
    }

    public void switchShare() {
        Log.log(Level.FINE, "Starts: Switch share button");
        share.click();
    }

    public void switchEditFile() {
        Log.log(Level.FINE, "Starts: Switch edit button");
        edit.click();
    }

    public boolean isCreateSelected() {
        return checkSwitch(createboxId);
    }

    public boolean isChangeSelected() {
        return checkSwitch(changeboxId);
    }

    public boolean isDeleteSelected() {
        return checkSwitch(deleteboxId);
    }

    public boolean isEditFileEnabled() {
        return checkSwitch(editboxId);
    }

    public boolean isShareEnabled() {
        return parseIntBool(findId(shareboxId).getAttribute("checked"));
    }

    public boolean isEditEnabled() {
        return parseIntBool(findId(editboxId).getAttribute("checked"));
    }

    private boolean checkSwitch(String id) {
        if (!findListId(id).isEmpty()) {
            return parseIntBool(findId(id).getAttribute("checked"));
        } else {
            return false;
        }
    }

    public void close() {
        close.click();
    }
}
