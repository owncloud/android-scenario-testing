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
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class SpaceMembersPage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/add_member_button")
    private WebElement addMemberButton;

    @AndroidFindBy(id = "com.owncloud.android:id/add_public_link_button")
    private WebElement addLinkButton;

    @AndroidFindBy(id = "com.owncloud.android:id/search_src_text")
    private WebElement memberSearchInput;

    @AndroidFindBy(id = "com.owncloud.android:id/public_link_name_edit_text")
    private WebElement linkNameInput;

    @AndroidFindBy(id = "com.owncloud.android:id/set_password_button")
    private WebElement setPasswordButton;

    @AndroidFindBy(id = "com.owncloud.android:id/remove_password_button")
    private WebElement removePasswordButton;

    @AndroidFindBy(id = "com.owncloud.android:id/generate_password_button")
    private WebElement generateRandomPasswordButton;

    @AndroidFindBy(id = "com.owncloud.android:id/set_password_button")
    private WebElement submitPasswordButton;

    @AndroidFindBy(id = "com.owncloud.android:id/permissions_title")
    private WebElement permissionsTitle;

    @AndroidFindBy(id = MEMBER_NAME_ID)
    private List<WebElement> memberSearchResults;

    @AndroidFindBy(id = "com.owncloud.android:id/expiration_date_switch")
    private WebElement expirationDateSwitch;

    @AndroidFindBy(id = "com.owncloud.android:id/invite_member_button")
    private WebElement inviteMemberButton;

    @AndroidFindBy(id = "com.owncloud.android:id/create_public_link_button")
    private WebElement createLinkButton;

    @AndroidFindBy(id = "com.owncloud.android:id/member_item_layout")
    private List<WebElement> memberList;

    @AndroidFindBy(id = "com.owncloud.android:id/public_link_item_layout")
    private List<WebElement> linkList;

    @AndroidFindBy(id = "android:id/next")
    private WebElement nextButton;

    @AndroidFindBy(id = "android:id/button1")
    private WebElement okButton;

    private static final String EDIT_MEMBER_BUTTON_ID = "com.owncloud.android:id/edit_member_button";
    private static final String MEMBER_NAME_ID = "com.owncloud.android:id/member_name";
    private static final String REMOVE_MEMBER_BUTTON_ID = "com.owncloud.android:id/remove_member_button";
    private static final String PUBLIC_LINK_DISPLAY_NAME_ID = "com.owncloud.android:id/public_link_display_name";
    private static final String EDIT_PUBLIC_LINK_BUTTON_ID = "com.owncloud.android:id/edit_public_link_button";
    private static final String REMOVE_PUBLIC_LINK_BUTTON_ID = "com.owncloud.android:id/remove_public_link_button";
    private static final String EXPIRATION_DATE_ID = "com.owncloud.android:id/expiration_date";
    private static final String YES_BUTTON_TEXT = "YES";

    public SpaceMembersPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void addMember() {
        Log.log(Level.FINE, "Tap add member");
        addMemberButton.click();
    }

    public void selectMember(String userName) {
        Log.log(Level.FINE, "Select member: " + userName);
        memberSearchInput.sendKeys(userName);
        memberSearchResults.get(0).click();
    }

    public void addLink() {
        Log.log(Level.FINE, "Tap add public link");
        addLinkButton.click();
    }

    public void setName(String linkName) {
        Log.log(Level.FINE, "Set public link name: " + linkName);
        waitById(WAIT_TIME, permissionsTitle);
        linkNameInput.clear();
        linkNameInput.sendKeys(linkName);
    }

    public void setPermission(String permission) {
        Log.log(Level.FINE, "Set permission: " + permission);
        waitById(WAIT_TIME, permissionsTitle);
        findUIAutomatorSubText(permission).click();
    }

    public void tapSetPassword() {
        Log.log(Level.FINE, "Tap set password");
        setPasswordButton.click();
    }

    public void tapRemovePassword() {
        Log.log(Level.FINE, "Tap remove password");
        removePasswordButton.click();
    }

    public void tapGenerateRandomPassword() {
        Log.log(Level.FINE, "Tap generate random password");
        generateRandomPasswordButton.click();
    }

    public void tapSubmitPassword() {
        Log.log(Level.FINE, "Submit generated password");
        submitPasswordButton.click();
    }

    public boolean isExpirationDateEnabled() {
        return "true".equals(expirationDateSwitch.getAttribute("checked"));
    }

    public void toggleExpirationDate() {
        Log.log(Level.FINE, "Toggle expiration date");
        expirationDateSwitch.click();
    }

    public boolean isCalendarDateVisible(String date) {
        return !findListAccesibility(date).isEmpty();
    }

    public void tapNextCalendarPage() {
        Log.log(Level.FINE, "Tap next calendar page");
        nextButton.click();
    }

    public void selectCalendarDate(String date) {
        Log.log(Level.FINE, "Select calendar date: " + date);
        findAccesibility(date).click();
    }

    public void tapOk() {
        Log.log(Level.FINE, "Tap OK");
        okButton.click();
    }

    public void confirmDialog() {
        Log.log(Level.FINE, "Confirm dialog");
        List<WebElement> yesButtons = findListUIAutomatorText(YES_BUTTON_TEXT);
        if (yesButtons.isEmpty()) {
            throw new IllegalStateException("Confirmation button was not found: " + YES_BUTTON_TEXT);
        }
        yesButtons.get(0).click();
    }

    public void inviteMember() {
        Log.log(Level.FINE, "Invite member");
        inviteMemberButton.click();
    }

    public void openEditMember(String userName) {
        Log.log(Level.FINE, "Open edit member: " + userName);
        findMemberByName(userName).findElement(AppiumBy.id(EDIT_MEMBER_BUTTON_ID)).click();
    }

    public void tapRemoveMember(String userName) {
        Log.log(Level.FINE, "Tap remove member: " + userName);
        findMemberByName(userName).findElement(AppiumBy.id(REMOVE_MEMBER_BUTTON_ID)).click();
    }

    public void createLink() {
        Log.log(Level.FINE, "Create public link");
        createLinkButton.click();
        // Wait until link is created and add link button appears again.
        waitById(WAIT_TIME, addLinkButton);
    }

    public void editLink(String linkName) {
        Log.log(Level.FINE, "Open edit link: " + linkName);
        findLinkByName(linkName).findElement(AppiumBy.id(EDIT_PUBLIC_LINK_BUTTON_ID)).click();
    }

    public void tapRemoveLink(String linkName) {
        Log.log(Level.FINE, "Tap remove link: " + linkName);
        findLinkByName(linkName).findElement(AppiumBy.id(REMOVE_PUBLIC_LINK_BUTTON_ID)).click();
    }

    public boolean isMemberDisplayed(String userName) {
        return findMemberByNameOrNull(userName) != null;
    }

    public boolean isMemberDisplayedWithPermission(String userName, String permission) {
        WebElement member = findMemberByNameOrNull(userName);
        if (member == null) {
            return false;
        }
        return !member.findElements(AppiumBy.androidUIAutomator(
                "new UiSelector().textContains(\"" + permission + "\")")).isEmpty();
    }

    public boolean isLinkDisplayed(String linkName) {
        return findLinkByNameOrNull(linkName) != null;
    }

    public boolean isLinkDisplayedWithPermission(String linkName, String permission) {
        WebElement link = findLinkByNameOrNull(linkName);
        if (link == null) {
            return false;
        }
        return !link.findElements(AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + permission + "\")")).isEmpty();
    }

    public boolean isLinkDisplayedWithExpirationDate(String linkName, String expirationDate) {
        WebElement link = findLinkByNameOrNull(linkName);
        if (link == null) {
            return false;
        }
        return !link.findElements(AppiumBy.androidUIAutomator(
                "new UiSelector().textContains(\"" + expirationDate + "\")")).isEmpty();
    }

    public boolean isExpirationDateDisplayed(String expectedExpirationDate) {
        List<WebElement> expirationDates = findListId(EXPIRATION_DATE_ID);
        if (expectedExpirationDate == null) {
            return expirationDates.isEmpty();
        }
        return !expirationDates.isEmpty() && expectedExpirationDate.equals(expirationDates.get(0).getText());
    }

    private WebElement findMemberByName(String userName) {
        return findMemberByNameOrNull(userName);
    }

    private WebElement findMemberByNameOrNull(String userName) {
        for (WebElement member : memberList) {
            WebElement nameElement = member.findElement(AppiumBy.id(MEMBER_NAME_ID));
            String memberName = nameElement.getText();
            if (memberName.contains(userName)) {
                return member;
            }
        }
        return null;
    }

    private WebElement findLinkByName(String linkName) {
        return findLinkByNameOrNull(linkName);
    }

    private WebElement findLinkByNameOrNull(String linkName) {
        for (WebElement link : linkList) {
            String linkInList = link.findElement(AppiumBy.id(PUBLIC_LINK_DISPLAY_NAME_ID)).getText();
            Log.log(Level.FINE, "Checking public link in list: " + linkInList);
            if (linkName.equals(linkInList)) {
                return link;
            }
        }
        return null;
    }
}
