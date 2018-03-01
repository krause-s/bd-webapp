package de.uni_koeln.dh.lyra;

import java.io.IOException;
import java.util.List;
import de.uni_koeln.dh.lyra.crawling.Crawler;
import de.uni_koeln.dh.lyra.crawling.IO;
import de.uni_koeln.dh.lyra.data.Song;



public class CrawlingApp {

//	private static String songXML = "src/main/resouces/songs.xml";
	private static String songLinks = "src/main/resources/song_links.txt";
	private static List<Song> songs = null;
//	private static List<Album> albums = null;


	public static void main(String[] args) throws IOException {
		Crawler crawler = new Crawler();
		IO io = new IO();

		try {
			songs = crawler.getSongsFromURLs(songLinks);


			// System.out.println(albumsMap.size() + " Albums");
			io.exportSongsToXLSX(songs);

		} catch (IOException e) {
			e.printStackTrace();
		}



	}

}
