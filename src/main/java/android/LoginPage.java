/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package android;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.logging.Level;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.log.Log;

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

    private String server;

    public LoginPage(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void typeURL(String server) {
        Log.log(Level.FINE, "Starts: Type URL.");
        //App takes a while in charging
        waitById(10, urlServer.get(0));
        urlServer.get(0).sendKeys(server);
        this.server = server;
        checkServerButton.click();
    }

    public void typeCredentials(String username, String password) {
        Log.log(Level.FINE, "Starts: Type credentials: username: "
                + username + " - password: " + password);
        acceptWarning();
        userNameText.sendKeys(username);
        passwordText.sendKeys(password);
    }

    public void submitLogin() {
        Log.log(Level.FINE, "Starts: Submit login");
        loginButton.click();
    }

    public void acceptWarning() {
        Log.log(Level.FINE, "Accept warning");
        String prefix = server.split("://")[0];
        Log.log(Level.FINE, "Prefix: " + prefix);
        if (prefix.equals("https")) {
            acceptCertificate.click();
        } else { //http
            acceptHttp.click();
        }
    }
}
