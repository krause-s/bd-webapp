package de.uni_koeln.dh.lyra.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.data.Song;
import de.uni_koeln.dh.lyra.model.place.Place;
import de.uni_koeln.dh.lyra.model.place.PopUp;
import de.uni_koeln.dh.lyra.processing.SongPreprocessor;

public class IO {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Map<String, Artist> artists = new HashMap<String, Artist>();
	List<Place> placesToEvaluate = new ArrayList<Place>();
	SongPreprocessor prep;
	
	public List<Place> getPlacesToEvaluate() {
		return placesToEvaluate;
	}

	/**
	 * reads .xlsx-File with the following columns: arist, title, release, year,
	 * compilation, lyrics, comment initializes for each row a song object, adds it
	 * to the referred artist and returns a list of all artist objects
	 * 
	 * @param dataPath
	 * @return
	 * @throws IOException
	 */
	public Map<String, Artist> getDataFromXLSX(File file) throws IOException {

		prep = new SongPreprocessor();

		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet songSheet = wb.getSheetAt(0);

		placesToEvaluate = readSongs(songSheet);
		generateBio(wb.getSheetAt(1));

		wb.close();

//		List<Artist> toReturn = new ArrayList<Artist>(artists.values());
		return artists;
	}

	private void generateBio(XSSFSheet bioSheet) {
		int row = 1;

		while (row <= bioSheet.getLastRowNum()) {
			XSSFRow r = bioSheet.getRow(row++);
			String artistName = getString(r, 0);
			String event = getString(r, 1);
			String placeName = getString(r, 2);

			PopUp popUp = new PopUp(placeName, event);
			Double[] latLon = null;
			latLon = prep.getCoordinates(placeName);
			if(latLon == null)
				continue;
			Place place = new Place(latLon[0], latLon[1]);

			Artist artist;

			if (artists.containsKey(artistName)) {
				artist = artists.get(artistName);
				if(artist.getBioPlaces().contains(place)) {
					place = artist.getBioPlaces().get(artist.getBioPlaces().indexOf(place));
					place.addPopUp(popUp);

				} else {
					place.addPopUp(popUp);
					artist.addBioPlace(place);
				}
			}  else {
				artist = new Artist(artistName);
				place.addPopUp(popUp);
				artist.addBioPlace(place);
			}
			artists.put(artistName, artist);

		}
	}

	private List<Place> readSongs(XSSFSheet songSheet) {

		logger.info("read songs");
		int row = 1;

		while (row <= songSheet.getLastRowNum()) {

			XSSFRow r = songSheet.getRow(row++);
			
			String artistName = getString(r, 0);
			String title = getString(r, 1);
			String medium = getString(r, 2);
			int year = getInteger(r, 3);
			String compilationString = getString(r, 4);
			String lyrics = getString(r, 5);
			String comment = getString(r, 6);

			boolean compilation = false;
			if (compilationString.equals("x"))
				compilation = true;
			
			if (lyrics.equals(""))
				continue;

			Song song = new Song(title, lyrics, artistName, medium, year, compilation, comment);

			song = prep.tokenizeSong(song);
			//search for quotes of places to add to great places list
			prep.addPlaces(song);	

			Artist artist = new Artist(artistName);
			if (artists.containsKey(artistName))
				artist = artists.get(artistName);
			artist.addSong(song);
			artists.put(artistName, artist);

		}
		List<Place> placesToEvaluate = prep.getPlacesToEvaluate();
		return placesToEvaluate;
		
		

	}

	private String getString(XSSFRow r, int i) {
		Cell c = r.getCell(i);
		if (c != null) 
			return r.getCell(i).getStringCellValue();
		else
			return "";
	}
	
	private Integer getInteger(XSSFRow r, int i) {
		Cell c = r.getCell(i);
		if (c != null) 
			return (int) r.getCell(i).getNumericCellValue();
		else
			return 0;
	}

	private static BufferedReader getReader(File file) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		return new BufferedReader(isr);
	}

	public static String getContent(File file) throws IOException {
		BufferedReader bfr = getReader(file);

		StringBuilder sb = new StringBuilder();
		String curLine;

		while ((curLine = bfr.readLine()) != null)
			sb.append(curLine);

		bfr.close();
		return sb.toString();
	}

	

}
