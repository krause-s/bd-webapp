package de.uni_koeln.dh.lyra.data;

import java.util.List;

import de.uni_koeln.dh.lyra.model.place.Place;

public class Artist {

	String name;
	List<Place> bioPlaces;
	List<Song> songs;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Place> getBioPlaces() {
		return bioPlaces;
	}

	public void setBioPlaces(List<Place> bioPlaces) {
		this.bioPlaces = bioPlaces;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

}
