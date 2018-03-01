package de.uni_koeln.dh.lyra.processing;

import java.util.ArrayList;
import java.util.List;

import de.uni_koeln.dh.lyra.data.Entry;
import de.uni_koeln.dh.lyra.data.albums.Album;
import de.uni_koeln.dh.lyra.data.songs.Song;

public class AlbumSongConnector {
	
	public static Album connectSongsToAlbum (Album album, List<Song> songs) {
		
		System.out.println(album.getTracklist().size());
		
		String title = album.getTitle();
		List<Song> songList	= new ArrayList<Song>();
		
		for (Song song : songs) {
			List<Entry> entries = song.getAlbums();
//			System.out.println(entries.size());
			for (Entry entry : entries) {
				if(entry.getValue().equals(title))
					songList.add(song);
			}
		}
		album.setSongElements(songList);
//		System.out.println(album.getSongElements() + " Songs");
		return album;
	}

}
