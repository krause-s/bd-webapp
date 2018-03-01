package de.uni_koeln.dh.lyra.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import de.uni_koeln.dh.lyra.model.place.Place;
import de.uni_koeln.dh.lyra.model.place.PopUp;

@Controller
@RequestMapping(value = "/analytics")
public class AnalysisController {

	// TODO places
	// Just prototyping.
	@RequestMapping(value = "/places")
	public String places(Model model) {
		// Map: key = artistname, value = list of all places (lyrics and meta)
		Map<String, List<Place>> artistPlacesMap = new HashMap<String, List<Place>>();

		List<Place> places = new ArrayList<Place>();
		List<Place> morePlaces = new ArrayList<Place>();

		// one Place has n >=1 PopUps and two coordinates
		Place london = new Place(51.508, -0.11);

		// one PopUp has one placeName and one content (shown in PopUp)
		PopUp londonPopUp = new PopUp("London");
		londonPopUp.setContent("London is a big City in the North!");

		PopUp anotherLondonPopUp = new PopUp("London City");
		anotherLondonPopUp.setContent("London is a bigger than the most other cities!");
		london.addPopUp(anotherLondonPopUp);
		london.addPopUp(londonPopUp);
		// Places can be "meta" or not
		london.setIsMeta(false);

		Place colonia = new Place(50.945312, 6.945928);
		PopUp colognePopUp = new PopUp("Köln");
		colognePopUp.setContent("Very popular city. especially in february!");
		colonia.addPopUp(colognePopUp);
		colonia.setIsMeta(false);

		// add places to ArrayList
		places.add(london);
		places.add(colonia);

		Place metaOne = new Place(55.508, 0.00);

		PopUp metaOnePopUp = new PopUp("Not London");
		metaOnePopUp.setContent("Test city one");
		metaOne.setIsMeta(true);

		PopUp anothermetaOnePopUp = new PopUp("Not London City");
		anothermetaOnePopUp.setContent("still test city one");
		metaOne.addPopUp(anothermetaOnePopUp);
		metaOne.addPopUp(metaOnePopUp);

		Place metaTwo = new Place(60.945312, 7.945928);
		PopUp metaTwoPopUp = new PopUp("Not Köln");
		metaTwoPopUp.setContent("dont know whats hidden here");
		metaTwo.addPopUp(metaTwoPopUp);
		metaTwo.setIsMeta(true);

		places.add(metaOne);
		places.add(metaTwo);

		// add list of places as value and artist name as key
		artistPlacesMap.put("Bob Dylan", places);

		Place lyricsTestZappa = new Place(74.945312, 4.945928);
		PopUp lyricsTestPopUp = new PopUp("Not Köln");
		lyricsTestPopUp.setContent("not meta at all");
		lyricsTestZappa.addPopUp(lyricsTestPopUp);
		lyricsTestZappa.setIsMeta(false);
		morePlaces.add(lyricsTestZappa);

		Place metaTestTwo = new Place(70.945312, 4.945928);
		PopUp metaTestTwoPopUp = new PopUp("Not Köln");
		metaTestTwoPopUp.setContent("test test hahaha");
		metaTestTwo.addPopUp(metaTestTwoPopUp);
		metaTestTwo.setIsMeta(true);
		morePlaces.add(metaTestTwo);

		artistPlacesMap.put("Frank Zapper", morePlaces);

		// artist places hashmap as model for map
		model.addAttribute("map", artistPlacesMap);

		return "places";
	}

	// TODO frequencies
	@RequestMapping(value = "/frequencies")
	public String frequencies(/*Model model*/) {
		return "frequencies";
	}

}
