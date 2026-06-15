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

public class LinksPage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkNameValue")
    private WebElement namePublicLink;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkEditPermissionReadOnly")
    private WebElement downloadViewOption;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkEditPermissionReadAndWrite")
    private WebElement downloadViewUploadOption;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkEditPermissionUploadFiles")
    private WebElement uploadOnlyOption;

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

    public LinksPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void addLinkName(String linkName) {
        Log.log(Level.FINE, "Starts: Add link name: " + linkName);
        namePublicLink.clear();
        namePublicLink.sendKeys(linkName);
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

    public void typePassword(String password) {
        Log.log(Level.FINE, "Starts: Add link password");
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

    public void clickExpirationSwitch() {
        Log.log(Level.FINE, "Starts: Click expiration switch");
        expirationSwitch.click();
    }

    public void clickNextButton() {
        Log.log(Level.FINE, "Starts: Click next button");
        nextButton.click();
    }

    public void clickOkButton() {
        Log.log(Level.FINE, "Starts: Click OK button");
        okButton.click();
    }

    public boolean isDownloadViewSelected() {
        return parseIntBool(downloadViewOption.getAttribute("checked"));
    }

    public boolean isDownloadViewUploadSelected() {
        return parseIntBool(downloadViewUploadOption.getAttribute("checked"));
    }

    public boolean isUploadOnlySelected() {
        return parseIntBool(uploadOnlyOption.getAttribute("checked"));
    }

    public boolean isPasswordEnabled() {
        return textPassword.isDisplayed();
    }

    public boolean isExpirationSwitchEnabled() {
        return parseIntBool(expirationSwitch.getAttribute("checked"));
    }

    public boolean isExpirationDateCorrect(String shortDate) {
        return expirationDate.getText().equals(shortDate);
    }

    public void close() {
        Log.log(Level.FINE, "Starts: Cancel public link view");
        cancelButton.click();
    }

    public void clickSave() {
        Log.log(Level.FINE, "Starts: Submit public link");
        //Depending on the screen size, the save button could need some scroll
        swipe(0.50, 0.60, 0.50, 0.20);
        saveButton.click();
    }
}
