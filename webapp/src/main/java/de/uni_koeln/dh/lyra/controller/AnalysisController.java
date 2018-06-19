package de.uni_koeln.dh.lyra.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.data.Place;
import de.uni_koeln.dh.lyra.data.Song;
import de.uni_koeln.dh.lyra.services.AnalysisService;
import de.uni_koeln.dh.lyra.services.CorpusService;
import de.uni_koeln.dh.lyra.services.SearchService;

/**
 * @author Johanna interface for all data processing in places and frequencies
 */
@Controller
@RequestMapping(value = "/analytics")
public class AnalysisController {

	@Autowired
	private CorpusService corpusService;
	
	@Autowired
	private SearchService searchService;

	@Autowired
	private AnalysisService analysisService;

	/**
	 * filters the songs by the given years
	 * 
	 * @param years
	 *            if null, all songs are return to places
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
	 * 
	 * @param years
	 *            if null, all songs are return to places
	 * @param model
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	@RequestMapping(value = { "/placeswords" })
	public String specificWordsForPlaces(@RequestParam(value = "place", required = false) String placeID,
			Model model) throws ParseException, IOException {
		model.addAttribute("placesList", corpusService.getPlaces());
		if (placeID != null) {
			
			Place chosenPlace = corpusService.getPlaceByID(placeID);
			if (chosenPlace != null) {
				Map<String, Integer[]> placeWordList = new HashMap<String, Integer[]>();
				Map<String, Integer> placeWordFreq = corpusService.calculateWordFrequencies(chosenPlace.getPopUps());
				placeWordFreq.forEach((word, placeContextFreq) -> {
					searchService.setSearchPhrase(word);
					searchService.setField("lyrics");
					int occurrences = 0;
					try {
						occurrences = searchService.search().size();
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					placeWordList.put(word, new Integer[]{placeContextFreq, occurrences});
				});
				
				model.addAttribute("placeWordList", placeWordList);
				Set<String> placeNames = new TreeSet<>();
				chosenPlace.getPopUps().forEach(popUp ->{
					placeNames.add(popUp.getPlaceName());
				});
				model.addAttribute("placeNames", placeNames);
				model.addAttribute("chosenPlace", chosenPlace);
			}
		}
		return "placeswords";
	}

	/**
	 * initializes the computation of relevant tokens. sets attribute "mapList"
	 * for the result
	 * 
	 * @param artists
	 *            list of two artist groups
	 * @param years
	 *            list of two time sections
	 * @param compilation
	 *            if compilations are regarded (in general)
	 * @param count
	 *            number of tokens per result
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
	 * This method takes care of the raw frequencies concerning the artists
	 * vocabulary
	 * 
	 * @param artist
	 *            the chosen Artist. If no Artist is chosen, there will be an
	 *            overview.
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

			if (artist.getSongs().size() != 0) {
				List<Song> notCompilationSongs = artist.getNotCompilationSongs();
				int uniqueWordsInSongs = 0;
				for (Song song : notCompilationSongs) {
					Set<String> uniqueWords = new HashSet<String>(Arrays.asList(song.getTokens()));
					uniqueWordsInSongs += uniqueWords.size();
				}
				wordsPerSong = uniqueWordsInSongs / notCompilationSongs.size();
			}
			model.addAttribute("chosenArtist", artist);
			model.addAttribute("artistVocabs", vocab);
			model.addAttribute("artistVocabsSize", vocab.size());
			model.addAttribute("wordsPerSong", wordsPerSong);
		} else {
			TreeMap<String, Integer[]> vocabs = new TreeMap<>();
			corpusService.getArtistList().forEach(currArtist -> {
				Map<String, Integer> currVocab = currArtist.getVocabulary();
				List<Song> notCompilationSongs = currArtist.getNotCompilationSongs();
				int wordsPerSong = 0;
				if (notCompilationSongs.size() != 0) {
					int uniqueWordsInSongs = 0;
					for (Song song : notCompilationSongs) {
						Set<String> uniqueWords = new HashSet<String>(Arrays.asList(song.getTokens()));
						uniqueWordsInSongs += uniqueWords.size();
					}
					wordsPerSong = uniqueWordsInSongs / notCompilationSongs.size();
				}
				vocabs.put(currArtist.getName(),
						new Integer[] { currVocab.size(), wordsPerSong, notCompilationSongs.size() });
			});
			model.addAttribute("allVocabs", vocabs);
		}
		model.addAttribute("artistsList", corpusService.getArtistList());
		return "artistvocab";
	}

	/**
	 * This calculates the number of words used per song in average
	 * 
	 * @param model
	 * @throws ParseException
	 * @throws IOException
	 */
	@RequestMapping(value = { "/songlenght" })
	public String songlength(Model model) throws ParseException, IOException {
		Map<String, Double> allSongLength = new TreeMap<>();
		corpusService.getArtistList().forEach(currArtist -> {
			double currSongLength = currArtist.getAverageSongLength();
			allSongLength.put(currArtist.getName(), currSongLength);
		});
		model.addAttribute("allSongLengths", allSongLength);
		model.addAttribute("artistsList", corpusService.getArtistList());
		return "length";
	}

	@RequestMapping(value = { "/similarlyrics" })
	public String classifyByArtist(@RequestParam(value = "classifytext", required = false) String textToClassify,
			Model model) throws ParseException, IOException {
		if (textToClassify != null)
			model.addAttribute("similarSongs", analysisService.thisSoundsLike(textToClassify));
		model.addAttribute("classifytext", textToClassify);
		return "similarity";
	}

}
