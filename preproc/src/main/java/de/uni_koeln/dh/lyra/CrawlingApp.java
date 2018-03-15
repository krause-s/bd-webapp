package de.uni_koeln.dh.lyra;

import java.io.IOException;
import java.util.List;
import de.uni_koeln.dh.lyra.crawling.Crawler;
import de.uni_koeln.dh.lyra.crawling.IO;
import de.uni_koeln.dh.lyra.data.Song;


/**
 * Application to crawl lyrics and meta data from a specific
 * web page
 * @author Johanna
 *
 */
public class CrawlingApp {


	private static String songLinks = "src/main/resources/song_links.txt";
	private static List<Song> songs = null;

	/**
	 * crawls songs from web page and exports them to .xlsx-File
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Crawler crawler = new Crawler();
		IO io = new IO();

		try {
			songs = crawler.getSongsFromURLs(songLinks);
			io.exportSongsToXLSX(songs);

		} catch (IOException e) {
			e.printStackTrace();
		}



	}

}
