package de.uni_koeln.dh.bd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
