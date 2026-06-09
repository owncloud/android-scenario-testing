/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.support.parser;

import static e2e.support.log.Log.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import e2e.model.OCSpaceLink;
import e2e.model.OCSpaceMember;
import e2e.model.OCSpacePermission;

public class OCMemberJSONHandler {

    public static List<OCSpacePermission> parsePermissions(String jsonText){
        JSONObject root = new JSONObject(jsonText);
        ArrayList<OCSpacePermission> permissionList = new ArrayList<OCSpacePermission>();
        JSONArray rolesArray = root
                .getJSONArray("@libre.graph.permissions.roles.allowedValues");
        for (int i = 0; i < rolesArray.length(); i++) {
            JSONObject permission = rolesArray.getJSONObject(i);
            String id = permission.getString("id");
            String displayName = permission.getString("displayName");
            String description = permission.getString("description");
            permissionList.add(new OCSpacePermission(id, displayName, description));
        }
        return permissionList;
    }

    public static List<OCSpaceMember> parseMembers(String jsonText) {
        JSONObject root = new JSONObject(jsonText);

        // 1. Mapping roles
        Map<String, String> rolesMap = new HashMap<>();
        JSONArray rolesArray = root
                .getJSONArray("@libre.graph.permissions.roles.allowedValues");

        for (int i = 0; i < rolesArray.length(); i++) {
            JSONObject role = rolesArray.getJSONObject(i);
            String id = role.getString("id");
            String displayName = role.getString("displayName");
            rolesMap.put(id, displayName);
        }

        // 2. Check members
        List<OCSpaceMember> result = new ArrayList<>();
        JSONArray values = root.getJSONArray("value");

        for (int i = 0; i < values.length(); i++) {
            JSONObject item = values.getJSONObject(i);

            String expirationDate = item.optString("expirationDateTime", null);

            // ignore non-users
            if (!item.has("grantedToV2"))
                continue;

            JSONObject memberSpace = item.getJSONObject("grantedToV2");
            JSONObject user = memberSpace.getJSONObject("user");
            String userId = user.optString("id", null);
            String userType = user.optString("@libre.graph.userType", null);
            String displayName = user.optString("displayName", null);

            // roles
            String permission = null;
            if (item.has("roles")) {
                JSONArray roles = item.getJSONArray("roles");
                if (!roles.isEmpty()) {
                    String roleId = roles.getString(0);
                    permission = rolesMap.get(roleId);
                }
            }

            OCSpaceMember m = new OCSpaceMember(userId, userType, displayName, permission, expirationDate);
            result.add(m);
        }
        return result;
    }

    public static List<OCSpaceLink> parseLinks(String jsonText) {
        Log.log(Level.FINE, "Parsing links from JSON: " + jsonText);
        JSONObject root = new JSONObject(jsonText);

        List<OCSpaceLink> result = new ArrayList<>();
        JSONArray values = root.getJSONArray("value");

        for (int i = 0; i < values.length(); i++) {
            JSONObject item = values.getJSONObject(i);

            // ignore non-links
            if (item.has("grantedToV2")) {
                continue;
            }

            String expirationDate = item.optString("expirationDateTime", null);
            boolean hasPassword = item.optBoolean("hasPassword");
            String id = item.optString("id", null);
            JSONObject linkAttributes = item.getJSONObject("link");
            String linkName = linkAttributes.optString("@libre.graph.displayName");
            String permission = linkAttributes.optString("type");

            OCSpaceLink link = new OCSpaceLink(id, linkName, permission, expirationDate, hasPassword);
            result.add(link);
        }
        return result;
    }
}
