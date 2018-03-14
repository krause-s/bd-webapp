package de.uni_koeln.dh.lyra.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.data.Song;
import de.uni_koeln.dh.lyra.model.place.Place;
import de.uni_koeln.dh.lyra.model.place.PopUp;
import de.uni_koeln.dh.lyra.processing.PlaceEvaluator;
import de.uni_koeln.dh.lyra.util.IO;

/**
 * @author Peter
 *
 *         This service generates, serializes, updates and reads the given
 *         corpus
 */

@Service
public class CorpusService {

	public static String dataPath;
	private static Map<String, Artist> artists = new HashMap<String, Artist>();

	private List<Place> placesToEvaluate;

	/**
	 * @author Peter
	 *
	 *         if an corpus allready exists, it is deserialized
	 */
	@PostConstruct
	public void initExistingData() {
		readExistingCorpus();
	}

	/**
	 * @param file
	 * @return List<Place>
	 * 
	 *         this method prepares the places evaluation. The artist map and
	 *         the detected places is filled temporarily.
	 * 
	 */
	public List<Place> prepareEvaluation(File file) {
		IO io = new IO();
		try {
			artists.putAll(io.getDataFromXLSX(file));
			placesToEvaluate = io.getPlacesToEvaluate();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return placesToEvaluate;
	}

	/**
	 * @param deletionMap
	 *            overrides artist map with evaluation result
	 */
	public void completeCorpus(Map<Place, Set<String>> deletionMap) {
		artists.putAll(PlaceEvaluator.evaluatePlaces(placesToEvaluate, deletionMap, artists));
		serializeCorpus();
	}

	/**
	 * @return
	 * 
	 * 		returns the map values as a list
	 */
	public List<Artist> getArtistList() {
		List<Artist> artistsList = new ArrayList<Artist>();
		for (String artistKey : artists.keySet()) {
			artistsList.add(artists.get(artistKey));
		}
		return artistsList;
	}

	/**
	 * @return returns all songs
	 */
	public List<Song> getAllSongs() {
		List<Song> allSongs = new ArrayList<>();
		for (Artist artist : getArtistList()) {
			for (Song song : artist.getSongs()) {
				allSongs.add(song);
			}
		}
		return allSongs;
	}

	/**
	 * @param uuid
	 * @return returns one specific song with the given id. Especially for
	 *         search and browse results
	 */
	public Song getSongByID(String uuid) {
		for (Song song : getAllSongs()) {
			if (song.getUuid().equals(uuid)) {
				return song;
			}
		}

		return null;
	}

	/**
	 * @param yearsFrom
	 * @param yearsTo
	 * @return
	 * 
	 * 		returns an submap of the class map artists concerning the given
	 *         range fo years
	 * 
	 */
	public List<Artist> getArtistSongsByYears(int yearsFrom, int yearsTo) {
		List<Artist> filteredArtists = new ArrayList<>();
		List<Artist> currArtists = new ArrayList<>();
		currArtists.addAll(getArtistList());

		for (Artist artist : currArtists) {
			Artist currArtist = new Artist(artist.getName());
			currArtist.setColor(artist.getColor());
			currArtist.setBioPlaces(artist.getBioPlaces());
			currArtist.setSongs(artist.getSongs());
			for (Place place : artist.getLyricsPlaces()) {
				Place currPlace = new Place(place.getLongitude(), place.getLatitude());
				currPlace.setMeta(place.getMeta());
				List<PopUp> popUps = new ArrayList<>();
				popUps.addAll(place.getPopUps());
				currPlace.setPopUps(popUps);
				currPlace.removePopUpByYears(yearsFrom, yearsTo);
				if (!currPlace.getPopUps().isEmpty())
					currArtist.addLyricsPlace(currPlace);
			}

			filteredArtists.add(currArtist);
		}
		return filteredArtists;
	}

	/**
	 * serializes the corpus
	 */
	public void serializeCorpus() {
		System.out.println("serializing corpus");
		new File("data/corpus").mkdirs();
		try {
			File d = new File("data/corpus");
			for (int i = 0; i < d.listFiles().length; i++) {
				d.listFiles()[i].delete();
			}
			
			FileOutputStream fileOut = new FileOutputStream("data//corpus/corpus.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(artists);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * deserializes the corpus. Useful for restarting the app.
	 */
	public void readExistingCorpus() {
		if (corpusExists()) {
			try {
				FileInputStream fileIn = new FileInputStream("data/corpus/corpus.ser");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				artists = (Map<String, Artist>) in.readObject();
				in.close();
				fileIn.close();
			} catch (IOException i) {
				i.printStackTrace();
				return;
			} catch (ClassNotFoundException c) {
				c.printStackTrace();
				return;
			}
		}
	}

	/**
	 * @return checks if there is already a corpus. Useful for restarting the
	 *         app.
	 */
	public boolean corpusExists() {
		return new File("data/corpus/corpus.ser").exists();
	}

	/**
	 * chooses three random songs, takes the first three lines of the lyrics and
	 * sets them as lyrics
	 * 
	 * @return
	 */
	public List<Song> getRandomQuotes() {
		Random rand = new Random();
		List<Song> allSongs = getAllSongs();
		List<Song> quotes = new ArrayList<Song>();
		while (quotes.size() < 3) {
			Song original = allSongs.get(rand.nextInt(allSongs.size() - 1));
			allSongs.get(rand.nextInt(allSongs.size()));
			String tempLyrics = "";
			String[] lines = original.getLyrics().split("\n");
			int limit = 2;
			// if lyrics are shorter than 3 lines
			if (lines.length <= limit) {
				tempLyrics = original.getLyrics() + "...";
			} else {
				for (int i = 0; i <= limit; i++) {
					String line = original.getLyrics().split("\n")[i];
					if (line.equals("")) { // don't count empty lines
						limit++;
						continue;
					}
					tempLyrics += original.getLyrics().split("\n")[i];
					if (i != limit)
						tempLyrics += "\n"; // don't add line break to last line
				}
				tempLyrics += "...";
			}

			Song currentSong = new Song(tempLyrics);
			currentSong.setUuid(original.getUuid());
			currentSong.setTitle(original.getTitle());
			currentSong.setYear(original.getYear());
			currentSong.setArtist(original.getArtist());
			quotes.add(currentSong);
		}
		return quotes;
	}

	/**
	 * @return
	 * 
	 * 		returns the oldest and the newest year of all songs in the corpus
	 * 
	 */
	public int[] getMinAndMaxYears() {
		int min = 0, max = 0;
		for (Song song : getAllSongs()) {
			int cur = song.getYear();

			if ((min == 0) && (max == 0)) {
				min = cur;
				max = cur;
			} else {
				if (cur > max) {
					max = cur;
				} else if (cur < min) {
					min = cur;
				}
			}
		}
		return new int[] { min, max };
	}

}
