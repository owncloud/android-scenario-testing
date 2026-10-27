/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.logging.Level;

import e2e.support.log.Log;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.support.PageFactory;

public class ShareNGPage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/add_member_button")
    private WebElement addShareButton;

    @AndroidFindBy(id = "android:id/button1")
    private WebElement confirmButton;

    @AndroidFindBy(id = "com.owncloud.android:id/permanent_link_button")
    private WebElement permanentLinkButton;

    private static final String MEMBER_ITEM_LAYOUT_ID = "com.owncloud.android:id/member_item_layout";
    private static final String SHAREE_NAME_ID = "com.owncloud.android:id/member_name";
    private static final String EXPIRATION_DATE_ID = "com.owncloud.android:id/expiration_date";
    private static final String REMOVE_MEMBER_BUTTON_ID = "com.owncloud.android:id/remove_member_button";
    private static final String EDIT_MEMBER_BUTTON_ID = "com.owncloud.android:id/edit_member_button";

    public ShareNGPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void addShare() {
        Log.log(Level.FINE, "Tap add share");
        waitByTextVisible(WAIT_TIME, "No data shared with users yet");
        addShareButton.click();
    }

    public boolean isShareeDisplayed(String sharee) {
        WebElement item = findShareByNameOrNull(sharee);
        if (item == null) {
            return false;
        }
        return true;
    }

    public boolean isShareeDisplayedWithPermission(String sharee, String permission) {
        WebElement item = findShareByNameOrNull(sharee);
        if (item == null) {
            return false;
        }
        return !item.findElements(AppiumBy.androidUIAutomator(
                "new UiSelector().textContains(\"" + permission + "\")")).isEmpty();
    }

    public boolean isShareeDisplayedWithExpirationDate(String sharee, String expirationDate) {
        WebElement item = findShareByNameOrNull(sharee);
        if (item == null) {
            return false;
        }
        if (expirationDate == null) {
            return item.findElements(AppiumBy.id(EXPIRATION_DATE_ID)).isEmpty();
        }
        return !item.findElements(AppiumBy.androidUIAutomator(
                "new UiSelector().textContains(\"" + expirationDate + "\")")).isEmpty();
    }

    public void removeShare(String sharee) {
        Log.log(Level.FINE, "Remove share for: " + sharee);
        WebElement item = findShareByNameOrNull(sharee);
        item.findElement(AppiumBy.id(REMOVE_MEMBER_BUTTON_ID)).click();
        confirmButton.click();
    }

    public void editShare(String sharee) {
        Log.log(Level.FINE, "Edit share for: " + sharee);
        WebElement item = findShareByNameOrNull(sharee);
        item.findElement(AppiumBy.id(EDIT_MEMBER_BUTTON_ID)).click();
    }

    public boolean isPermalinkButtonDisplayed() {
        Log.log(Level.FINE, "Check if permalink button is displayed");
        return !findListId("com.owncloud.android:id/permanent_link_button").isEmpty();
    }

    public void tapPermalinkButton() {
        Log.log(Level.FINE, "Tap permalink button");
        permanentLinkButton.click();
    }

    public boolean isPermalinkActionTriggered() {
        Log.log(Level.FINE, "Check if system share sheet is triggered");
        return !waitForListById(WAIT_TIME, "com.android.intentresolver:id/chooser_headline_row_container").isEmpty();
    }

    public void closeShareSheet() {
        Log.log(Level.FINE, "Dismiss system share sheet");
        driver.navigate().back();
    }

    private WebElement findShareByNameOrNull(String sharee) {
        for (WebElement item : waitForListById(WAIT_TIME, MEMBER_ITEM_LAYOUT_ID)) {
            List<WebElement> nameElements = item.findElements(AppiumBy.id(SHAREE_NAME_ID));
            if (!nameElements.isEmpty() && nameElements.get(0).getText().contains(sharee)) {
                return item;
            }
        }
        return null;
    }
}
