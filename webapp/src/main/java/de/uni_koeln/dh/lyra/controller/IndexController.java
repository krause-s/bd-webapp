package de.uni_koeln.dh.lyra.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.model.place.Place;
import de.uni_koeln.dh.lyra.model.place.PopUp;
import de.uni_koeln.dh.lyra.services.CorpusService;
import de.uni_koeln.dh.lyra.services.SearchService;

@Controller
public class IndexController {

	@Autowired
	CorpusService corpusService;

	@Autowired
	SearchService searchService;

	@RequestMapping(value = { "", "/" })
	public String index(Model model) {
		System.out.println("NO index -> overlay");
		model.addAttribute("upload", false);
		return "index";
	}
	
	@RequestMapping(value = {"/info" })
	public String indexIntro(Model model) {
		System.out.println("NO index -> overlay");
		model.addAttribute("upload", true);
		return "index";
	}

	@PostMapping(value = "/upload")
	public String upload(Model model) {
		System.out.println("UPLOAD");

		// TODO file upload (idle)
		List<Place> placesToEvaluate = corpusService.init(CorpusService.dataPath);

		if (placesToEvaluate != null) {
			map = new HashMap<Place, Set<String>>();

			for (Place place : placesToEvaluate) {
				Set<String> set = new TreeSet<String>();

				for (PopUp popup : place.getPopUps())
					set.add(popup.getPlaceName());

				map.put(place, set);
			}

			model.addAttribute("places", map);
		} else {
			// TODO no places -> no evaluation
		}

		return "index";
	}

	Map<Place, Set<String>> map;

	@PostMapping(value = "/evaluation")
	public String evaluation(@RequestParam(value = "placeName", required = false) List<String> placeNames,
			Model model) throws IOException {
		System.out.println("EVALUATION");

		// TODO placeNames == null

		Iterator<Map.Entry<Place, Set<String>>> iter = map.entrySet().iterator();

		while (iter.hasNext()) {
			Set<String> set = iter.next().getValue();

			set.removeAll(placeNames);
			if (set.isEmpty())
				iter.remove();
		}

		corpusService.init2(map);
		corpusService.serializeCorpus();
		searchService.updateIndex();
		
		// TODO idle
		initStock(model);
		return "index";
	}

	@RequestMapping(value = "/about")
	public String about() {
		return "about";
	}

	private void initStock(Model model) {
		int totalCount = corpusService.getAllSongs().size();
		
		Map<Integer, List<Artist>> map = new TreeMap<Integer, List<Artist>>(Collections.reverseOrder());
		
		List<Artist> artists = corpusService.getArtistList();
		int percentages = 0;
		
		for (int i = 0; i < artists.size(); i++) {
			int percentage;
			
			if (i == artists.size()-1) {
				percentage = 100-percentages;
			} else {
				int cnt = artists.get(i).getSongs().size();
				percentage = (int)((100 / (float)totalCount) * cnt);
			}
			
			if (!map.containsKey(percentage)) {
				map.put(percentage, new ArrayList<Artist>());
				percentages += percentage;
			}
						
			map.get(percentage).add(artists.get(i));
		}
		
		model.addAttribute("songCount", totalCount);
		model.addAttribute("artistMap", map);
	}
	
}
