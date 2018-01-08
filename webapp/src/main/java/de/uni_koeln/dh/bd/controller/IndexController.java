package de.uni_koeln.dh.bd.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import de.uni_koeln.dh.bd.model.place.Place;

@Controller
public class IndexController {

	// TODO song greeting
	@RequestMapping(value = { "", "/" })
	public String index(Model model) {
		return "index";
	}

	@RequestMapping(value = "/about")
	public String about() {
		return "about";
	}

	// TODO map
	@RequestMapping(value = "/map")
	public String map(Model model) {
		List<Place> places = new ArrayList<Place>();
		Place london = new Place(51.508, -0.11);
		london.addPlaceName("London");
		london.addPlaceName("London City");
		london.addTexts("London is a big City in the North!");
		london.addTexts("London is a bigger than the most other cities!");
		places.add(london);
		Place colonia = new Place(50.945312, 6.945928);
		colonia.addPlaceName("Köln");
		colonia.addTexts("Very popular city. especially in february!");
		places.add(colonia);
		model.addAttribute("places", places);
		return "map";
	}

}
