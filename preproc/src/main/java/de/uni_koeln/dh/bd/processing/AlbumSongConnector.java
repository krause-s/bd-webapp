package de.uni_koeln.dh.bd.processing;

import java.util.ArrayList;
import java.util.List;

import de.uni_koeln.dh.bd.data.Entry;
import de.uni_koeln.dh.bd.data.albums.Album;
import de.uni_koeln.dh.bd.data.songs.Song;

public class AlbumSongConnector {
	
	public static Album connectSongsToAlbum (Album album, List<Song> songs) {
		
		String title = album.getTitle();
		List<Song> songList	= new ArrayList<Song>();
		
		for (Song song : songs) {
			List<Entry> entries = song.getAlbums();
			System.out.println(entries.size());
			for (Entry entry : entries) {
				System.out.println(title);
				System.out.println(entry.getValue());
				if(entry.getValue().equals(title))
					songList.add(song);
			}
		}
		album.setSongElements(songList);
//		System.out.println(album.getSongElements() + " Songs");
		return album;
	}

}
