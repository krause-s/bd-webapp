package de.uni_koeln.dh.bd.data.albums;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import de.uni_koeln.dh.bd.data.Entry;
import de.uni_koeln.dh.bd.data.Reference;

@XmlType(propOrder = {"title", "year", "tracklist"})
public class Album extends Reference {
	
		private String title;
		private int year;
		private List<Entry> tracklist = new ArrayList<Entry>();
	
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
	
}
