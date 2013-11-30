import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;


public class Indexer {
	
	public Indexer() {
		
	}
	public void init(Document saxDoc) {
		// 0. Specify the analyzer for tokenizing text.
	    //    The same analyzer should be used for indexing and searching
	    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

	    // 1. create the index
	    Directory index = new RAMDirectory();

	    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);

	    IndexWriter w;
		try {
			w = new IndexWriter(index, config);
			/*addDoc(w, "Lucene in Action", "193398817");
		    addDoc(w, "Lucene for Dummies", "55320055Z");
		    addDoc(w, "Managing Gigabytes", "55063554A");
		    addDoc(w, "The Art of Computer Science", "9900333X");*/
			addDoc(w, saxDoc);
		    w.close();
		    Searcher searcher = new Searcher(index);
		    searcher.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	/*private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
	    Document doc = new Document();
	    doc.add(new TextField("title", title, Field.Store.YES));

	    // use a string field for isbn because we don't want it tokenized
	    doc.add(new StringField("isbn", isbn, Field.Store.YES));
	    w.addDocument(doc);
	  }*/
	private static void addDoc(IndexWriter w, Document doc) throws IOException {
		w.addDocument(doc);
	}

}
