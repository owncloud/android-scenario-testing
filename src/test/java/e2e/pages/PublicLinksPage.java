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

import e2e.support.date.DateUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class PublicLinksPage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkNameValue")
    private WebElement namePublicLink;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkEditPermissionReadOnly")
    private WebElement downloadViewOption;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkEditPermissionReadAndWrite")
    private WebElement downloadViewUploadOption;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkEditPermissionUploadFiles")
    private WebElement uploadOnlyOption;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkPasswordSwitch")
    private List<WebElement> passwordSwitch;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkPasswordValue")
    private WebElement textPassword;

    @AndroidFindBy(id = "generatePasswordButton")
    private WebElement generatePassword;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkExpirationSwitch")
    private WebElement expirationSwitch;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkExpirationValue")
    private WebElement expirationDate;

    @AndroidFindBy(id = "android:id/button1")
    private WebElement okButton;

    @AndroidFindBy(id = "android:id/next")
    private WebElement nextButton;

    @AndroidFindBy(id = "com.owncloud.android:id/cancelButton")
    private WebElement cancelButton;

    @AndroidFindBy(id = "com.owncloud.android:id/saveButton")
    private WebElement saveButton;

    public PublicLinksPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void addLinkName(String linkName) {
        Log.log(Level.FINE, "Starts: Add link name: " + linkName);
        namePublicLink.clear();
        namePublicLink.sendKeys(linkName);
    }

    public void setPermission(String permission) {
        Log.log(Level.FINE, "Starts: Set link permission: " + permission);
        switch (permission) {
            case ("1") -> downloadViewOption.click();
            case ("15")-> downloadViewUploadOption.click();
            case ("4") -> uploadOnlyOption.click();
        }
    }

    public void selectDownloadView() {
        Log.log(Level.FINE, "Starts: Select Download / View");
        downloadViewOption.click();
    }

    public void selectDownloadViewUpload() {
        Log.log(Level.FINE, "Starts: Select Download / View / Upload");
        downloadViewUploadOption.click();
    }

    public void selectUploadOnly() {
        Log.log(Level.FINE, "Starts: Select Upload Only (File drop)");
        uploadOnlyOption.click();
    }

    public boolean arePermissionsCorrect(String permissions) {
        Log.log(Level.FINE, "Starts: Check permissions: " + permissions);
        switch (permissions) {
            case ("1") -> {
                if (parseIntBool(downloadViewOption.getAttribute("checked"))) {
                    Log.log(Level.FINE, "Download / View is selected");
                    return true;
                }
            }
            case ("15")-> {
                if (parseIntBool(downloadViewUploadOption.getAttribute("checked"))) {
                    Log.log(Level.FINE, "Download / View / Upload is selected");
                    return true;
                }
            }
            case ("4") -> {
                if (parseIntBool(uploadOnlyOption.getAttribute("checked"))) {
                    Log.log(Level.FINE, "Upload only is selected");
                    return true;
                }
            }
        }
        return false;
    }

    public void typePassword(String itemName, String password) {
        Log.log(Level.FINE, "Starts: Add link password: " + password);
        //To avoid password keyboard to appear
        if (driver.isKeyboardShown()) {
            driver.hideKeyboard();
        }
        textPassword.sendKeys(password);
    }

    public void generatePassword() {
        Log.log(Level.FINE, "Starts: Generate password");
        //To avoid password keyboard to appear
        if (driver.isKeyboardShown()) {
            driver.hideKeyboard();
        }
        generatePassword.click();
    }

    public boolean isPasswordEnabled() {
        boolean switchEnabled = true;
        boolean passVisible;
        passVisible = textPassword.isDisplayed();
        return switchEnabled && passVisible;
    }

    public void setExpiration(String days) {
        Log.log(Level.FINE, "Starts: Set Expiration date in days: " + days);
        expirationSwitch.click();
        String dateToSet = DateUtils.dateInDaysAndroidFormat(days);
        Log.log(Level.FINE, "Days: " + days + " Date to set: " + dateToSet);
        if (findListAccesibility(dateToSet).isEmpty()) {
            Log.log(Level.FINE, "Date not found, next page");
            nextButton.click();
        }
        findAccesibility(dateToSet).click();
        okButton.click();
    }

    public boolean isExpirationCorrect(String days) {
        Log.log(Level.FINE, "Starts: Check expiration in days: " + days);
        boolean switchEnabled;
        boolean dateCorrect = false;
        int expiration = Integer.parseInt(days);
        String shortDate = DateUtils.formatDate(Integer.toString(expiration), DateUtils.DateFormatType.TEXT);
        Log.log(Level.FINE, "Date to check: " + shortDate + " Expiration: " + expiration);
        switchEnabled = parseIntBool(expirationSwitch.getAttribute("checked"));
        Log.log(Level.FINE, "SwitchEnabled -> " + switchEnabled);
        if (switchEnabled) {
            dateCorrect = expirationDate.getText().equals(shortDate);
        }
        Log.log(Level.FINE, "Date Correct -> " + dateCorrect);
        return switchEnabled && dateCorrect;
    }

    public void close() {
        Log.log(Level.FINE, "Starts: Cancel public link view");
        cancelButton.click();
    }

    public void submitLink() {
        Log.log(Level.FINE, "Starts: Submit public link");
        //Depending on the screen size, the save button could need some scroll
        swipe(0.50, 0.60, 0.50, 0.20);
        saveButton.click();
    }
}
