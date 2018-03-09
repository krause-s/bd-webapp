package de.uni_koeln.dh.lyra.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.services.CorpusService;

@Controller
@RequestMapping(value = "/analytics")
public class AnalysisController {

	@Autowired
	private CorpusService corpusService;

	@RequestMapping(value = { "/places" })
	public String places(@RequestParam("yearsFrom") Optional<Integer> yearsFrom,
			@RequestParam("yearsTo") Optional<Integer> yearsTo, Model model) throws ParseException, IOException {
		List<Artist> artists = new ArrayList<>();
		if (yearsFrom.isPresent() && yearsTo.isPresent()) {
			artists = corpusService.getArtistSongsByYears(yearsFrom.get(), yearsTo.get());
		} else {
			artists = corpusService.getArtistList();
		}
		
		model.addAttribute("artistsList", artists);
		return "places";
	}

	// TODO frequencies
	@RequestMapping(value = "/frequencies")
	public String frequencies(/* Model model */) {
		return "frequencies";
	}

}
