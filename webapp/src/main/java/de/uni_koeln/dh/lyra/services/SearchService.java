package de.uni_koeln.dh.lyra.services;

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
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.data.Song;

@Service
public class SearchService {

	private String indexDirPath;
	private String field;
	private int[] years;
	private boolean compilation;
	private boolean fuzzy;

	@Autowired
	private CorpusService corpusService;

	public void setIndexDirPath(String indexDirPath) {
		this.indexDirPath = "./data/" + indexDirPath + "/index/";
	}

	public void initIndex() throws IOException {
		Directory dir;
		File folder = new File(indexDirPath);
		if (!folder.exists() || folder.list().length <= 1) {
			folder.mkdirs();
			dir = new SimpleFSDirectory(new File(indexDirPath).toPath());
			IndexWriterConfig writerConfig = new IndexWriterConfig(new StandardAnalyzer());
			IndexWriter writer = new IndexWriter(dir, writerConfig);
			for (Artist artist : corpusService.getArtistList()) {
				for (Document doc : convertToLuceneDoc(artist)) {
					writer.addDocument(doc);
				}
			}
			writer.close();
		}
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setYears(int[] years) {
		this.years = years;
	}

	public void setFuzzy(boolean fuzzy) {
		this.fuzzy = fuzzy;
	}

	public void setCompilation(boolean compilation) {
		this.compilation = compilation;
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
		return doc;
	}

	public List<Song> search(String q) throws ParseException, IOException {
		Directory dir = new SimpleFSDirectory(new File(indexDirPath).toPath());
		DirectoryReader dirReader = DirectoryReader.open(dir);
		IndexSearcher is = new IndexSearcher(dirReader);

		if (fuzzy) {
			q = q + "~";
		}
		QueryParser parser = new QueryParser(field, new StandardAnalyzer());
		Query queryText = parser.parse(q);
		Query rangeQuery = IntPoint.newRangeQuery("year", years[0], years[1]);
		BooleanClause bcText = new BooleanClause(queryText, Occur.MUST);
		BooleanClause bcRange = new BooleanClause(rangeQuery, Occur.MUST);

		Builder builder = new BooleanQuery.Builder().add(bcText).add(bcRange);

		if (compilation) {
			parser = new QueryParser("compilation", new StandardAnalyzer());
			builder = builder.add(new BooleanClause(parser.parse("true"), Occur.MUST));
		}

		BooleanQuery booleanQuery = builder.build();

		System.out.println("booleanQuery: " + booleanQuery);
		TopDocs hits = is.search(booleanQuery, dirReader.numDocs());

		List<Song> resultList = new ArrayList<Song>();
		for (int i = 0; i < hits.scoreDocs.length; i++) {
			ScoreDoc scoreDoc = hits.scoreDocs[i];
			Document doc = is.doc(scoreDoc.doc);
			Song currentSong = corpusService.getSongByID(doc.get("uuid"));
			if (currentSong != null)
				resultList.add(currentSong);
		}
		dirReader.close();
		return resultList;
	}

}
