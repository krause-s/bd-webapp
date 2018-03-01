package de.uni_koeln.dh.lyra.data.songs;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import de.uni_koeln.dh.lyra.data.Entry;
import de.uni_koeln.dh.lyra.data.Reference;

@XmlType(propOrder = { "title", "artist", "lyrics", "tokens", "credits", "copyright", "firstPlayed", "lastPlayed", "timesPlayed",
		"albums" })
@Deprecated
public class Song extends Reference {

	private String title, lyrics, artist, annotatedLyrics, credits, copyright, comment;
	private boolean firstEdition;
	private int year;

	private DateLocation firstPlayed, lastPlayed;

	private int timesPlayed;
	private List<Entry> albums = new ArrayList<Entry>();

	private List<String> tokens = new ArrayList<String>();

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
	
	public void setAnnotated(String annotated) {
		annotatedLyrics = annotated;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getArtist() {
		return artist;
	}

	
	public List<String> getTokens() {
		return tokens;
	}

	public void setTokens(List<String> tokens) {
		this.tokens = tokens;
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

	public void setYear(String stringCellValue) {
		
	}

	public void setFirstEdition(String stringCellValue) {
		// TODO Auto-generated method stub
		
	}

	public void setComment(String stringCellValue) {
		// TODO Auto-generated method stub
		
	}

	

}
