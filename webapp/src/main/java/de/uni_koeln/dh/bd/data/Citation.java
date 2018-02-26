package de.uni_koeln.dh.bd.data;

public class Citation {
	
	private Song citedSong;
	private String line;
	
	/**
	 * represents a cited line of a song that contains
	 * a place
	 * @param citedSong
	 * @param line
	 */
	public Citation(Song citedSong, String line) {
		this.citedSong = citedSong;
		this.line = line;
	}

	public Song getCitedSong() {
		return citedSong;
	}

	public String getLine() {
		return line;
	}
	
	

}
