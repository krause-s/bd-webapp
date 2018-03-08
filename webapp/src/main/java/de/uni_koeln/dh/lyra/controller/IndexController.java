package de.uni_koeln.dh.lyra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.uni_koeln.dh.lyra.services.UserService;

@Controller
public class IndexController {

	@Autowired
	UserService userServide;

	// TODO song greeting
	@RequestMapping(value = "")
	public String index(/* Model model */) {
		return "index";
	}

	@RequestMapping(value = { "/" })
	public String index(@RequestParam("id") String id) {
		if (id != null) {
			userServide.setUserID(id);
		}
		return "index";
	}

	@RequestMapping(value = "/about")
	public String about() {
		return "about";
	}

}
