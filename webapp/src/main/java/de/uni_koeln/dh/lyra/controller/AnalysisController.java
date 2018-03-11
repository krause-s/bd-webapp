package de.uni_koeln.dh.lyra.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.data.Song;
import de.uni_koeln.dh.lyra.services.AnalysisService;
import de.uni_koeln.dh.lyra.services.CorpusService;

@Controller
@RequestMapping(value = "/analytics")
public class AnalysisController {

	@Autowired
	private CorpusService corpusService;
	
	@Autowired
	private AnalysisService analysisService;

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

	@RequestMapping(value = "/frequencies")
	public String frequencies(@RequestParam(value = "artistSelect", required = false) List<String> artists,
			@RequestParam(value = "yearSlider", required = false) List<String> years,
			@RequestParam(value = "compCheck", required = false) boolean compilation,
			@RequestParam(value = "countSelect", required = false) String count, Model model ) {
		if (artists != null) { 
			List<Map<Integer, Set<String>>> result = analysisService.doSth(artists, years, compilation, count);

			model.addAttribute("artists", artists);
			model.addAttribute("years", years);
			model.addAttribute("compilation", compilation);
			model.addAttribute("count", count);
			model.addAttribute("mapList", result);
		}
						
		List<String> artistList = new ArrayList<String>();
		for (Artist artist : corpusService.getArtistList()) {
			artistList.add(artist.getName());
		}
		
		Collections.sort(artistList);		
		model.addAttribute("artistsList", artistList);
		
		int min = 0, max = 0;
		for (Song song : corpusService.getAllSongs()) {
			int cur = song.getYear();
			
			if ((min == 0) && (max == 0)) {
				min = cur;
				max = cur;
			} else {	
				if (cur > max) {
					max = cur;
				} else
					if (cur < min) {
					min = cur;
				}
			}
		}
		System.out.println("min: " + min + " - max: " + max);
		model.addAttribute("songYears", new int[] {min, max});
		
		return "frequencies";
	}

}
