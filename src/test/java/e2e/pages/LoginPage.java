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

public class LoginPage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/hostUrlInput")
    private List<WebElement> urlServer;

    @AndroidFindBy(id = "com.owncloud.android:id/embeddedCheckServerButton")
    private WebElement checkServerButton;

    @AndroidFindBy(id = "com.owncloud.android:id/account_username")
    private WebElement userNameText;

    @AndroidFindBy(id = "com.owncloud.android:id/account_password")
    private WebElement passwordText;

    @AndroidFindBy(id = "com.owncloud.android:id/loginButton")
    private WebElement loginButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().textContains(\"YES\");")
    private WebElement acceptCertificate;

    @AndroidFindBy(id = "android:id/button1")
    private WebElement acceptHttp;

    public LoginPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void typeURL(String server) {
        Log.log(Level.FINE, "Type URL");
        waitById(WAIT_TIME, urlServer.get(0));
        urlServer.get(0).sendKeys(server);
    }

    public void clickCheckServer() {
        Log.log(Level.FINE, "Click check server button");
        checkServerButton.click();
    }

    public void typeUsername(String username) {
        userNameText.sendKeys(username);
    }

    public void typePassword(String password) {
        passwordText.sendKeys(password);
    }

    public void acceptCertificateWarning() {
        Log.log(Level.FINE, "Accept certificate warning");
        acceptCertificate.click();
    }

    public void acceptHttpWarning() {
        Log.log(Level.FINE, "Accept HTTP warning");
        acceptHttp.click();
    }

    public void submitLogin() {
        loginButton.click();
    }
}
