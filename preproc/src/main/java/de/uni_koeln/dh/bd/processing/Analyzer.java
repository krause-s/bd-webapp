
package de.uni_koeln.dh.bd.processing;

import java.util.List;

import de.uni_koeln.dh.bd.data.Song;



public class Analyzer {
	
public static void analyseCorpusStatistics(List<Song> songs){
		
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
		
		System.out.println("Bei insgesamt " + songs.size() + " fehlen:\n"
				+ missingLyrics + " Lyrics\n "
				+ missingTitles + " Titel\n"
						+ missingCredits + " Credits\n "
								+ missingCopyrights + " Copyrights\n ");

	}

}
