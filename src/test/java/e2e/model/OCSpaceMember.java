/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.model;

public class  OCSpaceMember {
    private String id;
    private String userType;
    private String displayName;
    private String permission;
    private String expirationDate;

    public OCSpaceMember(String id, String userType, String displayName, String permission, String expirationDate) {
        this.id = id;
        this.userType = userType;
        this.displayName = displayName;
        this.permission = permission;
        this.expirationDate = expirationDate;
    }

    public OCSpaceMember(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
