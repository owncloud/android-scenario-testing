/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import static e2e.support.log.Log.Log;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.logging.Level;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class SidebarPage extends CommonPage {

    @AndroidFindBy(id = HELP_MENU_ITEM_ID)
    private WebElement helpMenuItem;

    @AndroidFindBy(id = PRIVACY_POLICY_MENU_ITEM_ID)
    private WebElement privacyPolicyMenuItem;

    @AndroidFindBy(uiAutomator = HELP_WEB_UIAUTOMATOR_TEXT)
    private WebElement helpWebIndicator;

    @AndroidFindBy(uiAutomator = PRIVACY_POLICY_UIAUTOMATOR_TEXT)
    private WebElement privacyPolicyWebIndicator;

    private static final String HELP_MENU_ITEM_ID = "com.owncloud.android:id/drawer_menu_help";
    private static final String PRIVACY_POLICY_MENU_ITEM_ID = "com.owncloud.android:id/drawer_menu_privacy_policy";
    private static final String HELP_WEB_UIAUTOMATOR_TEXT = "new UiSelector().text(\"The Mobile App for Android\")";
    private static final String PRIVACY_POLICY_UIAUTOMATOR_TEXT = "new UiSelector().text(\"Android App Privacy Policy\")";

    public SidebarPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void openHelp() {
        Log.log(Level.FINE, "Starts: Open Help web view");
        helpMenuItem.click();
    }

    public void openPrivacyPolicy() {
        Log.log(Level.FINE, "Starts: Open Privacy Policy web view");
        privacyPolicyMenuItem.click();
    }

    public boolean isHelpWebDisplayed() {
        Log.log(Level.FINE, "Starts: Check if Help web is displayed");
        skipBrowserIntroductionIfPresent();
        return helpWebIndicator.isDisplayed();
    }

    public boolean isPrivacyPolicyWebDisplayed() {
        Log.log(Level.FINE, "Starts: Check if Privacy Policy web is displayed");
        skipBrowserIntroductionIfPresent();
        return privacyPolicyWebIndicator.isDisplayed();
    }

    private void skipBrowserIntroductionIfPresent() {
        Log.log(Level.FINE, "Starts: Skip browser introduction if present");
        try {
            WebElement skipButton = driver.findElement(By.id("com.android.chrome:id/signin_fre_dismiss_button"));
            if (skipButton.isDisplayed()) {
                skipButton.click();
                WebElement noThanksButton = driver.findElement(By.id("com.android.chrome:id/positive_button"));
                if (noThanksButton.isDisplayed()) {
                    noThanksButton.click();
                    WebElement allowButton = driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button"));
                    if (allowButton.isDisplayed()) {
                        allowButton.click();
                    }
                }
            }
        } catch (Exception e) {
            Log.log(Level.FINE, "No browser introduction present, continuing...");
        }
    }
}
