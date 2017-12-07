package de.uni_koeln.dh.bd.data;

import java.net.URL;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Song {

		@JacksonXmlProperty(isAttribute = true, localName = "ref")
		private URL url;
		
		//////////////
		
		@JacksonXmlProperty
		private String title, 
			lyrics, 
			credits, 
			copyright;
		
		@JacksonXmlProperty(localName = "firstplayed")
		private Date firstPlayed;
		@JacksonXmlProperty(localName = "lastplayed")
		private Date lastPlayed;
		
		@JacksonXmlProperty(localName = "timesplayed")
		private int timesPlayed;
	
	public URL getURL() {
		return url;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getLyrics() {
		return lyrics;
	}
	
	public String getCredits() {
		return credits;
	}
	
	public String getCopyright() {
		return copyright;
	}
	
	public URL getFirstPlayed() {
		return firstPlayed.getURL();
	}
	
	public URL getLastPlayed() {
		return lastPlayed.getURL();
	}
	
	public int getTimesPlayed() {
		return timesPlayed;
	}
	
	@Override
	public String toString() {
		return title;
	}
	
}
