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
@RequestMapping(value = "/browse")
public class BrowseController {

		private Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value = "/songs")
	public String listSongs(Model model) {
		logger.info("Listing songs...");
		
		CorpusService service = new CorpusService();
		List<Song> songList = service.getSongs();

		model.addAttribute("songList", songList);
	    return "songs";
	}
	
	// TODO albums
	@RequestMapping(value = "/albums")
	public String listAlbums() {
		logger.info("Listing albums...");
		return "albums";
	}
	
	// TODO setlists
	@RequestMapping(value = "/setlists")
	public String listSetlists() {
		logger.info("Listing setlists...");
		return "setlists";
	}
	
}
