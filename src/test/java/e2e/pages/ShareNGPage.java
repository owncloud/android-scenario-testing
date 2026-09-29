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
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class ShareNGPage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/add_member_button")
    private WebElement addShareButton;

    @AndroidFindBy(id = "android:id/button1")
    private WebElement confirmButton;

    private static final String MEMBER_ITEM_LAYOUT_ID = "com.owncloud.android:id/member_item_layout";
    private static final String SHAREE_NAME_ID = "com.owncloud.android:id/member_name";
    private static final String EXPIRATION_DATE_ID = "com.owncloud.android:id/expiration_date";
    private static final String REMOVE_MEMBER_BUTTON_ID = "com.owncloud.android:id/remove_member_button";

    public ShareNGPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void addShare() {
        Log.log(Level.FINE, "Tap add share");
        waitByTextVisible(WAIT_TIME, "No data shared with users yet");
        addShareButton.click();
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

    private WebElement findShareByNameOrNull(String sharee) {
        waitById(WAIT_TIME, MEMBER_ITEM_LAYOUT_ID);
        for (WebElement item : findListId(MEMBER_ITEM_LAYOUT_ID)) {
            List<WebElement> nameElements = item.findElements(AppiumBy.id(SHAREE_NAME_ID));
            if (!nameElements.isEmpty() && nameElements.get(0).getText().contains(sharee)) {
                return item;
            }
        }
        return null;
    }
}
