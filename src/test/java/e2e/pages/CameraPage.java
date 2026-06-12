/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.logging.Level;

import e2e.support.log.Log;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class CameraPage extends CommonPage {

    @AndroidFindBy(id = "android:id/ok")
    private List<WebElement> gotIt;

    @AndroidFindBy(id = "com.android.camera2:id/done_button")
    private WebElement doneButton;

    public CameraPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void waitUntilCameraIsDisplayed() {
        Log.log(Level.FINE, "Waiting until camera is displayed");
        String cameraViewId = "com.android.camera2:id/activity_root_view";
        waitById(WAIT_TIME, cameraViewId);
    }

    public void tapShutterButton() {
        Log.log(Level.FINE, "Tapping camera shutter by coordinates");
        double SHUTTER_X = 0.50;
        double SHUTTER_Y = 0.90;
        Dimension size = driver.manage().window().getSize();
        double x = size.width * SHUTTER_X;
        double y = size.height * SHUTTER_Y;
        tap((int) x, (int) y);
    }

    public void confirmPicture() {
        Log.log(Level.FINE, "Confirming picture");
        doneButton.click();
    }
}
