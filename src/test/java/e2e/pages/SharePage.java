/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import e2e.model.OCShare;
import e2e.support.date.DateUtils;
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

    @AndroidFindBy(id = "android:id/button3")
    private WebElement cancelDeletion;

    @AndroidFindBy(id = "com.owncloud.android:id/shareNoUsers")
    private WebElement noPrivateShares;

    @AndroidFindBy(id = "com.owncloud.android:id/shareNoPublicLinks")
    private WebElement noPublicLinks;

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

    public void addPublicLink() {
        Log.log(Level.FINE, "Starts: add public link");
        addPublicLink.click();
    }

    public void openPrivateShare(String itemName) {
        Log.log(Level.FINE, "Starts: edit private share: " + itemName);
        editPrivateShare.click();
    }

    public void openPublicLink(String itemName) {
        Log.log(Level.FINE, "Starts: open public link: " + itemName);
        editPublicLink.click();
    }

    public boolean isItemInListPrivateShares(String sharee) {
        return !shareeInfo.isEmpty() &&
                !findListUIAutomatorText(sharee).isEmpty();
    }

    public boolean isItemInListPublicShares(String itemName) {
        return !linkInfo.isEmpty() &&
                !findListUIAutomatorText(itemName).isEmpty();
    }

    public boolean isListPublicLinksEmpty() {
        return noPublicLinks.isDisplayed();
    }

    public void deletePrivateShare() {
        removePrivateShare.click();
    }

    public void deletePublicShare() {
        removePublicLink.click();
    }

    public boolean isShareCorrect(OCShare remoteShare, Map<String, String> dataList) {
        Log.log(Level.FINE, "Starts: Check correct share");
        if (remoteShare == null) {
            Log.log(Level.FINE, "Remote share is null, returning false");
            return false;
        }
        for (Map.Entry<String, String> entry : dataList.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.log(Level.FINE, "Entry KEY: " + key + " - VALUE: " + value);
            switch (key) {
                case "id" -> {
                    if (!remoteShare.getId().equals(value)) {
                        Log.log(Level.FINE, "ID does not match - Remote: " + remoteShare.getId()
                                + " - Expected: " + value);
                        return false;
                    }
                }
                case "user" -> {
                    if (remoteShare.getType().equals("0")) { // private share
                        if (!remoteShare.getShareeName().equalsIgnoreCase(value)) {
                            Log.log(Level.FINE, "Sharee does not match - Remote: " + remoteShare.getShareeName()
                                    + " - Expected: " + value);
                            return false;
                        }
                    }
                }
                case "password" -> {
                    if (!(remoteShare.getType().equals("3") && remoteShare.hasPassword())) {
                        Log.log(Level.FINE, "Password not present");
                        return false;
                    }
                }
                case "name" -> {
                    if (!remoteShare.getLinkName().equals(value)) {
                        Log.log(Level.FINE, "Item name does not match - Remote: " + remoteShare.getLinkName()
                                + " - Expected: " + value);
                        return false;
                    }
                }
                case "path" -> {
                    if (!remoteShare.getItemName().equals(value)) {
                        Log.log(Level.FINE, "Item path does not match - Remote: " + remoteShare.getItemName()
                                + " - Expected: " + value);
                        return false;
                    }
                }
                case "uid_owner" -> {
                    if (!remoteShare.getOwner().equalsIgnoreCase(value)) {
                        Log.log(Level.FINE, "Owner name does not match - Remote: " + remoteShare.getOwner()
                                + " - Expected: " + value);
                        return false;
                    }
                }
                case "permissions" -> {
                    if (!remoteShare.getPermissions().equals(value)) {
                        Log.log(Level.FINE, "Permissions do not match - Remote: " + remoteShare.getPermissions()
                                + " - Expected: " + value);
                        return false;
                    }
                }
                case "expiration days" -> {
                    String dateRemote = remoteShare.getExpiration();
                    String expDate = DateUtils.dateInDaysWithServerFormat(value);
                    Log.log(Level.FINE, "Expiration dates: Remote: " + dateRemote
                            + " - Expected: " + expDate);
                    if (!dateRemote.equals(expDate)) {
                        Log.log(Level.FINE, "Expiration dates do not match");
                        return false;
                    }
                }
            }
        }
        Log.log(Level.FINE, "All fields match. Returning true");
        return true;
    }

    public void acceptDeletion() {
        acceptDeletion.click();
    }

    public void close() {
        close.click();
    }
}
