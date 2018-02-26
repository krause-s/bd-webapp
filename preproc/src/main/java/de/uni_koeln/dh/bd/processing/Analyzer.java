
package de.uni_koeln.dh.bd.processing;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uni_koeln.dh.bd.data.albums.Album;
import de.uni_koeln.dh.bd.data.songs.Song;

@Deprecated
public class Analyzer {
	
	public static Map<String, Integer> sumTokenFreqs(Map<Album, Map<String, Integer>> tokensOfAlbums){
		//TODO relativieren anhand der anzahl der alben
		Map<String, Integer> tokenFreq = new HashMap<String, Integer>();
		for(Map<String, Integer> entry : tokensOfAlbums.values()) {
			for(Map.Entry<String, Integer> e : entry.entrySet()) {
				Integer freq = 0;
				if(tokenFreq.containsKey(e.getKey()))
					freq = tokenFreq.get(e.getKey());
				tokenFreq.put(e.getKey(), (freq + e.getValue()));
			}
		}
		
		
		return tokenFreq;
	}

	public static List<Entry<String, Integer>> countAlbumTokenFreq(Album album) {

		Map<String, Integer> tokenFreq = new HashMap<String, Integer>();
		
		List<Song> songs = album.getSongElements();
//		System.out.println(songs.size() + " connected Songs");
		for (Song song : songs) {
			List<String> tokens = song.getTokens();
			for (String token : tokens) {
				Integer freq = 0;
				if(tokenFreq.containsKey(token))
					freq = tokenFreq.get(token);
				tokenFreq.put(token, freq + 1);
				
			}
		}

		return sortByComparator(tokenFreq);
	}

	public static List<Entry<String, Integer>> sortByComparator(Map<String, Integer> unsorted) {
		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsorted.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		return list;
	}

	public static void analyseCorpusStatistics(List<Song> songs, Map<String, Album> albumsMap) {

		int missingLyrics = 0;
		int missingCredits = 0;
		int missingCopyrights = 0;
		int missingTitles = 0;

		for (Song song : songs) {

			if (song.getLyrics().equals(""))
				missingLyrics++;
			if (song.getCredits().equals(""))
				missingCredits++;
			if (song.getCopyright().equals(""))
				missingCopyrights++;
			if (song.getTitle().equals(""))
				missingTitles++;

		}
		System.out.println("----------------------------------------------");
		System.out.println("Bei insgesamt " + songs.size() + " fehlen:\n" + missingLyrics + " Lyrics\n" + missingTitles
				+ " Titel\n" + missingCredits + " Credits\n" + missingCopyrights + " Copyrights\n");

		int missingYear = 0;
		int missingTitle = 0;
		int missingLink = 0;

		for (Album album : albumsMap.values()) {

			if (album.getYear() == 0)
				missingYear++;
			if (album.getTitle().equals(""))
				missingTitle++;
			if (album.getURL() == null)
				missingLink++;
		}

		System.out.println("----------------------------------------------");
		System.out.println("Bei insgesamt " + albumsMap.size() + " fehlen:\n" + missingYear + " Jahresangaben\n"
				+ missingTitle + " Titel\n" + missingLink + " Links\n");
	}

}
