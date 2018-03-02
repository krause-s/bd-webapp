package de.uni_koeln.dh.lyra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.data.Song;
import de.uni_koeln.dh.lyra.model.place.Place;
import de.uni_koeln.dh.lyra.model.place.PopUp;
import de.uni_koeln.dh.lyra.processing.SongPreprocessor;
import de.uni_koeln.dh.lyra.util.IO;

public class PreprocessingApp {
	

	
	private static String dataPath = "src/main/resources/data/lyrics_database.xlsx";
//	private static List<Song> songs = new ArrayList<Song>();
	private static List<Artist> artists = new ArrayList<Artist>();
	
	public static void main(String[] args) {
		
//		Double a = 0.5;
//		Double b = 0.6;
//		
//		Place p1 = new Place(a, b);
//		Place p2 = new Place(a, b);
//		Place p3 = new Place(b, a);
//		
//		System.out.println(p1.equals(p2));
//		System.out.println(p2.equals(p3));
//		System.out.println(p3.equals(p1));
//		
//		List<Place> places = new ArrayList<Place>();
//		places.add(p1);
//		places.add(p3);
//		Place p = places.get(places.indexOf(p2));
//		p.addPopUp(new PopUp("dgjflkj"));
//		
//		for(Place place : places) {
//			System.out.println(place.getPopUps().size());
//		}
//		System.exit(0);
		
		
		IO io = new IO();
		try {
			artists = io.getDataFromXLSX(dataPath);
//			SongPreprocessor prep = new SongPreprocessor();
//			Map<Place, List<Song>>	foundPlaces = prep.detectNamedEntities(songs);
			
			//TODO evaluate found places
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
