package de.uni_koeln.dh.lyra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni_koeln.dh.lyra.crawling.Crawler;
import de.uni_koeln.dh.lyra.crawling.IO;
import de.uni_koeln.dh.lyra.data.Location;
import de.uni_koeln.dh.lyra.data.albums.Album;
import de.uni_koeln.dh.lyra.data.songs.Song;
import de.uni_koeln.dh.lyra.processing.GeoTagger;
import de.uni_koeln.dh.lyra.processing.NER;

public class CrawlingApp {

	private static String songXML = "songs.xml";
	private static String songLinks = "src/main/resources/song_links.txt";
	private static List<Song> songs = null;
	private static Map<String, Album> albumsMap = null;

	public static void main(String[] args) throws IOException {
		Crawler crawler = new Crawler();
		IO io = new IO();

		try {
			songs = crawler.getSongsFromURLs(songLinks);
			albumsMap = crawler.getAlbumsMap();

			// System.out.println(albumsMap.size() + " Albums");
			io.exportSongsToXLSX(songs);
			io.exportAlbumsToXLSX(new ArrayList<Album>(albumsMap.values()));
			io.exportAlbumsToXML(new ArrayList<Album>(albumsMap.values()));
			io.exportSongsToXML(songs);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Analyzer.analyseCorpusStatistics(songs, albumsMap);

//		NER ner = new NER();
//		songs = ner.detectNamedEntities(songs);
//		Set<String> places = ner.getPlaces();
//		GeoTagger geoTagger = new GeoTagger();
//		try {
//			System.out.println("Tag Locations");
//			Set<Location> tagged = geoTagger.getGeoDatesFromList(new ArrayList<String>(places));
//			crawler.exportPlaces(tagged);
//
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// TODO serialize placesSet

		// FeatureEngineering fe = new FeatureEngineering();
		// songs = fe.createFeatures(songs);

	}

}
