/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class UploadsPage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/uploadListGroupButtonClear")
    private WebElement clear;

    public UploadsPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void clearList() {
        clear.click();
    }

    public boolean isFileUploaded(String fileName) {
        boolean fileInList = !findUIAutomatorText(fileName).isDisplayed();
        boolean uploadedListVisible = findUIAutomatorText("UPLOADED").isDisplayed();
        boolean failedListNotVisible = findListUIAutomatorText("FAILED").isEmpty();
        boolean enqueuedListNotVisible = findListUIAutomatorText("ENQUEUED").isEmpty();
        return fileInList && uploadedListVisible && failedListNotVisible
                && enqueuedListNotVisible;
    }
}
