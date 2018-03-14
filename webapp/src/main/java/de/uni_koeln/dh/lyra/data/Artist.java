package de.uni_koeln.dh.lyra.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uni_koeln.dh.lyra.util.ColorGenerator;

/**
 * stores all information on an artist
 * 
 * @author Johanna
 *
 */
public class Artist implements Serializable, Comparable<Artist> {

	private static final long serialVersionUID = -991552598655095147L;

	private String name;
	private List<Place> bioPlaces;
	private List<Place> lyricsPlaces;
	private List<Song> songs;
	public String color;

	/**
	 * creates an artist object for an artist with the given name. generates
	 * random color (stock, places) for the artist
	 * 
	 * @param name
	 */
	public Artist(String name) {
		this.name = name;
		this.bioPlaces = new ArrayList<Place>();
		this.lyricsPlaces = new ArrayList<Place>();
		this.songs = new ArrayList<Song>();
		this.color = ColorGenerator.randomColor();
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * returns list with all biographical places
	 * 
	 * @return
	 */
	public List<Place> getBioPlaces() {
		return bioPlaces;
	}

	public void setBioPlaces(List<Place> bioPlaces) {
		this.bioPlaces = bioPlaces;
	}

	public void addBioPlace(Place bioPlace) {
		bioPlaces.add(bioPlace);
	}

	/**
	 * returns all songs of this artist in the corpus
	 * 
	 * @return
	 */
	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

	public void addSong(Song song) {
		songs.add(song);
	}

	/**
	 * returns list with all places mentioned in lyrics
	 * 
	 * @return
	 */
	public List<Place> getLyricsPlaces() {
		return lyricsPlaces;
	}

	public void setLyricsPlaces(List<Place> lyricsPlaces) {
		this.lyricsPlaces = lyricsPlaces;
	}

	public void addLyricsPlace(Place lyricsPlace) {
		lyricsPlaces.add(lyricsPlace);
	}

	public void removeLyricsPlace(Place lyricsPlace) {
		lyricsPlaces.remove(lyricsPlace);
	}

	@Override
	public boolean equals(Object obj) {
		Artist artist = (Artist) obj;
		return artist.getName().equals(this.getName());
	}

	@Override
	public int compareTo(Artist o) {
		if (getName().equals(o.getName()) && getBioPlaces().equals(o.getBioPlaces())
				&& getLyricsPlaces().equals(o.getLyricsPlaces())) {
			return 0;
		}
		if(getSongs().size() > o.getSongs().size())
			return +1;
		return -1;
	}

}
