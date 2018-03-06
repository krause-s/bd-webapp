package de.uni_koeln.dh.lyra.lucene.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.springframework.stereotype.Service;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.data.Song;

@Service
public class SearchService {

	private String indexDirPath = "./data/index/000001/";

	public void setIndexDirPath(String indexDirPath) {
		this.indexDirPath = indexDirPath;
	}

	public void initIndex(List<Artist> artistList) throws IOException {
		Directory dir;
		File folder = new File(indexDirPath);
		if (!folder.exists() || folder.list().length <= 1) {
			folder.mkdirs();
			dir = new SimpleFSDirectory(new File(indexDirPath).toPath());
			IndexWriterConfig writerConfig = new IndexWriterConfig(new StandardAnalyzer());
			IndexWriter writer = new IndexWriter(dir, writerConfig);
			for (Artist artist : artistList) {
				for (Document doc : convertToLuceneDoc(artist)) {
					writer.addDocument(doc);
				}
			}
			writer.close();
		}
	}

	private List<Document> convertToLuceneDoc(Artist artist) {
		List<Document> luceneDocLists = new ArrayList<Document>();
		for (Song song : artist.getSongs()) {
			luceneDocLists.add(convertToLuceneDoc(song));
		}
		return luceneDocLists;
	}

	private Document convertToLuceneDoc(Song song) {
		Document doc = new Document();
		doc.add(new TextField("title", song.getTitle(), Store.YES));
		doc.add(new TextField("lyrics", song.getLyrics(), Store.YES));
		doc.add(new TextField("artist", song.getArtist(), Store.YES));
		doc.add(new TextField("release", song.getRelease(), Store.YES));
		doc.add(new TextField("uuid", song.getUuid(), Store.YES));
		doc.add(new TextField("comment", song.getComment(), Store.YES));
		doc.add(new TextField("compilation", String.valueOf(song.isCompilation()), Store.YES));
		doc.add(new IntPoint("year", song.getYear()));
		System.out.println(doc.get("year"));
		return doc;
	}
	
	public List<Document> search(String q) throws IOException, ParseException {
		return search(q, "lyrics");
	}

	public List<Document> search(String q, String field) throws IOException, ParseException {
		
		Directory dir = new SimpleFSDirectory(new File(indexDirPath).toPath());
		DirectoryReader dirReader = DirectoryReader.open(dir);
		IndexSearcher is = new IndexSearcher(dirReader);

		QueryParser parser = new QueryParser(field, new StandardAnalyzer());
		Query query = parser.parse(q);

		TopDocs hits = is.search(query, dirReader.numDocs());

		long hitSize = hits.totalHits;
		System.out.println("hitSize: " + hitSize);

		List<Document> resultList = new ArrayList<Document>();
		for (int i = 0; i < hits.scoreDocs.length; i++) {
			ScoreDoc scoreDoc = hits.scoreDocs[i];
			Document doc = is.doc(scoreDoc.doc);
			resultList.add(doc);
		}
		dirReader.close();
		return resultList;
	}

}
