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

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class DocumentProviderPage extends CommonPage {

    @AndroidFindBy(xpath = "//android.widget.ImageButton[@content-desc=\"Show roots\"]")
    private WebElement hamburger;

    private static final String ROOTS_LIST_SELECTOR =
            "new UiSelector().resourceId(\"com.google.android.documentsui:id/roots_list\")";

    private static final String DOWNLOADS_OPTION_SELECTOR =
            "new UiSelector().textContains(\"Downloads\")";

    private static final String OWNCLOUD_BOTTOM_NAV_VIEW_ID =
            "com.owncloud.android:id/bottom_nav_view";

    public DocumentProviderPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void openRootsMenu() {
        Log.log(Level.FINE, "Open roots menu in Android document provider");
        hamburger.click();
    }

    public void selectDownloadsRoot() {
        Log.log(Level.FINE, "Select Downloads root in Android document provider");
        WebElement sideMenu = driver.findElement(AppiumBy.androidUIAutomator(ROOTS_LIST_SELECTOR));
        List<WebElement> downloads = sideMenu.findElements(AppiumBy.androidUIAutomator(DOWNLOADS_OPTION_SELECTOR));
        downloads.get(0).click();
    }

    public void selectFile(String fileName) {
        Log.log(Level.FINE, "Select file in Android document provider: " + fileName);
        findUIAutomatorText(fileName).click();
    }

    public void waitUntilOwnCloudIsDisplayed() {
        Log.log(Level.FINE, "Wait until ownCloud is displayed after document selection");
        waitById(WAIT_TIME, OWNCLOUD_BOTTOM_NAV_VIEW_ID);
    }
}
