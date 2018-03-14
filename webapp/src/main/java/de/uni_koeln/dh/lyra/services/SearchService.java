package de.uni_koeln.dh.lyra.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
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

	private String indexDirPath = "data/index";
	private String field;
	private int[] years = { 0, 3000 };
	private boolean compilation;
	private boolean fuzzy;
	private String searchPhrase = "";

	@Autowired
	private CorpusService corpusService;

	/**
	 * @throws IOException
	 * 
	 *             readsthe corpus given by corpusService and writes an index
	 */
	public void initIndex() throws IOException {
		Directory dir = new SimpleFSDirectory(new File(indexDirPath).toPath());
		File folder = new File(indexDirPath);
		folder.mkdirs();
		IndexWriterConfig writerConfig = new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter writer = new IndexWriter(dir, writerConfig);
		for (Artist artist : corpusService.getArtistList()) {
			for (Document doc : convertToLuceneDoc(artist)) {
				writer.addDocument(doc);
			}
		}
		writer.close();
	}

	/**
	 * Updating the index is equivalent to initializing. Index is completely
	 * rewritten instead of appended, because of references to the song UUID
	 */
	public void updateIndex() {
		try {
			initIndex();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param field
	 *            for query building
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @param years
	 *            for query building
	 */
	public void setYears(int[] years) {
		this.years = years;
	}

	/**
	 * @param fuzzy
	 *            for query building
	 */
	public void setFuzzy(boolean fuzzy) {
		this.fuzzy = fuzzy;
	}

	/**
	 * @param compilation
	 *            for query building
	 */
	public void setCompilation(boolean compilation) {
		this.compilation = compilation;
	}

	/**
	 * @param searchPhrase
	 *            for query building
	 */
	public void setSearchPhrase(String searchPhrase) {
		this.searchPhrase = searchPhrase;
	}

	/**
	 * @param artist
	 * @return a List of Lucene Docs converts all songs of an artist to Lucene
	 *         Documents
	 */
	private List<Document> convertToLuceneDoc(Artist artist) {
		List<Document> luceneDocLists = new ArrayList<Document>();
		for (Song song : artist.getSongs()) {
			luceneDocLists.add(convertToLuceneDoc(song));
		}
		return luceneDocLists;
	}

	/**
	 * @param song
	 * @return one Lucene Document converts a Song to Lucene Document
	 */
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

	/**
	 * @return BooleanQuery for Search uses the query fields set before to build
	 *         a query
	 */
	private BooleanQuery buildQuery() {
		String q = searchPhrase;
		if (fuzzy && !q.isEmpty()) {
			q = q + "~";
		}
		QueryParser parser = new QueryParser(field, new StandardAnalyzer());
		Builder builder = new BooleanQuery.Builder();
		if (!q.isEmpty()) {
			Query queryText = null;
			try {
				queryText = parser.parse(q);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			BooleanClause bcText = new BooleanClause(queryText, Occur.MUST);
			builder.add(bcText);
		}

		if (years != null && years.length == 2) {
			Query rangeQuery = IntPoint.newRangeQuery("year", years[0], years[1]);
			BooleanClause bcRange = new BooleanClause(rangeQuery, Occur.MUST);
			builder.add(bcRange);
		}

		if (compilation) {
			parser = new QueryParser("compilation", new StandardAnalyzer());
			try {
				builder.add(new BooleanClause(parser.parse("true"), Occur.MUST));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return builder.build();
	}

	/**
	 * @return List of songs as result
	 * @throws ParseException
	 * @throws IOException
	 * 
	 * reads an index and returns all retrieved results as a list of songs
	 */
	public List<Song> search() throws ParseException, IOException {
		DirectoryReader dirReader;
		IndexSearcher is;
		try {
			Directory dir = new SimpleFSDirectory(new File(indexDirPath).toPath());
			dirReader = DirectoryReader.open(dir);
			is = new IndexSearcher(dirReader);
		} catch (IndexNotFoundException e) {
			return new ArrayList<Song>();
		}
		TopDocs hits = is.search(buildQuery(), dirReader.numDocs());
		List<Song> resultList = getSearchResults(is, hits);

		// resetting all query fields for next query
		resetQueries();
		dirReader.close();
		return resultList;
	}

	/**
	 * @param is
	 * @param hits
	 * @return search result as list of songs
	 * 
	 */
	private List<Song> getSearchResults(IndexSearcher is, TopDocs hits) {
		List<Song> resultList = new ArrayList<Song>();
		for (int i = 0; i < hits.scoreDocs.length; i++) {
			ScoreDoc scoreDoc = hits.scoreDocs[i];
			Document doc = null;
			try {
				doc = is.doc(scoreDoc.doc);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Song currentSong = corpusService.getSongByID(doc.get("uuid"));
			if (currentSong != null)
				resultList.add(currentSong);
		}
		return resultList;
	}

	/**
	 * helper method to reset query for next search
	 */
	private void resetQueries() {
		setCompilation(false);
		setField("");
		setFuzzy(false);
		setSearchPhrase("");
		years[0] = 0;
		years[1] = 3000;
	}

}
