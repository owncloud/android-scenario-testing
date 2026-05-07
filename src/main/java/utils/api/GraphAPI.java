package utils.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.date.DateUtils;
import utils.entities.OCSpace;
import utils.entities.OCSpaceLink;
import utils.entities.OCSpaceMember;
import utils.entities.OCSpacePermission;
import utils.log.Log;
import utils.parser.OCMemberJSONHandler;
import utils.parser.OCUserJSONHandler;

public class GraphAPI extends CommonAPI {

    private final String graphPath = "/graph/v1.0/";
    private final String drives = "drives/";
    private final String myDrives = "me/drives/";
    private final String members = "/graph/v1beta1/drives/";
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static GraphAPI instance;

    private GraphAPI() throws IOException {
    }

    public static GraphAPI getInstance() throws IOException {
        if (instance == null) {
            instance = new GraphAPI();
        }
        return instance;
    }

    public void createSpace(String name, String description, String userName) throws IOException {
        Log.log(Level.FINE, "CREATE SPACE: " + name + " " + description);
        String url = urlServer + graphPath + drives;
        Log.log(Level.FINE, "URL: " + url);
        Request request = postRequest(url, createBodySpace(name, description), userName);
        Response response = httpClient.newCall(request).execute();
        Log.log(Level.FINE, "Response Code: " + response.code());
        Log.log(Level.FINE, "Response Body: " + response.body().string());
        response.close();
    }

    public OCSpace getPersonal() throws IOException {
        Log.log(Level.FINE, "Get personal space");
        String url = urlServer + graphPath + myDrives;
        Request request = getRequest(url);
        Response response = httpClient.newCall(request).execute();
        String json = response.body().string();
        OCSpace personal = new OCSpace();
        JSONObject obj = new JSONObject(json);
        JSONArray value = obj.getJSONArray("value");
        for (int i = 0; i < value.length(); i++) {
            JSONObject jsonObject = value.getJSONObject(i);
            String type = jsonObject.getString("driveType");
            if (type.equals("personal")) { //Just for user created spaces
                personal.setType(jsonObject.getString("driveType"));
                personal.setId(jsonObject.getString("id"));
                personal.setName(jsonObject.getString("name"));
                Log.log(Level.FINE, "Space id returned: " +
                        personal.getId() + " " + personal.getName());
            }
        }
        response.close();
        return personal;
    }

    private RequestBody createBodySpace(String name, String description) {
        Log.log(Level.FINE, "BODY SPACE: Name: " + name + " . Description: " + description);
        String json = "{\"name\":\"" + name + " \",\"driveType\":\"project\", \"description\":\"" + description + " \"}";
        RequestBody body = RequestBody.create(JSON, json);
        return body;
    }

    public List<OCSpace> getMySpaces() throws IOException {
        Log.log(Level.FINE, "GET my SPACES");
        String url = urlServer + graphPath + myDrives;
        Request request = getRequest(url);
        Response response = httpClient.newCall(request).execute();
        return getSpacesFromResponse(response);
    }

    //User "alice" by default
    public void removeSpacesOfUser() throws IOException {
        Log.log(Level.FINE, "REMOVE custom SPACES of: " + user);
        List<OCSpace> spacesOfUser = getMySpaces();
        for (OCSpace space : spacesOfUser) {
            String url = urlServer + graphPath + drives + space.getId();
            Log.log(Level.FINE, "URL remove space: " + url);
            //First, disable
            Request requestDisable = deleteRequest(url, "Alice");
            httpClient.newCall(requestDisable).execute();
            //Then, delete
            Request requestDelete = deleteSpaceRequest(url);
            httpClient.newCall(requestDelete).execute();
        }
    }

    public void disableSpace(String name, String description) throws IOException {
        Log.log(Level.FINE, "DISABLE SPACE: " + name + " " + description);
        String spaceId = getSpaceIdFromNameAndDescription(name, description);
        String url = urlServer + graphPath + drives + spaceId;
        Log.log(Level.FINE, "URL: " + url);
        Request request = deleteRequest(url, "Alice");
        Response response = httpClient.newCall(request).execute();
        Log.log(Level.FINE, "Response Code: " + response.code());
        Log.log(Level.FINE, "Response Body: " + response.body().string());
        response.close();
    }

    private Request deleteSpaceRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("OCS-APIREQUEST", "true")
                .addHeader("User-Agent", userAgent)
                .addHeader("Authorization", "Basic " + credentialsB64)
                .addHeader("Host", host)
                .addHeader("Purge", "T")
                .delete()
                .build();
        return request;
    }

    public String getSpaceIdFromNameAndDescription(String name, String description) throws IOException {
        Log.log(Level.FINE, "Look for space ID or null: " + name + " " + description);
        List<OCSpace> mySpaces = getMySpaces();
        for (OCSpace space : mySpaces) {
            if (space.getName().trim().equals(name) &&
                    space.getDescription().trim().equals(description)) {
                Log.log(Level.FINE, "FOUND: ID of space: " + space.getId() + " " + space.getName());
                return space.getId();
            }
        }
        return null;
    }

    public String getSpaceIdFromName(String name) throws IOException {
        Log.log(Level.FINE, "Look for space ID or null: " + name);
        List<OCSpace> mySpaces = getMySpaces();
        for (OCSpace space : mySpaces) {
            if (space.getName().trim().equals(name)) {
                Log.log(Level.FINE, "FOUND: ID of space: " + space.getId() + " " + space.getName());
                return space.getId();
            }
        }
        return null;
    }

    public OCSpaceMember getMemberOfSpace(String spaceName, String userName) throws IOException {
        Log.log(Level.FINE, "Get member of space: " + spaceName + " user: " + userName);
        String spaceId = getSpaceIdFromName(spaceName);
        String url = urlServer + members + spaceId + "/root/permissions";
        Log.log(Level.FINE, "URL: " + url);
        Request request = getRequest(url);
        Response response = httpClient.newCall(request).execute();
        List<OCSpaceMember> spaceMembers =  OCMemberJSONHandler.parseMembers(response.body().string());
        for (OCSpaceMember member : spaceMembers){
            Log.log(Level.FINE, "Checking " + member.getDisplayName());
            if (member.getDisplayName().equals(userName)) {
                response.close();
                return member;
            }
        }
        response.close();
        return null;
    }

    public OCSpaceLink getLinkOfSpace(String spaceName, String linkName) throws IOException {
        Log.log(Level.FINE, "Get links of space: " + spaceName);
        String spaceId = getSpaceIdFromName(spaceName);
        String url = urlServer + members + spaceId + "/root/permissions";
        Log.log(Level.FINE, "URL: " + url);
        Request request = getRequest(url);
        Response response = httpClient.newCall(request).execute();
        List<OCSpaceLink> spaceLinks =  OCMemberJSONHandler.parseLinks(response.body().string());
        Log.log(Level.FINE, "Links found: " + spaceLinks.size());
        for (OCSpaceLink link : spaceLinks){
            Log.log(Level.FINE, "Checking " + link.getLinkName());
            if (link.getLinkName().equals(linkName)) {
                Log.log(Level.FINE, "Link found!!: " + link.getLinkName() + " " + link.getPermission());
                response.close();
                return link;
            }
        }
        Log.log(Level.FINE, "Link not found!!");
        response.close();
        return null;
    }

    private OCSpaceMember getUserIdFromName(String userName) throws IOException {
        Log.log(Level.FINE, "Get OCMember from name: " + userName);
        String url = urlServer + graphPath + "users?%24search=%22" + userName + "%22&%24orderby=displayName";
        Log.log(Level.FINE, "URL: " + url);
        Request request = getRequest(url);
        Response response = httpClient.newCall(request).execute();
        OCSpaceMember member = OCUserJSONHandler.parse(response.body().string());
        response.close();
        return member;
    }

    private String getPermissionId(String spaceId, String permissionName) throws IOException {
        Log.log(Level.FINE, "Get id of permission: " + permissionName);
        String url = urlServer + members + spaceId + "/root/permissions";
        Log.log(Level.FINE, "URL: " + url);
        Request request = getRequest(url);
        Response response = httpClient.newCall(request).execute();
        List<OCSpacePermission> spacePermissions =  OCMemberJSONHandler.parsePermissions(response.body().string());
        for (OCSpacePermission permission : spacePermissions){
            if (permission.getPermissionName().contains(permissionName)) {
                Log.log(Level.FINE, "Found Permission: " + permission.getPermissionName() + " :" + permission.getPermissionId());
                response.close();
                return permission.getPermissionId();
            }
        }
        response.close();
        return "";
    }

    public void addMemberToSpace(String spaceName, String userName, String permission
            , String expirationDate) throws IOException {
        Log.log(Level.FINE, "Add user: " + userName + " to space: " + spaceName
                + " with permission "  + permission + " and expiration: " + expirationDate);
        String spaceId = getSpaceIdFromName(spaceName);
        String permissionId = getPermissionId(spaceId, permission);
        String userId = getUserIdFromName(userName).getId();
        boolean hasExpiration = expirationDate != null && !expirationDate.trim().isEmpty();
        String url = urlServer + members + spaceId + "/root/invite";
        Log.log(Level.FINE, "URL: " + url);
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        if (hasExpiration) {
            String expirationFormatted = DateUtils.daysToUTCForExpiration(expirationDate);
            Log.log(Level.FINE, "Formatted date: " + expirationFormatted);
            jsonBuilder.append("\"expirationDateTime\": \"")
                .append(expirationFormatted)
                .append("\",");
        }
        jsonBuilder.append("\"recipients\": [")
            .append("  {")
            .append("    \"@libre.graph.recipient.type\": \"user\",")
            .append("    \"objectId\": \"").append(userId).append("\"")
            .append("  }")
            .append("],")
            .append("\"roles\": [")
            .append("  \"").append(permissionId).append("\"")
            .append("]")
            .append("}");
        String json = jsonBuilder.toString();
        Log.log(Level.FINE, "Body: " + json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = postRequest(url, body, "Alice");
        Response response = httpClient.newCall(request).execute();
        Log.log(Level.FINE, "Response Code: " + response.code());
        Log.log(Level.FINE, "Response Body: " + response.body().string());
        response.close();
    }

    public void addLinkToSpace(String spaceName, String linkName, String permission, String expirationDate)
            throws IOException {
        Log.log(Level.FINE, "Add link: " + linkName + " to space: " + spaceName
                + " with permission "  + permission + " and expiration: " + expirationDate);
        String spaceId = getSpaceIdFromName(spaceName);
        String permissionName = getPermissionName(permission);
        boolean hasExpiration = expirationDate != null && !expirationDate.trim().isEmpty();
        String url = urlServer + members + spaceId + "/root/createLink";
        Log.log(Level.FINE, "URL: " + url);
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        if (hasExpiration) {
            String expirationFormatted = DateUtils.daysToUTCForExpiration(expirationDate);
            Log.log(Level.FINE, "Formatted date: " + expirationFormatted);
            jsonBuilder.append("\"expirationDateTime\": \"")
                    .append(expirationFormatted)
                    .append("\",");
        }
        jsonBuilder.append("\"displayName\":\"").append(linkName).append("\",")
                .append("\"type\":\"").append(permissionName).append("\",")
                .append("\"password\":\"").append("aa55AA..").append("\"")
                .append("}");
        String json = jsonBuilder.toString();
        Log.log(Level.FINE, "Body: " + json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = postRequest(url, body, "Alice");
        Response response = httpClient.newCall(request).execute();
        Log.log(Level.FINE, "Response Code: " + response.code());
        Log.log(Level.FINE, "Response Body: " + response.body().string());
        response.close();
    }

    private String getPermissionName(String permission) {
        return switch (permission) {
            case "Can view" -> "view";
            case "Can edit" -> "edit";
            case "Secret file drop" -> "createOnly";
            default -> "view";
        };
    }

    //Move to parser space
    private List<OCSpace> getSpacesFromResponse(Response httpResponse) throws IOException {
        String json = httpResponse.body().string();
        ArrayList<OCSpace> spaces = new ArrayList<>();
        JSONObject obj = new JSONObject(json);
        JSONArray value = obj.getJSONArray("value");
        for (int i = 0; i < value.length(); i++) {
            JSONObject jsonObject = value.getJSONObject(i);
            String type = jsonObject.getString("driveType");
            if (type.equals("project")) { //Just for user created spaces
                OCSpace space = new OCSpace();
                space.setType(jsonObject.getString("driveType"));
                space.setId(jsonObject.getString("id"));
                space.setName(jsonObject.getString("name"));
                // Description can be null
                space.setDescription(jsonObject.optString("description", ""));
                JSONObject owner = jsonObject.getJSONObject("owner");
                JSONObject user = owner.getJSONObject("user");
                space.setOwner(user.getString("id"));
                JSONObject quota = jsonObject.getJSONObject("quota");
                space.setQuota(quota.getLong("total"));
                spaces.add(space);
                Log.log(Level.FINE, "Space id returned: " + space.getId() + " " + space.getName());
            }
        }
        return spaces;
    }
}
