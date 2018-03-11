package de.uni_koeln.dh.lyra.controller;

import java.io.IOException;
import java.util.Optional;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.uni_koeln.dh.lyra.services.CorpusService;
import de.uni_koeln.dh.lyra.services.SearchService;

// TODO name
@Controller
public class BrowseController {

	@Autowired
	SearchService searchService;

	@Autowired
	CorpusService corpusService;

	// TODO browse
	@RequestMapping(value = "/browse/{songID}")
	public String browse(@PathVariable("songID") String songID, Model model) {
		System.out.println(corpusService.getSongByID(songID).getLyricsBR());
		model.addAttribute("song", corpusService.getSongByID(songID));
		return "song";
	}

	@RequestMapping(value = { "/browse/", "/browse" })
	public String browse(Model model) {
		model.addAttribute("songs", corpusService.getAllSongs());
		return "browse";
	}

	@RequestMapping(value = "/search")
	public String search() throws IOException, ParseException {
		return "search";
	}

	@RequestMapping(value = { "/result" })
	public String search(@RequestParam("searchForm") String searchPhrase, @RequestParam("fieldForm") String field,
			@RequestParam("rangefrom") Optional<Integer> yearsFrom, @RequestParam("rangeto") Optional<Integer> yearsTo,
			@RequestParam("fuzzyForm") Optional<String> fuzzy,
			@RequestParam("compilationForm") Optional<String> compilation, Model model)
			throws IOException, ParseException {
		if (!searchPhrase.isEmpty()) {
			searchService.setSearchPhrase(searchPhrase);
		}
		searchService.setField(field);
		if (fuzzy.isPresent()) {
			searchService.setFuzzy(true);
		} else {
			searchService.setFuzzy(false);
		}
		if (compilation.isPresent()) {
			searchService.setCompilation(true);
		} else {
			searchService.setCompilation(false);
		}
		if (yearsTo.isPresent() && yearsFrom.isPresent()) {
			int[] yearsRange = { yearsFrom.get(), yearsTo.get() };
			searchService.setYears(yearsRange);
		}
		model.addAttribute("docs", searchService.search());
		return "result";
	}

}
