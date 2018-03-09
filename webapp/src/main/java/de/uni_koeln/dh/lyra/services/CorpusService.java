package de.uni_koeln.dh.lyra.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.data.Song;
import de.uni_koeln.dh.lyra.model.place.Place;
import de.uni_koeln.dh.lyra.util.IO;

@Service
public class CorpusService {

	public static String dataPath = "src/main/resources/data/lyrics_database.xlsx";
	private static Map<String, Artist> artists = new HashMap<String, Artist>();

//	@PostConstruct
	public List<Place> init(String dataPath) {
		IO io = new IO();
		
		try {
			artists = io.getDataFromXLSX(dataPath);
			return io.getPlacesToEvaluate();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	// TODO johanna
	public void init2(Map<Place, Set<String>> deletionMap) {
		System.out.println(deletionMap);
//		List<Place> evaluatedPlaces = PlaceEvaluator.evaluatePlaces(placesToEvaluate);
//		artists = PlaceEvaluator.sortPopUpsToArtists(evaluatedPlaces, artists);			
	}

	public List<Artist> getArtistList() {
		List<Artist> artistsList = new ArrayList<Artist>();
		for (String artistKey : artists.keySet()) {
			artistsList.add(artists.get(artistKey));
		}
		return artistsList;
	}

	public List<Song> getAllSongs() {
		List<Song> allSongs = new ArrayList<>();
		for (Artist artist : getArtistList()) {
			for (Song song : artist.getSongs()) {
				System.out.println("song.getUuid(): " + song.getUuid());
				allSongs.add(song);
			}
		}
		return allSongs;
	}

	public Song getSongByID(String uuid) {
		for (Song song : getAllSongs()) {
			if (song.getUuid().equals(uuid)) {
				System.out.println("found a song");
				return song;
			}
		}
		return null;
	}

}
