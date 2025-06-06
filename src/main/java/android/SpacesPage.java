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
import java.util.logging.Level;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.log.Log;

public class SpacesPage extends CommonPage {

    @AndroidFindBy(id = "spaces_list_item_card")
    private List<WebElement> deviceSpacesList;

    @AndroidFindBy(id = "com.owncloud.android:id/root_toolbar_title")
    private WebElement searchBar;

    @AndroidFindBy(id = "com.owncloud.android:id/search_src_text")
    private WebElement searchInput;

    private final String spaceNameId = "com.owncloud.android:id/spaces_list_item_name";
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

    /*
     * Compares the list of spaces from feature file with the displayed spaces
     * @param list of spaces from feature file
     * @return true if both lists match
     */
    public boolean areAllSpacesVisible(List<List<String>> spaces) {
        Log.log(Level.FINE, "Starts: check all spaces are visible");
        HashMap<String, String> spacesInDevice = new HashMap<>();
        waitById(WAIT_TIME, spaceNameId);
        for (WebElement individualSpace : deviceSpacesList) {
            String spaceName = individualSpace.findElement(By.id(spaceNameId))
                    .getAttribute("text").trim();
            String spaceDescription = individualSpace.findElement(By.id(spaceSubtitleId))
                    .getAttribute("text").trim();
            spacesInDevice.put(spaceName, spaceDescription);
        }
        for (List<String> rows : spaces) {
            String name = rows.get(0);
            String description = rows.get(1);
            if (!description.equals(spacesInDevice.get(name))) {
                return false;
            }
        }
        return true;
    }

    /*
     * Check if the given space is displayed in device
     * @param name of the space to check if it is displayed
     * @return true if it is displayed
     */
    public boolean isSpaceVisible(String name) {
        Log.log(Level.FINE, "Starts: check space " + name + " is visible");
        List<WebElement> spaces = findListId(spaceNameId);
        for (WebElement space : spaces) {
            String spaceName = space.getAttribute("text");
            Log.log(Level.FINE, "Space Name: " + spaceName);
            Log.log(Level.FINE, "Name: " + name);
            if (spaceName.equals(name))
                return true;
        }
        return false;
    }

    public void typeSearch(String pattern) {
        Log.log(Level.FINE, "Starts: type search " + pattern);
        searchBar.click();
        searchInput.sendKeys(pattern);
    }

    public WebElement getSpace(String spaceName) {
        return deviceSpacesList.get(1);
    }

    public void openSpace(String spaceName) {
        deviceSpacesList.get(0).click();
    }
}
