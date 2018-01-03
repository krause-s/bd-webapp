package de.uni_koeln.dh.bd.model.place;

import java.util.ArrayList;
import java.util.List;

public class Place {

	double longitude;
	double latitude;
	List<String> placeNames;
	List<String> texts;
	
	public Place(double longitude, double latitude, List<String> placeName, List<String> texts) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.placeNames = placeName;
		this.texts = texts;
	}
	
	public Place(double longitude, double latitude, List<String> placeName) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.placeNames = placeName;
	}
	
	public Place(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public List<String> getPlaceName() {
		return placeNames;
	}
	
	public void setPlaceName(List<String> placeName) {
		this.placeNames = placeName;
	}
	
	public void addPlaceName(String placeName) {
		if(this.placeNames == null) {
			this.placeNames = new ArrayList<String>();
		}
		this.placeNames.add(placeName);
	}
	
	public List<String> getTexts() {
		return texts;
	}
	
	public void setTexts(List<String> texts) {
		this.texts = texts;
	}
	
	public void addTexts(String text) {
		if(texts == null){
			texts = new ArrayList<String>();
		}
		this.texts.add(text);
	}
}
