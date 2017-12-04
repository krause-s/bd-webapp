package de.uni_koeln.dh.bd.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import de.uni_koeln.dh.bd.data.Song;
import de.uni_koeln.dh.bd.service.CorpusService;

@Controller
public class IndexController {

		private Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value="/")
	public String init(Model model) {
		logger.info("Initializing web app...");

		CorpusService service = new CorpusService();
		List<Song> songs = service.getSongs();

		model.addAttribute("songs", songs);
	    return "index";
	}
	
}
