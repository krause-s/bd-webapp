package de.uni_koeln.dh.lyra.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.lucene.index.SearchService;
import de.uni_koeln.dh.lyra.model.place.Place;
import de.uni_koeln.dh.lyra.processing.PlaceEvaluator;
import de.uni_koeln.dh.lyra.util.IO;

@Controller
@RequestMapping(value = "/analytics")
public class AnalysisController {
	
	private Map<String, Artist> artists = new HashMap<String, Artist>();
	private String dataPath = "src/main/resources/data/lyrics_database.xlsx";
	
	public AnalysisController() {
		
		IO io = new IO();

			try {
				artists = io.getDataFromXLSX(dataPath);
				//TODO read data not here
				List<Place> placesToEvaluate = io.getPlacesToEvaluate();
				List<Place> evaluatedPlaces = PlaceEvaluator.evaluatePlaces(placesToEvaluate);
				artists = PlaceEvaluator.sortPopUpsToArtists(evaluatedPlaces, artists);
				List<Artist> artistsList = new ArrayList<>();
				for(String artistKey : artists.keySet()){
					artistsList.add(artists.get(artistKey));
				}
				SearchService li = new SearchService();
//				li.setIndexDirPath("/data/index/000001/");
				li.initIndex(artistsList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}

	@RequestMapping(value = "/places")
	public String places(Model model) {
		List<Artist> artistList = new ArrayList<Artist>();
		artistList.addAll(artists.values());
		model.addAttribute("artistsList", artistList);

		return "places";
	}

	// TODO frequencies
	@RequestMapping(value = "/frequencies")
	public String frequencies(/*Model model*/) {
		return "frequencies";
	}

}
