package de.uni_koeln.dh.lyra.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.data.Song;
import de.uni_koeln.dh.lyra.model.place.Place;
import de.uni_koeln.dh.lyra.model.place.PopUp;
import de.uni_koeln.dh.lyra.processing.PlaceEvaluator;
import de.uni_koeln.dh.lyra.processing.SongPreprocessor;

public class IO {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Map<String, Artist> artists = new HashMap<String, Artist>();
	SongPreprocessor prep;
	

	/**
	 * reads .xlsx-File with the following columns: arist, title, release, year,
	 * compilation, lyrics, comment initializes for each row a song object, adds it
	 * to the referred artist and returns a list of all artist objects
	 * 
	 * @param dataPath
	 * @return
	 * @throws IOException
	 */
	public List<Artist> getDataFromXLSX(String dataPath) throws IOException {

		prep = new SongPreprocessor();

		FileInputStream fis = new FileInputStream(new File(dataPath));
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet songSheet = wb.getSheetAt(0);

		readSongs(songSheet);
		generateBio(wb.getSheetAt(1));

		wb.close();

		List<Artist> toReturn = new ArrayList<Artist>(artists.values());
		return toReturn;
	}

	private void generateBio(XSSFSheet bioSheet) {
		int row = 1;

		while (row <= bioSheet.getLastRowNum()) {
			XSSFRow r = bioSheet.getRow(row++);
			String artistName = r.getCell(0).getStringCellValue();
			String event = r.getCell(1).getStringCellValue();
			String placeString = r.getCell(2).getStringCellValue();

			Place bioPlace = new Place(event, placeString);

			Artist artist = new Artist(artistName);
			if (artists.containsKey(artistName))
				artist = artists.get(artistName);
			artist.addBioPlace(bioPlace);
			artists.put(artistName, artist);

		}
	}

	private void readSongs(XSSFSheet songSheet) {

		int row = 1;
		while (row <= songSheet.getLastRowNum()) {
			XSSFRow r = songSheet.getRow(row++);
			String artistName = r.getCell(0).getStringCellValue();
			String title = r.getCell(1).getStringCellValue();
			String release = r.getCell(2).getStringCellValue();
			int year = (int) r.getCell(3).getNumericCellValue();
			String compilationString = r.getCell(4).getStringCellValue();
			String lyrics = r.getCell(5).getStringCellValue();
			String comment = r.getCell(6).getStringCellValue();

			boolean compilation = false;
			if (compilationString.equals("x"))
				compilation = true;

			Song song = new Song(title, lyrics, artistName, release, year, compilation, comment);

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
		
		//TODO UI to evaluate songs
		List<Place> evaluatedPlaces = PlaceEvaluator.evaluatePlaces(placesToEvaluate);
		//TODO sort the pop ups to the right artist
		
		for(Place place : evaluatedPlaces) {

			for(PopUp popUp : place.getPopUps()) {
				String artistName = popUp.getReferredSong().getArtist();
				Artist currArtist = artists.get(artistName);
				if(currArtist.getLyricsPlaces().contains(place)) {
					Place artistPlace = currArtist.getLyricsPlaces()
							.get(currArtist.getLyricsPlaces().indexOf(place));
					artistPlace.addPopUp(popUp);
				} else {
					Place artistPlace = new Place(place.getLongitude(), place.getLatitude());
					artistPlace.addPopUp(popUp);
					currArtist.addLyricsPlace(artistPlace);
				}
			}
		}
		
		for(Map.Entry<String, Artist> e : artists.entrySet()) {
			logger.info(e.getValue().getLyricsPlaces().size() + " annotated Places");
		}
		logger.info(evaluatedPlaces.size() + " evaluated Places");

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
