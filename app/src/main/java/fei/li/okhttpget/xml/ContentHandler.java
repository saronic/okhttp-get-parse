package fei.li.okhttpget.xml;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by li on 2017/10/9.
 */

public class ContentHandler extends DefaultHandler {
    private static final String TAG = "ContentHandler";
    private String nodeName;
    private StringBuffer city;
    private StringBuffer wendu;

    @Override
    public void startDocument() throws SAXException {
        city = new StringBuffer();
        wendu = new StringBuffer();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        nodeName = localName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if ("city".equals(nodeName)) {
            city.append(ch, start, length);
        } else if ("wendu".equals(nodeName)) {
            wendu.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("wendu".equals(localName)) {
            Log.d(TAG, "endElement: city: " + city.toString());
            Log.d(TAG, "endElement: wendu: " + wendu.toString());
        }
    }
}
