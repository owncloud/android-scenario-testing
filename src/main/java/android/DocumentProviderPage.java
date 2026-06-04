package android;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.logging.Level;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.log.Log;

public class DocumentProviderPage extends CommonPage {

    @AndroidFindBy(xpath = "//android.widget.ImageButton[@content-desc=\"Show roots\"]")
    private WebElement hamburger;

    private final String sideMenuDocsProviderId =
            "new UiSelector().resourceId(\"com.google.android.documentsui:id/roots_list\")";
    private final String downloadOptionId = "new UiSelector().textContains(\"Downloads\");";

    public DocumentProviderPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void openDownloadsInHamburger() {
        Log.log(Level.FINE, "Starts: Open hamburger button in documents provider");
        hamburger.click();
        // first find side menu to look for options inside it
        WebElement sideMenu = driver.findElement(AppiumBy.androidUIAutomator (sideMenuDocsProviderId));
        List<WebElement> download = sideMenu.findElements(AppiumBy.androidUIAutomator(downloadOptionId));
        download.get(0).click();
    }

    public void selectFileToUpload(String fileName) {
        Log.log(Level.FINE, "Starts: Select File to Upload: " + fileName);
        openDownloadsInHamburger();
        findUIAutomatorText(fileName).click();
        // Give some time to the app to finish the upload
        waitById(WAIT_TIME, "com.owncloud.android:id/bottom_nav_view");
    }

    public void selectImageToUpload(String fileName) {
        Log.log(Level.FINE, "Starts: Select Image to Upload: " + fileName);
        openDownloadsInHamburger();
        try {
            findUIAutomatorText(fileName).click();
        } catch (NoSuchElementException e) {
            findId("com.google.android.documentsui:id/sub_menu").click();
        }
        // Give some time to the app to finish the upload
        waitById(WAIT_TIME, "com.owncloud.android:id/bottom_nav_view");
    }
}
