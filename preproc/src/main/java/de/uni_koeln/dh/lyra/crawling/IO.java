package de.uni_koeln.dh.lyra.crawling;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.uni_koeln.dh.lyra.data.Song;

public class IO {
	
public void exportSongsToXLSX(List<Song> songs) throws IOException {
		
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
		
		File export = new File("src/main/resources/lyrics_database.xlsx");
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
		//sort songs by publishing year
		Collections.sort(songs, new Comparator<Song>() {


			public int compare(Song o1, Song o2) {
				Integer i1 = o1.getYear();
				Integer i2 = o2.getYear();
				return i1.compareTo(i2);
			}		
		});
		
		for (Song currentSong : songs) {
			row = sheet.createRow(r++);		
			
			int currentYear = currentSong.getYear();
			String release = currentSong.getRelease();
			String comp = "";
			String title = currentSong.getTitle();

					
			//create song to add to xlsx 
			if(!firstEdition.add(currentSong.getTitle())) //true if song has already been added
					comp = "x";
			
			String lyrics = currentSong.getLyrics();
			if(lyrics.equals(""))
				continue;
			String artist = currentSong.getArtist();
			
			
			row = sheet.createRow(r++);		
			
			//artist
			cell = row.createCell(0);
			cell.setCellValue(artist);
			//title
			cell = row.createCell(1);
			cell.setCellValue(title);
			//release
			cell = row.createCell(2);
			cell.setCellValue(release);
			//year
			cell = row.createCell(3);
			cell.setCellValue(currentYear);
			//firstedition
			cell = row.createCell(4);
			cell.setCellValue(comp);
			//lyrics
			cell = row.createCell(5);
			cell.setCellValue(lyrics);
			//comment
			cell = row.createCell(6);
			cell.setCellValue("");
		}
		
		FileOutputStream fos = new FileOutputStream(export);
		wb.write(fos);
		wb.close();
	
	}

	
	

}
