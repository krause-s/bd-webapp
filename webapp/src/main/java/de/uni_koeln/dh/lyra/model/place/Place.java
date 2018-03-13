package de.uni_koeln.dh.lyra.model.place;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Place implements Serializable{

	private static final long serialVersionUID = -7407350195770959386L;

	double longitude;
	double latitude;
	List<PopUp> popUps = new ArrayList<PopUp>();
	private String meta;

	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
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

	public List<PopUp> getPopUps() {
		return popUps;
	}

	public void setPopUps(List<PopUp> popUps) {
		this.popUps = popUps;
	}

	public void addPopUp(PopUp popUp) {
		this.popUps.add(popUp);
	}

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

	// TODO define equals
	@Override
	public boolean equals(Object obj) {
		Place place = (Place) obj;

		return (place.getLatitude() == this.getLatitude() && place.getLongitude() == this.getLongitude());
	}

}
