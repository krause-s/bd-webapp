package de.uni_koeln.dh.lyra.processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.uni_koeln.dh.lyra.data.Entry;
import de.uni_koeln.dh.lyra.data.albums.Album;
import de.uni_koeln.dh.lyra.data.songs.Song;
import de.uni_koeln.dh.lyra.service.CorpusService;
import de.uni_koeln.dh.lyra.util.Tokenizer;

public class IO {

	private Map<String, Album> albumsMap = new HashMap<String, Album>();

//	private int songCounter = 1;

	public List<Song> getSongsFromXML(String path) throws IOException {

		CorpusService cs = new CorpusService();
		List<Song> songs = cs.getSongs();

		return songs;
	}
	
	public List<Song> getSongsFromXLSX(String path) throws IOException {
		Tokenizer tokenizer = new Tokenizer();
		List<Song> toReturn = new ArrayList<Song>();
		
		FileInputStream fis = new FileInputStream(new File(path));
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheetAt(0);		

		int row = 1;
		while (row <= sheet.getLastRowNum()) {
			Song song = new Song();
			
			XSSFRow r = sheet.getRow(row);
			song.setTitle(r.getCell(0).getStringCellValue());
			song.setArtist(r.getCell(1).getStringCellValue());
			song.setLyrics(r.getCell(2).getStringCellValue());
			
			String albumString = r.getCell(3).getStringCellValue();
			if(!albumString.equals("")) {
				List<Entry> entries = new ArrayList<Entry>();
//				System.out.println(albumString);
				String[] albumStrings = albumString.split("\n");
				for (int i = 0; i < albumStrings.length; i++) {
					Entry entry = new Entry();
					entry.setValue(albumStrings[i]);
					entries.add(entry);
				}
				
				song.setAlbums(entries);
			}
			
			//Tokenize lyrics
			List<String> tokens = tokenizer.tokenize(song.getLyrics());
			song.setTokens(tokens);
			
			toReturn.add(song);
			row++;
		}
		wb.close();
		
		return toReturn;
	}


	public Map<String, Album> getAlbumsMap() {
		return albumsMap;
	}

	public void exportSongsToXML(List<Song> songs) throws IOException {

		CorpusService cs = new CorpusService();
		cs.exportSongs(songs, "songs.xml");

	}

	public void exportAlbumsToXML(List<Album> albums) throws IOException {

		CorpusService cs = new CorpusService();
		cs.exportAlbums(albums, "albums.xml");

	}

	public List<Album> getAlbumsFromXLSX(String path) throws IOException {
List<Album> toReturn = new ArrayList<Album>();
		
		FileInputStream fis = new FileInputStream(new File(path));
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheetAt(0);		

		int row = 1;
		while (row <= sheet.getLastRowNum()) {
			Album album = new Album();
			
			XSSFRow r = sheet.getRow(row);
			album.setTitle(r.getCell(0).getStringCellValue());
			album.setArtist(r.getCell(1).getStringCellValue());
			album.setYear((int) r.getCell(2).getNumericCellValue());
			
			
			String albumString = "";
			if((albumString = r.getCell(3).getStringCellValue()) != "") {
				List<Entry> entries = new ArrayList<Entry>();
				
				String[] albumStrings = albumString.split("\n"); //TODO refactor
				for (int i = 0; i < albumStrings.length; i++) {
					Entry entry = new Entry();
					entry.setValue(albumStrings[i]);
				}

				album.setTracklist(entries);
			}
			
			toReturn.add(album);
			row++;
		}
		wb.close();
		
		return toReturn;
	}
	
	public void exportToNewXLSX(List<Song> songs, List<Album> albums) throws IOException {
		Map<String, Song> songMap = new HashMap<String, Song>();
		for(Song song : songs) {
			songMap.put(song.getTitle(), song);
		}
		
		String[] headRow = new String[7];
		headRow[0] = "artist";
		headRow[1] = "title";
		headRow[2] = "release";
		headRow[3] = "year";
		headRow[4] = "compilation";
		headRow[5] = "lyrics";
		headRow[6] = "comment";
		
		Set<String> firstEdition = new HashSet<String>();
		
		File export = new File("src/main/resources/20180225_lyrics_database.xlsx");
		if (!export.exists())
			export.createNewFile();
		
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("songs");
		int r = 0;

		Row row = sheet.createRow(r++);
		Cell cell;
		for (int i = 0; i < headRow.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(headRow[i]);
		}
		//sort albums by publishing year
		Collections.sort(albums, new Comparator<Album>() {

			public int compare(Album o1, Album o2) {
				Integer i1 = o1.getYear();
				Integer i2 = o2.getYear();
				return i1.compareTo(i2);
			}		
		});
		
		for(Album album : albums) {
			List<Song> currentSongs = album.getSongElements();

			int currentYear = album.getYear();
			String albumTitle = album.getTitle();
			String first = "";
			
			Set<String> albumSongsSet = new HashSet<String>();
			for(Song song : currentSongs) {
				Song currentSong = songMap.get(song.getTitle());
				String title = currentSong.getTitle();
				if(!albumSongsSet.add(title)) //if set already contains title
					continue;
				row = sheet.createRow(r++);
				
				//create song to add to xlsx //TODO compilation markiert alle Songs, die nicht zum ersten mal erscheinen
				if(!firstEdition.add(song.getTitle())) //true if song haven't been in set yet
						first = "x";
				
				String lyrics = currentSong.getLyrics();
				String artist = currentSong.getArtist();
				
				//artist
				cell = row.createCell(0);
				cell.setCellValue(artist);
				//title
				cell = row.createCell(1);
				cell.setCellValue(title);
				//release
				cell = row.createCell(2);
				cell.setCellValue(albumTitle);
				//year
				cell = row.createCell(3);
				cell.setCellValue(currentYear);
				//firstedition
				cell = row.createCell(4);
				cell.setCellValue(first);
				//lyrics
				cell = row.createCell(5);
				cell.setCellValue(lyrics);
				//comment
				cell = row.createCell(6);
				cell.setCellValue("");
				
			}
		}
		FileOutputStream fos = new FileOutputStream(export);
		wb.write(fos);
		wb.close();
	}

//	private Song createSongObject(String url, String songID) throws IOException {
//
//		Document doc = null;
//
//		doc = Jsoup.connect(url)
//				.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0")
//				.referrer("http://www.google.com").ignoreHttpErrors(true).get();
//
//		Element article = doc
//				.selectFirst("body > div#variable > div.page-content > div.main-wrapper.page-wrapper > div.content"
//						+ " > div.interior-content > article");
//
//		Elements playInfos = doc
//				.select("body > div#variable > div.page-content > div.main-wrapper.page-wrapper > div.content"
//						+ " > div.interior-content > aside.blocks > div.played-stats > div.played");
//
//		Elements albums = doc
//				.select("body > div#variable > div.page-content > div.main-wrapper.page-wrapper > div.content"
//						+ " > div.interior-content > aside.blocks > div.item.album.secondary");
//
//		Element newAlbum = doc
//				.selectFirst("body > div#variable > div.page-content > div.main-wrapper.page-wrapper > div.content"
//						+ " > div.interior-content > aside.blocks > div.item.album");
//
//		albums.add(newAlbum); // contains all albums presented in html
//
//		Element h2 = article.selectFirst("h2");
//		Element credit = article.selectFirst("div.credit");
//		Element lyrics = article.selectFirst("div.article-content.lyrics");
//		Element copyright = lyrics.selectFirst("p.copytext");
//		// TODO copyright steht nicht immer im selben element
//		Element timesPlayed = playInfos.select("span.db").first();
//
//		String t = ""; // title
//		String l = ""; // lyrics
//		String copy = ""; // copyright
//		String cr = ""; // credits
//		String fp = ""; // first time played
//		String lp = ""; // last time played
//		String tp = ""; // times played
//
//		if (h2 != null)
//			t = h2.html();
//		if (credit != null)
//			cr = credit.html();
//		if (lyrics != null)
//			l = lyrics.html();
//		if (copyright != null)
//			copy = copyright.html();
//		if (timesPlayed != null)
//			tp = timesPlayed.html();
//
//		l = l.replaceAll("<br> ", "");
//		l = l.split("<p class=\"copytext")[0];
//
//		if (!tp.equals("0")) {
//			List<String> concerts = extractConcerts(playInfos.html());
//			fp = concerts.get(0);
//			lp = concerts.get(1);
//		}
//		DateLocation firstPlayed = new DateLocation();
//		DateLocation lastPlayed = new DateLocation();
//		try {
//			firstPlayed.setURL(new URL(fp));
//			lastPlayed.setURL(new URL(lp));
//		} catch (MalformedURLException e) {
//			
//		}
//		// TODO extraktion vom Datum aus Link
//
//		Song song = new Song();
//		song.setURL(new URL(url));
//		song.setId(songID);
//		song.setTitle(t);
//		song.setLyrics(l);
//		song.setCopyright(copy);
//		song.setCredits(cr);
//		song.setTimesPlayed(Integer.parseInt(tp));
//		song.setFirstPlayed(firstPlayed);
//		song.setLastPlayed(lastPlayed);
//
//		List<Entry> albumList = new ArrayList<Entry>();
//
//		for (Element element : albums) {
//			
//			Album album = createAlbum(element);
//			if(album == null)
//				continue;
//			album.addTrack(new Entry(songID, t));
//
//			Entry currentEntry = new Entry(album.getId(), album.getTitle());
//			
//			albumList.add(currentEntry);
//			albumsMap.put(album.getTitle(), album);
//		}
//		song.setAlbums(albumList);
//
//		return song;
//	}
	
//	private Album createAlbum(Element albumElement) {
//		
//		String albumTitle = "";
//		String albumRef = "";
//		int albumYear = 0;
//		
//		Element link = albumElement.selectFirst("div.caption > div.actions.double > a.buy");
//		Element infos = albumElement.selectFirst("div.caption > div.information > small");
//
//		try {
//			albumTitle = Strings.getTitle(infos.html());
//		} catch (NullPointerException e) { // might be that there is no album attached to the song
//			albumTitle = "none";
//			return null;
//		}
//		
//		try {
//			albumRef = link.attr("href");
//		} catch (NullPointerException e) {
//			albumRef = "";
//		}
//		
//		
//		albumYear = Strings.getYear(infos.html());
//
//		// verarbeitet sowohl das gesamte Album (currentAlbum) als auch den
//		// Album-Eintrag
//		// des aktuellen Songs
//		Album currentAlbum = null;
//
//
//		if (albumsMap.containsKey(albumTitle)) 
//			currentAlbum = albumsMap.get(albumTitle);
//		else {
//			currentAlbum = new Album();
//			currentAlbum.setId("a" + (albumsMap.size() + 1));
//			currentAlbum.setTitle(albumTitle);			
//			currentAlbum.setYear(0);
//
//		}
//		if (currentAlbum.getYear() == 0)
//			currentAlbum.setYear(albumYear);
//		if(currentAlbum.getURL() == null) {
//			try {
//				currentAlbum.setURL(new URL(albumRef));
//			} catch (MalformedURLException e) {
//				
//			}
//		}
//		
//		return currentAlbum;
//			
//		
//	}
//
//	private List<String> extractConcerts(String html) {
//
//		List<String> toReturn = new ArrayList<String>();
//
//		String[] parts = html.split("</a>");
//		String firstTime = parts[0];
//		String lastTime = parts[1];
//
//		toReturn.add(Strings.findConcertLink(firstTime));
//		toReturn.add(Strings.findConcertLink(lastTime));
//
//		return toReturn;
//	}
//
//	public void exportPlaces(Set<Location> tagged) throws IOException {
//		FileWriter fw = new FileWriter(new File("places.txt"));
//		StringBuffer sb = new StringBuffer();
//		for (Location location : tagged) {
//			sb.append(location.getName() + ": " + location.getLatitude()
//			+ " " + location.getLongitude() + "\n");
//		}
//
//		fw.write(sb.toString());
//
//		fw.flush();
//		fw.close();
//
//	}

}
