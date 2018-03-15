package de.uni_koeln.dh.lyra.crawling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.uni_koeln.dh.lyra.data.Album;
import de.uni_koeln.dh.lyra.data.Song;


import de.uni_koeln.dh.lyra.util.Strings;

/**
 * Web Crawler to generate song objects from web contents
 * @author Johanna
 *
 */
public class Crawler {

	private int songCounter = 1;

	/**
	 * takes every url in the file and creates a song object from
	 * information on the url web page
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public List<Song> getSongsFromURLs(String path) throws IOException {

		File file = new File(path);
		System.out.println("Crawl through links from file: " + file.getAbsolutePath());

		Reader reader = new FileReader(file);
		BufferedReader br = new BufferedReader(reader);

		List<Song> songs = new ArrayList<Song>();
		List<String> tryAgain = new ArrayList<String>();

		String line = "";
		while ((line = br.readLine()) != null) {

			try {
				List<Song> currentSongs = new ArrayList<Song>();
				if (line.contains("http")) {
					System.out.println(line);
					String id = "s" + songCounter++;
					currentSongs = createSongObjects(line, id);
					songs.addAll(currentSongs);
				}
			} catch (IOException e) {
				tryAgain.add(line);
			}

		}
		br.close();

		for (String url : tryAgain) {
			List<Song> currentSongs = new ArrayList<Song>();
			if (url.contains("http")) {
				System.out.println(url);
				String id = "s" + songCounter++;
				currentSongs = createSongObjects(url, id);
				songs.addAll(currentSongs);
			}
		}
		System.out.println("Crawled " + songs.size() + " Songs");
		return songs;
	}

	private List<Song> createSongObjects(String url, String songID) throws IOException {

		Document doc = null;

		doc = Jsoup.connect(url)
				.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0")
				.referrer("http://www.google.com").ignoreHttpErrors(true).get();

		Element article = doc
				.selectFirst("body > div#variable > div.page-content > div.main-wrapper.page-wrapper > div.content"
						+ " > div.interior-content > article");

//		Elements playInfos = doc
//				.select("body > div#variable > div.page-content > div.main-wrapper.page-wrapper > div.content"
//						+ " > div.interior-content > aside.blocks > div.played-stats > div.played");

		Elements albums = doc
				.select("body > div#variable > div.page-content > div.main-wrapper.page-wrapper > div.content"
						+ " > div.interior-content > aside.blocks > div.item.album.secondary");

		Element newAlbum = doc
				.selectFirst("body > div#variable > div.page-content > div.main-wrapper.page-wrapper > div.content"
						+ " > div.interior-content > aside.blocks > div.item.album");

		albums.add(newAlbum); // contains all albums presented in html

		Element h2 = article.selectFirst("h2");
		Element lyrics = article.selectFirst("div.article-content.lyrics");
		

		String t = ""; // title
		String l = ""; // lyrics

		if (h2 != null)
			t = h2.html();

		if (lyrics != null)
			l = lyrics.html();

		l = l.replaceAll("<br> ", "");
		l = l.split("<p class=\"copytext")[0];

		if (l.contains("VIEW ALL")) // div contains only information and no lyrics
			l = "";

		boolean comp = false;
		String comment = "";

		List<Song> toReturn = new ArrayList<Song>();
		//for each record of a song, a new song object will be initialized
		for (Element element : albums) {

			Album album = createAlbum(element);
			if (album == null)
				continue;

			toReturn.add(new Song(t, l, "Bob Dylan", album.getTitle(), 
					album.getYear(), comp, comment));

		}

		return toReturn;
	}

	private Album createAlbum(Element albumElement) {

		String albumTitle = "";
		int albumYear = 0;

//		Element link = albumElement.selectFirst("div.caption > div.actions.double > a.buy");
		Element infos = albumElement.selectFirst("div.caption > div.information > small");

		try {
			albumTitle = Strings.getTitle(infos.html());
		} catch (NullPointerException e) { // might be that there is no album attached to the song
			albumTitle = "none";
			return null;
		}

		albumYear = Strings.getYear(infos.html());

		// verarbeitet sowohl das gesamte Album (currentAlbum) als auch den
		// Album-Eintrag
		// des aktuellen Songs
		Album currentAlbum = new Album(albumTitle, albumYear);



		return currentAlbum;

	}

}
