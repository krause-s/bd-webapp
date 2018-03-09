package de.uni_koeln.dh.lyra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.data.Song;
import de.uni_koeln.dh.lyra.model.place.Place;
import de.uni_koeln.dh.lyra.processing.LyricsAnalyzer;
import de.uni_koeln.dh.lyra.processing.PlaceEvaluator;
import de.uni_koeln.dh.lyra.util.IO;

public class PreprocessingApp {

	private static String dataPath = "src/main/resources/data/lyrics_database.xlsx";
	private static Map<String, Artist> artists = new HashMap<String, Artist>();

	public static void main(String[] args) {

		IO io = new IO();
		try {
			artists = io.getDataFromXLSX(dataPath);
			List<Place> placesToEvaluate = io.getPlacesToEvaluate();
			List<Place> evaluatedPlaces = PlaceEvaluator.evaluatePlaces(placesToEvaluate, new HashMap<Place, Set<String>>());
			artists = PlaceEvaluator.sortPopUpsToArtists(evaluatedPlaces, artists);

			analyse();

			// TESTAUSGABE
			// for(Map.Entry<String, Artist> e : artists.entrySet()) {
			// System.out.println(e.getKey());
			// List<Place> lyricsPlaces = e.getValue().getLyricsPlaces();
			// List<Place> bioPlaces = e.getValue().getBioPlaces();
			// if(!lyricsPlaces.isEmpty()) {
			// Map<Double, Double> latLon = new HashMap<Double, Double>();
			// for (Place place : lyricsPlaces) {
			// if(latLon.containsKey(place.getLatitude())) {
			// if(latLon.get(place.getLatitude()).equals(place.getLongitude())) {
			// System.out.println("!!!");
			// }
			// }
			// latLon.put(place.getLatitude(), place.getLongitude());
			// }
			//
			// System.out.println(lyricsPlaces.size() + " annotated Places in Lyrics");
			// }
			//
			// if(!bioPlaces.isEmpty()) {
			// System.out.println(bioPlaces.size() + " BioPlaces");
			// for(Place place : bioPlaces) {
			// System.out.println(place.getLatitude() + " - " + place.getLongitude());
			// for (PopUp popUp : place.getPopUps()) {
			// System.out.println(popUp.getContent() + " (" + popUp.getPlaceName() + ")");
			// }
			// System.out.println("");
			// }
			// }
			//// System.out.println(bioPlaces.size() + " annotated Places in Bio");
			// System.out.println("------------------------------");
			// }
			// ENDE

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public static void analyse() {
		int numOfTokens = 10; // take the 10 most relevant tokens
		List<Song> allSongs = new ArrayList<Song>();
		for (Map.Entry<String, Artist> e : artists.entrySet()) {
			allSongs.addAll(e.getValue().getSongs());
		}
		// System.out.println(allSongs.size());
		LyricsAnalyzer analyzer = new LyricsAnalyzer(allSongs);
		allSongs = analyzer.getWeights();
		
		
		Map<Integer, Double> tokenAv = analyzer.getAverageTokenCount();
		
		for(Map.Entry<Integer, Double> e : tokenAv.entrySet()) {
			System.out.println(e.getKey() + ": " + e.getValue() + " tokens");
		}
		
		

		int from = 1976;
		int to = 1986;
		boolean useCompilations = false;
		List<String> artists = new ArrayList<String>();
		artists.add("Bob Dylan");

		Map<String, Integer> result = analyzer.getMostRelevantTokens(from, to, useCompilations, numOfTokens, artists);

		System.out.println(from + " - " + to);
		for (Map.Entry<String, Integer> t : result.entrySet()) {
			System.out.println(t.getKey() + ": " + t.getValue());
		}
		System.out.println("----------------------------");
		
		
		from = 1995;
		to = 2005;

		result = analyzer.getMostRelevantTokens(from, to, useCompilations, numOfTokens, artists);

		System.out.println(from + " - " + to);
		for (Map.Entry<String, Integer> t : result.entrySet()) {
			System.out.println(t.getKey() + ": " + t.getValue());
		}
		System.out.println("----------------------------");

		

	}

}
