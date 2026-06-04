package utils.api;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.LocProperties;
import utils.entities.OCShare;
import utils.log.Log;
import utils.parser.ShareSAXHandler;

public class ShareAPI extends CommonAPI {

    private String sharingEndpoint = "/ocs/v1.php/apps/files_sharing/api/v1/shares";
    private String pendingEndpoint = "/pending";
    private final String shareeU = LocProperties.getProperties().getProperty("userToShare");

    public ShareAPI() throws IOException {
        super();
    }

    public void createShare(String sharingUser, String itemPath, String sharee, String type,
                            String permissions, String name, String password, int sharelevel)
            throws IOException {
        String url = urlServer + sharingEndpoint;
        Log.log(Level.FINE, "Starts: Create Share - " + sharingUser + " " + sharee + " "
                + itemPath + " " + type + " " + " " + password + " " + permissions);
        Log.log(Level.FINE, "URL: " + url);
        Request request = postRequest(url, createBodyShare(itemPath, sharee, type, permissions,
                name, password, sharelevel), sharingUser);
        Response response = httpClient.newCall(request).execute();
        Log.log(Level.FINE, "Response Code: " + response.code());
        Log.log(Level.FINE, "Response Body: " + response.body().string());
        response.close();
    }

    public OCShare getShare(String itemPath)
            throws IOException, SAXException, ParserConfigurationException {
        String url = urlServer + sharingEndpoint + "?path=/" + itemPath;
        Log.log(Level.FINE, "Starts: Request Share from server - " + itemPath);
        Log.log(Level.FINE, "URL: " + url);
        Request request = getRequest(url);
        Response response = httpClient.newCall(request).execute();
        Log.log(Level.FINE, "Request sent");
        OCShare share = getId(response);
        response.close();
        return share;
    }

    public ArrayList<OCShare> getSharesByUser(String userName)
            throws IOException, SAXException, ParserConfigurationException {
        Log.log(Level.FINE, "-- Get Shares by user--");
        String url = urlServer + sharingEndpoint + "?state=all&shared_with_me=true";
        Log.log(Level.FINE, "Starts: Request Shares by user - " + userName);
        Log.log(Level.FINE, "URL: " + url);
        Request request = getRequest(url, userName);
        Response response = httpClient.newCall(request).execute();
        Log.log(Level.FINE, "Response code: " + response.code());
        ArrayList<OCShare> shares = getSharesFromRequest(response);
        Log.log(Level.FINE, "Shares from user " + userName + ": " + shares.size());
        response.close();
        return shares;
    }

    public ArrayList<OCShare> getLinksByDefault()
            throws IOException, SAXException, ParserConfigurationException {
        String url = urlServer + sharingEndpoint + "?state=all&shared_with_me=true";
        Log.log(Level.FINE, "Starts: Request Links by user - Alice");
        Log.log(Level.FINE, "URL: " + url);
        Request request = getRequest(url, "alice");
        Response response = httpClient.newCall(request).execute();
        Log.log(Level.FINE, "Response code: " + response.code());
        ArrayList<OCShare> shares = getSharesFromRequest(response);
        ArrayList<OCShare> linksInShares = new ArrayList<>();
        for (OCShare linkInShare : shares) {
            if (linkInShare.getType().equals("3")) {
                linksInShares.add(linkInShare);
            }
        }
        Log.log(Level.FINE, "Links from user Alice: " + linksInShares.size());
        response.close();
        return linksInShares;
    }


    public boolean isSharedWithMe(String itemName, String userName, boolean isGroup)
            throws IOException, ParserConfigurationException, SAXException {
        String url = urlServer + sharingEndpoint + "?shared_with_me=true";
        Log.log(Level.FINE, "Starts: Request items shared with me - " + itemName + " "
                + userName);
        Log.log(Level.FINE, "URL: " + url);
        Request request;
        //if it is a group, we use a predefined sharee inside the group (user2)
        if (isGroup) {
            request = getRequest(url, shareeU);
        } else {
            request = getRequest(url, userName);
        }
        Response response = httpClient.newCall(request).execute();
        ArrayList<OCShare> myShares = getSharesFromRequest(response);
        Log.log(Level.FINE, myShares.size() + " shares found");
        response.close();
        for (OCShare share : myShares) {
            Log.log(Level.FINE, "ItemName: " + itemName + " ShareName: "
                    + share.getItemName());
            if (share.getItemName().contains(itemName)) { //Current item found
                return share.getShareeName().equalsIgnoreCase(userName);
            }
        }
        return false;
    }

    public void acceptAllShares(String type, String userName)
            throws IOException, ParserConfigurationException, SAXException {
        Log.log(Level.FINE, "ACCEPT ALL SHARES FROM " + type + " - " + userName);
        String userToShare = userName;
        if (type.equals("group")) {
            userToShare = "bob"; //Using Bob as default user inside group to test.
        }
        Log.log(Level.FINE, "User to share: " + userToShare);
        ArrayList<OCShare> sharesToAccept = getSharesByUser(userToShare);
        for (OCShare share : sharesToAccept) {
            String url = urlServer + sharingEndpoint + pendingEndpoint + "/" + share.getId();
            Log.log(Level.FINE, "URL: " + url);
            Request request = postRequest(url, acceptPendingShare(share.getId()), userToShare);
            Response response = httpClient.newCall(request).execute();
            Log.log(Level.FINE, "Response Body: " + response.body().string());
            response.close();
        }
    }

    private RequestBody createBodyShare(String itemPath, String sharee, String type,
                                        String permissions, String name, String password,
                                        int isReshare)
            throws IOException {
        Log.log(Level.FINE, "BODY SHARE: path " + itemPath + " sharee: "
                + sharee + " type: " + type + " permi: " + permissions + " name:" + name);
        FormBody.Builder body = new FormBody.Builder();
        if (isReshare == 1 && isOCIS) {
            body.add("path", "/Shares/" + itemPath);
        } else {
            body.add("path", itemPath);
        }
        body.add("shareType", type);
        body.add("shareWith", sharee);
        body.add("permissions", permissions);
        body.add("name", name);
        body.add("password", password);
        return body.build();
    }

    private RequestBody acceptPendingShare(String shareId) {
        FormBody.Builder body = new FormBody.Builder();
        body.add("share_id", shareId);
        return body.build();
    }

    private OCShare getId(Response httpResponse)
            throws IOException, SAXException, ParserConfigurationException {
        //Create SAX parser
        SAXParserFactory parserFactor = SAXParserFactory.newInstance();
        SAXParser parser = parserFactor.newSAXParser();
        ShareSAXHandler handler = new ShareSAXHandler();

        parser.parse(new InputSource(new StringReader(httpResponse.body().string())), handler);
        httpResponse.body().close();
        return handler.getShare();
    }

    private ArrayList<OCShare> getSharesFromRequest(Response httpResponse)
            throws IOException, SAXException, ParserConfigurationException {
        //Create SAX parser
        SAXParserFactory parserFactor = SAXParserFactory.newInstance();
        SAXParser parser = parserFactor.newSAXParser();
        ShareSAXHandler handler = new ShareSAXHandler();

        parser.parse(new InputSource(new StringReader(httpResponse.body().string())), handler);
        httpResponse.body().close();
        return handler.getAllShares();
    }
}
