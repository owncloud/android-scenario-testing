/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */
package e2e.model;

public class OCSpaceLink {
    private String id;
    private String linkName;
    private String permission;
    private String expirationDate;
    private boolean hasPassword;

    public OCSpaceLink(String id, String linkName, String permission, String expirationDate, boolean hasPassword) {
        this.id = id;
        this.linkName = linkName;
        this.permission = permission;
        this.expirationDate = expirationDate;
        this.hasPassword = hasPassword;
    }

    public OCSpaceLink(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
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

    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }
}
