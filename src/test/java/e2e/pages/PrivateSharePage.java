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

    @AndroidFindBy(id = CREATE_CHECKBOX_ID)
    private WebElement create;

    @AndroidFindBy(id = CHANGE_CHECKBOX_ID)
    private WebElement change;

    @AndroidFindBy(id = DELETE_CHECKBOX_ID)
    private WebElement delete;

    @AndroidFindBy(id = EDIT_SWITCH_ID)
    private WebElement edit;

    @AndroidFindBy(id = SHARE_SWITCH_ID)
    private WebElement share;

    @AndroidFindBy(id = "com.owncloud.android:id/closeButton")
    private WebElement close;

    private static final String CREATE_CHECKBOX_ID = "com.owncloud.android:id/canEditCreateCheckBox";
    private static final String CHANGE_CHECKBOX_ID = "com.owncloud.android:id/canEditChangeCheckBox";
    private static final String DELETE_CHECKBOX_ID = "com.owncloud.android:id/canEditDeleteCheckBox";
    private static final String EDIT_SWITCH_ID = "com.owncloud.android:id/canEditSwitch";
    private static final String SHARE_SWITCH_ID = "com.owncloud.android:id/canShareSwitch";

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
        return checkSwitch(CREATE_CHECKBOX_ID);
    }

    public boolean isChangeSelected() {
        return checkSwitch(CHANGE_CHECKBOX_ID);
    }

    public boolean isDeleteSelected() {
        return checkSwitch(DELETE_CHECKBOX_ID);
    }

    public boolean isEditFileEnabled() {
        return checkSwitch(EDIT_SWITCH_ID);
    }

    public boolean isShareEnabled() {
        return parseIntBool(findId(SHARE_SWITCH_ID).getAttribute("checked"));
    }

    public boolean isEditEnabled() {
        return parseIntBool(findId(EDIT_SWITCH_ID).getAttribute("checked"));
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
