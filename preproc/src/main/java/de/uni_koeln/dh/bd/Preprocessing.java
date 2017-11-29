package de.uni_koeln.dh.bd;

import java.io.IOException;
import java.util.List;

import de.uni_koeln.dh.bd.data.Song;
import de.uni_koeln.dh.bd.processing.Analyzer;
import de.uni_koeln.dh.bd.processing.IO;

public class Preprocessing {
	
	public static void main(String[] args) {
		
		IO io = new IO();
		try {
			List<Song> songs = io.getSongsFromURLs("src/main/resources/song_links.txt");
			io.exportSongsToXML(songs);
			

			Analyzer.analyseCorpusStatistics(songs);
		} catch (IOException e) {

			e.printStackTrace();
		}
	
	}
	
}
