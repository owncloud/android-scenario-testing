/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.logging.Level;

import e2e.support.log.Log;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class ConflictPage extends CommonPage {

    @AndroidFindBy(id = "android:id/button1")
    private WebElement localVersionButton;

    @AndroidFindBy(id = "android:id/button2")
    private WebElement serverVersionButton;

    @AndroidFindBy(id = "android:id/button3")
    private WebElement bothVersionButton;

    public ConflictPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    /*public void fixConflict (String conflict) {
        switch (conflict) {
            case "local" -> selectLocalVersion();
            case "server" -> selectServerVersion();
            case "keep both" -> selectBothVersions();
        }
    }*/

    public void selectLocalVersion() {
        Log.log(Level.FINE, "Starts: Fix conflict with local version");
        localVersionButton.click();
    }

    public void selectServerVersion() {
        Log.log(Level.FINE, "Starts: Fix conflict with server version");
        serverVersionButton.click();
    }

    public void selectBothVersions() {
        Log.log(Level.FINE, "Starts: Fix conflict by keeping both versions");
        bothVersionButton.click();
    }

    public boolean isConflictPageDisplayed() {
        return isButtonVisibleWithText(localVersionButton, "LOCAL VERSION") &&
                isButtonVisibleWithText(serverVersionButton, "SERVER VERSION") &&
                isButtonVisibleWithText(bothVersionButton, "KEEP BOTH");
    }

    private boolean isButtonVisibleWithText(WebElement button, String expectedText) {
        return button.isDisplayed() && button.getText().contains(expectedText);
    }
}
