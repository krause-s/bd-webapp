package de.uni_koeln.dh.bd.model.coordinate;

import java.util.ArrayList;
import java.util.List;

public class Coordinate {

	double longitude;
	double latitude;
	String placeName;
	List<String> texts;
	
	public Coordinate(double longitude, double latitude, String placeName, List<String> texts) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.placeName = placeName;
		this.texts = texts;
	}
	
	public Coordinate(double longitude, double latitude, String placeName) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.placeName = placeName;
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
	
	public String getPlaceName() {
		return placeName;
	}
	
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
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
