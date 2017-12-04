package de.uni_koeln.dh.bd.processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.uni_koeln.dh.bd.data.Song;

public class IO {
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

	public void exportSongsToXML(List<Song> songs) throws IOException {
		System.out.println("Export to: export.xml");

		File xml = new File("export.xml");

		FileWriter fw = new FileWriter(xml);
		fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");

		fw.write("<songs>\n");
		for (Song song : songs) {
			fw.write("\t<song ref=\"" + song.getURL() + "\">\n");
			fw.write("\t\t<title>" + song.getTitle() + "</title>\n" + "\t\t<lyrics>" + song.getLyrics() + "</lyrics>\n"
					+ "\t\t<credits>" + song.getCredits() + "</credits>\n" + "\t\t<copyright>" + song.getCopyright()
					+ "</copyright>\n" 
					+ "\t\t<firstplayed ref=\"" + song.getFirstPlayed() + "\" />\n"
					+ "\t\t<lastplayed ref=\"" + song.getLastPlayed() + "\" />\n"
					+ "\t\t<timesplayed>" + song.getTimesPlayed() + "</timesplayed>\n");
			fw.write("\t</song>\n");
		}

		fw.write("</songs>");
		fw.flush();
		fw.close();
	}

	private Song createSongObject(String url) throws IOException {
		Document doc = null;

		doc = Jsoup.connect(url)
				.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0")
				.referrer("http://www.google.com").ignoreHttpErrors(true).get();

		Element article = doc
				.select("body > div#variable > div.page-content > div.main-wrapper.page-wrapper > div.content"
						+ " > div.interior-content > article")
				.first();

		Elements playInfos = doc
				.select("body > div#variable > div.page-content > div.main-wrapper.page-wrapper > div.content"
						+ " > div.interior-content > aside.blocks > div.played-stats > div.played");

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

		Song song = new Song(url, t, l, copy, cr, fp, lp, tp);

		return song;
	}

	private List<String> extractConcerts(String html) {

		List<String> toReturn = new ArrayList<String>();
		// System.out.println(html);

		String[] parts = html.split("</a>");
		String firstTime = parts[0];
		String lastTime = parts[1];

		toReturn.add(findLink(firstTime));
		toReturn.add(findLink(lastTime));

		return toReturn;
	}

	private String findLink(String html) {

		String regex = "(http:\\/\\/www\\.[a-z\\d-]+\\.com\\/date\\/[a-z\\d%-]+\\/)";
		Pattern pattern = Pattern.compile(regex);

		Matcher match = pattern.matcher(html);
		if (match.find())
			return html.substring(match.start(), match.end());
		else
			return "";
	}
}
