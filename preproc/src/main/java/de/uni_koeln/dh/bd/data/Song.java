package de.uni_koeln.dh.bd.data;

import java.util.ArrayList;
import java.util.List;

public class Song {

	private String id;
	private String lyrics;
	private String copyright;
	private String title;
	private String credits;
	private String url;
	private String firstPlayed;
	private String lastPlayed;
	private String timesPlayed;
	private List<Album> albums = new ArrayList<Album>();

	public Song(String id) {
		this.id = id;
	}

	public Song(String id, String url, String title, String lyrics, String copyright, String credits, String fp,
			String lp, String tp) {

		// title enthält kein html und kann übernommen werden
		this.id = id;
		this.title = title;
		this.url = url;
		lyrics = lyrics.replaceAll("<br>", "<br />");
		this.lyrics = lyrics.split("<p ")[0];
		this.copyright = copyright;
		firstPlayed = fp;
		lastPlayed = lp;
		timesPlayed = tp;

		this.credits = credits.replaceAll("Written by: ", "");
	}

	public void addAlbum(Album album) {
		albums.add(album);
	}

	public List<Album> getAlbums() {
		return albums;
	}

	public String getID() {
		return id;
	}

	public String getFirstPlayed() {
		return firstPlayed;
	}

	public String getLastPlayed() {
		return lastPlayed;
	}

	public String getTimesPlayed() {
		return timesPlayed;
	}

	public String getLyrics() {
		return lyrics;
	}

	public String getCopyright() {
		return copyright;
	}

	public String getTitle() {
		return title;
	}

	public String getCredits() {
		return credits;
	}

	public String getURL() {
		return url;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title + "\n" + lyrics + "\n" + credits + copyright;
	}

}
