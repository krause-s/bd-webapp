package de.uni_koeln.dh.lyra.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// TODO name
@Controller
public class BrowseController {

	// TODO browse
	@RequestMapping(value = "/browse")
	public String browse(/*Model model*/) {
		return "browse";
	}
	
	// TODO search
	@RequestMapping(value = "/search")
	public String search(/*Model model*/) {
		return "search";
	}
	
}
