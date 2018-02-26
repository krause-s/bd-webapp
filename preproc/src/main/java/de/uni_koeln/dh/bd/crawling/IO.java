package de.uni_koeln.dh.bd.crawling;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.uni_koeln.dh.bd.data.Entry;
import de.uni_koeln.dh.bd.data.albums.Album;
import de.uni_koeln.dh.bd.data.songs.Song;
import de.uni_koeln.dh.bd.service.CorpusService;

public class IO {
	
	public void exportSongsToXML(List<Song> songs) throws IOException {

		CorpusService cs = new CorpusService();
		cs.exportSongs(songs, "songs.xml");

	}

	public void exportAlbumsToXML(List<Album> albums) throws IOException {

		CorpusService cs = new CorpusService();
		cs.exportAlbums(albums, "albums.xml");

	}

	public void exportSongsToXLSX(List<Song> songs) throws IOException {
		String[] headRow = new String[4];
		headRow[0] = "title";
		headRow[1] = "artist";
		headRow[2] = "lyrics";
		headRow[3] = "albums";
		
		File export = new File("songs.xlsx");
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
		
		for (Song song : songs) {
			row = sheet.createRow(r++);
			
			//title 
			cell = row.createCell(0);
			cell.setCellValue(song.getTitle());
			
			//artist
			cell = row.createCell(1);
			cell.setCellValue("Bob Dylan");// TODO dynamisieren? auch crawlen?
			
			//lyrics
			cell = row.createCell(2);
			cell.setCellValue(song.getLyrics());
			
			//albums
			cell = row.createCell(3);
			cell.setCellValue(createString(song.getAlbums()));
		}
		
		FileOutputStream fos = new FileOutputStream(export);
		wb.write(fos);
		wb.close();
		
		
	}
	
	

	private String createString(List<Entry> entries) {
		StringBuilder sb = new StringBuilder();
		String separator = "";
		for (Entry entry : entries) {
			sb.append(separator + entry.getValue());
			separator = "\n";
		}
		return sb.toString();
	}

	public void exportAlbumsToXLSX(List<Album> albums) throws IOException {
		String[] headRow = new String[4];
		headRow[0] = "title";
		headRow[1] = "artist";
		headRow[2] = "year";
		headRow[3] = "songs";
		
		File export = new File("albums.xlsx");
		if (!export.exists())
			export.createNewFile();
		
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("albums");
		int r = 0;

		Row row = sheet.createRow(r++);
		Cell cell;
		for (int i = 0; i < headRow.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(headRow[i]);
		}
		
		for (Album album : albums) {
			
			if(album.getYear()!= 0){
				System.out.println(album.getYear());
			}
			
			row = sheet.createRow(r++);
			
			//title
			cell = row.createCell(0);
			cell.setCellValue(album.getTitle());
			
			//artist
			cell = row.createCell(1);
			cell.setCellValue("Bob Dylan");
			
			//year
			cell = row.createCell(2);
			cell.setCellValue(album.getYear());
			
			//songs
			cell = row.createCell(3);
			cell.setCellValue(createString(album.getTracklist()));
			
		}
		
		FileOutputStream fos = new FileOutputStream(export);
		wb.write(fos);
		wb.close();
		
	}
	
	

}
