package de.uni_koeln.dh.lyra.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.data.Place;
import de.uni_koeln.dh.lyra.data.PopUp;

public class PlaceEvaluator {

	/**
	 * takes a list of all found places and a map of all (by the user) deleted
	 * places and their deleted pop ups.
	 * compares both datasets, deletes the ones that should be deleted and sorts
	 * the remaining to the referred artist
	 * @param placesToEvaluate
	 * @param popUpsToDelete
	 * @param artists
	 * @return
	 */
	public static Map<String, Artist> evaluatePlaces(List<Place> placesToEvaluate,
			Map<Place, Set<String>> popUpsToDelete, Map<String, Artist> artists) {

		for (Place place : placesToEvaluate) {

			List<PopUp> popUpsToAdd = new ArrayList<PopUp>();

			// if some pop ups of a place have been deleted
			if (popUpsToDelete.containsKey(place)) {

				// find out which pop ups have been removed
				Set<String> removedStrings = popUpsToDelete.get(place);
				List<PopUp> annotatedPopUps = place.getPopUps();
				for (PopUp p : annotatedPopUps) {
					if (!removedStrings.contains(p.getPlaceName()))
						popUpsToAdd.add(p);
				}
			} else {
				popUpsToAdd.addAll(place.getPopUps());
			}

			for (PopUp popUp : popUpsToAdd) {
				String artistName = popUp.getReferredSong().getArtist();
				Artist currArtist = artists.get(artistName);
				if (currArtist.getLyricsPlaces().contains(place)) {
					Place artistPlace = currArtist.getLyricsPlaces().get(currArtist.getLyricsPlaces().indexOf(place));
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
