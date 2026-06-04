/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package android;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.logging.Level;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.log.Log;

public class DetailsPage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/lytName")
    private WebElement thumbnail;

    @AndroidFindBy(id = "com.owncloud.android:id/fdFilename")
    private WebElement itemName;

    @AndroidFindBy(id = "com.owncloud.android:id/fdType")
    private WebElement itemType;

    @AndroidFindBy(id = "com.owncloud.android:id/fdSize")
    private WebElement itemSize;

    @AndroidFindBy(id = "com.owncloud.android:id/fdProgressText")
    private WebElement downloading;

    @AndroidFindBy(id = "com.owncloud.android:id/text_preview")
    private WebElement textPreview;

    @AndroidFindBy(id = "com.owncloud.android:id/visual_area")
    private WebElement visualArea;

    @AndroidFindBy(id = "com.owncloud.android:id/media_controller")
    private WebElement mediaControls;

    @AndroidFindBy(id = "com.owncloud.android:id/currentTimeText")
    private WebElement currentTime;

    @AndroidFindBy(id = "com.owncloud.android:id/totalTimeText")
    private WebElement totalTime;

    @AndroidFindBy(id = "com.owncloud.android:id/photo_view")
    private WebElement photoPreview;

    @AndroidFindBy(id = "android:id/ok")
    private List<WebElement> gotIt;

    @AndroidFindBy(id = "com.owncloud.android:id/video_player")
    private WebElement videoPreview;

    @AndroidFindBy(id = "com.owncloud.android:id/errorText")
    private WebElement damagedPreview;

    @AndroidFindBy(id = "toolbar")
    private List<WebElement> toolbar;

    @AndroidFindBy(id = "android:id/contentPanel")
    private WebElement contentPanel;

    @AndroidFindBy(xpath = "//android.widget.ImageButton[@content-desc=\"Back\"]")
    private WebElement back;

    public DetailsPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public String getName() {
        return itemName.getText();
    }

    public String getType() {
        return itemType.getText();
    }

    public String getSize() {
        return itemSize.getText();
    }

    public void backListFiles() {
        Log.log(Level.FINE, "Start: Back to the list of files");
        back.click();
    }

    public void displayControls() {
        photoPreview.click();
    }

    public void removeShareSheet() {
        if (toolbar.isEmpty()) {
            driver.navigate().back();
        }
    }

    public void downloadFromThumbnail() {
        thumbnail.click();
    }

    public boolean isTextInFile(String text) {
        waitUntilTextIsNotEmpty(WAIT_TIME, "com.owncloud.android:id/text_preview");
        return findUIAutomatorText(text).isDisplayed();
    }

    public boolean isShareSheetDisplayed(String itemName) {
        return contentPanel.isDisplayed();
    }

    public boolean isItemPreviewed() {
        return textPreview.isDisplayed();
    }

    public boolean isImagePreviewed() {
        //If Android's ugly dialog is displayed
        if (!gotIt.isEmpty()) {
            gotIt.get(0).click();
        }
        return photoPreview.isDisplayed();
    }

    public boolean isAudioPreviewed() {
        boolean isArtDisplayed = visualArea.isDisplayed();
        boolean areControlsDisplayed = mediaControls.isDisplayed();
        return isArtDisplayed && areControlsDisplayed;
    }

    public boolean isVideoPreviewed() {
        return videoPreview.isDisplayed();
    }

    public boolean isDamagedPreviewed() {
        return damagedPreview.isDisplayed();
    }
}