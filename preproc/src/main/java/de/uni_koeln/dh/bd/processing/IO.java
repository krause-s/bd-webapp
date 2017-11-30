package de.uni_koeln.dh.bd.processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

//			if (!(line.startsWith("http") || line.startsWith("\n")))
//				System.out.println(line);

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

		File xml = new File("export.xml");

		FileWriter fw = new FileWriter(xml);
		fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"); 
		
		fw.write("<songs>\n");
		for (Song song : songs) {
			fw.write("\t<song ref=\"" + song.getURL() + "\">\n");
			fw.write("\t\t<title>" + song.getTitle() + "</title>\n" 
					+ "\t\t<lyrics>" + song.getLyrics() + "</lyrics>\n"
					+ "\t\t<credits>" + song.getCredits() + "</credits>\n"
					+ "\t\t<copyright>" + song.getCopyright() + "</copyright>\n");
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

		Element h2 = article.selectFirst("h2");
		Element credit = article.selectFirst("div.credit");
		Element lyrics = article.selectFirst("div.article-content.lyrics");
		Element copyright = lyrics.selectFirst("p.copytext");

		String t = "";
		String l = "";
		String copy = "";
		String cr = "";

		if (h2 != null)
			t = h2.html();
		if (credit != null)
			cr = credit.html();
		if (lyrics != null)
			l = lyrics.html();
		if (copyright != null)
			copy = copyright.html();

		Song song = new Song(url, t, l, copy, cr);

		return song;
	}
}
