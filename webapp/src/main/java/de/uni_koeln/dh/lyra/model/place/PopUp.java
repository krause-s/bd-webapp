package de.uni_koeln.dh.lyra.model.place;

import java.util.Date;

public class PopUp {

	// Placename from lyrics or metadata
	String placeName;
	// for songs = songtitle. For place the hallname etc.
	String title;
	// lyricssnippet or metadescription text
	String content;
	// if it's a song this is the link to full lyrics
	String lyricsLocation;
	// Publicationdate or eventdate
	Date date;
	
	public PopUp(String placeName){
		this.placeName = placeName;
	}
	
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLyricsLocation() {
		return lyricsLocation;
	}
	public void setLyricsLocation(String lyricsLocation) {
		this.lyricsLocation = lyricsLocation;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	
	
}
