/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.preconditions;

import e2e.LocProperties;
import e2e.support.log.Log;
import e2e.world.World;

import java.util.logging.Level;

public class LoginPreconditions {

    private final World world;

    public LoginPreconditions(World world) {
        this.world = world;
    }

    public void userIsLogged(String userName) {
        Log.log(Level.FINE, "Ensuring user is logged: " + userName);
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
        world.loginPage().typeCredentials(userName, password);
        world.loginPage().submitLogin();
    }
}
