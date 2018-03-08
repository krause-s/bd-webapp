package de.uni_koeln.dh.lyra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.data.Song;
import de.uni_koeln.dh.lyra.model.place.Place;
import de.uni_koeln.dh.lyra.model.place.PopUp;
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
			List<Place> evaluatedPlaces = PlaceEvaluator.evaluatePlaces(placesToEvaluate);
			artists = PlaceEvaluator.sortPopUpsToArtists(evaluatedPlaces, artists);
			
			analyse();
			
			//TESTAUSGABE
//			for(Map.Entry<String, Artist> e : artists.entrySet()) {
//				System.out.println(e.getKey());
//				List<Place> lyricsPlaces = e.getValue().getLyricsPlaces();
//				List<Place> bioPlaces = e.getValue().getBioPlaces();
//				if(!lyricsPlaces.isEmpty()) {
//					Map<Double, Double> latLon = new HashMap<Double, Double>();
//					for (Place place : lyricsPlaces) {
//						if(latLon.containsKey(place.getLatitude())) {
//							if(latLon.get(place.getLatitude()).equals(place.getLongitude())) {
//								System.out.println("!!!");
//							}
//						}
//						latLon.put(place.getLatitude(), place.getLongitude());
//					}
//					
//					System.out.println(lyricsPlaces.size() + " annotated Places in Lyrics");
//				}
//					
//				if(!bioPlaces.isEmpty()) {
//					System.out.println(bioPlaces.size() + " BioPlaces");
//					for(Place place : bioPlaces) {
//						System.out.println(place.getLatitude() + " - " + place.getLongitude());
//						for (PopUp popUp : place.getPopUps()) {
//							System.out.println(popUp.getContent() + " (" + popUp.getPlaceName() + ")");
//						}
//						System.out.println("");
//					}
//				}
////					System.out.println(bioPlaces.size() + " annotated Places in Bio");
//					System.out.println("------------------------------");
//			}
			//ENDE

		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void analyse() {
		int numOfSongs = 10; //take the 10 most relevant tokens
		List<Song> allSongs = new ArrayList<Song>();
		for(Map.Entry<String, Artist> e : artists.entrySet()) {
			allSongs.addAll(e.getValue().getSongs());
		}
//		System.out.println(allSongs.size());
		LyricsAnalyzer analyzer = new LyricsAnalyzer();
		allSongs = analyzer.getWeights(allSongs);
		Map<Integer[], Map<String, Integer>> result = analyzer.getMostRelevantTokens(1965, 1975,
				1990, 2000, true, numOfSongs);
		
		for(Map.Entry<Integer[], Map<String, Integer>> e : result.entrySet()) {
			System.out.println(e.getKey()[0] + " - " + e.getKey()[1]);
			for (Map.Entry<String, Integer> t : e.getValue().entrySet()) {
				System.out.println(t.getKey() + ": " + t.getValue());
			}
			System.out.println("----------------------------");
		}
		
		result = analyzer.getMostRelevantTokens(1965, 1975,
				1990, 2000, false, numOfSongs);
		
		for(Map.Entry<Integer[], Map<String, Integer>> e : result.entrySet()) {
			System.out.println(e.getKey()[0] + " - " + e.getKey()[1]);
			for (Map.Entry<String, Integer> t : e.getValue().entrySet()) {
				System.out.println(t.getKey() + ": " + t.getValue());
			}
			System.out.println("----------------------------");
		}
		
	}

}
