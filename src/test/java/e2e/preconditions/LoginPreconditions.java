/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.preconditions;

import java.util.logging.Level;

import e2e.LocProperties;
import e2e.support.log.Log;
import e2e.world.World;

public class LoginPreconditions {

    private final World world;

    public LoginPreconditions(World world) {
        this.world = world;
    }

    public void userIsLogged(String userName) {
        Log.log(Level.FINE, "Checking if user is logged: " + userName);
        if (world.fileListPage().isFileListVisible()) {
            Log.log(Level.FINE, "User is already logged");
            return;
        }
        loginUser(userName);
    }

    private void loginUser(String userName) {
        Log.log(Level.FINE, "Logging user: " + userName);
        String server = System.getProperty("server");
        String password = LocProperties.getProperties().getProperty("passw1");
        world.loginPage().typeURL(server);
        world.loginPage().clickCheckServer();
        acceptServerWarning(server);
        world.loginPage().typeUsername(userName);
        world.loginPage().typePassword(password);
        world.loginPage().submitLogin();
    }

    private void acceptServerWarning(String server) {
        if (isHttps(server)) {
            world.loginPage().acceptCertificateWarning();
        } else {
            world.loginPage().acceptHttpWarning();
        }
    }

    private boolean isHttps(String serverUrl) {
        return serverUrl != null && serverUrl.startsWith("https://");
    }
}
