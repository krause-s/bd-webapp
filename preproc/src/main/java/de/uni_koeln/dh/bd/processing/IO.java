package de.uni_koeln.dh.bd.processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import de.uni_koeln.dh.bd.data.Album;
import de.uni_koeln.dh.bd.data.Song;
import de.uni_koeln.dh.bd.util.Strings;

public class IO {

	private Map<String, Album> albumsMap = new HashMap<String, Album>();

	private int songCounter = 1;
	private int albumCounter = 1;

	public List<Song> getSongsFromURLs(String path) throws IOException {

		File file = new File(path);
		System.out.println("Crawl through links from file: " + file.getAbsolutePath());

		Reader reader = new FileReader(file);
		BufferedReader br = new BufferedReader(reader);

		List<Song> songs = new ArrayList<Song>();

		String line = "";
		while ((line = br.readLine()) != null) {

			// if (!(line.startsWith("http") || line.startsWith("\n")))
			// System.out.println(line);

			Song song;
			if (line.contains("http")) {
				song = createSongObject(line);
				songs.add(song);
			}

		}
		br.close();
		System.out.println("Crawled " + songs.size() + " Songs");
		return songs;
	}

	public Map<String, Album> getAlbumsMap() {
		return albumsMap;
	}

//	public List<Album> getAlbumsFromURLs(String path) throws IOException {
//		File file = new File(path);
//		System.out.println("Crawl through links from file: " + file.getAbsolutePath());
//
//		Reader reader = new FileReader(file);
//		BufferedReader br = new BufferedReader(reader);
//
//		List<Album> albums = new ArrayList<Album>();
//		String line = "";
//		while ((line = br.readLine()) != null) {
//
//			Album album;
//			if (line.contains("http")) {
//				album = createAlbumObject(line);
//				albums.add(album);
//			}
//
//		}
//		br.close();
//		System.out.println("Crawled " + albums.size() + " Songs");
//		return albums;
//
//	}

//	private Album createAlbumObject(String url) throws IOException {
//
//		// crawl informations
//		Document doc = null;
//		doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0") // Mozilla/5.0
//																												// Firefox/57.0
//				.referrer("http://www.google.com").ignoreHttpErrors(true).maxBodySize(0).get();
//
//		Element interior_content = doc.select(
//				"body > div#variable > div.page-content > div.main-wrapper.page-wrapper > div.content.album-detail > div.interior-content")
//				.first();
//
//		Element album = interior_content.select("div.album > div.information > h2.headline").first();
//
//		Element trackslist = interior_content.select("div.tracks-list").first();
//		List<Node> children = interior_content.childNodes();
//		for (Node node : children) {
//			System.out.println(node);
//			System.out.println("------------");
//		}
//		System.out.println();
//		System.out.println(trackslist.html());
//
//		// System.out.println(titleAndYear);
//		// System.out.println("****");
//
//		// set values for new object
//		String id = "a" + albumCounter++;
//		String titleAndYear = album.html();
//		Map<String, String> tracks = new HashMap<String, String>();
//
//		return new Album(id, tracks, titleAndYear);
//	}

	public void exportSongsToXML(List<Song> songs) throws IOException {
		String path = "songs.xml";
		System.out.println("Export to: " + path);

		File xml = new File(path);

		FileWriter fw = new FileWriter(xml);
		fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");

		fw.write("<songs>\n");
		for (Song song : songs) {
			fw.write("\t<song id=\"" + song.getID() + "\" ref=\"" + song.getURL() + "\">\n");
			fw.write("\t\t<title>" + song.getTitle() + "</title>\n" + "\t\t<lyrics>" + song.getLyrics() + "</lyrics>\n"
					+ "\t\t<credits>" + song.getCredits() + "</credits>\n" + "\t\t<copyright>" + song.getCopyright()
					+ "</copyright>\n" + "\t\t<firstplayed ref=\"" + song.getFirstPlayed() + "\" />\n"
					+ "\t\t<lastplayed ref=\"" + song.getLastPlayed() + "\" />\n" + "\t\t<timesplayed>"
					+ song.getTimesPlayed() + "</timesplayed>\n");
			fw.write("\t\t<albums>\n");
			for (Album album : song.getAlbums()) {
				fw.write("\t\t\t<album id=\"" + album.getID() + "\">" + album.getTitle() + "</album>\n");
			}
			fw.write("\t\t</albums>");
			fw.write("\t</song>\n");
		}

		fw.write("</songs>");
		fw.flush();
		fw.close();
	}

	public void exportAlbumsToXML(List<Album> albums) throws IOException {
		String path = "albums.xml";
		System.out.println("Export to: " + path);

		File xml = new File(path);

		FileWriter fw = new FileWriter(xml);
		fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");

		fw.write("<albums>\n");
		for (Album album : albums) {
			fw.write("\t<album id=\"" + album.getID() + "\" ref=\"" + album.getLink() + "\">\n");
			fw.write("\t\t<title>" + album.getTitle() + "</title>\n" + "\t\t<year>" + album.getYear() + "</year>\n"
					+ "\t\t<tracklist>\n");
			for (Song song : album.getTracklist()) {
				fw.write("\t\t\t<track id=\"" + song.getID() + "\">" + song.getTitle() + "</track>\n");
			}
			fw.write("\t\t</tracklist>\n");
			fw.write("\t</album>\n");
		}

		fw.write("</albums>");
		fw.flush();
		fw.close();
	}

	private Song createSongObject(String url) throws IOException {

		Document doc = null;

		doc = Jsoup.connect(url)
				.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0")
				.referrer("http://www.google.com").ignoreHttpErrors(true).get();

		Element article = doc
				.selectFirst("body > div#variable > div.page-content > div.main-wrapper.page-wrapper > div.content"
						+ " > div.interior-content > article");

		Elements playInfos = doc
				.select("body > div#variable > div.page-content > div.main-wrapper.page-wrapper > div.content"
						+ " > div.interior-content > aside.blocks > div.played-stats > div.played");

		Elements albums = doc
				.select("body > div#variable > div.page-content > div.main-wrapper.page-wrapper > div.content"
						+ " > div.interior-content > aside.blocks > div.item.album.secondary");

		Element newAlbum = doc
				.selectFirst("body > div#variable > div.page-content > div.main-wrapper.page-wrapper > div.content"
						+ " > div.interior-content > aside.blocks > div.item.album");

		albums.add(newAlbum); // contains all albums presented in html

		Element h2 = article.selectFirst("h2");
		Element credit = article.selectFirst("div.credit");
		Element lyrics = article.selectFirst("div.article-content.lyrics");
		Element copyright = lyrics.selectFirst("p.copytext");
		Element timesPlayed = playInfos.select("span.db").first();

		String t = ""; // title
		String l = ""; // lyrics
		String copy = ""; // copyright
		String cr = ""; // credits
		String fp = ""; // first time played
		String lp = ""; // last time played
		String tp = ""; // times played

		if (h2 != null)
			t = h2.html();
		if (credit != null)
			cr = credit.html();
		if (lyrics != null)
			l = lyrics.html();
		if (copyright != null)
			copy = copyright.html();
		if (timesPlayed != null)
			tp = timesPlayed.html();

		if (!tp.equals("0")) {
			List<String> concerts = extractConcerts(playInfos.html());
			fp = concerts.get(0);
			lp = concerts.get(1);
		}

		String sID = "s" + songCounter++;
		Song song = new Song(sID, url, t, l, copy, cr, fp, lp, tp);

		for (Element element : albums) {

			Element link = element.selectFirst("div.caption > div.actions.double > a.buy");

			Element infos = element.selectFirst("div.caption > div.information > small");
			String title = "";
			String href = "";
			String year = "";
			try {
				title = Strings.getTitle(infos.html());
				href = link.attr("href");
				year = Strings.getYear(infos.html());
			} catch (NullPointerException e) { // might be that there is no album attached to the song
				title = "none";
				href = "none";
			}

			Album currentAlbum = null;

			if (albumsMap.containsKey(title)) {
				currentAlbum = albumsMap.get(title);
				if(currentAlbum.getYear().equals(""))
					currentAlbum.setYear(year);
			} else {
				currentAlbum = new Album("a" + (albumsMap.size() + 1));
				currentAlbum.setTitle(title);
				currentAlbum.setYear(year);
				currentAlbum.setLink(href);
	
			}
				
			song.addAlbum(currentAlbum);
			currentAlbum.addTrack(song);
			albumsMap.put(title, currentAlbum);
			// TODO add song to Album
			
		}

		return song;
	}

	private List<String> extractConcerts(String html) {

		List<String> toReturn = new ArrayList<String>();
		// System.out.println(html);

		String[] parts = html.split("</a>");
		String firstTime = parts[0];
		String lastTime = parts[1];

		toReturn.add(Strings.findConcertLink(firstTime));
		toReturn.add(Strings.findConcertLink(lastTime));

		return toReturn;
	}

}
