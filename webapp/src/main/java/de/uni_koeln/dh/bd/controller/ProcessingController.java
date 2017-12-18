package de.uni_koeln.dh.bd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
	    return "places";
	}
	
}
