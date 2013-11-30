import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;


public class Searcher {
	private Directory index;
	
	public Searcher(Directory index) {
		this.index = index;
	}
	
	public void init() {
		 String querystr = "Zane Pasolini";
		 StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
		 //Directory index = new RAMDirectory();

		    // the "title" arg specifies the default field to use
		    // when no field is explicitly specified in the query.
		    Query q = null;
			try {
				q = new QueryParser(Version.LUCENE_40, "", analyzer).parse(querystr);
				
				 // 3. search
		    int hitsPerPage = 10;
		    IndexReader reader = DirectoryReader.open(index);
		    IndexSearcher searcher = new IndexSearcher(reader);
		    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		    searcher.search(q, collector);
		    ScoreDoc[] hits = collector.topDocs().scoreDocs;
		    
		    // 4. display results
		    System.out.println("Found " + hits.length + " hits.");
		    for(int i=0;i<hits.length;++i) {
		      int docId = hits[i].doc;
		      Document d = searcher.doc(docId);
		      System.out.println((i + 1) + ". " + d.get("name"));
		      reader.close();
		    }
				
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		   

		    // reader can only be closed when there
		    // is no need to access the documents any more.

	}

}
