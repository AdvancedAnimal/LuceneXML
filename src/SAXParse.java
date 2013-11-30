import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParse extends DefaultHandler {
	private StringBuilder elementBuffer = new StringBuilder();
	private Map<String, String> attributeMap = new HashMap<String, String>();
	private Document doc;
	private String dir = "C:\\Users\\Kyle\\Documents\\workspace\\LessBullshitExample\\";
	private String file = "sampleXML.xml";
	
	public SAXParse() {
		
	}
	
	public void init() {
		SAXParse handler = new SAXParse();
		Document doc = null;
		try {
			doc = handler.getDocument(
					new FileInputStream(new File(dir+file)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(doc);
		Indexer indexer = new Indexer();
		indexer.init(doc);
	}

	public Document getDocument(InputStream is)
	// throws DocumentHandlerException
	{
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser parser = spf.newSAXParser();
			parser.parse(is, this);
		} catch (Exception e) {
			// throw new DocumentHandlerException(
			// "Cannot parse XML document", e);
		}
		return doc;
	}

	public void startDocument() {
		doc = new Document();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {

		elementBuffer.setLength(0);
		attributeMap.clear();
		int numAtts = atts.getLength();
		if (numAtts > 0) {
			for (int i = 0; i < numAtts; i++) {
				attributeMap.put(atts.getQName(i), atts.getValue(i));
			}
		}
	}

	public void characters(char[] text, int start, int length) {
		elementBuffer.append(text, start, length);
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("address-book")) {
			return;
		} else if (qName.equals("contact")) {
			for (Entry<String, String> attribute : attributeMap.entrySet()) {
				String attName = attribute.getKey();
				String attValue = attribute.getValue();
				doc.add(new Field(attName, attValue, Field.Store.YES,
						Field.Index.NOT_ANALYZED));
			}
		} else {
			doc.add(new Field(qName, elementBuffer.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
	}

}
