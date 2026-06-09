/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.support.parser;

import static e2e.support.log.Log.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.logging.Level;

import e2e.model.OCFile;

public class FileSAXHandler extends DefaultHandler {

    private OCFile file;
    private ArrayList<OCFile> listFiles = new ArrayList<OCFile>();
    private static String text = null;

    @Override
    public void startElement(String uri, String localName, String node, Attributes attributes) {
        if (node.equals("d:response")) {
            file = new OCFile();
        }
    }

    @Override
    public void endElement(String uri, String localName, String node) {
        switch (node) {
            case "d:href": {
                file.setPath(text);
                break;
            }
            case "oc:permissions": {
                file.setPermissions(text);
                break;
            }
            case "oc:size": {
                file.setSize(text);
                break;
            }
            case "oc:privatelink": {
                file.setPrivateLink(text);
                break;
            }
            case "d:getlastmodified": {
                file.setLastModified(text);
                break;
            }
            case "d:response": {
                file.setName(getFileNameFromPath(file.getPath()));
                listFiles.add(file);
                break;
            }
            case "d:getcontenttype": {
                file.setType(text);
                break;
            }
            default:
                break;
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        text = String.copyValueOf(ch, start, length).trim();
    }

    public ArrayList<OCFile> getListFiles() {
        return listFiles;
    }

    private String getFileNameFromPath(String path) {
        try {
            String[] pathSplitted = path.split("/");
            return URLDecoder.decode(pathSplitted[pathSplitted.length - 1], "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.log(Level.SEVERE, "Unsupported Encoding Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
