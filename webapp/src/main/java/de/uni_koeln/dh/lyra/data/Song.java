package de.uni_koeln.dh.lyra.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.uni_koeln.dh.lyra.model.place.Place;

public class Song {
	
	private String title, lyrics, artist, release, comment;
	private int year;
	private boolean compilation;
	private List<Place> places = new ArrayList<Place>();
	private UUID uuid;
	
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
		this.uuid = UUID.randomUUID(); //TODO Generierung der ID so ok?
		this.title = title;
		this.lyrics = lyrics;
		this.artist = artist;
		this.release = release;
		this.year = year;
		this.compilation = compilation;
		this.comment = comment;
	}
	
	public UUID getID() {
		return uuid;
	}

	public String getTitle() {
		return title;
	}

	public String getLyrics() {
		return lyrics;
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

	public int getYear() {
		return year;
	}

	public boolean isCompilation() {
		return compilation;
	}

	public List<Place> getPlaces() {
		return places;
	}

	public void setPlaces(List<Place> places) {
		this.places = places;
	}
	
	public void addPlace(Place place) {
		this.places.add(place);
	}
	
	
	

}
