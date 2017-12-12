
package de.uni_koeln.dh.bd.processing;

import java.util.List;
import java.util.Map;

import de.uni_koeln.dh.bd.data.Album;
import de.uni_koeln.dh.bd.data.Song;



public class Analyzer {
	
public static void analyseCorpusStatistics(List<Song> songs, Map<String, Album> albumsMap){
		
		int missingLyrics = 0;
		int missingCredits = 0;
		int missingCopyrights = 0;
		int missingTitles = 0;
		
		for (Song song : songs) {
			
			if(song.getLyrics().equals(""))
					missingLyrics++;
			if(song.getCredits().equals(""))
				missingCredits++;
			if(song.getCopyright().equals(""))
				missingCopyrights++;
			if(song.getTitle().equals(""))
				missingTitles++;
			
		}
		System.out.println("----------------------------------------------");
		System.out.println("Bei insgesamt " + songs.size() + " fehlen:\n"
				+ missingLyrics + " Lyrics\n"
				+ missingTitles + " Titel\n"
						+ missingCredits + " Credits\n"
								+ missingCopyrights + " Copyrights\n");

		
		int missingYear = 0;
		int missingTitle = 0;
		int missingLink = 0;
		
		for (Album album : albumsMap.values()) {
			
			if(album.getYear().equals(""))
				missingYear++;
			if(album.getTitle().equals(""))
				missingTitle++;
			if(album.getLink().equals(""))
				missingLink++;
		}
		
		System.out.println("----------------------------------------------");
		System.out.println("Bei insgesamt " + albumsMap.size() + " fehlen:\n"
				+ missingYear + " Jahresangaben\n"
				+ missingTitle + " Titel\n"
						+ missingLink + " Links\n");
	}

}
