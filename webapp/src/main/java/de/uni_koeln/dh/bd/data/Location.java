package de.uni_koeln.dh.bd.data;

public class Location {

	private Double longitude;
	private Double latitude;
	private String name;
	
	public Location(String name, Double latitude, Double longitude) {
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
