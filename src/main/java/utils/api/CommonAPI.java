package utils.api;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.logging.Level;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.LocProperties;
import utils.log.Log;
import utils.network.oCHttpClient;
import utils.parser.DrivesJSONHandler;

public class CommonAPI {

    protected OkHttpClient httpClient = oCHttpClient.getUnsafeOkHttpClient();

    protected String urlServer = System.getProperty("server");
    protected String userAgent = LocProperties.getProperties().getProperty("userAgent");
    protected String host = getHost();

    protected String user = LocProperties.getProperties().getProperty("userName1");
    protected String password = LocProperties.getProperties().getProperty("passw1");
    protected String credentialsB64 = Base64.getEncoder().encodeToString((user + ":" + password).getBytes());

    protected final String webdavEndpoint = "/remote.php/dav/files";
    protected final String spacesEndpoint = "/dav/spaces/";
    protected final String graphDrivesEndpoint = "/graph/v1.0/me/drives";
    protected String davEndpoint = "";
    boolean isOCIS = System.getProperty("backend").equals("oCIS");
    protected HashMap<String, String> personalSpaces;

    protected String basicPropfindBody = "<?xml version='1.0' encoding='UTF-8' ?>\n" +
            "<propfind xmlns=\"DAV:\" xmlns:CAL=\"urn:ietf:params:xml:ns:caldav\"" +
            " xmlns:CARD=\"urn:ietf:params:xml:ns:carddav\" " +
            " xmlns:SABRE=\"http://sabredav.org/ns\" " +
            " xmlns:OC=\"http://owncloud.org/ns\">\n" +
            "  <prop>\n" +
            "    <displayname />\n" +
            "    <getcontenttype />\n" +
            "    <resourcetype />\n" +
            "    <getcontentlength />\n" +
            "    <getlastmodified />\n" +
            "    <creationdate />\n" +
            "    <getetag />\n" +
            "    <quota-used-bytes />\n" +
            "    <quota-available-bytes />\n" +
            "    <OC:permissions />\n" +
            "    <OC:id />\n" +
            "    <OC:size />\n" +
            "    <OC:privatelink />\n" +
            "  </prop>\n" +
            "</propfind>";

    public CommonAPI() throws IOException {
        if (isOCIS) {
            personalSpaces = new HashMap<>();
            personalSpaces.put("Alice", getPersonalDrives(urlServer, "Alice"));
            personalSpaces.put("Bob", getPersonalDrives(urlServer, "Bob"));
            personalSpaces.put("Charles", getPersonalDrives(urlServer, "Charles"));
        } else {
            davEndpoint = webdavEndpoint + "/" + user;
        }
        Log.log(Level.FINE, "Endpoint: " + davEndpoint);
    }

    public String getEndpoint(String userName) {
        String endpoint;
        if (isOCIS) {
            endpoint = spacesEndpoint + personalSpaces.get(userName);
        } else {
            endpoint = davEndpoint = webdavEndpoint + "/" + user;
        }
        return endpoint;
    }

    public String getEndpoint() {
        if (isOCIS) {
            return spacesEndpoint + personalSpaces.get("Alice");
        } else {
            return davEndpoint = webdavEndpoint + "/" + user;
        }
    }

    public String getSharedEndpoint() throws IOException {
        Log.log(Level.FINE, "Starts: Get Shared Space Endpoint");
        String sharesSpaceId = getSharesDrives(urlServer);
        String endpoint = spacesEndpoint + sharesSpaceId;
        Log.log(Level.FINE, "Endpoint: " + endpoint);
        return endpoint;
    }

    protected Request davRequest(String url, String method, RequestBody body, String userName) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("OCS-APIREQUEST", "true")
                .addHeader("User-Agent", userAgent)
                .addHeader("Authorization", "Basic " + credentialsBuilder(userName))
                .addHeader("Host", host)
                .method(method, body)
                .build();
        return request;
    }

    protected Request postRequest(String url, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("OCS-APIREQUEST", "true")
                .addHeader("User-Agent", userAgent)
                .addHeader("Authorization", "Basic " + credentialsB64)
                .addHeader("Host", host)
                .post(body)
                .build();
        Log.log(Level.FINE, "RE: " + request.toString());
        return request;
    }

    protected Request postRequest(String url, RequestBody body, String userName) {
        Log.log(Level.FINE, "Username: " + userName + " - url: " + url);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("OCS-APIREQUEST", "true")
                .addHeader("User-Agent", userAgent)
                .addHeader("Authorization", "Basic " + credentialsBuilder(userName))
                .addHeader("Host", host)
                .post(body)
                .build();
        Log.log(Level.FINE, "RE: " + request.toString());
        return request;
    }

    protected Request deleteRequest(String url, String userName) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("OCS-APIREQUEST", "true")
                .addHeader("User-Agent", userAgent)
                .addHeader("Authorization", "Basic " + credentialsBuilder(userName))
                .addHeader("Host", host)
                .delete()
                .build();
        return request;
    }

    protected Request getRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("OCS-APIREQUEST", "true")
                .addHeader("User-Agent", userAgent)
                .addHeader("Authorization", "Basic " + credentialsB64)
                .addHeader("Host", host)
                .get()
                .build();
        return request;
    }

    //overloaded, to use with specific credentials
    protected Request getRequest(String url, String userName) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("OCS-APIREQUEST", "true")
                .addHeader("User-Agent", userAgent)
                .addHeader("Authorization", "Basic " + credentialsBuilder(userName))
                .addHeader("Host", host)
                .get()
                .build();
        return request;
    }

    private String getHost() {
        String host = System.getProperty("host");
        if (host == null || host.isEmpty()) {
            host = urlServer.split("//")[1];
        }
        Log.log(Level.FINE, "HOST: " + host);
        return host;
    }

    private String getPersonalDrives(String url, String userName) throws IOException {
        Log.log(Level.FINE, "Starts: Call get personal ID: " + url);
        Request request = getRequest(url + graphDrivesEndpoint, userName);
        Response response = httpClient.newCall(request).execute();
        String body = response.body().string();
        response.close();
        String personalId = DrivesJSONHandler.getPersonalDriveId(body);
        Log.log(Level.FINE, "Personal Drive ID: " + personalId);
        return personalId;
    }

    private String getSharesDrives(String url)
            throws IOException {
        Log.log(Level.FINE, "Starts: GET personal drives: " + url);
        Request request = getRequest(url + graphDrivesEndpoint, user);
        Response response = httpClient.newCall(request).execute();
        String body = response.body().string();
        response.close();
        String sharesId = DrivesJSONHandler.getSharesDriveId(body);
        Log.log(Level.FINE, "Shares Drive ID: " + sharesId);
        return sharesId;
    }

    private String credentialsBuilder(String userName) {
        return Base64.getEncoder().encodeToString((userName.toLowerCase() + ":" + password).getBytes());
    }
}
