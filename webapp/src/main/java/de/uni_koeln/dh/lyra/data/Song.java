package de.uni_koeln.dh.lyra.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Song implements Serializable{
	
	private static final long serialVersionUID = 930011990465438709L;
	private String title, lyrics, artist, release, comment;
	private int year;
	private boolean compilation;
	private String uuid;
	private String[] tokens;
	private double[] weights;
	private Map<String, Integer> termFreqs;
	
	/**
	 * initialized a song object with the given data and generates a
	 * random UUID for the song object
	 * @param title
	 * @param lyrics
	 * @param artist
	 * @param release
	 * @param year
	 * @param compilation
	 * @param comment
	 */
	public Song(String title, String lyrics, String artist,
			String release, int year, boolean compilation, String comment) {
		this.uuid = UUID.randomUUID().toString();
		this.title = title;
		this.lyrics = lyrics;
		this.artist = artist;
		this.release = release;
		this.year = year;
		this.compilation = compilation;
		this.comment = comment;
	}
	
	public Song(String lyrics) {
		this.lyrics = lyrics;
		this.uuid = UUID.randomUUID().toString();
	}

	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid){
		this.uuid = uuid;
	}

	public String getTitle() {
		return title;
	}
	
	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}

	/**
	 * returns lyrics of the song. line breaks are
	 * set as "\n"
	 * @return
	 */
	public String getLyrics() {
		
		return lyrics;
	}
	
	/**
	 * returns lyrics of the song. line breaks are
	 * set as "<br />"
	 * @return
	 */
	public String getLyricsBR() {
		lyrics = lyrics.replaceAll("\n", "<br />");
		return lyrics;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getArtist() {
		return artist;
	}

	public String getRelease() {
		return release;
	}

	public String getComment() {
		return comment;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public int getYear() {
		return year;
	}

	public boolean isCompilation() {
		return compilation;
	}
	
	/**
	 * processes the tokens in terms of deleting punctuations etc
	 * @param tokens
	 */
	public void setTokens(String[] tokens) {
		List<String> toAdd = new ArrayList<String>();
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i].toLowerCase();
			// token.length > 1 ?
			token = tokens[i].replaceAll("[\\W]", "");
			if(token.length() > 1) //TODO tokens auf length > 1 beschränken?
				toAdd.add(token);
		}
		tokens = new String[toAdd.size()];
		toAdd.toArray(tokens);
		this.tokens = tokens;
	}
	
	public String[] getTokens() {
		return tokens;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}
	
	public double[] getWeights() {
		return weights;
	}

	public void setTermFreqs(Map<String, Integer> termFreqs) {
		this.termFreqs = termFreqs;
	}
	
	public Map<String, Integer> getTermFreqs() {
		return termFreqs;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
//	public void setRelease(String release){
//		this.release = release;
//	}
	

}
