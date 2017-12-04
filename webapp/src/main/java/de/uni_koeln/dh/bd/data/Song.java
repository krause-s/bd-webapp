package de.uni_koeln.dh.bd.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Song {

		@JacksonXmlProperty(isAttribute = true)
		private String ref; 		// TODO URL?
		
		@JacksonXmlProperty
		private String title, 
			lyrics, 
			credits, 
			copyright;
	
	// required by Jackson	
	public Song() {}	
	
	public String getURL() {
		return ref;
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
	
	@Override
	public String toString() {
		return title;
	}
	
}
