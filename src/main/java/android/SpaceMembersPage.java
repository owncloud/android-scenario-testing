package android;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.logging.Level;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.date.DateUtils;
import utils.log.Log;

public class SpaceMembersPage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/add_member_button")
    private WebElement addMember;

    @AndroidFindBy(id = "com.owncloud.android:id/add_public_link_button")
    private WebElement addLink;

    @AndroidFindBy(id = "com.owncloud.android:id/search_src_text")
    private WebElement searchMember;

    @AndroidFindBy(id = "com.owncloud.android:id/public_link_name_edit_text")
    private WebElement linkName;

    @AndroidFindBy(id = "com.owncloud.android:id/set_password_button")
    private WebElement setPassword;

    @AndroidFindBy(id = "com.owncloud.android:id/remove_password_button")
    private WebElement removePassword;

    @AndroidFindBy(id = "com.owncloud.android:id/generate_password_button")
    private WebElement generateRandomPassword;

    @AndroidFindBy(id = "com.owncloud.android:id/set_password_button")
    private WebElement submitPassword;

    @AndroidFindBy(id = "com.owncloud.android:id/permissions_title")
    private WebElement permissionsTitle;

    @AndroidFindBy(id = "com.owncloud.android:id/member_name")
    private List<WebElement> searchMemberList;

    @AndroidFindBy(id = "com.owncloud.android:id/expiration_date_layout")
    private WebElement expirationDateLayout;

    @AndroidFindBy(id = "com.owncloud.android:id/expiration_date_switch")
    private WebElement expirationDateSwitch;

    @AndroidFindBy(id = "com.owncloud.android:id/invite_member_button")
    private WebElement inviteMember;

    @AndroidFindBy(id = "com.owncloud.android:id/create_public_link_button")
    private WebElement createLink;

    @AndroidFindBy(id = "com.owncloud.android:id/member_item_layout")
    private List<WebElement> memberList;

    @AndroidFindBy(id = "com.owncloud.android:id/public_link_item_layout")
    private List<WebElement> linkList;

    @AndroidFindBy(id = "android:id/next")
    private WebElement nextButton;

    @AndroidFindBy(id = "android:id/button1")
    private WebElement okButton;

    public static SpaceMembersPage instance;
    private final String editMemberId = "com.owncloud.android:id/edit_member_button";
    private final String memberNameId = "com.owncloud.android:id/member_name";
    private final String removeMemberId = "com.owncloud.android:id/remove_member_button";

    private SpaceMembersPage() {
        super();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public static SpaceMembersPage getInstance() {
        if (instance == null) {
            instance = new SpaceMembersPage();
        }
        return instance;
    }

    public void addMember(String userName) {
        Log.log(Level.FINE, "Starts: Add Member " + userName);
        addMember.click();
        searchMember.sendKeys(userName);
        searchMemberList.get(0).click();
    }

    public void addLink() {
        Log.log(Level.FINE, "Starts: Add Link ");
        addLink.click();
    }

    public void setName(String linkName) {
        waitById(WAIT_TIME, permissionsTitle);
        this.linkName.sendKeys(linkName);
    }

    public void setPermission(String permission) {
        waitById(WAIT_TIME, permissionsTitle);
        findUIAutomatorText(permission).click();
    }

    public void setPassword(){
        setPassword.click();
        generateRandomPassword.click();
        submitPassword.click();
    }

    public void editPassword(){
        removePassword.click();
        setPassword.click();
        generateRandomPassword.click();
        submitPassword.click();
    }

    public void setExpirationDate(String days) {
        Log.log(Level.FINE, "Starts: Add expiration date in days " + days);
        // To normalize null values
        days = normalizeOptional(days);
        boolean isSwitchOn = "true".equals(expirationDateSwitch.getAttribute("checked"));
        Log.log(Level.FINE, "Switch state: " + isSwitchOn);
        boolean hasDays = days != null;
        if (!isSwitchOn && hasDays) { // Switch it on and set days
            expirationDateSwitch.click();
            selectExpirationDate(days);
        } else if (isSwitchOn && hasDays) { // Just set days
            expirationDateLayout.click();
            selectExpirationDate(days);
        } else if (isSwitchOn && !hasDays) { // Switch it off
            expirationDateSwitch.click();
        }
    }

    public void inviteMember() {
        Log.log(Level.FINE, "Starts: Invite member with button");
        inviteMember.click();
    }

    public void openEditMember(String userName){
        Log.log(Level.FINE, "Starts: edit member " + userName);
        for (WebElement member : memberList) {
            WebElement nameElement = member.findElement(AppiumBy.id(memberNameId));
            String memberName = nameElement.getText();
            if (memberName.contains(userName)) {
                member.findElement(AppiumBy.id(editMemberId)).click();
                break;
            }
        }
    }

    public void removeMember(String userName) {
        Log.log(Level.FINE, "Starts: remove member " + userName);
        for (WebElement member : memberList) {
            WebElement nameElement = member.findElement(AppiumBy.id(memberNameId));
            String memberName = nameElement.getText();
            if (memberName.contains(userName)) {
                member.findElement(AppiumBy.id(removeMemberId)).click();
                findListUIAutomatorText("YES").get(0).click();
                break;
            }
        }
    }

    public boolean isMemberOfSpace(String userName, String spaceName) {
        Log.log(Level.FINE, "Starts: check if member of space: " + userName + " " + spaceName);
        waitById(WAIT_TIME, memberList.get(0));
        for (WebElement member : memberList) {
            WebElement nameElement = member.findElement(AppiumBy.id(memberNameId));
            String memberName = nameElement.getText();
            if (memberName.contains(userName)) {
                return true;
            }
        }
        return false;
    }

    public void createLink(){
        Log.log(Level.FINE, "Starts: Save Link with button");
        createLink.click();
        //Wait till link is created and appears in the list
        waitById(WAIT_TIME, addLink);
    }

    public void editLink(String linkName){
        Log.log(Level.FINE, "Starts: Edit Link " + linkName);
        List<WebElement> linkList = findListId("com.owncloud.android:id/public_link_item_layout");
        for (WebElement link : linkList) {
            String linkInList = link.findElement(AppiumBy.id
                    ("com.owncloud.android:id/public_link_display_name")).getText();
            Log.log(Level.FINE, "Checking link in list: " + linkInList);
            if (linkInList.equals(linkName)) {
                Log.log(Level.FINE, "Link found, opening edit screen: " + linkName);
                link.findElement(AppiumBy.accessibilityId("Edit public link for " + linkName)).click();
                return;
            }
        }
        Log.log(Level.FINE, "Link not found in the list: " + linkName);
    }

    public boolean isUserMember(String userName, String permission) {
        Log.log(Level.FINE, "Starts: check membership: " + userName);
        // For every member, check if name and role match
        for (WebElement member : memberList) {
            boolean memberFound = member.findElement(AppiumBy.androidUIAutomator(
                    "new UiSelector().text(\"" + userName + "\")")).isDisplayed();
            boolean roleFound = member.findElement(AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\"" + permission + "\")")).isDisplayed();
            if (memberFound && roleFound) {
                Log.log(Level.FINE, userName + " found");
                return true;
            } else {
                Log.log(Level.FINE, userName + " not found");
            }
        }
        return false;
    }

    public boolean isLinkCreated(String linkName, String permission, String expirationDate) {
        Log.log(Level.FINE, "Starts: Check Link: " + linkName);
        WebElement link = getLinkByName(linkName);
        boolean permissionCorrect = false;
        boolean expirationDateCorrect = true;
        if (link != null) {
            Log.log(Level.FINE, "Link found in UI: " + linkName);
            permissionCorrect = link.findElement(AppiumBy.androidUIAutomator(
                    "new UiSelector().text(\"" + permission + "\")")).isDisplayed();
            if (expirationDate != null && !expirationDate.equals("")) {
                String expDate = DateUtils.formatDate(expirationDate, DateUtils.DateFormatType.NUMERIC);
                Log.log(Level.FINE, "Expiration date to check: " + expDate);
                expirationDateCorrect = link.findElement(AppiumBy.androidUIAutomator(
                        "new UiSelector().textContains(\"" + expDate + "\")")).isDisplayed();
            }
            Log.log(Level.FINE, "Permission correct: " + permissionCorrect);
        }
        return permissionCorrect && expirationDateCorrect;
    }

    private WebElement getLinkByName(String linkName){
        Log.log(Level.FINE, "Starts: Check Link: " + linkName);
        for (WebElement link : linkList) {
            if (link.findElement(AppiumBy.androidUIAutomator(
                    "new UiSelector().text(\"" + linkName + "\")")).isDisplayed()){
                return link;
            }
        }
        return null;
    }

    public boolean isExpirationDateCorrect(String days) {
        Log.log(Level.FINE, "Starts: check expiration date: " + days);
        boolean dateCorrect;
        int expiration = days==null ? 0 : Integer.parseInt(days);
        List<WebElement> expirationDate = findListId("com.owncloud.android:id/expiration_date");
        if (expiration != 0) { // Checking existing expiration date
            // Get date from number of days
            String expDate = DateUtils.formatDate(Integer.toString(expiration), DateUtils.DateFormatType.NUMERIC);
            Log.log(Level.FINE, "Date to check: " + expDate + " Expiration: " + expiration);
            Log.log(Level.FINE, "Expiration date: " + expirationDate.get(0).getText());
            dateCorrect = expirationDate.get(0).getText().equals(expDate);
            return dateCorrect;
        } else { // No expiration date
            return expirationDate.isEmpty();
        }
    }

    private void selectExpirationDate(String days) {
        Log.log(Level.FINE, "Starts: select expiration date: " + days);
        String dateToSet = DateUtils.dateInDaysAndroidFormat(days);
        Log.log(Level.FINE, "Date to set: " + dateToSet);
        if (findListAccesibility(dateToSet).isEmpty()) {
            Log.log(Level.FINE, "Date not found, next page");
            nextButton.click();
        }
        findAccesibility(dateToSet).click();
        okButton.click();
    }

    private String normalizeOptional(String value) {
        if (value == null)
            return null;
        String v = value.trim();
        return v.isEmpty() ? null : v;
    }
}
