package de.uni_koeln.dh.lyra.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.uni_koeln.dh.lyra.model.place.Place;
import de.uni_koeln.dh.lyra.model.place.PopUp;
import de.uni_koeln.dh.lyra.services.CorpusService;
import de.uni_koeln.dh.lyra.services.UserService;

@Controller
public class IndexController {

		@Autowired
		CorpusService corpusService;
		@Autowired
		UserService userService;

	@RequestMapping(value = { "", "/" })
	public String index(Model model) {
		System.out.println("NO index -> overlay");
		
		model.addAttribute("upload", true);
		return "index";
	}
	
	@PostMapping(value = "/upload")
	public String upload(Model model) {
		System.out.println("UPLOAD");
		
		// TODO file upload (idle)
		List<Place> placesToEvaluate = 
				corpusService.init(CorpusService.dataPath);
//				Arrays.asList(new Place[] { 
//						new Place(0, 0, Arrays.asList(new PopUp[] { new PopUp("cambridge"), new PopUp("cambridge") }), false), 
//						new Place(1, 1, Arrays.asList(new PopUp[] { new PopUp("new york"), new PopUp("new york city"), new PopUp("new york") }), false),
//						new Place(2, 2, Arrays.asList(new PopUp[] { new PopUp("koeln") }), false),});
		
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
	public String evaluation(@RequestParam(value = "placeName", required = false) List<String> placeNames, Model model) {
		System.out.println("EVALUATION");

		// TODO placeNames == null

		Iterator<Map.Entry<Place,Set<String>>> iter = map.entrySet().iterator();
		
		while (iter.hasNext()) {
			Set<String> set = iter.next().getValue();
			
			set.removeAll(placeNames);
		    if (set.isEmpty()) iter.remove();
		}
		
		corpusService.init2(map);
		
		// TODO idle, setId, model (stock)
		
		return "index";
	}
	
	@RequestMapping(value =  "/{id}")
	public String index(@PathVariable("id") int id/*, Model model*/) {
		System.out.println("index " + id + " -> NO overlay");

//		userService.setUserID(id);	
		return "index";
	}
	
	@RequestMapping(value = "/about")
	public String about() {
		return "about";
	}
	
}