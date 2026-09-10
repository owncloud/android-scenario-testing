/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.logging.Level;

import e2e.support.log.Log;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class ShareNGCreatePage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/search_src_text")
    private WebElement searchInput;

    @AndroidFindBy(id = "com.owncloud.android:id/member_name")
    private List<WebElement> searchResults;

    @AndroidFindBy(id = "com.owncloud.android:id/permissions_title")
    private WebElement permissionsTitle;

    @AndroidFindBy(id = "com.owncloud.android:id/expiration_date_switch")
    private WebElement expirationDateSwitch;

    @AndroidFindBy(id = "android:id/next")
    private WebElement nextButton;

    @AndroidFindBy(id = "android:id/button1")
    private WebElement okButton;

    @AndroidFindBy(id = "com.owncloud.android:id/invite_member_button")
    private WebElement inviteButton;

    public ShareNGCreatePage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void selectSharee(String userName) {
        Log.log(Level.FINE, "Search and select sharee: " + userName);
        searchInput.sendKeys(userName);
        searchResults.get(0).click();
    }

    public void setPermission(String permission) {
        Log.log(Level.FINE, "Set permission: " + permission);
        waitById(WAIT_TIME, permissionsTitle);
        findUIAutomatorSubText(permission).click();
    }

    public boolean isExpirationDateEnabled() {
        return "true".equals(expirationDateSwitch.getAttribute("checked"));
    }

    public void toggleExpirationDate() {
        Log.log(Level.FINE, "Toggle expiration date");
        expirationDateSwitch.click();
    }

    public boolean isCalendarDateVisible(String date) {
        return !findListAccesibility(date).isEmpty();
    }

    public void tapNextCalendarPage() {
        Log.log(Level.FINE, "Tap next calendar page");
        nextButton.click();
    }

    public void selectCalendarDate(String date) {
        Log.log(Level.FINE, "Select calendar date: " + date);
        findAccesibility(date).click();
    }

    public void tapOk() {
        Log.log(Level.FINE, "Tap OK");
        okButton.click();
    }

    public void invite() {
        Log.log(Level.FINE, "Confirm invite");
        waitById(WAIT_TIME, inviteButton);
        inviteButton.click();
    }
}
