package de.uni_koeln.dh.bd.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import de.uni_koeln.dh.bd.model.coordinate.Coordinate;

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
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		Coordinate coordinateLondon = new Coordinate(51.508, -0.11, "London");
		coordinateLondon.addTexts("London is a big City in the North!");
		coordinateLondon.addTexts("London is a bigger than the most other cities!");
		coordinates.add(coordinateLondon);
		Coordinate coordinateColonia = new Coordinate(50.945312, 6.945928, "Köln");
		coordinateColonia.addTexts("Very popular city. especially in febraury!");
		coordinates.add(coordinateColonia);
		model.addAttribute("coordinates", coordinates);
		return "map";
	}

}
