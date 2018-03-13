package de.uni_koeln.dh.lyra.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
		model.addAttribute("song", corpusService.getSongByID(songID));
		return "song";
	}

	@RequestMapping(value = { "/browse/", "/browse" })
	public String browse(Model model) {
		model.addAttribute("title", "Browse");
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
			@RequestParam("compilationForm") Optional<String> compilation,
			@RequestParam(value = "formRequired", required = false) boolean required, Model model)
			throws IOException, ParseException {
		// TODO min, max for years
		
		List<String> list = new ArrayList<String>();
		
		if (!searchPhrase.isEmpty()) {
			searchService.setSearchPhrase(searchPhrase);
			list.add(searchPhrase);
		}
		searchService.setField(field);
		if (fuzzy.isPresent()) {
			searchService.setFuzzy(true);
			list.add("Fuzzy");
		} else {
			searchService.setFuzzy(false);
		}
		if (compilation.isPresent()) {
			searchService.setCompilation(true);
			list.add("Compilations");
		} else {
			searchService.setCompilation(false);
		}
		if (yearsTo.isPresent() && yearsFrom.isPresent()) {
			int[] yearsRange = { yearsFrom.get(), yearsTo.get() };
			searchService.setYears(yearsRange);
			
			if (yearsRange[0] != yearsRange[1]) {
				if (list.size() == 0) 
					list.add("All");
				else 
					list.add(yearsRange[0] + "-" + yearsRange[1]);
			} else 
				list.add(String.valueOf(yearsRange[0]));
		}

		String title = null;
		
		if (required) {
			title = "Search";
			
			if (list.size() > 1) {
				String search = "";

				for (int i = 0; i < list.size(); i++) {
					search += "» " + list.get(i) + " ";
				}

				model.addAttribute("search", search);
			}
		} else {
			title = "Browse";
		}
		
		if (list.size() == 1)
			model.addAttribute("search", "» " + list.get(0));
		
		model.addAttribute("title", title);
		model.addAttribute("songs", searchService.search());
		return "browse";
	}

}
