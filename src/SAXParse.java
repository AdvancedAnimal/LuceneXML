import java.util.Vector;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.FieldInfo.IndexOptions;


public class SAXParse extends DefaultHandler {

        private String startTag;
        private Vector<Document> documents;
        private Document doc;
        
        public SAXParse() {
                documents = new Vector<Document>();
        }
        
        public Vector<Document> getDoc()
        {
                return documents;
        }

        @Override
        public void characters(char[] ch, int start, int length)
                        throws SAXException {
                String printS = "";
            int i = start;
            while (i < start + length) {
                    if(ch[i] != 10 && ch[i] != 32)
                            printS += ch[i];
                    i++;
                }
            printS.trim();
            if(printS.length()>0){
                    System.out.print(" | tag:" + startTag + ": " + printS);
            	FieldType fieldType = new FieldType();
            	fieldType.setStored(true);
            	fieldType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
                doc.add(new Field(startTag, printS, fieldType));
            }
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                        Attributes attributes) throws SAXException {
        	if(localName.equals("article")){
        		doc = new Document();
        	}
                super.startElement(uri, localName, qName, attributes);
                startTag = localName;
        }
        
     

        @Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			// TODO Auto-generated method stub
			super.endElement(uri, localName, qName);
			if(localName.equals("article")) {
				documents.add(doc);
				doc = null;
			}
		}

		public void clearDoc() {
                doc = new Document();
        }

}
