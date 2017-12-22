package de.uni_koeln.dh.bd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni_koeln.dh.bd.data.Entry;
import de.uni_koeln.dh.bd.data.Location;
import de.uni_koeln.dh.bd.data.albums.Album;
import de.uni_koeln.dh.bd.data.songs.Song;
import de.uni_koeln.dh.bd.processing.Analyzer;
import de.uni_koeln.dh.bd.processing.GeoTagger;
import de.uni_koeln.dh.bd.processing.IO;
import de.uni_koeln.dh.bd.processing.NER;
import de.uni_koeln.dh.bd.processing.featureEngineering.FeatureEngineering;

public class Preprocessing {

	private static String songXML = "songs.xml";
	private static String songLinks = "src/main/resources/song_links.txt";
	private static List<Song> songs = null;
	private static Map<String, Album> albumsMap = null;

	public static void main(String[] args) throws IOException {
		IO io = new IO();
		if (new File(songXML).exists())
			songs = io.getSongsFromXML(songXML);
		else {
			try {
				songs = io.getSongsFromURLs(songLinks);
				albumsMap = io.getAlbumsMap();
				
				// System.out.println(albumsMap.size() + " Albums");
				io.exportSongsToXML(songs);
				io.exportAlbumsToXML(new ArrayList<Album>(albumsMap.values()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Analyzer.analyseCorpusStatistics(songs, albumsMap);

		NER ner = new NER();
		songs = ner.detectNamedEntities(songs);
		Set<String> places = ner.getPlaces();
		GeoTagger geoTagger = new GeoTagger();
		try {
			System.out.println("Tag Locations");
			Set<Location> tagged = geoTagger.getGeoDatesFromList(new ArrayList<String>(places));
			io.exportPlaces(tagged);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO serialize placesSet

		// FeatureEngineering fe = new FeatureEngineering();
		// songs = fe.createFeatures(songs);

	}

}
