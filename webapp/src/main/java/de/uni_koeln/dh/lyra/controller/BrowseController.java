package de.uni_koeln.dh.lyra.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.uni_koeln.dh.lyra.data.Song;
import de.uni_koeln.dh.lyra.lucene.index.SearchService;

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
	public String search(@RequestParam("searchForm") String searchPhrase, Model model)
			throws IOException, ParseException {
		List<Song> songs = new ArrayList<>();
		for (Document doc : searchService.search(searchPhrase)) {
			int year = 0;
			if (doc.get("year") != null) {
				year = Integer.valueOf(doc.get("year"));
			}
			songs.add(new Song(doc.get("title"), doc.get("lyrics"), doc.get("artist"), doc.get("release"), year,
					Boolean.valueOf(doc.get("compilation")), doc.get("comment")));
		}
		model.addAttribute("docs", songs);
		return "result";
	}

}
