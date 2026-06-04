/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package android;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.logging.Level;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.log.Log;

public class InputNamePage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/user_input")
    private WebElement newName;

    @AndroidFindBy(id = "android:id/button1")
    private WebElement acceptButton;

    public InputNamePage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void setItemName(String itemName) {
        Log.log(Level.FINE, "Start: Set name to item: " + itemName);
        newName.clear();
        newName.sendKeys(itemName);
        acceptButton.click();
    }
}
