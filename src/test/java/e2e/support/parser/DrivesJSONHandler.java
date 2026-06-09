/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.support.parser;

import org.json.JSONArray;
import org.json.JSONObject;

public class DrivesJSONHandler {

    public static String getPersonalDriveId(String json){
        JSONObject drivesObj = new JSONObject(json);
        JSONArray valuesArr = drivesObj.getJSONArray("value");
        for (int i = 0; i < valuesArr.length() ; i++){
            JSONObject drivesList = valuesArr.getJSONObject(i);
            if (drivesList.get("driveType").equals("personal")){
                return (String)drivesList.get("id");
            }
        }
        return "";
    }

    public static String getSpaceId(String json, String spaceName){
        JSONObject drivesObj = new JSONObject(json);
        JSONArray valuesArr = drivesObj.getJSONArray("value");
        for (int i = 0; i < valuesArr.length() ; i++){
            JSONObject drivesList = valuesArr.getJSONObject(i);
            if (drivesList.get("driveType").equals("project") &&
                    drivesList.get("driveAlias").equals("project/" + spaceName)){
                return (String)drivesList.get("id");
            }
        }
        return "";
    }

    public static String getSharesDriveId(String json) {
        JSONObject drivesObj = new JSONObject(json);
        JSONArray valuesArr = drivesObj.getJSONArray("value");
        for (int i = 0; i < valuesArr.length(); i++) {
            JSONObject drivesList = valuesArr.getJSONObject(i);
            if (drivesList.get("driveType").equals("virtual")) {
                return (String) drivesList.get("id");
            }
        }
        return "";
    }
}
