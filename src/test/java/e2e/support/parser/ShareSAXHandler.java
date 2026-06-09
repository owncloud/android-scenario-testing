/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.support.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.logging.Level;

import e2e.model.OCShare;
import e2e.support.log.Log;

public class ShareSAXHandler extends DefaultHandler {

    private OCShare share;
    private ArrayList<OCShare> allShares;
    private static String text = null;


    @Override
    public void startElement(String uri, String localName, String node, Attributes attributes) {
        switch (node) {
            case ("data"): {
                allShares = new ArrayList<OCShare>();
            }
            case ("element"): {
                share = new OCShare();
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String node)
            throws SAXException {
        switch (node) {
            case ("id"): {
                Log.log(Level.FINE, "Id: " + text);
                share.setId(text);
                break;
            }
            case ("uid_file_owner"): {
                Log.log(Level.FINE, "uid: " + text);
                share.setOwner(text);
                break;
            }
            case ("share_type"): {
                Log.log(Level.FINE, "type: " + text);
                share.setType(text);
                break;
            }
            case ("share_with"): {
                Log.log(Level.FINE, "with: " + text);
                share.setShareeName(text);
                break;
            }
            case ("name"): {
                Log.log(Level.FINE, "name: " + text);
                share.setLinkName(text);
                break;
            }
            case ("path"): {
                Log.log(Level.FINE, "name: " + text);
                share.setItemName(text.substring(1, text.length()));
                break;
            }
            case ("permissions"): {
                Log.log(Level.FINE, "permission: " + text);
                share.setPermissions(text);
                break;
            }
            case ("expiration"): {
                Log.log(Level.FINE, "expiration: " + text);
                share.setExpiration(text);
                break;
            }
            case ("element"): {
                allShares.add(share);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        text = String.copyValueOf(ch, start, length).trim();
    }

    public OCShare getShare() {
        return share;
    }

    public ArrayList<OCShare> getAllShares() {
        return allShares;
    }

}
