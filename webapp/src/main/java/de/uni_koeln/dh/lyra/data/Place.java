package de.uni_koeln.dh.lyra.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * class represents a geographical place. 
 * 
 * @author Johanna
 *
 */
public class Place implements Serializable{

	private static final long serialVersionUID = -7407350195770959386L;

	double longitude;
	double latitude;
	List<PopUp> popUps = new ArrayList<PopUp>();
	public String id;
	private String meta;
	
	/**
	 * creates a geographical Place with the given latitude
	 * and longitude
	 * @param longitude
	 * @param latitude
	 */
	public Place(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.id = latitude + "-" + longitude;
	}
	
	public String getID(){
		return id;
	}

	/**
	 * 
	 * @return String with meta data (country, town,...)
	 */
	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
	}	

	/**
	 * 
	 * @return longitude value
	 */
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 *
	 * @return latitude value
	 */
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * returns a list of referred information to the geographical place.
	 * may contain quotes of lyrics or biographical information
	 * @return
	 */
	public List<PopUp> getPopUps() {
		return popUps;
	}

	public void setPopUps(List<PopUp> popUps) {
		this.popUps = popUps;
	}

	/**
	 * add a popUp containing quote or biographical information
	 * @param popUp
	 */
	public void addPopUp(PopUp popUp) {
		this.popUps.add(popUp);
	}

	/**
	 * filters the popUps by the given years
	 * @param start
	 * @param end
	 */
	public void removePopUpByYears(int start, int end) {
		if (this.popUps != null) {
			List<PopUp> toRemove = new ArrayList<>();
			for (PopUp pu : this.popUps) {
				if (pu.getReferredSong().getYear() < start || pu.getReferredSong().getYear() > end) {
					toRemove.add(pu);
				}
			}
			this.popUps.removeAll(toRemove);
		}
	}

	/**
	 * compares latitudes and longitudes
	 */
	@Override
	public boolean equals(Object obj) {
		Place place = (Place) obj;

		return (place.getLatitude() == this.getLatitude() && place.getLongitude() == this.getLongitude());
	}

}
