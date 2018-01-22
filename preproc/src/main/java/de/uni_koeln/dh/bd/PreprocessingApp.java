package de.uni_koeln.dh.bd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uni_koeln.dh.bd.data.albums.Album;
import de.uni_koeln.dh.bd.data.songs.Song;
import de.uni_koeln.dh.bd.processing.AlbumSongConnector;
import de.uni_koeln.dh.bd.processing.Analyzer;
import de.uni_koeln.dh.bd.processing.IO;

public class PreprocessingApp {

	private static String songPath = "songs.xlsx";
	private static String albumPath = "albums.xlsx";
	private static List<Song> songs = new ArrayList<Song>();
	private static List<Album> albums = new ArrayList<Album>();

	private static Map<Album, List<Entry<String, Integer>>> tokenFreqByAlbum = new HashMap<Album, List<Entry<String, Integer>>>();

	public static void main(String[] args) {

		IO io = new IO();
		try {
			songs = io.getSongsFromXLSX(songPath);
			albums = io.getAlbumsFromXLSX(albumPath);

			for (Song song : songs) {
				// System.out.println(song.getTitle());
				//// System.out.println(song.getLyrics());
				// System.out.println(song.getTokens());
			}
			// for (Album album : albums) {
			// System.out.println(album.getTitle() + " - " + album.getYear());
			// }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO tokenFreq pro Album
		for (Album album : albums) {
			album = AlbumSongConnector.connectSongsToAlbum(album, songs);
			List<Entry<String, Integer>> tokenFreq = Analyzer.countAlbumTokenFreq(album);
			tokenFreqByAlbum.put(album, tokenFreq);
		}

		for (Entry<Album, List<Entry<String, Integer>>> e : tokenFreqByAlbum.entrySet()) {
			System.out.println(e.getKey().getTitle() + " - " + e.getKey().getYear());
			List<Entry<String, Integer>> freqs = e.getValue();
			int iterator = 0;
			for (Entry<String, Integer> f : freqs) {
				System.out.println(f.getKey() + " - " + f.getValue());
				if(iterator++ > 10)
					break;
			}
		}

	}

}
