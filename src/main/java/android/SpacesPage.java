/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package android;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.log.Log;

public class SpacesPage extends CommonPage {

    @AndroidFindBy(id = "spaces_list_item_card")
    private List<WebElement> deviceSpacesList;

    @AndroidFindBy(id = "com.owncloud.android:id/fab_create_space")
    private WebElement createSpace;

    @AndroidFindBy(id = "com.owncloud.android:id/create_space_dialog_name_value")
    private WebElement nameEditText;

    @AndroidFindBy(id = "com.owncloud.android:id/create_space_dialog_subtitle_value")
    private WebElement subtitleEditText;

    @AndroidFindBy(id = "com.owncloud.android:id/create_space_dialog_quota_switch")
    private WebElement quotaSwitch;

    @AndroidFindBy(id = "com.owncloud.android:id/create_space_dialog_quota_value")
    private WebElement quotaValueEdittext;

    @AndroidFindBy(id = "com.owncloud.android:id/create_space_dialog_quota_unit_label")
    private WebElement quotaUnit;

    @AndroidFindBy(id = "com.owncloud.android:id/create_space_button")
    private WebElement createButton;

    @AndroidFindBy(id = "com.owncloud.android:id/root_toolbar_title")
    private WebElement searchBar;

    @AndroidFindBy(id = "com.owncloud.android:id/search_src_text")
    private WebElement searchInput;

    private final String spaceNameId = "com.owncloud.android:id/spaces_list_item_name";
    private final String cardId = "com.owncloud.android:id/spaces_list_item_card";
    private final String spaceSubtitleId = "com.owncloud.android:id/spaces_list_item_subtitle";

    public static SpacesPage instance;

    private SpacesPage() {
        super();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public static SpacesPage getInstance() {
        if (instance == null) {
            instance = new SpacesPage();
        }
        return instance;
    }

    public enum MenuItems {
        MEMBERS("Members"),
        EDIT("Edit space"),
        EDIT_IMAGE("Edit image"),
        DISABLE("Disable space"),
        ENABLE("Enable space"),
        DELETE("Delete space");

        private final String label;

        MenuItems(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public void createSpace (String name, String subtitle, String quota){
        Log.log(Level.FINE, "Starts: Create space " + name);
        createSpace.click();
        fillSpaceInfo(name, subtitle, quota);
    }

    public void openMembers(String spaceName){
        Log.log(Level.FINE, "Starts: Open members " + spaceName);
        findUIAutomatorDescription(spaceName + " space menu").click();
        openMenuOption(MenuItems.MEMBERS.getLabel());
    }

    public void openEditSpace(String spaceName){
        Log.log(Level.FINE, "Starts: Open edit space " + spaceName);
        findUIAutomatorDescription(spaceName + " space menu").click();
        openMenuOption(MenuItems.EDIT.getLabel());
    }

    public void openDisableSpace(String spaceName){
        Log.log(Level.FINE, "Starts: Open disable space " + spaceName);
        findUIAutomatorDescription(spaceName + " space menu").click();
        openMenuOption(MenuItems.DISABLE.getLabel());
        findListUIAutomatorText("YES").get(0).click();
    }

    public void openEnableSpace(String spaceName){
        Log.log(Level.FINE, "Starts: Open enable space " + spaceName);
        findUIAutomatorDescription(spaceName + " space menu").click();
        openMenuOption(MenuItems.ENABLE.getLabel());
        findListUIAutomatorText("YES").get(0).click();
    }

    public void openDeleteSpace(String spaceName){
        Log.log(Level.FINE, "Starts: Open delete space " + spaceName);
        findUIAutomatorDescription(spaceName + " space menu").click();
        openMenuOption(MenuItems.DELETE.getLabel());
        findListUIAutomatorText("YES").get(0).click();
        waitByIdInvisible(WAIT_TIME, cardId);
    }

    public void editSpace (String name, String subtitle, String quota){
        Log.log(Level.FINE, "Starts: Update space " + name);
        fillSpaceInfo(name, subtitle, quota);
    }

    private void fillSpaceInfo(String spaceName, String subtitle, String quota) {
        Log.log(Level.FINE, "Starts: fill space info: " + spaceName + ", " + subtitle + ", " + quota);
        nameEditText.clear();
        nameEditText.sendKeys(spaceName);
        subtitleEditText.clear();
        subtitleEditText.sendKeys(subtitle);
        setQuota(quota);
        createButton.click();
        // Wait until the dialog disappears
        waitByIdInvisible(WAIT_TIME, "com.owncloud.android:id/create_space_dialog_quota_switch");
    }

    private void setQuota(String quota) {
        Log.log(Level.FINE, "Starts: set quota: " + quota);
        boolean withQuota = !"No restriction".equals(quota);
        boolean switchChecked = Boolean.parseBoolean(quotaSwitch.getAttribute("checked"));
        if (withQuota != switchChecked) {
            quotaSwitch.click();
        }
        if (withQuota) {
            waitById(WAIT_TIME, quotaValueEdittext);
            quotaValueEdittext.clear();
            quotaValueEdittext.sendKeys(quota);
        }
    }

    public void openEditSpaceImage(String spaceName){
        Log.log(Level.FINE, "Starts: Open edit space image " + spaceName);
        findUIAutomatorDescription(spaceName + " space menu").click();
        openMenuOption(MenuItems.EDIT_IMAGE.getLabel());
    }

    public void typeSearch(String pattern) {
        Log.log(Level.FINE, "Starts: type search " + pattern);
        searchBar.click();
        searchInput.sendKeys(pattern);
    }

    public void openSpace(String spaceName) {
        deviceSpacesList.get(0).click();
    }

    public boolean isSpaceDisplayed(String spaceName, String spaceSubtitle, String status) {
        Log.log(Level.FINE, "Starts: check if space " + spaceName + " is " + status);
        // Loop to check every space in the device
        List<WebElement> cardsDisplayed = driver.findElements(By.id(cardId));
        HashMap<String, String> spacesInDevice = new HashMap<>();
        for (int i = 0; i < cardsDisplayed.size(); i++) {
            Log.log(Level.FINE, "Checking space number " + i);
            // We have to double the check because the UI changes making the DOM different
            WebElement individualSpace = driver.findElements(By.id(cardId)).get(i);
            // Get space name in the card
            String spaceNameCard = individualSpace.findElement(By.id(spaceNameId))
                    .getAttribute("text").trim();
            // Get description in the card
            String spaceSubtitleCard = "";
            List<WebElement> spaceSubtitles = individualSpace.findElements(By.id(spaceSubtitleId));
            if (!spaceSubtitles.isEmpty()) {
                spaceSubtitleCard = spaceSubtitles.get(0).getAttribute("text").trim();
            }
            if (status.equals("disabled")){
                if (individualSpace.findElements(By.id("com.owncloud.android:id/spaces_list_item_disabled_label")).isEmpty())
                    continue;
            }
            Log.log(Level.FINE, "Card: " + spaceNameCard + " - " +
                    (spaceSubtitleCard.equals("")?"empty":spaceSubtitleCard));
            Log.log(Level.FINE, "Scenario: " + spaceName + " - " +
                    (spaceSubtitle.equals("")?"empty":spaceSubtitle));
            spacesInDevice.put(spaceNameCard, spaceSubtitleCard);
        }
        // Check all spaces from the list are in the device
        return spacesInDevice.containsKey(spaceName) &&
                Objects.equals(spacesInDevice.get(spaceName), spaceSubtitle);
    }

    public boolean isQuotaDisplayed(String value, String unit) {
        Log.log(Level.FINE, "Starts: check quota: " + value + " " + unit);
        String displayedQuota = quotaValueEdittext.getAttribute("text");
        String displayedUnit = quotaUnit.getAttribute("text");
        Log.log(Level.FINE, "Displayed: " + displayedQuota + " " + displayedUnit);
        return (value.equals(displayedQuota) && unit.equals(displayedUnit));
    }

    private void openMenuOption(String option) {
        Log.log(Level.FINE, "Starts: open menu option: " + option);
        findListUIAutomatorText(option).get(0).click();
    }
}
