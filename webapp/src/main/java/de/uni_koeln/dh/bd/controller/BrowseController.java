package de.uni_koeln.dh.bd.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import de.uni_koeln.dh.bd.data.albums.Album;
import de.uni_koeln.dh.bd.data.songs.Song;
import de.uni_koeln.dh.bd.service.CorpusService;

@Controller
@RequestMapping(value = "/browse")
public class BrowseController {

		private Logger logger = LoggerFactory.getLogger(getClass());
	
		private CorpusService service;
		
		private List<Song> songs;
		private List<Album> albums;
		
	public BrowseController() {
		service = new CorpusService();
	}	
		
	@RequestMapping(value = "/songs")
	public String listSongs(Model model) {
		logger.info("Listing songs...");
		
		char[] alphabet = service.getAlphabet();
		
		if (songs == null) {
			logger.info("Loading songs...");
			songs = service.getSongs();
		}
		
		model.addAttribute("alphabet", alphabet);
		model.addAttribute("songs", songs);
		
	    return "songs";
	}
	
	@RequestMapping(value = "/albums")
	public String listAlbums(Model model) {
		logger.info("Listing albums...");
		
		if (albums == null) {
			logger.info("Loading albums...");
			albums = service.getAlbums();
		}
		
		model.addAttribute("albums", albums);		
		return "albums";
	}
	
	// TODO setlists
	@RequestMapping(value = "/setlists")
	public String listSetlists(Model model) {
//		logger.info("Listing setlists...");
		return "setlists";
	}
	
}
