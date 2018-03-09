package de.uni_koeln.dh.lyra.model.place;

import java.io.Serializable;

import de.uni_koeln.dh.lyra.data.Song;

public class PopUp implements Serializable{

	private static final long serialVersionUID = -158703385862088096L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((placeName == null) ? 0 : placeName.hashCode());
		result = prime * result + ((referredSong == null) ? 0 : referredSong.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PopUp other = (PopUp) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (placeName == null) {
			if (other.placeName != null)
				return false;
		} else if (!placeName.equals(other.placeName))
			return false;
		if (referredSong == null) {
			if (other.referredSong != null)
				return false;
		} else if (!referredSong.equals(other.referredSong))
			return false;
		return true;
	}
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
