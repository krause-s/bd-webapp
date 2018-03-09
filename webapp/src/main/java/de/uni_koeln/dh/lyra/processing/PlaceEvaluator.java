package de.uni_koeln.dh.lyra.processing;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.model.place.Place;
import de.uni_koeln.dh.lyra.model.place.PopUp;

public class PlaceEvaluator {
	
	public static List<Place> evaluatePlaces(List<Place> placesToEvaluate, Map<Place, Set<String>> popUpsToDelete) {
		
		//TODO UI to evaluate
		return placesToEvaluate;
	}
	
	public static Map<String, Artist> sortPopUpsToArtists (List<Place> evaluatedPlaces, Map<String, Artist> artists) {
		
		for(Place place : evaluatedPlaces) {

			for(PopUp popUp : place.getPopUps()) {
				String artistName = popUp.getReferredSong().getArtist();
				Artist currArtist = artists.get(artistName);
				if(currArtist.getLyricsPlaces().contains(place)) {
					Place artistPlace = currArtist.getLyricsPlaces()
							.get(currArtist.getLyricsPlaces().indexOf(place));
					artistPlace.addPopUp(popUp);
				} else {
					Place artistPlace = new Place(place.getLongitude(), place.getLatitude());
					artistPlace.addPopUp(popUp);
					currArtist.addLyricsPlace(artistPlace);
				}
			}
		}
		return artists;
	}

}
