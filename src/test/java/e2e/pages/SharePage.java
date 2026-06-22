/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.logging.Level;

import e2e.support.log.Log;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class SharePage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/addUserButton")
    private WebElement addPrivateShare;

    @AndroidFindBy(id = "com.owncloud.android:id/addPublicLinkButton")
    private WebElement addPublicLink;

    @AndroidFindBy(id = "com.owncloud.android:id/editShareButton")
    private WebElement editPrivateShare;

    @AndroidFindBy(id = "com.owncloud.android:id/userOrGroupName")
    private List<WebElement> shareeInfo;

    @AndroidFindBy(id = "com.owncloud.android:id/editPublicLinkButton")
    private WebElement editPublicLink;

    @AndroidFindBy(id = "com.owncloud.android:id/sharePublicLinksList")
    private List<WebElement> linkInfo;

    @AndroidFindBy(id = "com.owncloud.android:id/unshareButton")
    private WebElement removePrivateShare;

    @AndroidFindBy(id = "com.owncloud.android:id/deletePublicLinkButton")
    private WebElement removePublicLink;

    @AndroidFindBy(id = "android:id/button1")
    private WebElement acceptDeletion;

    @AndroidFindBy(id = "com.owncloud.android:id/shareNoPublicLinks")
    private WebElement noLinks;

    @AndroidFindBy(xpath = "//android.widget.ImageButton[@content-desc=\"Back\"]")
    private WebElement close;

    public SharePage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void addPrivateShare() {
        Log.log(Level.FINE, "Starts: add private share");
        addPrivateShare.click();
    }

    public void addLink() {
        Log.log(Level.FINE, "Starts: add public link");
        addPublicLink.click();
    }

    public void editPrivateShare(String itemName) {
        Log.log(Level.FINE, "Starts: edit private share: " + itemName);
        editPrivateShare.click();
    }

    public void editLink(String itemName) {
        Log.log(Level.FINE, "Starts: edit link: " + itemName);
        editPublicLink.click();
    }

    public void deletePrivateShare() {
        removePrivateShare.click();
    }

    public void deleteLink(String itemName) {
        Log.log(Level.FINE, "Starts: delete link: " + itemName);
        removePublicLink.click();
    }

    public boolean isItemInListPrivateShares(String sharee) {
        return !shareeInfo.isEmpty() && !findListUIAutomatorText(sharee).isEmpty();
    }

    public boolean isItemInListLinks(String itemName) {
        return !linkInfo.isEmpty() && !findListUIAutomatorText(itemName).isEmpty();
    }

    public boolean isListLinksEmpty() {
        return noLinks.isDisplayed();
    }

    public void acceptDeletion() {
        acceptDeletion.click();
    }

    public void close() {
        close.click();
    }
}
