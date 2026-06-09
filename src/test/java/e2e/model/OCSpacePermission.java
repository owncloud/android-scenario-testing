/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.model;

public class OCSpacePermission {

    private String permissionName;
    private String permissionId;
    private String description;

    public OCSpacePermission(){}

    public OCSpacePermission(String id, String name, String description){
        this.permissionId = id;
        this.permissionName = name;
        this.description = description;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
