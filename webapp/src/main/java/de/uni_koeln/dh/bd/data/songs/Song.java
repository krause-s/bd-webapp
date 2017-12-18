package de.uni_koeln.dh.bd.data.songs;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import de.uni_koeln.dh.bd.data.Entry;
import de.uni_koeln.dh.bd.data.Reference;

@XmlType(propOrder = {"title", "lyrics", "credits", "copyright", "firstPlayed", "lastPlayed", "timesPlayed", "albums"})
public class Song extends Reference {

		private String title,
			lyrics,
			credits,
			copyright;
		
		private DateLocation firstPlayed,
			lastPlayed;
		
		private int timesPlayed;		
		private List<Entry> albums = new ArrayList<Entry>();
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}	
		
	public String getLyrics() {
		return lyrics;
	}

	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}
	
	public String getCredits() {
		return credits;
	}
	
	public void setCredits(String credits) {
		this.credits = credits;
	}
	
	public String getCopyright() {
		return copyright;
	}
	
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	
	@XmlElement(name = "firstplayed")
	public DateLocation getFirstPlayed() {
		return firstPlayed;
	}
	
	public URL getFirstPlayedURL() {
		return firstPlayed.getURL();
	}
	
	public void setFirstPlayed(DateLocation firstPlayed) {
		this.firstPlayed = firstPlayed;
	}
	
	@XmlElement(name = "lastplayed")
	public DateLocation getLastPlayed() {
		return lastPlayed;
	}
	
	public URL getLastPlayedURL() {
		return lastPlayed.getURL();
	}
	
	public void setLastPlayed(DateLocation lastPlayed) {
		this.lastPlayed = lastPlayed;
	}
	
	@XmlElement(name = "timesplayed")
	public int getTimesPlayed() {
		return timesPlayed;
	}
	
	public void setTimesPlayed(int timesPlayed) {
		this.timesPlayed = timesPlayed;
	}
	
	@XmlElementWrapper
	@XmlElement(name = "album")
	public List<Entry> getAlbums() {
		return albums;
	}
	
	public void setAlbums(List<Entry> albums) {
		this.albums = albums;
	}
	
	@Override
	public String toString() {
		return "[" + getId() + "]\t" + title;
	}
	
}
