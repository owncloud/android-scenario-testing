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

public class RemoveDialogPage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/dialog_remove_yes")
    private WebElement buttonYes;

    @AndroidFindBy(id = "com.owncloud.android:id/dialog_remove_local_only")
    private WebElement buttonLocal;

    @AndroidFindBy(id = "com.owncloud.android:id/dialog_remove_no")
    private WebElement buttonNo;

    @AndroidFindBy(id = "android:id/button2")
    private WebElement buttonLocalFolders;

    public RemoveDialogPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void removeAll() {
        Log.log(Level.FINE, "Starts: Remove from server and local");
        buttonYes.click();
    }

    public void dontRemove() {
        Log.log(Level.FINE, "Starts: Cancel Remove");
        buttonNo.click();
    }

    public void onlyLocal() {
        Log.log(Level.FINE, "Starts: Remove only from local");
        buttonLocal.click();
    }

    public void onlyLocalFolders() {
        Log.log(Level.FINE, "Starts: Remove only from local");
        buttonLocalFolders.click();
    }
}
