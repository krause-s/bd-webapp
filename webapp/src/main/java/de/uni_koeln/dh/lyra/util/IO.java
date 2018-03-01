package de.uni_koeln.dh.lyra.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.lyra.data.Song;

public class IO {

	private Logger logger = LoggerFactory.getLogger(getClass());

	// private List<Event> bio = new ArrayList<Event>();

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

	/**
	 * reads .xlsx-File with the following columns: arist, title, release, year,
	 * compilation, lyrics, comment initializes for each row a song object and
	 * returns the songs as list
	 * 
	 * @param dataPath
	 * @return
	 * @throws IOException
	 */
	public List<Song> getDataFromXLSX(String dataPath) throws IOException {
		// TODO import bio information

		FileInputStream fis = new FileInputStream(new File(dataPath));
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet songSheet = wb.getSheetAt(0);

		List<Song> toReturn = getSongs(songSheet);
//		generateBio(wb.getSheetAt(1));

		// logger.info(bio.get(0).getEvent());
		// logger.info(bio.size() + "");

		wb.close();
		return toReturn;
	}

	// private void generateBio(XSSFSheet bioSheet) {
	// int row = 1;
	//
	// while (row <= bioSheet.getLastRowNum()) {
	// XSSFRow r = bioSheet.getRow(row++);
	// String artist = r.getCell(0).getStringCellValue();
	// String event = r.getCell(1).getStringCellValue();
	// String placeString = r.getCell(2).getStringCellValue();
	//
	// bio.add(new Event(artist, event, placeString));
	// }
	//
	// }

	private List<Song> getSongs(XSSFSheet songSheet) {

		List<Song> toReturn = new ArrayList<Song>();
		int row = 1;
		// logger.info(sheet.getLastRowNum() + "");
		while (row <= songSheet.getLastRowNum()) {
			XSSFRow r = songSheet.getRow(row++);
			String artist = r.getCell(0).getStringCellValue();
			String title = r.getCell(1).getStringCellValue();
			String release = r.getCell(2).getStringCellValue();
			int year = (int) r.getCell(3).getNumericCellValue();
			String compilationString = r.getCell(4).getStringCellValue();
			String lyrics = r.getCell(5).getStringCellValue();
			String comment = r.getCell(6).getStringCellValue();

			boolean compilation = false;
			if (compilationString.equals("x"))
				compilation = true;

			Song song = new Song(title, lyrics, artist, release, year, compilation, comment);
			toReturn.add(song);
		}

		return toReturn;
	}

}
