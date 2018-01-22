package de.uni_koeln.dh.bd.data.albums;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import de.uni_koeln.dh.bd.data.Entry;
import de.uni_koeln.dh.bd.data.Reference;
import de.uni_koeln.dh.bd.data.songs.Song;

@XmlType(propOrder = {"title", "artist", "year", "tracklist"})
public class Album extends Reference {
	
		private String title, artist;
		private int year;
		private List<Entry> tracklist = new ArrayList<Entry>();
		private List<Song> songElements = new ArrayList<Song>();
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}	
		
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	@XmlElementWrapper
	@XmlElement(name = "track")
	public List<Entry> getTracklist() {
		return tracklist;
	}
	
	public void setTracklist(List<Entry> tracklist) {
		this.tracklist = tracklist;
	}
	
	public void addTrack(Entry track) {
		tracklist.add(track);
	}
	
	@Override
	public String toString() {
		return "[" + getId() + "]\t" + title;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getArtist() {
		return artist;
	}

	@XmlTransient
	public List<Song> getSongElements() {
		return songElements;
	}

	public void setSongElements(List<Song> songElements) {
		this.songElements = songElements;
	}
	
}
