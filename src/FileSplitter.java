//import javax.swing.text.Document;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


/**
 * Author: Sam Bolgert
 * Date: 11/29/13
 * Time: 6:52 PM
 */
public class FileSplitter {

	private static String dir ="C:\\Users\\Michael\\Documents\\UWSP\\Fall 2013\\XMLWorkspace\\LuceneXML\\";
	private static String file = "source.xml";
	
	private static String writeDir = "C:\\Users\\Michael\\Documents\\UWSP\\Fall 2013\\XMLWorkspace\\LuceneXML\\Data\\";
	private static String writeFile = "articleDoc";

    public static void main(String args[]){
        //read the file
    	
    	
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			//factory.setAttribute("entityExpansionLimit", 1000000);
			
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(dir+file);
	    	XPathFactory xPathfactory = XPathFactory.newInstance();
	    	XPath xpath = xPathfactory.newXPath();
	    	XPathExpression expr = xpath.compile("//article");
	    	NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			
	    	int count = 0;
	    	int fileCount = 0;
	    	String buffer = "";
	    	System.out.println("created string buffer");
	    	for(int i=0; i<nl.getLength(); i++) {
	    		buffer += nl.item(i).toString();
	    		count++;
	    		if (count == 1000) {
	    			String fileName = writeDir + writeFile + fileCount;
	    			FileWriter writer = new FileWriter(fileName);
	    			writer.write(buffer);
	    			buffer = "";
	    			writer.close();
	    			count = 0;
	    			System.out.println("Wrote file " + fileCount);
	    			fileCount++;
	    			
	    		}
	    	}
	    
	    	
	    	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

        //run the //article xpath

        //for article in articles
            //if count >= 1000
                //write file
		
		
		
    }
}
