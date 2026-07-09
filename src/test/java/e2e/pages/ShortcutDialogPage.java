/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import static e2e.support.log.Log.Log;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Level;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class ShortcutDialogPage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/create_shortcut_dialog_url_value")
    private WebElement shortcutURL;

    @AndroidFindBy(id = "com.owncloud.android:id/create_shortcut_dialog_name_file_value")
    private WebElement shortcutName;

    @AndroidFindBy(id = "com.owncloud.android:id/createButton")
    private WebElement submitShortcut;

    @AndroidFindBy(id = "android:id/button1")
    private WebElement openShortcut;

    public ShortcutDialogPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void typeURLName(String name, String url) {
        Log.log(Level.FINE, "Starts: enter shortcut fields: " + name + " " + url);
        shortcutURL.sendKeys(url);
        shortcutName.sendKeys(name);
    }

    public void submitShortcut() {
        Log.log(Level.FINE, "Starts: submit shortcut");
        submitShortcut.click();
    }

    public void openShortcut() {
        Log.log(Level.FINE, "Starts: open shortcut");
        openShortcut.click();
    }

    public boolean isBrowserOpen() {
        Log.log(Level.FINE, "Starts: check if browser is open");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME));
        try {
            return wait.until(d -> {
                String currentPackage = ((AndroidDriver) d).getCurrentPackage();
                Log.log(Level.FINE, "Current package while waiting browser: " + currentPackage);
                return currentPackage.equals("com.android.chrome") || currentPackage.equals("org.mozilla.firefox")
                        || currentPackage.equals("com.microsoft.emmx") || currentPackage.equals("com.android.browser");
            });
        } catch (TimeoutException e) {
            Log.log(Level.WARNING, "Browser was not opened. Last current package: " + driver.getCurrentPackage());
            return false;
        }
    }
}
