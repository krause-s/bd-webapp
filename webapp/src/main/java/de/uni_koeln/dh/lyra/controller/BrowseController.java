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

@Controller
public class BrowseController {

	@Autowired
	SearchService searchService;

	@Autowired
	CorpusService corpusService;

	/**
	 * @param songID
	 * @param model
	 * @return 
	 * leads via an unique id to a certain song and presents it in the song template
	 */
	@RequestMapping(value = "/browse/{songID}")
	public String browse(@PathVariable("songID") String songID, Model model) {
		model.addAttribute("song", corpusService.getSongByID(songID));
		return "song";
	}

	/**
	 * @param model
	 * @return
	 * returns all songs in the browse template
	 */
	@RequestMapping(value = { "/browse/", "/browse" })
	public String browse(Model model) {
		model.addAttribute("title", "Browse");
		model.addAttribute("songs", corpusService.getAllSongs());
		return "browse";
	}

	/**
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * initial search view without any results shown. 
	 */
	@RequestMapping(value = "/search")
	public String search(Model model) throws IOException, ParseException {
		model.addAttribute("songYears", corpusService.getMinAndMaxYears());
		return "search";
	}

	/**
	 * @param searchPhrase
	 * @param field
	 * @param years
	 * @param fuzzy
	 * @param compilation
	 * @param required
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * sets all given parameters in the searchService for queryBuilder. Presents the results in the browse template as a table. 
	 */
	@RequestMapping(value = { "/result" })
	public String search(@RequestParam("searchForm") String searchPhrase, @RequestParam("fieldForm") String field,
			@RequestParam(value = "yearSlider", required = false) List<String> years,
			@RequestParam("fuzzyForm") Optional<String> fuzzy,
			@RequestParam("compilationForm") Optional<String> compilation,
			@RequestParam(value = "formRequired", required = false) boolean required, Model model)
			throws IOException, ParseException {
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
		if (years != null) {			
			int[] yearsRange = { Integer.valueOf(years.get(0)), Integer.valueOf(years.get(1)) };
			searchService.setYears(yearsRange);
			
			if (yearsRange[0] != yearsRange[1]) 
				list.add(years.get(0) + "-" + years.get(1));
			else 
				list.add(years.get(0));
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
			
			model.addAttribute("songYears", corpusService.getMinAndMaxYears());
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
