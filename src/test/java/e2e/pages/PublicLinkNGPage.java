/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.logging.Level;

import e2e.support.date.DateUtils;
import io.appium.java_client.AppiumBy;
import e2e.support.log.Log;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.support.PageFactory;

public class PublicLinkNGPage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/add_public_link_button")
    private WebElement addPublicLinkButton;

    @AndroidFindBy(id = "com.owncloud.android:id/permissions_title")
    private WebElement permissionsTitle;

    @AndroidFindBy(id = "com.owncloud.android:id/set_password_button")
    private WebElement setPasswordButton;

    @AndroidFindBy(id = "com.owncloud.android:id/password_value")
    private WebElement passwordValue;

    @AndroidFindBy(id = "com.owncloud.android:id/public_link_name_edit_text")
    private WebElement linkNameInput;

    @AndroidFindBy(id = "com.owncloud.android:id/expiration_date_switch")
    private WebElement expirationDateSwitch;

    @AndroidFindBy(id = "com.owncloud.android:id/create_public_link_button")
    private WebElement createPublicLinkButton;

    @AndroidFindBy(id = "com.owncloud.android:id/remove_password_button")
    private WebElement removePasswordButton;

    @AndroidFindBy(id = "android:id/next")
    private WebElement nextButton;

    @AndroidFindBy(id = "android:id/button1")
    private WebElement okButton;

    private static final String PUBLIC_LINK_ITEM_LAYOUT_ID = "com.owncloud.android:id/public_link_item_layout";
    private static final String PUBLIC_LINK_DISPLAY_NAME_ID = "com.owncloud.android:id/public_link_display_name";
    private static final String EXPIRATION_DATE_ID = "com.owncloud.android:id/expiration_date";
    private static final String EDIT_PUBLIC_LINK_BUTTON_ID = "com.owncloud.android:id/edit_public_link_button";

    public PublicLinkNGPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void tapAddPublicLink() {
        Log.log(Level.FINE, "Tap add public link");
        addPublicLinkButton.click();
    }

    public void selectPermission(String permissionName) {
        Log.log(Level.FINE, "Select permission: " + permissionName);
        waitById(WAIT_TIME, permissionsTitle);
        findUIAutomatorSubText(permissionName).click();
    }

    public void enterPassword(String password) {
        Log.log(Level.FINE, "Enter password");
        setPasswordButton.click();
        passwordValue.sendKeys(password);
        setPasswordButton.click();
    }

    public void enterName(String name) {
        Log.log(Level.FINE, "Enter link name: " + name);
        linkNameInput.clear();
        linkNameInput.sendKeys(name);
    }

    public void setExpirationDays(int days) {
        Log.log(Level.FINE, "Set expiration days: " + days);
        expirationDateSwitch.click();
        String dateToSet = DateUtils.dateInDaysAndroidFormat(String.valueOf(days));
        Log.log(Level.FINE, "Date to set: " + dateToSet);
        if (findListAccesibility(dateToSet).isEmpty()) {
            nextButton.click();
        }
        findAccesibility(dateToSet).click();
        okButton.click();
    }

    public boolean isConfirmEnabled() {
        return createPublicLinkButton.isEnabled();
    }

    public void confirmCreation() {
        Log.log(Level.FINE, "Confirm public link creation");
        createPublicLinkButton.click();
        waitById(WAIT_TIME, addPublicLinkButton);
    }

    public void tapEditPublicLink(String linkName) {
        Log.log(Level.FINE, "Tap edit public link: " + linkName);
        WebElement link = findLinkByNameOrNull(linkName);
        link.findElement(AppiumBy.id(EDIT_PUBLIC_LINK_BUTTON_ID)).click();
        waitById(WAIT_TIME, permissionsTitle);
    }

    public boolean isSaveEnabled() {
        return createPublicLinkButton.isEnabled();
    }

    public void confirmEdit() {
        Log.log(Level.FINE, "Confirm public link edit");
        createPublicLinkButton.click();
        waitById(WAIT_TIME, addPublicLinkButton);
    }

    public void setNameOnEdit(String name) {
        Log.log(Level.FINE, "Set link name on edit: " + name);
        linkNameInput.clear();
        if (!name.equals("(none)")) {
            linkNameInput.sendKeys(name);
        }
    }

    public void enterPasswordOnEdit(String password) {
        Log.log(Level.FINE, "Enter password on edit");
        // when a password is already set, only remove_password_button is visible;
        // clicking it clears the password and reveals set_password_button
        removePasswordButton.click();
        setPasswordButton.click();
        passwordValue.sendKeys(password);
        setPasswordButton.click();
    }

    public void removePasswordOnEdit() {
        Log.log(Level.FINE, "Remove password on edit");
        removePasswordButton.click();
    }

    public void setExpirationDaysOnEdit(int days) {
        Log.log(Level.FINE, "Set expiration days on edit: " + days);
        // switch is ON when a date already exists; turn it OFF so setExpirationDays's
        // click arrives at an OFF switch and correctly opens the date picker
        if ("true".equals(expirationDateSwitch.getAttribute("checked"))) {
            expirationDateSwitch.click();
        }
        setExpirationDays(days);
    }

    public void removeExpirationDateOnEdit() {
        Log.log(Level.FINE, "Remove expiration date on edit");
        expirationDateSwitch.click();
    }

    public boolean isPublicLinkListed(String linkName) {
        return findLinkByNameOrNull(linkName) != null;
    }

    public boolean isPublicLinkListedWithPermission(String linkName, String permission) {
        WebElement link = findLinkByNameOrNull(linkName);
        if (link == null) {
            return false;
        }
        return !link.findElements(AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + permission + "\")")).isEmpty();
    }

    public String getPublicLinkExpirationDays(String linkName) {
        WebElement link = findLinkByNameOrNull(linkName);
        if (link == null) {
            return null;
        }
        List<WebElement> elements = link.findElements(AppiumBy.id(EXPIRATION_DATE_ID));
        return elements.isEmpty() ? null : elements.get(0).getText();
    }

    private WebElement findLinkByNameOrNull(String linkName) {
        for (WebElement link : waitForListById(WAIT_TIME, PUBLIC_LINK_ITEM_LAYOUT_ID)) {
            List<WebElement> nameElements = link.findElements(AppiumBy.id(PUBLIC_LINK_DISPLAY_NAME_ID));
            if (!nameElements.isEmpty() && linkName.equals(nameElements.get(0).getText())) {
                return link;
            }
        }
        return null;
    }
}
