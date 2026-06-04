package utils.api;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.entities.OCFile;
import utils.log.Log;
import utils.parser.FileSAXHandler;

public class FilesAPI extends CommonAPI {

    public FilesAPI() throws IOException {
        super();
    }

    public void removeItem(String itemName, String userName)
            throws IOException {
        String url = urlServer + getEndpoint(userName) + "/" + itemName + "/";
        Log.log(Level.FINE, "Starts: Request remove item from server");
        Log.log(Level.FINE, "URL: " + url);
        Request request = deleteRequest(url, userName);
        Response response = httpClient.newCall(request).execute();
        response.close();
    }

    public void createFolder(String folderName, String userName)
            throws IOException {
        String url = urlServer + getEndpoint(userName) + "/" + folderName + "/";
        Log.log(Level.FINE, "Starts: Request create folder");
        Log.log(Level.FINE, "URL: " + url);
        Request request = davRequest(url, "MKCOL", null, userName);
        Response response = httpClient.newCall(request).execute();
        response.close();
    }

    public byte[] getFile(String fileName, String userName)
            throws IOException {
        String url = urlServer + getEndpoint(userName) + "/" + fileName + "/";
        Log.log(Level.FINE, "Starts: Request get file");
        Log.log(Level.FINE, "URL: " + url);
        Request request = davRequest(url, "GET", null, userName);
        Response response = httpClient.newCall(request).execute();
        byte[] body = response.body().bytes();
        response.close();
        return body;
    }

    public void pushFile(String fileName, String userName)
            throws IOException {
        String url = urlServer + getEndpoint(userName) + "/" + fileName + "/";
        Log.log(Level.FINE, "Starts: Request create file");
        Log.log(Level.FINE, "URL: " + url);
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),
                "textExample");
        Request request = davRequest(url, "PUT", body, userName);
        Response response = httpClient.newCall(request).execute();
        response.close();
    }

    public void pushFileByMime(String itemName, String mimetype)
            throws IOException {
        String url = urlServer + getEndpoint() + "/" + itemName + "/";
        File rootPath = new File(System.getProperty("user.dir"));
        Log.log(Level.FINE, "Starts: Request create file");
        Log.log(Level.FINE, "URL: " + url);
        File appDir = new File(rootPath, "src/test/resources");
        File content = new File(appDir, "io/cucumber/example-files/" + itemName);
        RequestBody body = RequestBody.create(MediaType.parse(mimetype), content);
        Request request = davRequest(url, "PUT", body, user);
        Response response = httpClient.newCall(request).execute();
        response.close();
    }

    public void pushFile(String fileName, String content, String userName)
            throws IOException {
        String url = urlServer + getEndpoint(userName) + "/" + fileName + "/";
        Log.log(Level.FINE, "Starts: Request modification file");
        Log.log(Level.FINE, "URL: " + url);
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),
                content);
        Request request = davRequest(url, "PUT", body, user);
        Response response = httpClient.newCall(request).execute();
        response.close();
    }

    public boolean itemExist(String itemName)
            throws IOException {
        String url = urlServer + getEndpoint() + "/" + itemName;
        Log.log(Level.FINE, "Starts: Request check if item exists in server");
        Log.log(Level.FINE, "URL: " + url);
        Response response;
        Request request = davRequest(url, "PROPFIND", null, user);
        response = httpClient.newCall(request).execute();
        response.close();
        switch (response.code() / 100) {
            case (2): {
                Log.log(Level.FINE, "Response " + response.code() + ". Item exists");
                return true;
            }
            case (4): {
                Log.log(Level.FINE, "Response " + response.code() + " "
                        + response.message() + ". Item does not exist");
                return false;
            }
            default: {
                Log.log(Level.WARNING, "Response neither 4xx nor 2xx. " +
                        "Something went wrong");
                return false;
            }
        }
    }

    // Overload for a specific space
    public boolean itemExist(String spaceId, String itemName)
            throws IOException {
        String url = urlServer + spacesEndpoint + spaceId + itemName;
        Log.log(Level.FINE, "Starts: Request check if item exists in server");
        Log.log(Level.FINE, "URL: " + url);
        Response response;
        Request request = davRequest(url, "PROPFIND", null, user);
        response = httpClient.newCall(request).execute();
        response.close();
        switch (response.code() / 100) {
            case (2): {
                Log.log(Level.FINE, "Response " + response.code() + ". Item exists");
                return true;
            }
            case (4): {
                Log.log(Level.FINE, "Response " + response.code() + " "
                        + response.message() + ". Item does not exist");
                return false;
            }
            default: {
                Log.log(Level.WARNING, "Response neither 4xx nor 2xx. " +
                        "Something went wrong");
                return false;
            }
        }
    }

    public ArrayList<OCFile> listShared()
            throws IOException, SAXException, ParserConfigurationException {
        Log.log(Level.FINE, "Starts: Request to fetch list of shared items from oCIS");
        Response response;
        String url = urlServer + getSharedEndpoint();
        Log.log(Level.FINE, "URL: " + url);
        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"),
                basicPropfindBody);
        Request request = davRequest(url, "PROPFIND", body, user);
        response = httpClient.newCall(request).execute();
        ArrayList<OCFile> listItems = getList(response);
        response.close();
        Log.log(Level.FINE, "ITEMS: " + listItems.size());
        return listItems;
    }

    public ArrayList<OCFile> listItems(String path, String userName)
            throws IOException, SAXException, ParserConfigurationException {
        Response response;
        String url = urlServer + getEndpoint(userName) + "/" + path;
        Log.log(Level.FINE, "Starts: Request to fetch list of items from server");
        Log.log(Level.FINE, "URL: " + url);
        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"),
                basicPropfindBody);
        Request request = davRequest(url, "PROPFIND", body, userName);
        response = httpClient.newCall(request).execute();
        ArrayList<OCFile> listItems = getList(response);
        response.close();
        return listItems;
    }

    private ArrayList<OCFile> getList(Response httpResponse)
            throws IOException, SAXException, ParserConfigurationException {
        //Create SAX parser
        SAXParserFactory parserFactor = SAXParserFactory.newInstance();
        SAXParser parser = parserFactor.newSAXParser();
        FileSAXHandler handler = new FileSAXHandler();
        parser.parse(new InputSource(new StringReader(httpResponse.body().string())), handler);
        httpResponse.body().close();
        return handler.getListFiles();
    }
}