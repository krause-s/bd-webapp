package de.uni_koeln.dh.lyra.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.services.AnalysisService;
import de.uni_koeln.dh.lyra.services.CorpusService;


/**
 * @author Johanna
 * interface for all data processing in
 * places and frequencies
 */
@Controller
@RequestMapping(value = "/analytics")
public class AnalysisController {

	@Autowired
	private CorpusService corpusService;

	@Autowired
	private AnalysisService analysisService;

	/**
	 * filters the songs by the given years
	 * @param years if null, all songs are return to places
	 * @param model
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	@RequestMapping(value = { "/places" })
	public String places(@RequestParam(value = "yearSlider", required = false) List<String> years, Model model)
			throws ParseException, IOException {
		List<Artist> artists = new ArrayList<>();
		if (years != null) {
			int yearsFrom = Integer.valueOf(years.get(0));
			int yearsTo = Integer.valueOf(years.get(1));
			artists = corpusService.getArtistSongsByYears(yearsFrom, yearsTo);
			model.addAttribute("years", years);
		} else {
			artists = corpusService.getArtistList();
		}
		model.addAttribute("songYears", corpusService.getMinAndMaxYears());
		model.addAttribute("artistsList", artists);
		return "places";
	}

	/**
	 * initializes the computation of relevant tokens.
	 * sets attribute "mapList" for the result
	 * @param artists list of two artist groups
	 * @param years list of two time sections
	 * @param compilation if compilations are regarded (in general)
	 * @param count number of tokens per result 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/frequencies")
	public String frequencies(@RequestParam(value = "artistSelect", required = false) List<String> artists,
			@RequestParam(value = "yearSlider", required = false) List<String> years,
			@RequestParam(value = "compCheck", required = false) boolean compilation,
			@RequestParam(value = "countSelect", required = false) String count, Model model) {

		if (artists != null) {
			List<Map<Integer, Set<String>>> result = analysisService.getFrequencies(artists, years, compilation, count);

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
		model.addAttribute("songYears", corpusService.getMinAndMaxYears());

		return "frequencies";
	}

	
	
	/**
	 * This method takes care of the raw frequencies concerning the artists vocabulary
	 * @param artist the chosen Artist. If no Artist is chosen, there will be an overview.
	 * @param model 
	 * @throws ParseException
	 * @throws IOException
	 */
	@RequestMapping(value = { "/artistvocab" })
	public String artistsvocab(@RequestParam(value = "artist", required = false) Artist artist, Model model)
			throws ParseException, IOException {
		
		if (artist != null) {
			artist = corpusService.getArtistByName(artist.getName());
			Map<String, Integer> vocab = artist.getVocabulary();
			int wordsPerSong = 0;

			if (artist.getSongs().size() != 0)
				wordsPerSong = vocab.size() / artist.getSongs().size();
			model.addAttribute("chosenArtist", artist);
			model.addAttribute("artistVocabs", vocab);
			model.addAttribute("artistVocabsSize", vocab.size());
			model.addAttribute("wordsPerSong", wordsPerSong);
		} else {
			Map<String, Integer[]> vocabs = new TreeMap<>();
			corpusService.getArtistList().forEach(currArtist -> {
				Map<String, Integer> currVocab = currArtist.getVocabulary();
				int wordsPerSong = 0;
				if (currArtist.getSongs().size() != 0) {
					wordsPerSong = currVocab.size() / currArtist.getSongs().size();
				}
				vocabs.put(currArtist.getName(),
						new Integer[] { currVocab.size(), wordsPerSong });
			});
			model.addAttribute("allVocabs", vocabs);
		}
		model.addAttribute("artistsList", corpusService.getArtistList());
		return "artistvocab";
	}

}
