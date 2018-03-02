package de.uni_koeln.dh.lyra.model.place;

import java.util.Date;

import de.uni_koeln.dh.lyra.data.Song;

public class PopUp {

	// Placename from lyrics or metadata
	String placeName;
	// for songs = songtitle. For place the hallname etc.
//	String title;
	// lyricssnippet or metadescription text
	String content;
	// if it's a song this is the link to full lyrics
//	String lyricsLocation;
	// Publicationdate or eventdate
//	Date date;
	
	private Song referredSong;
	
	public PopUp(String placeName){
		this.placeName = placeName;
	}
	
	public PopUp(String placeName, String content, Song referredSong) {
		this.placeName = placeName;
		this.content = content;
		this.referredSong = referredSong;
	}

	public PopUp(String placeName, String content) {
		this.placeName = placeName;
		this.content = content;
	}
	
	public Song getReferredSong() {
		return referredSong;
	}

	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
//	public String getTitle() {
//		return title;
//	}
//	public void setTitle(String title) {
//		this.title = title;
//	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
//	public String getLyricsLocation() {
//		return lyricsLocation;
//	}
//	public void setLyricsLocation(String lyricsLocation) {
//		this.lyricsLocation = lyricsLocation;
//	}
//	public Date getDate() {
//		return date;
//	}
//	public void setDate(Date date) {
//		this.date = date;
//	}

	
	
}
