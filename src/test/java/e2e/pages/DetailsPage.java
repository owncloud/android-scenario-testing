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

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class DetailsPage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/lytName")
    private WebElement thumbnail;

    @AndroidFindBy(id = "com.owncloud.android:id/fdFilename")
    private WebElement itemName;

    @AndroidFindBy(id = "com.owncloud.android:id/fdType")
    private WebElement itemType;

    @AndroidFindBy(id = "com.owncloud.android:id/fdSize")
    private WebElement itemSize;

    @AndroidFindBy(id = "com.owncloud.android:id/text_preview")
    private WebElement textPreview;

    @AndroidFindBy(id = "com.owncloud.android:id/visual_area")
    private WebElement visualArea;

    @AndroidFindBy(id = "com.owncloud.android:id/media_controller")
    private WebElement mediaControls;

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

    private static final String TEXT_PREVIEW_ID = "com.owncloud.android:id/text_preview";

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

    public void closeShareSheet() {
        if (toolbar.isEmpty()) {
            driver.navigate().back();
        }
    }

    public void tapThumbnail() {
        thumbnail.click();
    }

    public boolean isTextDisplayedInPreview(String text) {
        // Giving additional time since it could take a bit longer to load the text preview
        waitUntilTextIsNotEmpty(WAIT_TIME + 10, TEXT_PREVIEW_ID);
        return findUIAutomatorText(text).isDisplayed();
    }

    public boolean isShareSheetDisplayed(String itemName) {
        return contentPanel.isDisplayed();
    }

    public boolean isItemPreviewed() {
        return textPreview.isDisplayed();
    }

    public void acceptImagePreviewDialogIfDisplayed() {
        if (!gotIt.isEmpty()) {
            Log.log(Level.FINE, "Accepting image preview dialog");
            gotIt.get(0).click();
        }
    }

    public boolean isImagePreviewed() {
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
