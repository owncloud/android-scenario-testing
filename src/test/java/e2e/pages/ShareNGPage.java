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

    @AndroidFindBy(id = "com.owncloud.android:id/member_item_layout")
    private List<WebElement> shareList;

    private static final String SHAREE_NAME_ID = "com.owncloud.android:id/member_name";
    private static final String EXPIRATION_DATE_ID = "com.owncloud.android:id/expiration_date";

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

    private WebElement findShareByNameOrNull(String sharee) {
        for (WebElement item : shareList) {
            WebElement nameElement = item.findElement(AppiumBy.id(SHAREE_NAME_ID));
            if (nameElement.getText().contains(sharee)) {
                return item;
            }
        }
        return null;
    }
}
