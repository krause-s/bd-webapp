package de.uni_koeln.dh.bd.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO deprecated?
public class Album {
	
	private String id;
	private String link;
	private List<Song> tracklist = new ArrayList<Song>();
	private String title;
	private String year = "";
	
	
	public Album(String id) {
		this.id = id;
	}
	
	public Album (String id, String infos, Song song) {
		this.id = id;
		tracklist.add(song);
		// TODO process infos
	}
	
	public Album(String id, Map<String, String> tracklist, String titleAndYear) {
		this.id = id;
		this.title = titleAndYear.replaceAll(" \\(\\d{4}\\)", "");
		
		String regex = "\\(\\d{4}\\)";
		Pattern pattern = Pattern.compile(regex);

		Matcher match = pattern.matcher(titleAndYear);
		if (match.find())
			year =  titleAndYear.substring(match.start() + 1, match.end() - 1);
		
		System.out.println(id + "-" + title + "-" + year);
		
	}

	public List<Song> getTracklist() {
		return tracklist;
	}

	public void addTrack(Song track) {
		tracklist.add(track);
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getID() {
		return id;
	}

	public void setLink(String link) {
		this.link = link;
		
	}
	
	public String getLink() {
		return link;
	}

	
	
	

}
