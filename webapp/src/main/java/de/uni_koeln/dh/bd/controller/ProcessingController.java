package de.uni_koeln.dh.bd.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import de.uni_koeln.dh.bd.model.place.Place;
import de.uni_koeln.dh.bd.model.place.PopUp;

// TODO name?
@Controller
public class ProcessingController {

	// TODO analytics
	@RequestMapping(value = "/analytics")
	public String analytics(Model model) {
		return "analytics";
	}

	// TODO places
	@RequestMapping(value = "/places")
	public String places(Model model) {
		List<Place> places = new ArrayList<Place>();
		Place london = new Place(51.508, -0.11);
		
		PopUp londonPopUp = new PopUp("London");
		londonPopUp.setContent("London is a big City in the North!");
		
		PopUp anotherLondonPopUp = new PopUp("London City");
		anotherLondonPopUp.setContent("London is a bigger than the most other cities!");
		london.addPopUp(anotherLondonPopUp);
		london.addPopUp(londonPopUp);
		
		Place colonia = new Place(50.945312, 6.945928);
		PopUp colognePopUp = new PopUp("Köln");
		colognePopUp.setContent("Very popular city. especially in february!");
		colonia.addPopUp(colognePopUp);
		
		places.add(london);
		places.add(colonia);
		
		List<Place> metaPlaces = new ArrayList<Place>();
		Place metaOne = new Place(55.508, 0.00);
		
		PopUp metaOnePopUp = new PopUp("Not London");
		metaOnePopUp.setContent("Test city one");
		
		PopUp anothermetaOnePopUp = new PopUp("Not London City");
		anothermetaOnePopUp.setContent("still test city one");
		metaOne.addPopUp(anothermetaOnePopUp);
		metaOne.addPopUp(metaOnePopUp);
		
		Place metaTwo = new Place(60.945312, 7.945928);
		PopUp metaTwoPopUp = new PopUp("Not Köln");
		metaTwoPopUp.setContent("dont know whats hidden here");
		metaTwo.addPopUp(metaTwoPopUp);
		
		metaPlaces.add(metaOne);
		metaPlaces.add(metaTwo);
		
		model.addAttribute("places", places);
		model.addAttribute("metaPlaces", metaPlaces);
		return "places";
	}

}
