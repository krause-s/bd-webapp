package de.uni_koeln.dh.bd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uni_koeln.dh.bd.data.Album;
import de.uni_koeln.dh.bd.data.Song;
import de.uni_koeln.dh.bd.processing.Analyzer;
import de.uni_koeln.dh.bd.processing.IO;

public class Preprocessing {
	
	public static void main(String[] args) {
		
		IO io = new IO();
		try {
			List<Song> songs = io.getSongsFromURLs("src/main/resources/song_links.txt");
			Map<String, Album> albumsMap = io.getAlbumsMap();
			
			System.out.println(songs.size() + " Songs");
			System.out.println(albumsMap.size() + " Albums");
			io.exportSongsToXML(songs);
			io.exportAlbumsToXML(new ArrayList<Album>(albumsMap.values()));			
			
			Analyzer.analyseCorpusStatistics(songs, albumsMap);
		} catch (IOException e) {

			e.printStackTrace();
		}
	
	}
	
}
