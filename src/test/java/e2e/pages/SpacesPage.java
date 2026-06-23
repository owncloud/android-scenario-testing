/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.logging.Level;

import e2e.support.log.Log;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class SpacesPage extends CommonPage {

    @AndroidFindBy(id = SPACE_CARD_ID)
    private List<WebElement> spaceCards;

    @AndroidFindBy(id = "com.owncloud.android:id/fab_create_space")
    private WebElement createSpaceButton;

    @AndroidFindBy(id = "com.owncloud.android:id/create_space_dialog_name_value")
    private WebElement nameInput;

    @AndroidFindBy(id = "com.owncloud.android:id/create_space_dialog_subtitle_value")
    private WebElement subtitleInput;

    @AndroidFindBy(id = QUOTA_SWITCH_ID)
    private WebElement quotaSwitch;

    @AndroidFindBy(id = "com.owncloud.android:id/create_space_dialog_quota_value")
    private WebElement quotaValueInput;

    @AndroidFindBy(id = "com.owncloud.android:id/create_space_dialog_quota_unit_label")
    private WebElement quotaUnit;

    @AndroidFindBy(id = "com.owncloud.android:id/create_space_button")
    private WebElement createOrSaveButton;

    @AndroidFindBy(id = "com.owncloud.android:id/root_toolbar_title")
    private WebElement searchBar;

    @AndroidFindBy(id = "com.owncloud.android:id/search_src_text")
    private WebElement searchInput;

    private static final String NO_QUOTA_RESTRICTION = "No restriction";
    private static final String SPACE_CARD_ID = "com.owncloud.android:id/spaces_list_item_card";
    private static final String SPACE_NAME_ID = "com.owncloud.android:id/spaces_list_item_name";
    private static final String SPACE_SUBTITLE_ID = "com.owncloud.android:id/spaces_list_item_subtitle";
    private static final String SPACE_DISABLED_LABEL_ID = "com.owncloud.android:id/spaces_list_item_disabled_label";
    private static final String QUOTA_SWITCH_ID = "com.owncloud.android:id/create_space_dialog_quota_switch";
    private static final String YES_BUTTON_TEXT = "YES";

    public SpacesPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
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

        public String label() {
            return label;
        }
    }

    public void createSpace(String name, String subtitle, String quota) {
        Log.log(Level.FINE, "Create space: " + name);
        tapCreateSpace();
        fillSpaceForm(name, subtitle, quota);
        submitSpaceForm();
        waitUntilSpaceDialogIsClosed();
    }

    public void editSpace(String name, String subtitle, String quota) {
        Log.log(Level.FINE, "Update space: " + name);
        fillSpaceForm(name, subtitle, quota);
        submitSpaceForm();
        waitUntilSpaceDialogIsClosed();
    }

    public void openMembers(String spaceName) {
        Log.log(Level.FINE, "Open members for space: " + spaceName);
        openSpaceMenu(spaceName);
        selectMenuOption(MenuItems.MEMBERS);
    }

    public void openEditSpace(String spaceName) {
        Log.log(Level.FINE, "Open edit space: " + spaceName);
        openSpaceMenu(spaceName);
        selectMenuOption(MenuItems.EDIT);
    }

    public void openEditSpaceImage(String spaceName) {
        Log.log(Level.FINE, "Open edit image for space: " + spaceName);
        openSpaceMenu(spaceName);
        selectMenuOption(MenuItems.EDIT_IMAGE);
    }

    public void openDisableSpace(String spaceName) {
        Log.log(Level.FINE, "Disable space: " + spaceName);
        openSpaceMenu(spaceName);
        selectMenuOption(MenuItems.DISABLE);
        confirmDialog();
    }

    public void openEnableSpace(String spaceName) {
        Log.log(Level.FINE, "Enable space: " + spaceName);
        openSpaceMenu(spaceName);
        selectMenuOption(MenuItems.ENABLE);
        confirmDialog();
    }

    public void openDeleteSpace(String spaceName) {
        Log.log(Level.FINE, "Delete space: " + spaceName);
        openSpaceMenu(spaceName);
        selectMenuOption(MenuItems.DELETE);
        confirmDialog();
        waitUntilSpaceCardIsInvisible();
    }

    public void typeSearch(String pattern) {
        Log.log(Level.FINE, "Type space search: " + pattern);
        searchBar.click();
        searchInput.sendKeys(pattern);
    }

    public void openSpace(String spaceName) {
        Log.log(Level.FINE, "Open space: " + spaceName);
        findSpaceCardByName(spaceName).click();
    }

    public String getDisplayedQuotaValue() {
        String displayedQuota = quotaValueInput.getAttribute("text");
        Log.log(Level.FINE, "Displayed quota value: " + displayedQuota);
        return displayedQuota;
    }

    public String getDisplayedQuotaUnit() {
        String displayedUnit = quotaUnit.getAttribute("text");
        Log.log(Level.FINE, "Displayed quota unit: " + displayedUnit);
        return displayedUnit;
    }

    public List<WebElement> getDisplayedSpaceCards() {
        Log.log(Level.FINE, "Get displayed space cards");
        return driver.findElements(By.id(SPACE_CARD_ID));
    }

    public void tapCreateSpace() {
        Log.log(Level.FINE, "Tap create space");
        createSpaceButton.click();
    }

    public void clearAndTypeName(String name) {
        Log.log(Level.FINE, "Type space name: " + name);
        nameInput.clear();
        nameInput.sendKeys(name);
    }

    public void clearAndTypeSubtitle(String subtitle) {
        Log.log(Level.FINE, "Type space subtitle: " + subtitle);
        subtitleInput.clear();
        subtitleInput.sendKeys(subtitle);
    }

    public void setQuota(String quota) {
        Log.log(Level.FINE, "Set space quota: " + quota);
        boolean quotaEnabled = !NO_QUOTA_RESTRICTION.equals(quota);
        boolean switchChecked = Boolean.parseBoolean(quotaSwitch.getAttribute("checked"));
        if (quotaEnabled != switchChecked) {
            quotaSwitch.click();
        }
        if (quotaEnabled) {
            waitById(WAIT_TIME, quotaValueInput);
            quotaValueInput.clear();
            quotaValueInput.sendKeys(quota);
        }
    }

    public void submitSpaceForm() {
        Log.log(Level.FINE, "Submit space form");
        createOrSaveButton.click();
    }

    public void waitUntilSpaceDialogIsClosed() {
        Log.log(Level.FINE, "Wait until space dialog is closed");
        waitByIdInvisible(WAIT_TIME, QUOTA_SWITCH_ID);
    }

    public void waitUntilDisabledLabelIsDisplayed() {
        Log.log(Level.FINE, "Wait until disabled space label is displayed");
        waitById(WAIT_TIME, SPACE_DISABLED_LABEL_ID);
    }

    public void openSpaceMenu(String spaceName) {
        Log.log(Level.FINE, "Open space menu: " + spaceName);
        findUIAutomatorDescription(spaceName + " space menu").click();
    }

    public void confirmDialog() {
        Log.log(Level.FINE, "Confirm dialog");
        List<WebElement> yesButtons = findListUIAutomatorText(YES_BUTTON_TEXT);
        if (yesButtons.isEmpty()) {
            throw new IllegalStateException("Confirmation button was not found: " + YES_BUTTON_TEXT);
        }
        yesButtons.get(0).click();
    }

    public void waitUntilSpaceCardIsInvisible() {
        Log.log(Level.FINE, "Wait until space card is invisible");
        waitByIdInvisible(WAIT_TIME, SPACE_CARD_ID);
    }

    private void fillSpaceForm(String name, String subtitle, String quota) {
        Log.log(Level.FINE, "Fill space form. Name: " + name + " - Subtitle: " + subtitle
                + " - Quota: " + quota);
        clearAndTypeName(name);
        clearAndTypeSubtitle(subtitle);
        setQuota(quota);
    }

    private void selectMenuOption(MenuItems menuItem) {
        Log.log(Level.FINE, "Open space menu option: " + menuItem.label());
        List<WebElement> options = findListUIAutomatorText(menuItem.label());
        options.get(0).click();
    }

    private WebElement findSpaceCardByName(String spaceName) {
        List<WebElement> cardsDisplayed = driver.findElements(By.id(SPACE_CARD_ID));
        for (WebElement spaceCard : cardsDisplayed) {
            String cardName = getSpaceName(spaceCard);
            if (spaceName.equals(cardName)) {
                return spaceCard;
            }
        }
        return null;
    }

    public String getSpaceName(WebElement spaceCard) {
        return spaceCard.findElement(By.id(SPACE_NAME_ID)).getAttribute("text").trim();
    }

    public String getSpaceSubtitle(WebElement spaceCard) {
        List<WebElement> subtitles = spaceCard.findElements(By.id(SPACE_SUBTITLE_ID));
        if (subtitles.isEmpty()) {
            return "";
        }
        return subtitles.get(0).getAttribute("text").trim();
    }

    public boolean isDisabledSpace(WebElement spaceCard) {
        return !spaceCard.findElements(By.id(SPACE_DISABLED_LABEL_ID)).isEmpty();
    }
}
