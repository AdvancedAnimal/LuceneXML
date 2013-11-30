//import java.io.File;
//import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.xml.sax.Attributes;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public class ProgramStart {

	/**
	 * @param args
	 */
	private static String writeDir = "C:\\Users\\Michael\\Documents\\UWSP\\Fall 2013\\XMLWorkspace\\LuceneXML\\Data\\";
	private static int numberOfDocs = 2;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//load files, create array
		File dir = new File("Data/");
		Vector<File> xmlDocs = new Vector<File>();
		//Articles-Dump-#
		String fileName = "Articles-Dump-";
		for(int i = 0; i < numberOfDocs; i++) {
			xmlDocs.add(new File(writeDir+fileName+i+".xml"));
		}
		
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		SAXParser saxParser;
		try {
			saxParser = factory.newSAXParser();
			XMLReader xmlReader =  saxParser.getXMLReader();
			SAXParse parseToIndex = new SAXParse();
			xmlReader.setContentHandler(parseToIndex);
			Vector<Document> docList = new Vector<Document>();
			
			if(xmlDocs != null) {
				for(int j = 0; j < xmlDocs.size(); j++) {
					xmlReader.parse(new InputSource(new FileReader(xmlDocs.get(j))));
					Vector<Document> docs = parseToIndex.getDoc();
					for(Document doc : docs){
						FieldType fieldType = new FieldType();
		            	fieldType.setStored(true);
		            	fieldType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
		                doc.add(new Field("$$$Filename", xmlDocs.get(j).getPath(), fieldType));
		                docList.add(doc);
					}
				}
			}
			
			Directory directory = new RAMDirectory();
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
			IndexWriter indexWriter = new IndexWriter(directory, config);
			for(int x = 0; x < docList.size(); x++) {
				indexWriter.addDocument(docList.get(x));
			}
			
			indexWriter.close();
			
			IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
			String element = "";
			do{
				System.out.println("Enter tag (press q to quit)");
				element = keyboard.readLine();
				if(!element.equalsIgnoreCase("Q")) {
					System.out.println("Enter value: ");
					String value = keyboard.readLine();
					System.out.println("Searching for " + element + ": " + value);
					
					Query query = new QueryParser(Version.LUCENE_46, element, analyzer).parse(value);
					TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
					searcher.search(query, collector);
					ScoreDoc[] hits = collector.topDocs().scoreDocs;
					
					System.out.println("Found " + hits.length + " hits.");
					for(int h = 0; h < hits.length; h++) {
						int id = hits[h].doc;
						Document de = searcher.doc(id);
						System.out.println((h+1 + ". " + de.get("$$$FileName")));
					}
					
				}
			}
			while(!element.equalsIgnoreCase("Q"));
			
			
			
			
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//SAXParse butthole = new SAXParse();
		//butthole.init();
		//Searcher searcher = new Searcher();
		//searcher.init();

	}

}
