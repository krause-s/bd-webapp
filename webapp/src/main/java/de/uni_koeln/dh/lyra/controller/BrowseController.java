package de.uni_koeln.dh.lyra.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.uni_koeln.dh.lyra.data.Song;
import de.uni_koeln.dh.lyra.services.SearchService;

// TODO name
@Controller
public class BrowseController {

	@Autowired
	SearchService searchService;

	// TODO browse
	@RequestMapping(value = "/browse")
	public String browse(/* Model model */) {
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
		List<Song> songs = new ArrayList<Song>();
		List<Document> resultList = new ArrayList<>();
		// All this will move to Searchservice
		if(searchPhrase.isEmpty()){
			model.addAttribute("docs", songs);
			return "result";
		}
		if (fuzzy.isPresent()) {
			searchPhrase = searchPhrase + "~";
		}
		if (compilation.isPresent()) {
			System.out.println("compilations is selected");
		}
		if (yearsTo.isPresent() && yearsFrom.isPresent()) {
			int[] yearsRange = { yearsFrom.get(), yearsTo.get() };
			resultList = searchService.search(searchPhrase, field, yearsRange);
		}
		if (resultList == null)
			resultList = searchService.search(searchPhrase, field);

		for (Document doc : resultList) {
			int year;
			if (doc.getField("year") != null) {
				year = doc.getField("year").numericValue().intValue();
			} else {
				year = 0;
			}

			songs.add(new Song(doc.get("title"), doc.get("lyrics"), doc.get("artist"), doc.get("release"), year,
					Boolean.valueOf(doc.get("compilation")), doc.get("comment")));
		}
		model.addAttribute("docs", songs);
		return "result";
	}

}
