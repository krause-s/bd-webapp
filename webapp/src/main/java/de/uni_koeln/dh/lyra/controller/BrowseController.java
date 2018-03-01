package de.uni_koeln.dh.lyra.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/browse")
public class BrowseController {

		private Logger logger = LoggerFactory.getLogger(getClass());
	
//		private CorpusService service;
//		private List<Song> songs;
		
//	public BrowseController() {
//		service = new CorpusService();
//	}	
	
	// TODO artists
	@RequestMapping(value = "/artists")
	public String listArtists(/*Model model*/) {
		logger.info("Listing artists...");
		return "artists";
	}
	
	// TODO songs
	@RequestMapping(value = "/songs")
	public String listSongs(/*Model model*/) {
		logger.info("Listing songs...");
		
//		char[] alphabet = service.getAlphabet();
//		
//		if (songs == null) {
//			logger.info("Loading songs...");
//			songs = service.getSongs();
//		}
//		
//		model.addAttribute("alphabet", alphabet);
//		model.addAttribute("songs", songs);
		
	    return "songs";
	}
	
}
