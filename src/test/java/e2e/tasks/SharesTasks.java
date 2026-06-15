/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.tasks;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import e2e.support.log.Log;
import e2e.world.World;

public class SharesTasks {

    private static final int SHARE_PERMISSION_INDEX = 0;
    private static final int DELETE_PERMISSION_INDEX = 1;
    private static final int CREATE_PERMISSION_INDEX = 2;
    private static final int CHANGE_PERMISSION_INDEX = 3;

    private final World world;

    public SharesTasks(World world) {
        this.world = world;
    }

    public void selectSharee(String type, String sharee)
            throws IOException, ParserConfigurationException, SAXException {
        Log.log(Level.FINE, "Select sharee. Type: " + type + " - Sharee: " + sharee);
        world.sharePage().addPrivateShare();
        world.searchShareePage().shareWithUser(sharee);
        world.shareAPI().acceptAllShares(type, sharee);
    }

    public void editPrivateSharePermissions(String itemType, String itemName, String permissions) {
        Log.log(Level.FINE, "Edit private share permissions. Type: " + itemType
                + " - Item: " + itemName
                + " - Permissions: " + permissions);
        world.sharePage().editPrivateShare(itemName);
        try {
            String binaryPermissions = convertPermissionsToBinary(permissions);
            Log.log(Level.FINE, "Permissions converted: " + binaryPermissions);
            applyPrivateSharePermissions(itemType, binaryPermissions);
        } finally {
            // To close the page anyway and avoid leaving it open in case of failure, which would affect next steps
            world.privateSharePage().close();
        }
    }

    public void deletePrivateShare() {
        Log.log(Level.FINE, "Delete private share");
        world.sharePage().deletePrivateShare();
        world.sharePage().acceptDeletion();
    }

    public void closeShareView() {
        Log.log(Level.FINE, "Close share view");
        world.sharePage().close();
    }

    private String convertPermissionsToBinary(String permissions) {
        int permissionsAsInt = Integer.parseInt(permissions);
        return String.format("%5s", Integer.toBinaryString(permissionsAsInt))
                .replace(" ", "0");
    }

    private void applyPrivateSharePermissions(String itemType, String binaryPermissions) {
        for (int index = 0; index < binaryPermissions.length(); index++) {
            boolean shouldBeEnabled = binaryPermissions.charAt(index) == '1';
            switch (index) {
                case SHARE_PERMISSION_INDEX -> {
                    // Share permission is not used since resharing is not enabled anymore.
                    Log.log(Level.FINE, "Ignoring Share permission");
                }
                case DELETE_PERMISSION_INDEX -> applyDeletePermissionIfFolder(itemType, shouldBeEnabled);
                case CREATE_PERMISSION_INDEX -> applyCreatePermissionIfFolder(itemType, shouldBeEnabled);
                case CHANGE_PERMISSION_INDEX -> applyChangePermission(itemType, shouldBeEnabled);
                default -> Log.log(Level.FINE, "Ignoring unsupported permission index: " + index);
            }
        }
    }

    private void applyDeletePermissionIfFolder(String itemType, boolean shouldBeEnabled) {
        if (!isFolder(itemType)) {
            return;
        }
        Log.log(Level.FINE, "Check Delete");
        boolean currentlyEnabled = world.privateSharePage().isDeleteSelected();
        Log.log(Level.FINE, "Delete permission. Current: "
                + currentlyEnabled + " - Expected: " + shouldBeEnabled);
        if (currentlyEnabled != shouldBeEnabled) {
            world.privateSharePage().switchDelete();
        }
    }

    private void applyCreatePermissionIfFolder(String itemType, boolean shouldBeEnabled) {
        if (!isFolder(itemType)) {
            return;
        }
        Log.log(Level.FINE, "Check Create");
        boolean currentlyEnabled = world.privateSharePage().isCreateSelected();
        Log.log(Level.FINE, "Create permission. Current: "
                + currentlyEnabled + " - Expected: " + shouldBeEnabled);
        if (currentlyEnabled != shouldBeEnabled) {
            world.privateSharePage().switchCreate();
        }
    }

    private void applyChangePermission(String itemType, boolean shouldBeEnabled) {
        Log.log(Level.FINE, "Check Change");
        if (isFolder(itemType)) {
            boolean currentlyEnabled = world.privateSharePage().isChangeSelected();
            Log.log(Level.FINE, "Change Folder permission. Current: "
                    + currentlyEnabled + " - Expected: " + shouldBeEnabled);
            if (currentlyEnabled != shouldBeEnabled) {
                world.privateSharePage().switchChange();
            }
        } else if (isFile(itemType)) {
            boolean currentlyEnabled = world.privateSharePage().isEditFileEnabled();
            Log.log(Level.FINE, "Edit File permission. Current: "
                    + currentlyEnabled + " - Expected: " + shouldBeEnabled);
            if (currentlyEnabled != shouldBeEnabled) {
                world.privateSharePage().switchEditFile();
            }
        }
    }

    private boolean isFolder(String itemType) {
        return "folder".equalsIgnoreCase(itemType);
    }

    private boolean isFile(String itemType) {
        return "file".equalsIgnoreCase(itemType);
    }
}
